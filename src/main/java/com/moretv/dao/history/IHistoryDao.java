package com.moretv.dao.history; 

import com.moretv.entity.History;


/** 
 * IHistory.java 
 *
 * author:Amandayang
 * 
 * Date 2015-2-11 下午07:52:58
 */
public interface IHistoryDao {
	
	public History getHistory(String userType, String uid, String sid);

}
 
