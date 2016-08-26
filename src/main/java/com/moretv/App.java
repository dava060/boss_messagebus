package com.moretv;

import com.mongodb.util.Hash;
import com.moretv.service.history.impl.HistoryMessageServiceImpl;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import sun.misc.BASE64Decoder;

import java.util.*;

public class App {
    public static AbstractApplicationContext ctx = null;

    public static void main(String[] args) throws Exception {
//        FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(new String[] {"file:/Users/armstrong/work/java/helios_messagebus/resource/applicationContext.xml", "file:/Users/armstrong/work/java/helios_messagebus/resource/applicationContext-jedis.xml", "file:/Users/armstrong/work/java/helios_messagebus/resource/applicationContext-rabbitmq.xml" });
//default:      FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(new String[] { "file:/home/putao/code/java_code/helios_messagebus/config/applicationContext.xml", "file:/home/putao/code/java_code/helios_messagebus/config/applicationContext-jedis.xml", "file:/home/putao/code/java_code/helios_messagebus/config/applicationContext-rabbitmq.xml" });

        // config path, TODO:改成生产环境的,
        //String configFileCatalog = "F:/test/boss_messagebus/resource/";//本机环境：：

        String configFileCatalog = "/data/webapps/dolphin-message_bus/config/";//生产环境



       // String configFileCatalog = "/data/webapps/message_bus/config/";//测试环境
        //String configFileCatalog = "/data/webapps/dolphin-message-bus/config/";//开发机测试环境


        // load config
        String configFilePrefix = "file:" + configFileCatalog;
        FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(new String[]{
                configFilePrefix +"applicationContext.xml"
                , configFilePrefix +"applicationContext-jedis.xml"
                , configFilePrefix +"applicationContext-rabbitmq.xml"
        });

        LogFactory.getLog("log4j.properties");
        System.out.println("# message bus has started");
    }

}

