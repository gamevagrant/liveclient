package com.meetvr.liveshowclient.weight;

import com.meetvr.share.info.UserInfo;
import com.meetvr.share.reponse.Response;
import com.meetvr.share.utrils.Mutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by wzm-pc on 2016/8/24.
 */
public class RoomsResponse extends Response {
    private ArrayList<LiveHallItemData> datas=null;

    public ArrayList<LiveHallItemData> parse(JSONObject jobj, UserInfo userInfo){

        try {
            JSONObject data = super.parse(jobj);
            int total = Mutils.getJsonInt(data,"total");
            JSONArray jsonObject = Mutils.getJsonArray(data,"list");
            if(Mutils.isResponseOk(getAct_stat())){
                if(jsonObject.length()>0){
                    datas = new ArrayList<LiveHallItemData>();
                    for(int i=0;i<jsonObject.length();i++){
                        LiveHallItemData itemData = new LiveHallItemData();
                        JSONObject j=jsonObject.getJSONObject(i);
                        itemData.setId(Mutils.getJsonInt(j,"live_id"));
                        itemData.setType(Mutils.getJsonString(j,"live_stat"));
                        itemData.setUser_id(Mutils.getJsonString(j,"host_id"));
                        //itemData.setUser_code(Mutils.getJsonString(j,"user_code"));
                        itemData.setHead_pic(Mutils.getJsonString(j,"head_pic"));
                        String pic_url = Mutils.getJsonString(j,"live_show_url_big");
                        if(null==pic_url || pic_url.equals("null") || pic_url.equals(""))
                        {
                            pic_url = Mutils.getJsonString(j,"live_show_url");
                        }
                        itemData.setRoom_show_pic(pic_url);
                        itemData.setNickname(Mutils.getJsonString(j,"nickname"));
                        itemData.setSex(Mutils.getJsonString(j,"sex"));
                        itemData.setLive_url(Mutils.getJsonString(j,"live_url"));
                        itemData.setRoom_id(Mutils.getJsonString(j,"room_id"));
                        //itemData.setLiving_count(Mutils.getJsonString(j,"living_count"));
                        itemData.setView_count(Mutils.getJsonString(j,"view_count"));
                        itemData.setContent(Mutils.getJsonString(j,"content"));
                        itemData.jsonValue = j.toString();
                        datas.add(itemData);

                    }
                    return datas;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public ArrayList<LiveHallItemData> getDatas(){
        return datas;
    }
}
