package com.moretv.dao.personal.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.moretv.common.constants.ConstantMess;
import com.moretv.dao.personal.IPersonalMessageJedisDao;

public class PersonalMessageJedisDaoImpl  implements IPersonalMessageJedisDao {
	private JedisPool jedisPool;
	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}


	public void addCollMess(String jsonMess) {
			Jedis jedis = jedisPool.getResource();
			try {
				JSONObject jsonobj = new JSONObject(jsonMess);
				String keyPrex = ConstantMess.FROM_TV.equals(jsonobj.getString("userType"))?ConstantMess.TV_COLLECT_MESS_INDEX_KY_PREX:ConstantMess.USER_COLLECT_MESS_INDEX_KY_PREX;
				String collKey = keyPrex + jsonobj.getString("uid");
				
				String scoreTimeStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				long scoreTime = Long.parseLong(scoreTimeStr);
				
				jedis.zadd(collKey, scoreTime, jsonobj.getString("sid"));
			} catch (JSONException e) {
				e.printStackTrace();
			}finally{
				jedisPool.returnResource(jedis);
			}
	}
	
	public void delCollMess(String jsonMess) {
		Jedis jedis = jedisPool.getResource();
		try {
			JSONObject jsonobj = new JSONObject(jsonMess);
			String keyPrex = ConstantMess.FROM_TV.equals(jsonobj.getString("userType"))?ConstantMess.TV_COLLECT_MESS_INDEX_KY_PREX:ConstantMess.USER_COLLECT_MESS_INDEX_KY_PREX;
			String collKey = keyPrex + jsonobj.getString("uid");
			
			jedis.zrem(collKey, jsonobj.getString("sid"));
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
			jedisPool.returnResource(jedis);
		}
	}

	public Object getSystemMeaasge(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public long saveJson(String key, long score, Object systemMessage) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void bufferAndProcessMess(String jsonMess){
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.lpush(ConstantMess.BUFFER_KEY, jsonMess);
		}finally{
			jedisPool.returnResource(jedis);
		}
	}
	
	
	
	

}
