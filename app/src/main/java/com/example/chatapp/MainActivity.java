package com.example.chatapp;

        import androidx.appcompat.app.AppCompatActivity;

        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button saveButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppController.setMainActivity(this);
        AppController.runClient("http://10.150.8.190:3000");

        saveButton = findViewById(R.id.button);

        // "접속" 버튼 클릭 이벤트 처리
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼을 눌렀을 경우, 학번과 닉네임을 저장
                String id = ((EditText)findViewById(R.id.editTextNumber)).getText().toString();
                String nickname = ((EditText)findViewById(R.id.editTextNickname)).getText().toString();
                AppController.setUser_id(id);
                AppController.setUser_nickname(nickname);
                AppController.log_in();

                // chat_room_list.xml 페이지 이동
                Intent intent = new Intent(MainActivity.this, ChatRoomListActivity.class);
                startActivity(intent);
            }
        });
    }
}
