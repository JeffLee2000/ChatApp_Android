package com.example.chatapp;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    ChatRoomAdapter adapter;
    ArrayList<ChatTextDTO> chatTextDTOS = AppController.texts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        AppController.setChatRoomActivity(this);
        updateChat();

        EditText messageEdit = (EditText) findViewById(R.id.message_edit); // 메세지 입력 텍스트
        Button sendBtn = (Button) findViewById(R.id.send_btn);             // 메세지 전송 버튼

        // 메세지 텍스트 Enter키로 전송
        messageEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                // Enter키 누름
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 메세지 텍스트 내용이 있으면 전송
                    if(!String.valueOf(messageEdit.getText()).equals("\n")) {
                        sendBtn.callOnClick();
                    } else { // 없으면 전송 안함
                        messageEdit.setText("");
                    }
                }
                return false;
            }
        });

        // 메세지 전송 버튼 처리
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메세지 텍스트 입력한 경우에 전송
                if(!messageEdit.getText().toString().equals("")) {
                    AppController.chat(messageEdit.getText().toString());
                    updateChat();
                    messageEdit.setText("");
                }
            }
        });
    } // protected void onCreate(Bundle savedInstanceState)

    public void updateChat(){
        // RecyclerView 어댑터 설정
        RecyclerView recyclerView = findViewById(R.id.chat_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatRoomActivity.this));
        adapter = new ChatRoomAdapter(ChatRoomActivity.this, chatTextDTOS);
        recyclerView.setAdapter(adapter);

        // 채팅방 자동 스크롤
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        });
    }
}

