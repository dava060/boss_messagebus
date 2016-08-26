package com.moretv.message.personal;

import com.moretv.service.personal.IPersonalMessageService;

public class PersonMessageListener {
	public IPersonalMessageService personalMessageService;
	public void listenCollection(Object collMess){
		personalMessageService.syncMessagePool(collMess);
	}

	public void listenStar(Object starMess){
		personalMessageService.Star2Uid(starMess);
	}

	public IPersonalMessageService getPersonalMessageService() {
		return personalMessageService;
	}

	public void setPersonalMessageService(IPersonalMessageService personalMessageService) {
		this.personalMessageService = personalMessageService;
	}
}
