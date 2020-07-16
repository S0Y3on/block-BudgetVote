package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone.R;

public class Menu_StudentCouncil extends AppCompatActivity {
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_studentcouncil);

        //Intent 수신
        Intent incomingIntent = getIntent();
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");

        Button upload_vote_btn = findViewById(R.id.UploadVote);
        Button upload_execution_btn = findViewById(R.id.UploadExecution);
        Button upload_budget_btn = findViewById(R.id.upload_budget_upload);
        Button before_budget = findViewById(R.id.before_budget);
        Button current_budget = findViewById(R.id.current_budget);
        Button voting = findViewById(R.id.voting);
        Button execution_history = findViewById(R.id.execution_history);

        before_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), View_Last_Budget.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });
        current_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), View_Now_Budget.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });
        voting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), View_Vote.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });
        execution_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), View_Execution.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });

        upload_vote_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_StudentCouncil.this, Upload_Vote.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });
        upload_execution_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_StudentCouncil.this, Upload_Execution.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });
        upload_budget_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_StudentCouncil.this, Upload_Budget.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });
    }
}
