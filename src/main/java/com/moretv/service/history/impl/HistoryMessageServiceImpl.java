package com.moretv.service.history.impl;

import com.moretv.service.history.IHistoryMessageService;
import java.util.*;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class HistoryMessageServiceImpl implements IHistoryMessageService
{
    private JedisPool jedisPool;

    public void truncate(Object hisMess)
    {
        Jedis jedis = (Jedis)this.jedisPool.getResource();
        try {
            JSONObject hisJson = new JSONObject(hisMess.toString());
            String uid = "history_"+hisJson.get("uid").toString();
            System.out.printf("uid:" + uid);
            int savecnt = Integer.parseInt(hisJson.get("save").toString());

            Map oldMap = jedis.hgetAll(uid);
            Map newMap = sortMap(oldMap);
            int delSize = newMap.size() - savecnt;
            System.out.printf("dele size:" + delSize);

            Set hisSet = newMap.entrySet();
            Iterator hisIter = hisSet.iterator();

            int cnt = 0;
            while ((hisIter.hasNext()) && (cnt < delSize)) {
                Map.Entry hisEntry = (Map.Entry)hisIter.next();
                jedis.hdel(uid, (String)hisEntry.getKey());
                System.out.println("to delete sid " + (String)hisEntry.getValue());
                cnt++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            this.jedisPool.returnResource(jedis);
        }
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public Map sortMap(Map oldMap) {
        ArrayList<Map.Entry<String,String>> list = new ArrayList(oldMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String,String>>() {
            @Override
            public int compare(Entry<String, String> his1, Entry<String, String> his2) {
                try {

                    JSONObject hisJson1 = new JSONObject(his1.getValue());
                    JSONObject hisJson2 = new JSONObject(his2.getValue());
                    float time1 = Float.parseFloat(hisJson1.get("updateTime").toString());
                    float time2 = Float.parseFloat(hisJson2.get("updateTime").toString());
                    if(time1 - time2 < 0) return -1;
                    if(time1 - time2 > 0) return 1;
                    if(time1 - time2 == 0) return 0;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        Map newMap = new LinkedHashMap();
        for (int i = 0; i < list.size(); i++) {
            newMap.put(((Map.Entry)list.get(i)).getKey(), ((Map.Entry)list.get(i)).getValue());
        }
        return newMap;
    }
}