package com.moretv.common.keepConnect.impl;


import com.moretv.common.constants.ConstantMess;
import com.moretv.common.keepConnect.IMessageable;
import com.moretv.common.utils.Coder;
import com.moretv.common.utils.SHA1;
import com.moretv.common.utils.StringUtils;
import com.moretv.common.utils.TimeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class SendMessage implements IMessageable {
    private static Logger LOG = Logger.getLogger(SendMessage.class);


    /**
     * 调用API接口
     * @param message
     * @param targets
     * @param type
     * @return
     */
    public String sendMessage(String message,String targets,int type){
        String API_add = "";

        //dev 开发环境
       //String HOST = "http://172.16.17.121:8080/";

        //test 内网测试环境
        //String HOST = "http://123.59.156.50:8080/";
        String HOST = "http://lcinter.aginomoto.com/";
        //product 现网环境
        //String HOST = "";

        switch (type){
            case 1 : API_add = HOST + ConstantMess.API_FORUSERS; break;    //发给8位 用户
            case 2 : API_add = HOST + ConstantMess.API_FORCLIENTS; break;  //发给32位 用户
            case 3 : API_add = HOST + ConstantMess.API_FORONLINE; break;   //发给在线客户端
            case 4 : API_add = HOST + ConstantMess.API_FORALL; break;      //发给所有客户端
            case 5 : API_add = HOST + ConstantMess.API_FORALLBATCHES; break;//按策略发送
            case 6 : API_add = HOST + ConstantMess.API_FORDEVICEID; break;  //发给deviceid客户端
            case 7 : API_add = HOST + ConstantMess.API_FORSN;break;         //发给SN客户端
        }
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(API_add);
        LOG.info("调用API ： " + API_add);
        String strResult = "";
        try {
            String round = StringUtils.getRandomString(10);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str = sdf.format(date);
            String timestamp = TimeUtils.getTimestamp(str);
            String signstr = ConstantMess.APP_SECRET + round + timestamp;
            String sign =  SHA1.hex_sha1(signstr);
            System.out.println("sign:"+sign);
            String data = "{\"msgType\":\"content\",\"data\":" + message +"}";
            String messageBase64 = Coder.getBASE64(data);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("app_key", ConstantMess.APP_KEY));
            nameValuePairs.add(new BasicNameValuePair("app_secret", ConstantMess.APP_SECRET));
            nameValuePairs.add(new BasicNameValuePair("targets", targets));
            nameValuePairs.add(new BasicNameValuePair("round", round));
            nameValuePairs.add(new BasicNameValuePair("timestamp", timestamp));
            nameValuePairs.add(new BasicNameValuePair("sign", sign));
            nameValuePairs.add(new BasicNameValuePair("targets", targets));
            nameValuePairs.add(new BasicNameValuePair("data", messageBase64));
            nameValuePairs.add(new BasicNameValuePair("business_type", "100"));
            nameValuePairs.add(new BasicNameValuePair("receipt", "1"));
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

            HttpResponse response = httpclient.execute(httppost);
            System.out.println(response);
            if (response.getStatusLine().getStatusCode() == 200) {
                String conResult = EntityUtils.toString(response.getEntity());
                System.out.println(conResult);
            } else {
                String err = response.getStatusLine().getStatusCode()+"";
                strResult += "发送失败:"+err;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResult;
    }


    /**
     * 对消息系统提供的统一发长连接消息的接口
     * @param message
     * @param uidList
     * @param type
     * @throws org.json.JSONException
     */
    public void sendMessageToIca(String message,List<String> uidList,int type)throws JSONException{
        if(uidList.size() != 0){
            List<String> uid_8 = new ArrayList<>();
            List<String> uid_32 = new ArrayList<>();
            for (String uid : uidList){
                if( uid.length() == 8){
                    uid_8.add(uid);
                }
                if(uid.length() == 32){
                    uid_32.add(uid);
                }
            }
            if (uid_8.size() != 0 && type == ConstantMess.FORUSERORCLIENT ){
                type = ConstantMess.FORUSERS;
                JSONObject jsonobj = new JSONObject(message);
                jsonobj.put("userType",ConstantMess.ACCOUNT);
                listInBatches(jsonobj.toString(), uid_8, type); //批量发送长连接消息给指定8位用户
            }
            if (uid_32.size() != 0 && type == ConstantMess.FORUSERORCLIENT){
                type = ConstantMess.FORCLIENTS;
                JSONObject jsonobj = new JSONObject(message);
                jsonobj.put("userType",ConstantMess.MAC);
                listInBatches(jsonobj.toString(),uid_32,type); //批量发送长连接消息给指定32位客户端
            }
            if(type == ConstantMess.FORONLINE || type == ConstantMess.FORALLCLIENTS){
                sendMessage(message,"",type); //向所有客户端发消息
            }
            if(type == ConstantMess.FORDEVICEID){
                listInBatches(message,uidList,type); //批量发送长连接消息给指定deviceID客户端
            }
        }
    }


    /**
     * 将大量用户进行批量处理 每900个发一次
     * @param updateMess
     * @param uidList
     * @param type
     */
    public void listInBatches(String updateMess,List<String> uidList,int type){
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for (String uid :uidList){
            i++;
            buffer.append(uid +",");
            if(i > 900){
                String targets = buffer.toString().substring(0, buffer.toString().length()-1);
                sendMessage(updateMess,targets,type); //长连接发消息
                buffer = null;
                i = 0;
            }
        }
        String targets = buffer.toString().substring(0, buffer.toString().length()-1);
        System.out.println("message :"+updateMess +"   uidList:"+targets);
        sendMessage(updateMess, targets, type); //长连接发消息
    }


    /**
     * 构造统一的消息返回格式
     * @return
     */
    public JSONObject getMessageJson(){
        JSONObject json = new JSONObject();
        try{
            json.put("sid","");
            json.put("title","");
            json.put("content","");
            json.put("url","");
            json.put("icon", "");
            json.put("contentType","");
            json.put("displayTime", "5");
            String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            json.put("createTime", createTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /*
    public String sendSmsToAllClients(String message) throws JSONException {
        HttpClient httpclient = new DefaultHttpClient();
        String smsUrl="http://172.16.17.121:8080/Api/BroadcastInNoTime";
        HttpPost httppost = new HttpPost(smsUrl);
        String strResult = "";
        try {
            String messageBase64 = Coder.getBASE64(message);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("app_key", ConstantMess.APP_KEY));
            nameValuePairs.add(new BasicNameValuePair("app_secret", ConstantMess.APP_SECRET));
            nameValuePairs.add(new BasicNameValuePair("targets",""));
            nameValuePairs.add(new BasicNameValuePair("data", messageBase64));
            nameValuePairs.add(new BasicNameValuePair("business_type", "1"));
            nameValuePairs.add(new BasicNameValuePair("receipt", "1"));
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200) {
                    *//*读返回数据*//*
                String conResult = EntityUtils.toString(response
                        .getEntity());
                JSONObject sobj = new JSONObject(conResult);
                //sobj = sobj.fromObject(conResult);
                System.out.println(sobj);
                String code = sobj.getString("msg_state");
                if(code.equals("200")){
                    strResult += "发送成功,code=" +code;
                }else{
                    strResult += "发送失败，code="+code;
                }
            } else {
                String err = response.getStatusLine().getStatusCode()+"";
                strResult += "发送失败:"+err;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResult;
    }
*/


}
