package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomListActivity extends AppCompatActivity implements FabBtnSlideAction {
    private List<String> roomNameList = new ArrayList<>(); // 현재 생성된 채팅방 이름 저장
    private FloatingActionButton createChatRoomBtn; // 채팅방 생성 버튼
    private FloatingActionButton removeChatRoomBtn; // 채팅방 삭제 버튼
    private FabBtnSlideDTO fabBtnInfo = new FabBtnSlideDTO();
    private LinearLayout buttonLayout; // 채팅방 목록 layout

    private int buttonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room_list);

        AppController.setChatRoomList(this);

        // fabBtnSlide R.id값 설정
        fabBtnInfo.setSlide_status(false);
        createChatRoomBtn = findViewById(R.id.createChatRoom);
        removeChatRoomBtn = findViewById(R.id.removeChatRoom);
        fabBtnInfo.setCreateChatRoomBtn(createChatRoomBtn);
        fabBtnInfo.setRemoveChatRoomBtn(removeChatRoomBtn);
        buttonLayout = findViewById(R.id.buttonlayout);
        FloatingActionButton setRoomList = findViewById(R.id.setRoomList); // 채팅방 설정 리스트 버튼

        // 공개방의 정보를 읽어온다.
        AppController.room_list.clear();
        AppController.request_search_room(null);

        // 채팅방 목록 설정 슬라이딩(생성 or 삭제)
        setRoomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideFabBtn(fabBtnInfo); // 슬라이딩 동작 (채팅방 생성, 삭제 fabButton)
            }
        });

        // 채팅방 생성 fabButton
        createChatRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createChatRoomDialog();
            }
        });

        // 채팅방 삭제 fabButton
        removeChatRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteChatRoomDialog();
            }
        });
    }
    // 채팅방 삭제 dialog
    private void showDeleteChatRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("채팅방 삭제");
        builder.setMessage("삭제할 채팅방을 선택하세요:");

        // 채팅방 이름 목록을 다이얼로그 선택을 위한 배열로 변환
        final CharSequence[] roomNamesArray = roomNameList.toArray(new CharSequence[roomNameList.size()]);

        // 새로운 레이아웃을 inflate하여 ListView를 표시
        View dialogView = getLayoutInflater().inflate(R.layout.chat_room_remove, null);
        builder.setView(dialogView);

        ListView checklistView = dialogView.findViewById(R.id.checklistView);

        // ArrayAdapter를 사용하여 채팅방 목록을 ListView에 연결
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, roomNamesArray);
        checklistView.setAdapter(adapter);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 확인 버튼을 눌렀을 때 호출됨
                // 선택한 채팅방 삭제 로직 추가
                SparseBooleanArray checkedItems = checklistView.getCheckedItemPositions();
                for (int i = 0; i < checkedItems.size(); i++) {
                    int position = checkedItems.keyAt(i);
                    if (checkedItems.valueAt(i)) {
                        String selectedRoomName = roomNamesArray[position].toString();
                        showDeleteConfirmDialog(selectedRoomName);
                    }
                }
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    // 채팅방 삭제 확인 dialog
    private void showDeleteConfirmDialog(final String selectedRoomName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(selectedRoomName + " 채팅방을 삭제하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteChatRoom(selectedRoomName);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    // 채팅방 레이아웃 제거
    public void deleteChatRoom(String roomName) {
        // roomName에 해당하는 채팅방 레이아웃을 삭제하는 코드를 작성합니다.
        Log.d("MyApp", "deleteChatRoom called for room: " + roomName);

        // 1. 레이아웃에서 해당 채팅방 레잉아웃을 찾아 제거
        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            View child = buttonLayout.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                if (button.getText().toString().equals("Room: " + roomName)) {
                    buttonLayout.removeViewAt(i);
                    break;
                }
            }
        }

        // 2. 채팅방 목록에서도 삭제
        roomNameList.remove(roomName);
    }



    // 채팅방 생성 dialog
    private void createChatRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.create_room, null); //
        builder.setView(dialogView);

        // 채팅방 생성하는 dialog 입력 부분
        EditText roomNameEditText = dialogView.findViewById(R.id.roomNameEditText); // 채팅방 제목 텍스트
        CheckBox passwordCheckBox = dialogView.findViewById(R.id.passwordCheckBox); // 비밀번호 설정 체크박스
        EditText passwordEditText = dialogView.findViewById(R.id.passwordEditText); // 비밀번호 설정 텍스트

        // 채팅방 생성 "확인" 버튼
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String roomName = roomNameEditText.getText().toString();
                boolean isPasswordSet = passwordCheckBox.isChecked();
                String roomPassword = passwordEditText.getText().toString();

                if(!isPasswordSet) roomPassword = "null";
                AppController.make_chat_room(roomName, roomPassword);
                createChatRoomLayout(roomName, isPasswordSet, roomPassword);
                dialog.dismiss();
            }
        });
        // 채팅방 생성 "취소" 버튼
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();  // 채팅방 생성 dialog 출력 삭제
            }
        });
        // 채팅방 생성시 비밀번호 설정 유무 CheckBox
        passwordCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordCheckBox.isChecked()) {
                    // 체크박스가 선택된 경우 비밀번호 EditText를 표시
                    passwordEditText.setVisibility(View.VISIBLE);
                } else {
                    // 체크박스가 선택되지 않은 경우 비밀번호 EditText를 숨김
                    passwordEditText.setVisibility(View.GONE);
                }
            }
        });

        builder.show();
    }

    // 비밀번호 설정 Dialog
    private void showPasswordVerificationDialog(String roomName, String roomPassword) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.verify_password, null);
        builder.setView(dialogView);

        EditText passwordVerificationEditText = dialogView.findViewById(R.id.passwordVerificationEditText);

        // 비밀번호 설정 완료 "확인" 버튼
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredPassword = passwordVerificationEditText.getText().toString();

                if (enteredPassword.equals(roomPassword)) {
                    // 비밀번호가 맞는 경우
                    navigateToChatRoom(roomName);
                } else {
                    // 비밀번호가 틀린 경우
                    showPasswordIncorrectDialog();
                }
            }
        });

        // 비밀번호 설정 완료 "취소" 버튼
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    // 올바르지 않은 Password 입력 Dialog
    private void showPasswordIncorrectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("비밀번호가 틀렸습니다!")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 비밀번호 입력 Dialog (재입력) or 다른 작업
                    }
                });
        builder.show();
    }

    // 새로운 채팅방 레이아웃 추가
    public void createChatRoomLayout(String roomName, boolean isPasswordSet, String roomPassword) {
        Button newButton = new Button(this);
        newButton.setText("Room: " + roomName);
        buttonLayout.addView(newButton);
        buttonCount++;

        roomNameList.add(roomName); // 채팅방 목록에 존재하는 채팅방 이름 저장

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordSet) { // 비밀번호 설정 유무
                    showPasswordVerificationDialog(roomName, roomPassword);
                } else {
                    navigateToChatRoom(roomName);
                }
                AppController.enter_room(roomName);
            }
        });
    }

    // chaat_room_list에서 선택한 chat_room으로 이동
    private void navigateToChatRoom(String roomName) {
        // ChatRommList -> ChatRoomActivity로 이동하도록 설정
        Intent intent = new Intent(ChatRoomListActivity.this, ChatRoomActivity.class);
        intent.putExtra("roomName", roomName); // roomName데이터를 ChatRoomActivity로 넘김 (이후 채팅방 위에 방 제목 쓸 수 있음)
        startActivity(intent); // ChatRoomActivity로 이동 (roomName값 넘김)
    }
}
