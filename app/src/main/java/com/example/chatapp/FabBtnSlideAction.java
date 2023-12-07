package com.example.chatapp;

import android.animation.ObjectAnimator;

public interface FabBtnSlideAction {

    // 플로팅 액션 버튼(fabBtn) 클릭시 slide up & down 효과
    default void slideFabBtn(FabBtnSlideDTO fabBtnInfo) {
        // slide up 상태
        if (fabBtnInfo.getSlide_status()) {
            // slide down action
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabBtnInfo.getCreateChatRoomBtn(), "translationY", 0f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabBtnInfo.getRemoveChatRoomBtn(), "translationY", 0f);
            fe_animation.start();
            ObjectAnimator fs_animation = ObjectAnimator.ofFloat(fabBtnInfo.getSearchRoomBtn(), "translationY", 0f);
            fs_animation.start();

            fabBtnInfo.setSlide_status(false); // slide down 상태로 변경
        } else { // slide down 상태
            // slide up action
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabBtnInfo.getCreateChatRoomBtn(), "translationY", -175f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabBtnInfo.getRemoveChatRoomBtn(), "translationY", -330f);
            fe_animation.start();
            ObjectAnimator fs_animation = ObjectAnimator.ofFloat(fabBtnInfo.getSearchRoomBtn(), "translationY", -505f);
            fs_animation.start();

            fabBtnInfo.setSlide_status(true); // slide up 상태로 변경
        }
    }
}
