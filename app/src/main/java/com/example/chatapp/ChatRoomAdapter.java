package com.example.chatapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {
    // Integer = 0 => 전송한 메세지들(본인 메세지) | Integer = 1 => 전송 받은 메세지들(상대방 메세지)
    private ArrayList<ChatTextDTO> chatTextDTOS;
    private LayoutInflater mInflater;

    ChatRoomAdapter(Context context, ArrayList<ChatTextDTO> texts) {
        this.mInflater = LayoutInflater.from(context);
        this.chatTextDTOS = texts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView current_time;
        TextView userId;

        ViewHolder(View itemView) {
            super(itemView);
            // 메세지 출력 객체
            messageText = itemView.findViewById(R.id.send_message_text);
            // 시간 출력 객체
            current_time = itemView.findViewById(R.id.current_time);
            userId = itemView.findViewById(R.id.userId);
        }
    }

    // 현재 시간 PM/AM hh:mm 형식 반환
    private String getTime() {
        long currTime = System.currentTimeMillis();
        SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm");

        return timeFormat.format(new Date(currTime));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // attachToRoot: true => add the child view to parent "RIGHT NOW"
        // attachToRoot: true => add the child view to parent "NOT NOW"
        View send_view;
        View recv_view;
        View view;

        send_view = mInflater.inflate(R.layout.send_message_layout, parent, false);
        recv_view = mInflater.inflate(R.layout.receive_message_layout, parent, false);
        if(viewType == 0)
            view = send_view;
        else
            view = recv_view;

        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        return chatTextDTOS.get(position).getNum();
    }

    // 생성된 ViewHolder에 데이터를 바인딩 해주는 함수 (스크롤을 내리거나 올릴때마다 호출)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 메세지 출력
        holder.messageText.setText(chatTextDTOS.get(position).getText());
        holder.userId.setText(chatTextDTOS.get(position).nickname + "(" +
                chatTextDTOS.get(position).getId() + ")");

        // 현재 시간 출력 (형식: 오후/오전 hh:mm)
        String[] currTimeArr = String.valueOf(getTime()).split(" ");
        if(currTimeArr[0].equals("PM")) {
            currTimeArr[0] = "오후";
        } else {
            currTimeArr[0] = "오전";
        }
        String currTime = String.join(" ", currTimeArr);
        Log.d("ChatRoomAdapter", "current_time:" + currTime);
        holder.current_time.setText(currTime);
    }

    @Override
    public int getItemCount() {
        Log.d("ChatRoomAdapter", "text_array_size" + String.valueOf(chatTextDTOS.size()));
        return chatTextDTOS.size();
    }
}
