package com.moretv.message.history;

import com.moretv.service.history.IHistoryMessageService;
import com.moretv.service.program.IProgramMessageService;

public class HistoryListener {
	private IHistoryMessageService historyMessageService;
	public void listenDelHistory(Object delMess){
		historyMessageService.truncate(delMess);
	}

	public IHistoryMessageService getHistoryMessageService() {
		return historyMessageService;
	}

	public void setHistoryMessageService(IHistoryMessageService historyMessageService) {
		this.historyMessageService = historyMessageService;
	}
}
