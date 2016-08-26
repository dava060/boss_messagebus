package com.moretv.common.daosupport; 

import redis.clients.jedis.JedisPool;


/** 
 * JedisDaoSupport.java 
 *
 * author:Amandayang
 * 
 * Date 2014-3-11 下午02:20:33
 */
public class JedisDaoSupport {
	private JedisPool systemMessageJedisPool;
	public JedisPool getSystemMessageJedisPool() {
		return systemMessageJedisPool;
	}
	public void setSystemMessageJedisPool(JedisPool systemMessageJedisPool) {
		this.systemMessageJedisPool = systemMessageJedisPool;
	}
}
 
