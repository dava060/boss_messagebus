package com.moretv.dao.system;

public interface ISystemMessageJedisDao {
	public void save(String sysMess);
	public String getSystemMeaasge(String key);
}

