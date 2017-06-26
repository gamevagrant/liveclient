package com.meetvr.liveshowclient.weight;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.Toast;

import com.meetvr.share.request.ImSigNewRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.request.Room_ListsRequest;
import com.meetvr.share.utrils.Mutils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wzm-pc on 2016/8/31.
 */
public class Room_Lists {
        public void getMore(final Context context, String token, String identify,final RoomListInterface roomListInterface,int start){
            try {

                Room_ListsRequest room_listsRequest = new Room_ListsRequest(token,identify);
                room_listsRequest.addParam(start,10);
                room_listsRequest.addQuery(context, new Request.OnNetWorkProc() {
                        @Override
                        public void onStart() {

                        }
                        @Override
                        public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                            try {
                                if(isOK){

                                    RoomsResponse roomsResponse = new RoomsResponse();
                                    ArrayList<LiveHallItemData> datas = roomsResponse.parse(response,null);
                                    if(Mutils.isResponseOk(roomsResponse.getAct_stat())){

                                        roomListInterface.onResulte(true,datas);

                                    }else{
                                        if(roomsResponse.getErr_code()==4010001){
                                            context.sendBroadcast(new Intent("com.login.again"));
                                        }
                                        roomListInterface.onResulte(false,null); //网络错误
                                        Toast.makeText(context,roomsResponse.getErr_msg(),Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    roomListInterface.onResulte(false,null);
                                    Toast.makeText(context,"请检查网络",Toast.LENGTH_SHORT).show();
                                }

                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

}
