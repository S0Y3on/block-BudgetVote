package com.example.capstone;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

public class Upload_Vote_Select_Time extends AppCompatActivity {

    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌
    private String Type;
    private TextView textView_Type, textView_Date, textView_Time;
    int mYear, mMonth, mDay, mHour, mMinute;
    private String title = null;    //집행내역 내용
    private String execution_date = null; // 집행내역 내용에 해당하는 날짜

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_vote_select_time);

        //Intent 수신
        Intent incomingIntent = getIntent();
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");
        Type = incomingIntent.getStringExtra("type");
        if("execution".equals(Type)){
            title = incomingIntent.getStringExtra("title");
            Access_DB_Task task = new Access_DB_Task();
            try {
                execution_date = task.execute("Lookup_DB","execution_date", title).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        textView_Type = (TextView)findViewById(R.id.Type);
        if("budget".equals(Type)){
            textView_Type.setText("<<예산안의 ");}
        else if("execution".equals(Type)){
            textView_Type.setText("<<" + title + "의 ");}
        textView_Date = (TextView)findViewById(R.id.Date);
        textView_Time = (TextView)findViewById(R.id.Time);

        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        Button btn = (Button) findViewById(R.id.upload_vote_back2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Upload_Vote_Select_Time.this, Upload_Vote.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });
        Button btn2 = (Button)findViewById(R.id.DateBtn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Upload_Vote_Select_Time.this, mDateSetListener, mYear, mMonth, mDay).show();
            }
        });
        Button btn3 = (Button)findViewById(R.id.TimeBtn);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(Upload_Vote_Select_Time.this, mTimeSetListener, mHour, mMinute, true).show();
            }
        });
        // 투표시작 버튼 클릭
        Button btn4 = (Button) findViewById(R.id.upload_vote_select_time_startvote);
        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if("budget".equals(Type)) {
                    // 예산안 투표 등록
                    try {
                        Connect_Web3j web3j = new Connect_Web3j(ID);
                        //예산안의 날짜,종료시간 vote_date, ex)2020.06.30
                        web3j.initVote("2020.06.30");

                        // 투표시작 버튼 클릭 후 자동으로 메인 화면으로 넘어감
                        Toast.makeText(Upload_Vote_Select_Time.this, "예산안 투표 업로드 완료.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Upload_Vote_Select_Time.this, Menu_StudentCouncil.class);
                        intent.putExtra("ID",ID);           //학번
                        intent.putExtra("Address",Address); //계좌
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (CipherException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchProviderException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                else if("execution".equals(Type)){
                    // 집행내역 투표 등록
                    try {
                        Connect_execution_Web3j web3j_execution = new Connect_execution_Web3j(ID);
                        web3j_execution.initVote_execution(execution_date);
                        // 투표시작 버튼 클릭 후 자동으로 메인 화면으로 넘어감
                        Toast.makeText(Upload_Vote_Select_Time.this, "집행내역 투표 업로드 완료.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Upload_Vote_Select_Time.this, Menu_StudentCouncil.class);
                        intent.putExtra("ID",ID);           //학번
                        intent.putExtra("Address",Address); //계좌
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (CipherException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchProviderException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            //사용자가 입력한 값을 가져온뒤
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            //텍스트뷰의 값을 업데이트함
            UpdateNow();
        }
    };

    TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //사용자가 입력한 값을 가져온뒤
            mHour = hourOfDay;
            mMinute = minute;
            //텍스트뷰의 값을 업데이트함
            UpdateNow();
        }
    };

    void UpdateNow(){
        textView_Date.setText(String.format("%d년 %d월 %d일", mYear, mMonth, mDay));
        textView_Time.setText(String.format("%d시 %d분", mHour, mMinute));
    }
}
