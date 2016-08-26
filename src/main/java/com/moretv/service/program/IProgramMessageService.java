package com.moretv.service.program;

import org.json.JSONObject;

import java.util.Map;

public interface IProgramMessageService {
	public void syncMessagePool(Object obj);
	public void produceStarMess(Object obj);
	public void produceTagMess(Object obj);
	//豆瓣标签列表调整
	public void syncSidsInTagForDouban(JSONObject jo);
	//标签新增，变更消息生成，include：豆瓣 自定义
	public void makeMess(JSONObject jo);

	/**
	 * pht,boss,2015-10-15，监听索湘云的会员过期的消息，
	 * 生成消息（过期消息,type=9） -- 把rabbitMQ消息（BOSS系统传来的） 存入到 redis（交互系统）
	 * @param obj
	 */
	public void produceMemberExpiredMess(Object obj);

	/**
	 * pht,boss,2016-04-20，监听唐志成的~会员到期消息～下一步就要付会员费
	 * 生成消息（过期消息,type=11） -- 把rabbitMQ消息（BOSS系统传来的） 存入到 redis（交互系统）
	 * @param obj
	 */
	public void produceMemberEffectiveTime(Object obj);

	/**
	 * pht,boss,2016-02-02，监听程旗的日志开关的消息，
	 * 生成消息（日志开关消息, type=10） -- 把rabbitMQ消息（BOSS系统传来的） 存入到 redis（交互系统）
	 * @param obj 消息队列传来的消息（json格式）
	 */
	public void produce_terminalLogSwitchMess(Object obj);

}
