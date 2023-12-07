package com.example.chatapp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class HttpWebSocketListener {
    private static final Handler chatRoomUpdater = new Handler(){
        public void handleMessage(Message msg){
            AppController.updateChatRoom();
        }
    };

    private static final Handler chatRoomListUpdater = new Handler(){
        public void handleMessage(Message msg){
            AppController.updateChatRoomList();
        }
    };
    public static WebSocketListener listener = new WebSocketListener() {
        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosed(webSocket, code, reason);
        }
        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosing(webSocket, code, reason);
        }
        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable okhttp3.Response response) {
            super.onFailure(webSocket, t, response);
            // 연결 실패 시 에러 메시지를 로그에 출력
            Log.e("HttpWebSocketListener", "onFailure is operate: " + t.getMessage());
        }
        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);
            process_json_text(text);
        }
        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }
        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull okhttp3.Response response) {
            super.onOpen(webSocket, response);
        }
    };

    private static void reply_search_room(JSONObject jObject){
        try {
            String room_name = jObject.getString("room-name");
            String room_id = jObject.getString("room-id");
            AppController.room_list.put(room_name, room_id);
            Message msg = chatRoomListUpdater.obtainMessage();
            chatRoomListUpdater.sendMessage(msg);
        }catch(Exception e){
            Log.e("reply_search_room", e.getMessage());
        }
    }
    private static boolean reply_log_in(JSONObject jObject){
        try {
            String answer = jObject.getString("answer");
            if(answer.equals("Error")) return false;
        } catch(Exception e){
            Log.e("reply_log_in", e.getMessage());
            return false;
        }

        return true;
    }
    private static boolean reply_enter_room(JSONObject jObject){
        try {
            String answer = jObject.getString("answer");
            if(answer.equals("Error")) return false;

            String user_id = jObject.getString("user-id");
            String user_nickname = jObject.getString("user-nickname");
            String text = jObject.getString("text");

            AppController.addChatText(user_id, user_nickname, text);
            Message msg = chatRoomUpdater.obtainMessage();
            chatRoomUpdater.sendMessage(msg);
        } catch(Exception e){
            Log.e("reply_enter_room", e.getMessage());
            return false;
        }

        return true;
    }
    private static boolean reply_chat(JSONObject jObject){
        try {
            System.out.println(jObject.toString());
            String answer = jObject.getString("answer");
            if(answer.equals("Error")) return false;

            String user_id = jObject.getString("user-id");
            String user_nickname = jObject.getString("user-nickname");
            String text = jObject.getString("text");

            AppController.addChatText(user_id, user_nickname, text);
            Message msg = chatRoomUpdater.obtainMessage();
            chatRoomUpdater.sendMessage(msg);
        } catch(Exception e){
            Log.e("reply_chat", e.getMessage());
            return false;
        }

        return true;
    }
    private static boolean reply_make_room(JSONObject jObject){
        try {
            String answer = jObject.getString("answer");
            if(answer.equals("Error")) return false;

            String room_name = jObject.getString("room-name");
            String room_id = jObject.getString("room-id");

            AppController.room_list.put(room_name, room_id);
            Message msg = chatRoomListUpdater.obtainMessage();
            chatRoomListUpdater.sendMessage(msg);
        } catch(Exception e){
            Log.e("reply_make_room", e.getMessage());
            return false;
        }

        return true;
    }
    private static void process_json_text(String json_text){
        try {
            JSONObject jObject = new JSONObject(json_text);
            String reply_type = jObject.getString("reply");

            if(reply_type.equals("log-in"))
                reply_log_in(jObject);
            else if(reply_type.equals("enter-room"))
                reply_enter_room(jObject);
            else if(reply_type.equals("chat"))
                reply_chat(jObject);
            else if(reply_type.equals("search-room"))
                reply_search_room(jObject);
            else if(reply_type.equals("make-room"))
                reply_make_room(jObject);
            else {
                Log.e("invalid reply", jObject.toString());
                throw new Exception("invalid reply code error");
            }
        } catch(Exception e){
            Log.e("json_process error", e.getMessage());
        }
    }
}