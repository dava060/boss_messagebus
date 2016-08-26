


--------//部署BOSS的message_bus的测试环境搭建一套--------    
弄好后和金梅说一下，3天内。
    【redis IP】：10.10.164.31（测试环境的），10.10.92.128（真实环境的）    

    【测试机：】helios rabbitMQ的消费者～10.10.90.59
        MSD的测试机10.10.145.124没有安装java，

    【测试环境的rabbitMQ的ip地址】BOSS的message_bus的测试环境的rabbitMQ的ip地址是？   
        索湘云,java告诉的：
        测试环境IP：10.10.88.242
        现网环境IP【配置到现网环境上】：10.10.119.17        


--------//消息队列；--------
# 消息队列：
    队列名称： sendMemberExpiredQueue 
    队列内容：uid：xxx   content:xxxxx sid:xxxx 
    {"sn":"KA1529A000000","content":"请在2016/10/31前到设置-微鲸账户领取微鲸会员","sid":"3fvw6k4ffh3e"}

索湘云,java 2016-1-18 21:16:52
{"sn":"XX1501VIPTEST00041","content":"请在2016年1月21日前到设置-微鲸帐户领取微鲸会员","sid":"1453122871422"}
葡萄  21:25:17
rabbitMQ队列名是?
索湘云,java  21:25:56
sendMemberExpiredQueue

--------//TODO；--------
关闭代码中的其它监听器（消费者），applicationContext-rabbitmq.xml

--------//部署流程；--------
备份现网文件->上传升级文件->停止服务（kill -9 xxx）->修改配置文件->启动服务->测试ok->git提交代码

--------//现网机器配置；--------
IP：     10.10.26.94
目录：     /data/webapps/dolphin-message_bus

--------//java文件（现网部署相关的）；--------
boss_messagebus/src/main/java/com/moretv/App.java

                String configFileCatalog = "/home/putao/code/java_code/boss_messagebus/resource/";//测试环境
        //        String configFileCatalog = "/data/webapps/dolphin-message_bus/config/";//生产环境    
    
    
boss_messagebus/src/main/java/com/moretv/service/program/impl/ProgramMessageServiceImpl.java

        String addProgramToString = new String((byte[])addProgram);//TODO：测使用（给rabbitMQ后台发消息用的）
        //			String addProgramToString = addProgram.toString();//TODO:生产用(给程序发消息用的)


--------//配置文件（现网部署相关的）；--------
boss_messagebus/resource/applicationContext.xml

    <value>file:/home/putao/code/java_code/helios_messagebus/resource/redis.properties</value>
    <value>file:/home/putao/code/java_code/helios_messagebus/resource/application.properties</value>

boss_messagebus/resource/redis.properties     
   
    jedis.hsitory.host=172.16.10.56
    #jedis.hsitory.host=10.6.2.188

    rabbitmq.host=172.16.10.56
    #rabbitmq.host=10.6.20.203        

        
        
--------//配置文件（和部署无关）；--------
boss_messagebus/resource/applicationContext-rabbitmq.xml


--------//tips；------------

搭建测试环境：新建消费者.jar，2015-10-15
{"a":"b"}

索湘云：
    队列名称： sendMemberExpiredQueue 
    队列内容：uid：xxx   content:xxxxx sid:xxxx 
    {"sn":"KA1529A000000","content":"请在2016/10/31前到设置-微鲸账户领取微鲸会员","sid":"3fvw6k4ffh3e"}
张董：
﻿    消费者jar：10.10.26.94
        /data/webapps/dolphin-message_bus/message_bus.jar
    rabbit 内网ip： 10.10.88.242
              外网ip：120.132.56.30【user:root / passwd:moretv2015!@# 】
              安装目录：/home/moretv/install/rabbitmq/rabbitmq_server-3.4.2
    rabbit 后台访问：http://120.132.56.30:15672     
部署：
    /data/webapps/dolphin-message_bus
    java -jar -Djava.util.Arrays.useLegacyMergeSort=true message_bus.jar
    
杨苗：
    redis IP：10.10.164.31（测试环境的），10.10.92.128（真实环境的）
    参照明星（star）来做，
    type=9

    代码参照：
    /home/putao/code/java_code/helios_messagebus/src/main/java/com/moretv/service/program/impl
    /ProgramMessageServiceImpl.java
    produceStarMess()
        jedis.hset(ConstantMess.STAR_MESS_PREX + uid, base64Name,json.toString());
        uid2paymess:xxx(uid) ;    

--------//End；------------



