package com.example.capstone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutionException;

public class View_Last_Budget extends AppCompatActivity {
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_last_budget);
        //Intent 수신
        Intent incomingIntent = getIntent();
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");
        final EditText yearText = (EditText)findViewById(R.id.Year);
        final Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = yearText.getText().toString();
                if ( yearText.length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "모든 칸을 다 작성해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    set(year);
                }
            }
        });
    }
    public void set(String year){

        try {
            Access_DB_Task task = new Access_DB_Task();
            String result = task.execute("View","budget",year).get();
            String[] array = result.split("\n");

            if(array[5].equals("false")){
                Toast.makeText(View_Last_Budget.this, "해당년도 예산안이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                result = "";
            }
            else{
                TableLayout stk = init();

                for(int i=5; i<array.length;){
                    TextView t1v = new TextView(this);
                    TextView t2v = new TextView(this);
                    TextView t3v = new TextView(this);
                    TextView t4v = new TextView(this);
                    TextView t5v = new TextView(this);

                    TableRow tbrow = new TableRow(this);
                    t1v.setText(array[i++]+"   ");
                    t1v.setTextColor(Color.WHITE);
                    t1v.setGravity(Gravity.CENTER);
                    t1v.setTextSize(15);

                    tbrow.addView(t1v);
                    t2v.setText(" "+array[i++]);
                    t2v.setTextColor(Color.WHITE);
                    t2v.setGravity(Gravity.CENTER);
                    t2v.setTextSize(20);

                    tbrow.addView(t2v);
                    t3v.setText(" "+array[i++]);
                    t3v.setTextColor(Color.WHITE);
                    t3v.setGravity(Gravity.CENTER);
                    t3v.setTextSize(20);

                    tbrow.addView(t3v);
                    t4v.setText(" "+array[i++]);
                    t4v.setTextColor(Color.WHITE);
                    t4v.setGravity(Gravity.CENTER);
                    t4v.setTextSize(20);

                    tbrow.addView(t4v);

                    t5v.setText(" "+array[i++]);
                    t5v.setTextColor(Color.WHITE);
                    t5v.setGravity(Gravity.CENTER);
                    t5v.setTextSize(20);

                    tbrow.addView(t5v);
                    stk.addView(tbrow);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public TableLayout init() {
        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("uploadDate");
        tv0.setTextSize(20);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" date");
        tv1.setTextSize(20);

        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" contents ");
        tv2.setTextSize(20);

        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(" expenditure ");
        tv3.setTextSize(20);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText(" note");
        tv4.setTextSize(20);
        tbrow0.addView(tv4);

        stk.addView(tbrow0);

        return stk;

    }
}
