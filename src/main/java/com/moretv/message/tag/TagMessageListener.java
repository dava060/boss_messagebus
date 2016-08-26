package com.moretv.message.tag;

import com.moretv.service.personal.IPersonalMessageService;
import com.moretv.service.tag.ITagMessageService;

public class TagMessageListener {
	public ITagMessageService tagMessageService;

	public void listenTag(Object TagMess){ tagMessageService.tag2Uid(TagMess); }

	public void listenCustTag(Object TagMess){ tagMessageService.custTag2Sid(TagMess); }

	public ITagMessageService getTagMessageService() {
		return tagMessageService;
	}

	public void setTagMessageService(ITagMessageService tagMessageService) {
		this.tagMessageService = tagMessageService;
	}
}
