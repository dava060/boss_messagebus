package com.moretv.service.tag.impl;

import com.moretv.common.constants.ConstantMess;
import com.moretv.common.utils.CodeIDOperator;
import com.moretv.common.utils.Coder;
import com.moretv.dao.personal.IPersonalMessageJedisDao;
import com.moretv.dao.personal.impl.PersonalMessageJedisDaoImpl;
import com.moretv.message.program.ProgramMessageListener;
import com.moretv.service.personal.IPersonalMessageService;
import com.moretv.service.program.IProgramMessageService;
import com.moretv.service.tag.ITagMessageService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import static com.google.common.base.Strings.isNullOrEmpty;
public class TagMessageServiceImpl implements ITagMessageService{
	private IPersonalMessageJedisDao personalMessageJedisDao;
	private JedisPool metadataJedisPool;
	private JedisPool custTagJedisPool;
	public IProgramMessageService programMessageService;
	//默认
	private int latch = 10;

	//订阅标签动作
	@Override
	public void tag2Uid(Object tagMess) {
		JedisPool pool = ((PersonalMessageJedisDaoImpl)personalMessageJedisDao).getJedisPool();
		Jedis jedis = pool.getResource();
		try {
			//uid->tag
			JSONObject jo = new JSONObject(tagMess.toString());
			String tag = jo.get("tag").toString();
			String base64Name = Coder.getBASE64(tag);
			String tag2UidKey = ConstantMess.TAG_PREX+base64Name;

			if(ConstantMess.OP_ADD.equals(jo.getString("op"))) jedis.lpush(tag2UidKey,jo.getString("uid"));
			if(ConstantMess.OP_DEL.equals(jo.getString("op"))) jedis.lrem(tag2UidKey,0,jo.getString("uid"));

		} catch (JSONException e) {
			e.printStackTrace();
		}finally {
			pool.returnResource(jedis);
		}
	}

	//打、删除自定义标签动作
	//用户自定义标签{"uid":"yyyyyyyyyyyyyy","tags":["tag1","tag2"],"sid":"3fb2m7u9hjmn","op":"del"}
	@Override
	public void custTag2Sid(Object tagMess) {
		JedisPool pool = ((PersonalMessageJedisDaoImpl)personalMessageJedisDao).getJedisPool();
		Jedis jedis = pool.getResource();
		try {
			JSONObject jo = new JSONObject(tagMess.toString());
			JSONArray tagsJo = jo.getJSONArray("tags");
			HashSet<String> tags = new HashSet<String>();
			for (int i = 0; i < tagsJo.length();i++) tags.add(tagsJo.getString(i));

			for (String tag : tags) {
				if(isNullOrEmpty(tag)) continue;
				String sid = jo.getString("sid");
				Double currCnt = new Double(-1);

				System.out.println("process tag is:"+tag);
				String sid2custTagKey = ConstantMess.SID2CUST_TAG_PREX + sid;
				//sid->custTag  score=persons
				String base64CustTag = Coder.getBASE64(tag.toString());
				if (ConstantMess.OP_ADD.equals(jo.getString("op")))currCnt = jedis.zincrby(sid2custTagKey, 1, base64CustTag);
				if (ConstantMess.OP_DEL.equals(jo.getString("op")))currCnt = jedis.zincrby(sid2custTagKey, -1, base64CustTag);

				//跨入10门槛:1,将此节目加入tag-sid列表 2,触发订阅标签消息
				if (currCnt == latch && ConstantMess.OP_ADD.equals(jo.getString("op"))) {
					System.out.println("the latch is:"+currCnt);
					addSidInTag(sid,tag,ConstantMess.OP_CUST);

					//触发订阅标签消息
					if (ConstantMess.OP_ADD.equals(jo.getString("op"))) {
						JSONObject tagJo = new JSONObject();
						Jedis metaJedis = metadataJedisPool.getResource();
						try {
							String joStr = metaJedis.get(CodeIDOperator.codeToId(sid).toString());
							JSONObject metaJo = new JSONObject(joStr);
							String title = metaJo.getString("title");
							String icon1 = metaJo.getString("icon1");
							tagJo.put("newTags", tag);
							tagJo.put("sid", sid);
							tagJo.put("title", title);
							tagJo.put("icon1", icon1);
							programMessageService.makeMess(tagJo);
						}finally {
							metadataJedisPool.returnResource(metaJedis);
						}
					}
					//触发订阅标签消息
				}
				//跨入10门槛 将此节目加入tag-sid列表


				//如果标签跌入10以下 触发用户自定义
				if (currCnt == latch - 1 && ConstantMess.OP_DEL.equals(jo.getString("op"))) {
					removeSidInTag(sid,tag,ConstantMess.OP_CUST);
				}
				//如果count(sid,tag) < 10 触发用户自定义
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}finally {
			pool.returnResource(jedis);
		}
	}

	@Override
	public void removeSidInTag(String sid, String tag,String type) {
		Jedis custJedis = custTagJedisPool.getResource();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String base64Name = Coder.getBASE64(tag);
			String sidInTagKey = base64Name + ConstantMess.TAG_SID_TAIL;
			Integer id = CodeIDOperator.codeToId(sid);
			Set<String> allValues = custJedis.zrange(sidInTagKey, 0, -1);
			for (String v : allValues){
				try{
					JSONObject tmpJo = new JSONObject(v);
					if(tmpJo.getString("contentId").equals(id.toString())){
						System.out.println("in");
						if(!tmpJo.has("isCust")&&ConstantMess.OP_DOUBAN.equals(type)) custJedis.zrem(sidInTagKey,v);
						if(tmpJo.has("isCust")&&tmpJo.getBoolean("isCust")&&ConstantMess.OP_CUST.equals(type)){
							custJedis.zrem(sidInTagKey,v);
						}
					}
				}catch (JSONException e){
					System.out.println(e);
				}
			}
		} finally {
			custTagJedisPool.returnResource(custJedis);
		}
	}

	public void addSidInTag(String sid, String tag,String type){
		Date now = new Date();
		Jedis custJedis = custTagJedisPool.getResource();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			boolean hasSame = false;
			String base64Name = Coder.getBASE64(tag);
			String custTag2sid = base64Name + ConstantMess.TAG_SID_TAIL;
			Integer id = CodeIDOperator.codeToId(sid);
			Set<String> allValues = custJedis.zrange(custTag2sid, 0, -1);
			for (String v : allValues){
				JSONObject tmpJo = new JSONObject(v);
				if(tmpJo.get("contentId").equals(id.toString())){
					hasSame = true;
					break;
				}
			}
			//没有相同
			if(!hasSame){
				JSONObject custTagJo = new JSONObject();
				custTagJo.put("publishTime", format.format(now));
				custTagJo.put("contentId", id.toString());
				if(ConstantMess.OP_CUST.equals(type))custTagJo.put("isCust",true);
				custJedis.zadd(custTag2sid, now.getTime(), custTagJo.toString());
			}
		} catch (JSONException e){
			System.out.println(e);
		} finally {
			custTagJedisPool.returnResource(custJedis);
		}
	}

	public IPersonalMessageJedisDao getPersonalMessageJedisDao() {
		return personalMessageJedisDao;
	}
	public void setPersonalMessageJedisDao(
			IPersonalMessageJedisDao personalMessageJedisDao) {
		this.personalMessageJedisDao = personalMessageJedisDao;
	}

	public JedisPool getMetadataJedisPool() {
		return metadataJedisPool;
	}

	public void setMetadataJedisPool(JedisPool metadataJedisPool) {
		this.metadataJedisPool = metadataJedisPool;
	}

	public JedisPool getCustTagJedisPool() {
		return custTagJedisPool;
	}

	public void setCustTagJedisPool(JedisPool custTagJedisPool) {
		this.custTagJedisPool = custTagJedisPool;
	}

	public int getLatch() {
		return latch;
	}

	public void setLatch(int latch) {
		this.latch = latch;
	}

	public IProgramMessageService getProgramMessageService() {
		return programMessageService;
	}

	public void setProgramMessageService(IProgramMessageService programMessageService) {
		this.programMessageService = programMessageService;
	}

//		public static void main(String[] args) throws JSONException {
//			Integer id = CodeIDOperator.codeToId("3fd3qrik3ff4");
//
//	}
}
