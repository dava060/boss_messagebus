
package com.moretv.common.utils.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * MongoDB DAO 支持类
 * 
 * @author WangZx
 * 
 * Date 2012-12-29 下午02:24:30
 * 
 */
public class MongoDaoSupport {

	protected MongoTemplate mongoTemplate;

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	
}
