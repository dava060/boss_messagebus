package com.moretv.common.constants;

public class ConstantMess {
	//============ //operation type ================================================
	public static final String OP_DEL = "del";
	public static final String OP_ADD = "add";

	//============ // personal & program message ===================================
	public static final String USER_COLLECT_MESS_INDEX_KY_PREX = "u:collect:";
	public static final String TV_COLLECT_MESS_INDEX_KY_PREX = "t:collect:";
	public static final String UPDATED_PROGRAM_MESS_KY_PREX = "n:sid:";
	public static final String BUFFER_KEY = "collect:buffer:temp";

	//keep connection
	public static final String APP_KEY = "jA0KXMtpZwIEgAoyzGI9fn8OA5RsnHt8fn5bTzDnDDM=";
	public static final String APP_SECRET = "7Ycqazt-qnlwyYXHFepjAUIa0BsQsxyqHwe774JKSZfsriQrdIr4dRax5IIdIgqS";
	//keep connection API
	public static final String API_FORUSERS = "Api/PushForSpecificUsers";
	public static final String API_FORCLIENTS = "Api/PushForSpecificServices";
	public static final String API_FORONLINE = "Api/PushForOnlineClients";
	public static final String API_FORALL = "Api/BroadcastInNoTime";
	public static final String API_FORALLBATCHES = "Api/BroadcastInBatches";
	public static final String API_FORDEVICEID = "Api/PushForSpecificClients";
	public static final String API_FORSN = "Api/PushForSpecificSns/";

	/** 为8位 和 32位 区分消息	 */
	public static final int ACCOUNT = 1;
	public static final int MAC = 2;

	//APT type
	/**	给指定8位用户发 */
	public static final int FORUSERS = 1;
	/**	给指定32位客户端发 */
	public static final int FORCLIENTS = 2;
	/**	给在线所有设备发 */
	public static final int FORONLINE = 3;
	/**	给所有设备发 */
	public static final int FORALLCLIENTS = 4;
	/**	按策略发 */
	public static final int FORBATCHES = 5;
	/** 给指定deviceID设备发 */
	public static final int FORDEVICEID = 6;
	/** 给指定SN设备发 */
	public static final int FORSN = 7;
	/** 给指定8位用户或32位客户端发 */
	public static final int FORUSERORCLIENT = 12;

	//============ //tag ================================================
	//tag mess
	public static final String TAG_PREX = "tag2uid:";
	public static final String TAG_MESS_PREX = "uid2tagmess:";

	//tag2sid
	public static final String TAG_SID_TAIL = "_sortedByUpdateTime";

	//operate tag type
	public static final String OP_DOUBAN = "douban";
	public static final String OP_CUST = "cust";

	//custom tag
	public static final String SID2CUST_TAG_PREX = "sid2custTag:";

	//============ //mess KEY ================================================
	//star mess，明星
	public static final String STAR_PREX = "star2uid:";
	public static final String STAR_MESS_PREX = "uid2starmess:";

	// BOSS mess key
	public static final String PAY_MESS_PREX = "uid2paymess:";//pht,
	public static final String MEMBER_EFFECTIVE_TIME_MESS_PREX = "uid2Membermess:";//pht,2016-04-20 从@唐志成收到消息（你的微鲸会员即将于2016-04-20过期，快到帐户中心-微鲸会员续费吧）
	public static final String KEY_LOG_SWITCH_PREX = "logSwitch:";//日志开关

	//system message
	public static final String SYSTEM_MESS_INDEX_KY_PREX = "sm:userType:";
	public static final String SYSTEM_MESS_KY_PREX = "sm:uid:";

	//message source
	public static final String FROM_TV = "tv";
	public static final String FROM_MOBILE = "user";


	//============ //mess type ================================================
	/** 追剧更新 */
	public static final int ZHUIJU_MESS = 1;
	/** 升级提示 */
	public static final int UPGRADE_MESS = 2;
	/** 系统公告 */
	public static final int SYS_MESS = 3;
	/**消息推送 */
	public static final int PUSH_UPDATE_MESS = 4;
	/**预约消息提醒*/
	public static final int YUYUE_UPDATE_MESS = 5;
	/** 综艺更新 */
	public static final int ZONGYI_UPDATE_MESS = 6;
	/** 订阅标签 */
	public static final int TAG_UPDATE_MESS = 7;
	/** 订阅标签 */
	public static final int STAR_UPDATE_MESS = 8;
	/** pht,BOSS系统（的湘云）传来的消息（提醒会员到期）， 2015-11*/
	public static final int PAY_UPDATE_MESS = 9;
	/** pht,BOSS系统（的程旗）传来的消息（日志开关）， 2016-02-02*/
	public static final int TYPE_LOG_SWITCH_MESS = 10;
	/** pht,BOSS系统（的唐志成）传来的消息（微鲸会员过期消息提醒～下一步就要付会员费）， 2016-04-20*/
	public static final int TYPE_MEMBER_EFFECTIVE_TIME_MESS = 11;

}
