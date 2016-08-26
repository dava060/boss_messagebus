package com.moretv.dao.system.impl;



import org.json.JSONException;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.moretv.common.constants.ConstantMess;
import com.moretv.dao.system.ISystemMessageJedisDao;
//有时间用springdata
public class SystemMessageJedisDaoImpl  implements ISystemMessageJedisDao {
	private JedisPool jedisPool;
	public void save(String jsonMess) {
		Jedis jedis = jedisPool.getResource();
		try {
			JSONObject jsonobj = new JSONObject(jsonMess);
			
			String sysIndexKey = ConstantMess.SYSTEM_MESS_INDEX_KY_PREX + jsonobj.getString("userType");
			long scoreTime = Long.parseLong(jsonobj.getString("publishTime"));
			jedis.zadd(sysIndexKey, scoreTime, jsonobj.getString("uid"));
			
			String messKey = ConstantMess.SYSTEM_MESS_KY_PREX+jsonobj.getString("uid");
//			System.out.println("system mess:"+jsonMess);
			jedis.set(messKey, jsonMess);
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
			jedisPool.returnResource(jedis);
		}
	}

	public String getSystemMeaasge(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

}
