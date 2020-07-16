package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Menu_Supervisor extends AppCompatActivity {

    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_supervisor);

        //Intent 수신
        Intent incomingIntent = getIntent();
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");

        Button before_budget = (Button)findViewById(R.id.before_budget);
        Button current_budget = (Button)findViewById(R.id.current_budget);
        Button voting = (Button)findViewById(R.id.voting);
        Button execution_history = (Button)findViewById(R.id.execution_history);
        Button check_execution = (Button)findViewById(R.id.CheckExecution);

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
        check_execution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Check_Execution.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });
    }
}
