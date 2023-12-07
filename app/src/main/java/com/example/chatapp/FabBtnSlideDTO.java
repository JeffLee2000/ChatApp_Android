package com.example.chatapp;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FabBtnSlideDTO {
    private boolean slide_status = false; // 플로팅 버튼 상태
    private FloatingActionButton createChatRoomBtn; // 채팅방 생성 버튼
    private FloatingActionButton removeChatRoomBtn; // 채팅방 삭제 버튼
    private FloatingActionButton searchRoomBtn;

    // Getter & Setter
    public Boolean getSlide_status() {
        return slide_status;
    }

    public void setSlide_status(Boolean slide_status) {
        this.slide_status = slide_status;
    }

    public FloatingActionButton getCreateChatRoomBtn() {
        return createChatRoomBtn;
    }

    public void setCreateChatRoomBtn(FloatingActionButton createChatRoomBtn) {
        this.createChatRoomBtn = createChatRoomBtn;
    }

    public FloatingActionButton getRemoveChatRoomBtn() {
        return removeChatRoomBtn;
    }

    public void setRemoveChatRoomBtn(FloatingActionButton removeChatRoomBtn) {
        this.removeChatRoomBtn = removeChatRoomBtn;
    }

    public FloatingActionButton getSearchRoomBtn(){ return searchRoomBtn; }

    public void setSearchRoomBtn(FloatingActionButton searchRoomBtn){
        this.searchRoomBtn = searchRoomBtn;
    }
}
