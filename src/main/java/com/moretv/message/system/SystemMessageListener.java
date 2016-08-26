package com.moretv.message.system;

import com.moretv.service.system.ISystemMessageService;


public class SystemMessageListener {
	public ISystemMessageService systemMessageService;
	public void listen(Object sysMess){
		System.out.println("sysMess:"+sysMess);
		systemMessageService.syncMessagePool(sysMess);
	}
	
	public ISystemMessageService getSystemMessageService() {
		return systemMessageService;
	}
	public void setSystemMessageService(ISystemMessageService systemMessageService) {
		this.systemMessageService = systemMessageService;
	}
}
