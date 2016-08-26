package com.moretv.service.tag;

public interface ITagMessageService {
	public void tag2Uid(Object o);
	public void custTag2Sid(Object o);
	public void removeSidInTag(String sid,String tag,String type);
	public void addSidInTag(String sid,String tag,String type);
}
