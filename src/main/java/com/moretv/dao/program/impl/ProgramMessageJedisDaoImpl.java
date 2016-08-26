package com.moretv.dao.program.impl;

import org.json.JSONException;
import org.json.JSONObject;





import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.moretv.common.constants.ConstantMess;
import com.moretv.dao.program.IProgramMessageJedisDao;

public class ProgramMessageJedisDaoImpl  implements IProgramMessageJedisDao {
	private JedisPool jedisPool;
	public void updateProgram(String updateMess) {
		Jedis jedis = jedisPool.getResource();
		try {
			JSONObject jsonobj = new JSONObject(updateMess);
			String programKey = ConstantMess.UPDATED_PROGRAM_MESS_KY_PREX + jsonobj.getString("sid");
			jedis.set(programKey, updateMess);
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
			jedisPool.returnResource(jedis);
		}
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}



}
