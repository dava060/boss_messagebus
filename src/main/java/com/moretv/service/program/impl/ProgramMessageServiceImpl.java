package com.moretv.service.program.impl;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.common.base.Joiner;
import com.moretv.common.keepConnect.IMessageable;
import com.moretv.common.utils.Coder;
import com.moretv.dao.program.impl.ProgramMessageJedisDaoImpl;
import com.moretv.service.tag.ITagMessageService;
import org.json.JSONException;
import org.json.JSONObject;

import com.moretv.common.constants.ConstantMess;
import com.moretv.dao.program.IProgramMessageJedisDao;
import com.moretv.service.program.IProgramMessageService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import static com.google.common.base.Strings.isNullOrEmpty;

public class ProgramMessageServiceImpl implements IProgramMessageService{
	public ITagMessageService tagMessageService;

	public IMessageable messageable;

	private static JSONObject getMessToken(){
		JSONObject json = new JSONObject();
		try{
			json.put("sid","");
			json.put("title","");
			json.put("content","");
			json.put("url","");
			json.put("icon", "");
			json.put("contentType","");
			json.put("displayTime", "5");
			String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			json.put("createTime", createTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	private IProgramMessageJedisDao programMessageJedisDao;
	public void syncMessagePool(Object updateMess) {
		try {
			String buildedMess = translateMess(new JSONObject(updateMess.toString())).toString();
			System.out.println(buildedMess);
			programMessageJedisDao.updateProgram(buildedMess);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private JSONObject translateMess(JSONObject jsonUpdated){
		JSONObject jsonResult = new JSONObject();
		String content = "";
		String num;
		try {
			num = jsonUpdated.getString("episode");
			String title = jsonUpdated.getString("title");
			jsonResult.put("sid", jsonUpdated.get("sid"));
			jsonResult.put("title", jsonUpdated.get("title"));
			if("zongyi".equals(jsonUpdated.getString("contentType"))){
				if(num.length() == 8){
					content = "<${title}> 已更新至${num}期".replace("${title}",title).replace("${num}", num.substring(4));
				}else{
					content = "<${title}> 已更新至${num}集".replace("${title}",title).replace("${num}", num);
				}
				jsonResult.put("content", content);
				jsonResult.put("type",ConstantMess.ZONGYI_UPDATE_MESS);
			}else if("movie".equals(jsonUpdated.get("contentType"))){
				content = "<${title}> 上线了".replace("${title}", title);
				jsonResult.put("content", content);
				jsonResult.put("type",ConstantMess.YUYUE_UPDATE_MESS);
			}else {
				content = "<${title}> 已更新至${num}集".replace("${title}",title).replace("${num}", num);
				
				jsonResult.put("content", content);
				jsonResult.put("type",ConstantMess.ZHUIJU_MESS);
			}
			
			jsonResult.put("url","");
			jsonResult.put("icon",jsonUpdated.getString("icon1"));
			jsonResult.put("displayTime","5");
			jsonResult.put("contentType",jsonUpdated.has("contentType")?jsonUpdated.getString("contentType") : "");
			
			Date now = new Date();
			String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
			jsonResult.put("createTime", createTime);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonResult;
	}

	//redis操作去掉到层
	@Override
	public void produceStarMess(Object addProgram) {
		JedisPool pool = ((ProgramMessageJedisDaoImpl) programMessageJedisDao).getJedisPool();
		Jedis jedis = pool.getResource();
		try {
			String[] starArray = new String[0];
			JSONObject jo = new JSONObject(addProgram.toString());
			if(jo.get("star") != null)starArray = jo.getString("star").toString().split("\\|");

			for(String starName:starArray){
				String base64Name = Coder.getBASE64(starName);
				String star2uidKey = ConstantMess.STAR_PREX+base64Name;
				List<String> uidList = jedis.lrange(star2uidKey, 0, jedis.llen(star2uidKey));
				for (String uid :uidList){
					JSONObject json = getMessToken();
					json.put("content","您收藏的明星：" + starName + " 有新作品了，《"+jo.getString("title").toString()+"》");
					json.put("type",ConstantMess.STAR_UPDATE_MESS);
					json.put("title",starName);
					json.put("sid",jo.getString("sid"));
					json.put("contentType",jo.has("contentType")?jo.getString("contentType") : "");
					jedis.hset(ConstantMess.STAR_MESS_PREX + uid,base64Name,json.toString());
//					jedis.lpush(ConstantMess.STAR_MESS_PREX + uid, json.toString());
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}finally {
			pool.returnResource(jedis);
		}
	}



	// pht,boss,2015-10-15，监听索湘云的会员过期的消息，
	// 生成过期消息 -- 把rabbitMQ消息（BOSS系统传来的） 存入到 redis（交互系统）
	@Override
	public void produceMemberExpiredMess(Object addProgram) {
		System.out.println("produceMemberExpiredMess().begin");
		JedisPool pool = ((ProgramMessageJedisDaoImpl) programMessageJedisDao).getJedisPool();
		Jedis jedis = pool.getResource();
		try {
			String[] starArray = new String[0];
			//String addProgramToString = new String((byte[])addProgram);//TODO：测使用（给rabbitMQ后台发消息用的）
			String addProgramToString = addProgram.toString();//TODO:生产用(给程序发消息用的)
			System.out.println("addProgram.toString():");
			System.out.println(addProgramToString);


			JSONObject jo = new JSONObject(addProgramToString);
			System.out.println("addProgram.toString()2:");
			System.out.println(addProgram.toString());
			System.out.println("jo:");
			System.out.println(jo);


			JSONObject json = getMessToken();
			json.put("title", jo.getString("content"));
			json.put("content", jo.getString("content"));
			json.put("contentType", "");			// 默认为空就可以，交互组暂不需要此数据，
			json.put("sn", jo.getString("sn"));		// 电视机的sn（序列号）
			json.put("type", ConstantMess.PAY_UPDATE_MESS);
			json.put("sid", jo.getString("sid"));
			messageable.sendMessage(json.toString(),jo.getString("sn"),ConstantMess.FORSN); //向长连接发送会员过期消息
			jedis.hset(ConstantMess.PAY_MESS_PREX + jo.getString("sn"), Long.toString(System.currentTimeMillis())
					, json.toString());				//field参数不重要--交互组用redis的HVALS取数据

		} catch (JSONException e) {
			e.printStackTrace();
		}finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * pht,boss,2016-04-20，监听唐志成的~会员到期消息，
	 * @param addProgram
	 */
	@Override
	public void produceMemberEffectiveTime(Object addProgram) {
		System.out.println("produceMemberEffectiveTime().begin");
		JedisPool pool = ((ProgramMessageJedisDaoImpl) programMessageJedisDao).getJedisPool();
		Jedis jedis = pool.getResource();
		try {
			String[] starArray = new String[0];
			//String addProgramToString = new String((byte[])addProgram);//TODO：测使用（给rabbitMQ后台发消息用的）
			String addProgramToString = addProgram.toString();//TODO:生产用(给程序发消息用的)
			System.out.println("addProgram.toString():");
			System.out.println(addProgramToString);


			JSONObject jo = new JSONObject(addProgramToString);
			System.out.println("addProgram.toString()2:");
			System.out.println(addProgram.toString());
			System.out.println("jo:");
			System.out.println(jo);


			JSONObject json = getMessToken();
			json.put("title", jo.getString("content"));
			json.put("content", jo.getString("content"));
			json.put("contentType", "");			// 默认为空就可以，交互组暂不需要此数据，
			json.put("sn", jo.getString("sn"));		// 电视机的sn（序列号）
			json.put("type", ConstantMess.TYPE_MEMBER_EFFECTIVE_TIME_MESS);
			json.put("sid", jo.getString("sid"));
			messageable.sendMessage(json.toString(), jo.getString("sn"), ConstantMess.FORSN); //向长连接系统发送会员到期消息
			jedis.hset(ConstantMess.MEMBER_EFFECTIVE_TIME_MESS_PREX + jo.getString("sn") + ":" + jo.getString("whaleyAccount")
					, Long.toString(System.currentTimeMillis())
					, json.toString());				//field参数不重要--交互组用redis的HVALS取数据

		} catch (JSONException e) {
			e.printStackTrace();
		}finally {
			pool.returnResource(jedis);
		}
	}


	/**
	 * pht,boss,2016-02-02，监听程旗的日志开关的消息，
	 * <rabbit:queue name="terminalLogSwitchMessQueue" durable="true" />
	 * 生成消息（日志开关消息, type=10） -- 把rabbitMQ消息（BOSS系统传来的） 存入到 redis（交互系统）
	 * @param addProgram
	 */
	@Override
	public void produce_terminalLogSwitchMess(Object addProgram) {
		System.out.println("# produce_terminalLogSwitchMess()");
		JedisPool pool = ((ProgramMessageJedisDaoImpl) programMessageJedisDao).getJedisPool();
		Jedis jedis = pool.getResource();
		try {
			String[] starArray = new String[0];
			//String addProgramToString = new String((byte[])addProgram);//测使用（给rabbitMQ后台发消息用的）
			String addProgramToString = addProgram.toString();//生产用(给程序发消息用的)
			System.out.println("addProgram.toString():"+addProgramToString);

			JSONObject jo = new JSONObject(addProgramToString);
			System.out.println("addProgram.toString()2:");
			System.out.println(addProgram.toString());
			System.out.println("jo:");
			System.out.println(jo);


			/* 设置日志开关值 */
			// 1/2）从rabbitMQ（程旗）得到的开关值：开关类型（1：开启或关闭日志开关  2：上传日志开关）
			//开关值 （1：关闭  2：打开）
			String messageTypeStr = jo.getInt("messageType") == 1 ? "记录日志开关" : "上传日志开关";
			String openOrCloseStr = jo.getInt("openOrClose") == 1 ? "关闭" : "打开";
			String json_LogSwitchStr = openOrCloseStr + messageTypeStr;

			// 2/2）下发给杨苗redis的开关值：1-打开日志开关，2-关闭日志开关，3-上传日志
			int json_logSwitchValue = 0;
			int msg_messageType = jo.getInt("messageType");
			int msg_openOrClose = jo.getInt("openOrClose");
			if (msg_messageType == 1) {		//1：开启或关闭日志开关
				if (msg_openOrClose == 1) {	//1：关闭日志开关
					json_logSwitchValue = 2;
				} else {					//2：打开日志开关
					json_logSwitchValue = 1;
				}
			}else{							//2：上传日志开关
				if (msg_openOrClose == 2) {	//2：打开
					json_logSwitchValue = 3;
				}
			}
			System.out.println("json_LogSwitchStr="+json_LogSwitchStr+"; json_logSwitchValue="+json_logSwitchValue);


			JSONObject json = getMessToken();
			json.put("title", json_LogSwitchStr);					// 日志开关
			json.put("content", String.valueOf(json_logSwitchValue));				// 日志开关
			json.put("contentType", "");						// 默认为空就可以，交互组暂不需要此数据，
			json.put("sn", jo.getString("sn"));					// 电视机的sn（序列号）
			json.put("type", ConstantMess.TYPE_LOG_SWITCH_MESS);
			json.put("sid", "");
			System.out.println("json=");
			System.out.println(json);
			messageable.sendMessage(json.toString(),jo.getString("sn"),ConstantMess.FORSN);//给长连接发送日志开关的消息

			//TODO: 去掉注释,生产环境里,2016-02-02
			jedis.hset(ConstantMess.KEY_LOG_SWITCH_PREX + jo.getString("sn"), Long.toString(System.currentTimeMillis())
					, json.toString());				//field参数不重要--交互组用redis的HVALS取数据  logSwitch:

		} catch (JSONException e) {
			e.printStackTrace();
		}finally {
			pool.returnResource(jedis);
		}
	}

	//约定好，进入此方法都是豆瓣相关，非custtag
	@Override
	public void produceTagMess(Object modiTag) {
		try {
			JSONObject jo = new JSONObject(modiTag.toString());
			String newTag = jo.has("newDoubanTag")?jo.getString("newDoubanTag"):null;
			//同步到tag--sid列表
			syncSidsInTagForDouban(jo);
			//生成mess
			if(!isNullOrEmpty(newTag))makeMess(jo);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	//{tag,sid,title,icon1}
	@Override
	public void makeMess(JSONObject jo) {
		JedisPool pool = ((ProgramMessageJedisDaoImpl) programMessageJedisDao).getJedisPool();
		Jedis jedis = pool.getResource();
		System.out.println("new mess");
		try {
			String status = jo.getString("status");
			if("0".equals(status))return;

			String sid = jo.getString("sid");
			String title = jo.getString("title");
			String icon1 = jo.getString("icon1");
			String newTagTemp = jo.has("newTags")?jo.getString("newTags"):"";
			String[] newTags = newTagTemp.split("\\|");
			for(String newTag : newTags){
				if (isNullOrEmpty(newTag)) continue;
				String base64Name = Coder.getBASE64(newTag);
				String tag2uidKey = ConstantMess.TAG_PREX+base64Name;
				List<String> uidList = jedis.lrange(tag2uidKey, 0, jedis.llen(tag2uidKey));
				for (String uid :uidList){
					JSONObject json = getMessToken();
					json.put("sid",sid);
					json.put("content", "标签“"+newTag+"”中更新了《"+title+"》");
					json.put("title",newTag);
					json.put("type",ConstantMess.TAG_UPDATE_MESS);
					json.put("icon",icon1);
					json.put("contentType",jo.has("contentType")?jo.getString("contentType") : "");
					jedis.hset(ConstantMess.TAG_MESS_PREX + uid,base64Name,json.toString());
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}finally {
			pool.returnResource(jedis);
		}
	}

	public void offlineProgramInTagList(String sid,String[] doubanTags){
		//offline douban
		for (String tag : doubanTags) {
			tagMessageService.removeSidInTag(sid, tag, ConstantMess.OP_DOUBAN);
		}

		//offline cust
		JedisPool messPool = ((ProgramMessageJedisDaoImpl) programMessageJedisDao).getJedisPool();
		Jedis jedis = messPool.getResource();
		try {
			String sid2custTagKey = ConstantMess.SID2CUST_TAG_PREX + sid;
			Set<String> custTags = jedis.zrange(sid2custTagKey, 0, -1);
			for (String tag : custTags){
				tagMessageService.removeSidInTag(sid, tag, ConstantMess.OP_CUST);
			}
		}finally {
			messPool.returnResource(jedis);
		}
	}


	@Override
	public void syncSidsInTagForDouban(JSONObject jo) {
		try{
			String status = jo.has("status")?jo.getString("status"):"1";
			String oldTag = jo.has("oldDoubanTag")?jo.getString("oldDoubanTag"):"";
			String newTag = jo.has("newDoubanTag")?jo.getString("newDoubanTag"):"";
			String sid = jo.getString("sid");
			String[] oldTags = oldTag.split("\\|");
			String[] newTags = newTag.split("\\|");

			//如果是节目下线（下线对应豆瓣和自定义）
			if("0".equals(status) && oldTags.length > 0) {
				this.offlineProgramInTagList(sid,oldTags);
				return;
			}

			//如果没有oldtag，直接下线，直接返回
			if("0".equals(status) && oldTags.length == 0) return;

			Set<String> toDels = new HashSet<String>(Arrays.asList(oldTags));
			Set<String> toAdds = new HashSet<String>(Arrays.asList(newTags));
			Set<String> tempOldTags = new HashSet<String>(Arrays.asList(oldTags));
			Set<String> tempNewTags = new HashSet<String>(Arrays.asList(newTags));

			toDels.removeAll(tempNewTags);
			toAdds.removeAll(tempOldTags);

			for (String tag : toDels) {
				tagMessageService.removeSidInTag(sid, tag, ConstantMess.OP_DOUBAN);
			}
			for (String tag : toAdds) {
				tagMessageService.addSidInTag(sid, tag, ConstantMess.OP_DOUBAN);
			}

			//newTags will be used in makeMess
			if(toAdds.size() > 0) jo.put("newTags", Joiner.on("|").join(toAdds));

		}catch (JSONException e){
			e.printStackTrace();
		}
	}

	public IProgramMessageJedisDao getProgramMessageJedisDao() {
		return programMessageJedisDao;
	}
	public void setProgramMessageJedisDao(
			IProgramMessageJedisDao programMessageJedisDao) {
		this.programMessageJedisDao = programMessageJedisDao;
	}

	public ITagMessageService getTagMessageService() {
		return tagMessageService;
	}

	public void setTagMessageService(ITagMessageService tagMessageService) {
		this.tagMessageService = tagMessageService;
	}

	public IMessageable getMessageable() {
		return messageable;
	}

	public void setMessageable(IMessageable messageable) {
		this.messageable = messageable;
	}
}
