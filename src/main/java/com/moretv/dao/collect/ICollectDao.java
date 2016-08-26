package com.moretv.dao.collect; 

import com.moretv.entity.Collect;


/** 
 * ICollectDao.java 
 *
 * author:Amandayang
 * 
 * Date 2015-2-11 下午07:42:45
 */
public interface ICollectDao {
	
	public void save(Collect collect);
	
	public Collect findCollect(String userType, String uid, String sid);
	

}
 
