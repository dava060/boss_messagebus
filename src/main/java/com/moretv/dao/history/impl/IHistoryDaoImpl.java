package com.moretv.dao.history.impl; 

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.moretv.common.utils.mongo.MongoDaoSupport;
import com.moretv.dao.history.IHistoryDao;
import com.moretv.entity.History;



/** 
 * IHistoryDao.java 
 *
 * author:Amandayang
 * 
 * Date 2015-2-11 下午07:53:30
 */
public class IHistoryDaoImpl extends MongoDaoSupport implements IHistoryDao, Serializable {

	public History getHistory(String userType, String uid, String sid) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid).and("userType").is(userType).and("sid").is(sid));
		query.limit(10);
		List<History> list = this.mongoTemplate.find(query, History.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	
	}
}
 
