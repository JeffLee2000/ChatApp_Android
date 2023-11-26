package com.example.chatapp;

public class ChatTextDTO {
    private String text;
    private int num;
    private String id;
    public String nickname;

    public ChatTextDTO(int n, String t, String id, String nickname){
        this.text = t;
        this.num = n;
        this.id = id;
        this.nickname = nickname;
    }

    public String toString(){
        return "n: "+num+" id: "+id;
    }

    // Getter & Setter
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
