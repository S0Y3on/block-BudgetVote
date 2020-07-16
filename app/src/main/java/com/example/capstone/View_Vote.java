package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone.R;

import java.util.concurrent.ExecutionException;

public class View_Vote extends AppCompatActivity {
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vote);
        //Intent 수신
        Intent incomingIntent = getIntent();
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");
        final Button vote_button = (Button) findViewById(R.id.vote);
        final Button result_button = (Button) findViewById(R.id.vote_result);

        vote_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), View_Voting.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });

        result_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), View_Vote_Result.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼 클릭 시 메인 메뉴로 돌아가기.
        Button btn = (Button) findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한에 맞는 메인메뉴로 복귀
                Access_DB_Task lookup_db_task = new Access_DB_Task();
                String role = null;
                try {
                    role = lookup_db_task.execute("Lookup_DB","login_role",ID).get();
                } catch (ExecutionException e) {e.printStackTrace();} catch (InterruptedException e) {e.printStackTrace(); }
                if("0".equals(role)) {
                    Intent intent = new Intent(View_Vote.this, Menu_NormalStudent.class);
                    intent.putExtra("ID",ID);           //학번
                    intent.putExtra("Address",Address); //계좌
                    startActivity(intent);
                }
                else if("1".equals(role)) {
                    Intent intent = new Intent(View_Vote.this, Menu_StudentCouncil.class);
                    intent.putExtra("ID",ID);           //학번
                    intent.putExtra("Address",Address); //계좌
                    startActivity(intent);
                }
                else if("2".equals(role)) {
                    Intent intent = new Intent(View_Vote.this, Menu_Supervisor.class);
                    intent.putExtra("ID",ID);           //학번
                    intent.putExtra("Address",Address); //계좌
                    startActivity(intent);
                }
            }
        });
    }
}
