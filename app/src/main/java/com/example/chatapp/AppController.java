package com.example.chatapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class AppController {
    private static ChatRoomListActivity chatRoomListActivity = null;
    private static MainActivity mainActivity = null;
    private static ChatRoomActivity chatRoomActivity = null;
    private static WebSocketClient client = null;
    public static String user_id = null;
    public static String user_nickname = null;
    public static String current_room_id = null;
    //채팅방 리스트 (채팅방 이름이 키, id가 값)
    public static HashMap<String, String> room_list = new HashMap<String, String>();

    public static void setChatRoomList(ChatRoomListActivity activity){ chatRoomListActivity = activity; }
    public static void setMainActivity(MainActivity activity){ mainActivity = activity; }
    public static void setChatRoomActivity(ChatRoomActivity activity){ chatRoomActivity = activity; }
    public static void setUser_id(String id){ user_id = id; }
    public static void setUser_nickname(String nickname){ user_nickname = nickname; }

    public static ArrayList<ChatTextDTO> texts = new ArrayList<ChatTextDTO>();


    //채팅방 리스트를 업데이트함
    public static void updateChatRoomList(){
        for(String name : room_list.keySet())
            chatRoomListActivity.createChatRoomLayout(name, false, null);
    }
    //웹 소켓 클라이언트를 작동시킨다.
    public static void runClient(String url) {
        client = new WebSocketClient(url);
    }
    //웹 서버에 채팅방 리스트를 요구하는 함수
    public static void request_search_room(String room_name){
        boolean success = client.request_search_room(room_name);
        while(!success)
            success = client.request_search_room(room_name);
    }

    //채팅방 내용을 추가
    public static void addChatText(String id, String nickname, String text){
        int num = (id.equals(user_id)) ? 0 : 1;
        texts.add(new ChatTextDTO(num, text, id, nickname));
    }
    //로그인
    public static boolean log_in(){
        String id = user_id;
        String nickname = user_nickname;
        return client.request_log_in(id, nickname);
    }

    //방 접속
    public static boolean enter_room(String room_name){
        texts.clear();

        for(String key : room_list.keySet()) {
            if (key.equals(room_name)) {
                current_room_id = room_list.get(key);
                return client.request_enter_room(room_list.get(key));
            }
        }
        return false;
    }

    public static boolean enter_room_by_id(String room_id){
        texts.clear();
        return client.request_enter_room(room_id);
    }

    //채팅 전송
    public static boolean chat(String text){
        return client.request_chat(current_room_id, user_id, user_nickname, text);
    }

    //채팅 방 만들기
    public static boolean make_chat_room(String room_name, String password){
        return client.request_make_room(room_name, password);
    }

    public static boolean isChatRoomActivity(){
        if(chatRoomActivity == null) return false;
        return true;
    }

    public static void updateChatRoom(){
        if(isChatRoomActivity()) chatRoomActivity.updateChat();
    }
}
