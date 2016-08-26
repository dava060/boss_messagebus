package com.moretv.dao.personal;

public interface IPersonalMessageJedisDao {
	public void addCollMess(String collMess);
	public void delCollMess(String collMess);
	public void bufferAndProcessMess(String jsonMess);
}

