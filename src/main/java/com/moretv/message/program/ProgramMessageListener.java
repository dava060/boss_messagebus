package com.moretv.message.program;

import com.moretv.service.personal.IPersonalMessageService;
import com.moretv.service.program.IProgramMessageService;

public class ProgramMessageListener {
	private IProgramMessageService programMessageService;
	public void listenUpdate(Object updateMess){
		programMessageService.syncMessagePool(updateMess);
	}
	public void listenAdd(Object updateMess){
		programMessageService.produceStarMess(updateMess);
	}

	// pht,boss,2015-10-15，监听索湘云的~会员过期的消息，
	public void listenMemberExpired(Object updateMess){
		programMessageService.produceMemberExpiredMess(updateMess);
	}

	// pht,boss,2016-04-20，监听唐志成的~会员到期消息，
	public void listenMemberEffectiveTime(Object updateMess){
		programMessageService.produceMemberEffectiveTime(updateMess);
	}

	// pht,boss,2016-02-02，监听程旗的日志开关的消息，
	public void listen_terminalLogSwitchMessQueue(Object updateMess){
		programMessageService.produce_terminalLogSwitchMess(updateMess);
	}

	public void listenTag(Object tagMess){
		programMessageService.produceTagMess(tagMess);
	}

	public IProgramMessageService getProgramMessageService() {
		return programMessageService;
	}
	public void setProgramMessageService(
			IProgramMessageService programMessageService) {
		this.programMessageService = programMessageService;
	}
	
}
