package com.moretv.dao.collect.impl; 

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.moretv.common.utils.mongo.MongoDaoSupport;
import com.moretv.dao.collect.ICollectDao;
import com.moretv.entity.Collect;



/** 
 * ICollectDaoImpl.java 
 *
 * author:Amandayang
 * 
 * Date 2015-2-11 下午07:43:20
 */
public class ICollectDaoImpl extends MongoDaoSupport implements ICollectDao, Serializable  {

	/**
	 * 保存收藏
	 */
	public void save(Collect collect) {
		// TODO Auto-generated method stub
		this.mongoTemplate.save(collect);
	}

	
	/**
	 * 获取收藏
	 */
	public Collect findCollect(String userType, String uid, String sid) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid).and("userType").is(userType).and("sid").is(sid));
		List<Collect> list = this.mongoTemplate.find(query, Collect.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	

}
 
