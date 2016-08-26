package com.moretv.service.personal.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.moretv.common.utils.CodeIDOperator;
import com.moretv.common.utils.Coder;
import com.moretv.dao.personal.impl.PersonalMessageJedisDaoImpl;
import com.moretv.message.program.ProgramMessageListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.moretv.common.constants.ConstantMess;
import com.moretv.dao.personal.IPersonalMessageJedisDao;
import com.moretv.service.personal.IPersonalMessageService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PersonalMessageServiceImpl implements IPersonalMessageService{
	private IPersonalMessageJedisDao personalMessageJedisDao;
	public void syncMessagePool(Object collMess) {
		try {
			JSONObject jsonObj = new JSONObject(collMess.toString());
			if(ConstantMess.OP_ADD.equals(jsonObj.getString("operation"))){
				personalMessageJedisDao.addCollMess(collMess.toString());
			}
			if(ConstantMess.OP_DEL.equals(jsonObj.getString("operation"))) personalMessageJedisDao.delCollMess(collMess.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void Star2Uid(Object starMess) {
		JedisPool pool = ((PersonalMessageJedisDaoImpl)personalMessageJedisDao).getJedisPool();
		Jedis jedis = pool.getResource();
		try {
			JSONObject jo = new JSONObject(starMess.toString());
			String star = jo.get("star").toString();
			String base64Name = Coder.getBASE64(star);
			String star2UidKey = ConstantMess.STAR_PREX+base64Name;

			if(ConstantMess.OP_ADD.equals(jo.getString("op")))jedis.lpush(star2UidKey,jo.getString("uid"));
			if(ConstantMess.OP_DEL.equals(jo.getString("op")))jedis.lrem(star2UidKey,0,jo.getString("uid"));
		} catch (JSONException e) {
			e.printStackTrace();
		}finally {
			pool.returnResource(jedis);
		}
	}
	public IPersonalMessageJedisDao getPersonalMessageJedisDao() {
		return personalMessageJedisDao;
	}
	public void setPersonalMessageJedisDao(
			IPersonalMessageJedisDao personalMessageJedisDao) {
		this.personalMessageJedisDao = personalMessageJedisDao;
	}

}
