package com.example.chatapp;

import android.util.Log;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;

public class WebSocketClient {
    private WebSocket webSocket;

    public WebSocketClient(String str_url){
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(str_url).build();

            // client.newWebSocket(request, HttpWebSocket.listener).send("테스트 메시지!");
            webSocket = client.newWebSocket(request, HttpWebSocketListener.listener);
            // client.dispatcher().executorService().shutdown();
        } catch(Exception e) {
            Log.e("TLOG", "MAIN 소켓 통신 오류 : " + e.toString());
        }
    }

    //문자열을 전송
    private boolean sendString(String str){
        try {
            Log.d("WebSocketClient", "Sending string: " + str);
            webSocket.send(str);
        } catch(Exception e){
            Log.e("sendString",e.getMessage());
            return false;
        }

        return true;
    }

    //데이터를 전송
    private boolean sendBytes(ByteString bytes){
        try {
            webSocket.send(bytes);
        } catch(Exception e){
            return false;
        }
        return true;
    }

    public boolean request_search_room(String room_name){
        if(room_name == null){
            try {
                JSONObject jObject = new JSONObject();
                jObject.put("request", "search-room");
                jObject.put("room-name", String.valueOf(room_name));
                sendString(jObject.toString());
            } catch(Exception e){
                Log.e("request_search_room", e.getMessage());
                return false;
            }
        }
        return true;
    }

    public boolean request_log_in(String id, String nickname){
        try {
            JSONObject jObject = new JSONObject();
            jObject.put("request", "log-in");
            jObject.put("user-id", id);
            jObject.put("user-nickname", nickname);
            sendString(jObject.toString());
        }catch(Exception e){
            Log.e("request_log_in", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean request_enter_room(String room_id){
        try {
            JSONObject jObject = new JSONObject();
            jObject.put("request", "enter-room");
            jObject.put("room-id", room_id);
            sendString(jObject.toString());
        }catch(Exception e){
            Log.e("request_enter_room", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean request_chat(String room_id, String user_id, String user_nickname, String text){
        try {
            JSONObject jObject = new JSONObject();
            jObject.put("request", "chat");
            jObject.put("room-id", room_id);
            jObject.put("user-id", user_id);
            jObject.put("user-nickname", user_nickname);
            jObject.put("text", text);
            sendString(jObject.toString());
        }catch(Exception e){
            Log.e("request_chat", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean request_make_room(String room_name, String password){
        try {
            JSONObject jObject = new JSONObject();
            jObject.put("request", "make-room");
            jObject.put("room-name", room_name);
            jObject.put("room-password", password);
            sendString(jObject.toString());
        }catch(Exception e){
            Log.e("request_make_room", e.getMessage());
            return false;
        }
        return true;
    }

    //웹 소켓 닫기
    @Override
    protected void finalize(){
        webSocket.close(1000, null);
    }
}
