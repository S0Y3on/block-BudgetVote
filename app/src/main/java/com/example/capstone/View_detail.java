package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutionException;

public class View_detail extends AppCompatActivity {
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView budgetTextView;
    private TextView expenditureTextView;
    private TextView noteTextView;
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        //Intent 수신
        ID = intent.getStringExtra("ID");
        Address = intent.getStringExtra("Address");

        titleTextView = (TextView) findViewById(R.id.title);
        dateTextView = (TextView) findViewById(R.id.date);
        budgetTextView = (TextView) findViewById(R.id.budget);
        expenditureTextView = (TextView) findViewById(R.id.expenditure);
        noteTextView = (TextView) findViewById(R.id.note);

        titleTextView.setText("[ "+title+" ]"+" 집행내역");

        try {
            Access_DB_Task task = new Access_DB_Task();
            String result = task.execute("View","detail",title).get();
            String[] array = result.split("\n");

            if(array[5].equals("false")){
                Toast.makeText(View_detail.this, "해당 집행내역의 상세정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                result = "";
            }
            else{
                for(int i=5; i<array.length;){
                    dateTextView.setText(i-4+". 일시 : " +array[i++]);
                    budgetTextView.setText(i-4+". 예산 : " +array[i++]);
                    expenditureTextView.setText(i-4+". 지출내역 : " +array[i++]);
                    noteTextView.setText(i-4+". 비고 : " +array[i++]);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
