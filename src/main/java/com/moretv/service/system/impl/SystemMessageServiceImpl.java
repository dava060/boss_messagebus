package com.moretv.service.system.impl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.moretv.dao.system.ISystemMessageJedisDao;
import com.moretv.service.system.ISystemMessageService;

public class SystemMessageServiceImpl implements ISystemMessageService {
	private ISystemMessageJedisDao systemMessageJedisDao;
	private RabbitTemplate rabbitTemplate;
	public void syncMessagePool(Object sysMess) {
		systemMessageJedisDao.save(sysMess.toString());
	}
	public ISystemMessageJedisDao getSystemMessageJedisDao() {
		return systemMessageJedisDao;
	}
	public void setSystemMessageJedisDao(
			ISystemMessageJedisDao systemMessageJedisDao) {
		this.systemMessageJedisDao = systemMessageJedisDao;
	}
	public RabbitTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}
	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	

}
