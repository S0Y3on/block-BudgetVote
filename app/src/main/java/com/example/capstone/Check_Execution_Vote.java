package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.web3j.crypto.CipherException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;

public class Check_Execution_Vote extends AppCompatActivity {
    private RadioGroup RadioGroup;
    private RadioButton Agree;
    private RadioButton Disagree;
    private String str_result; // 선택 결과값을 담을 변수
    private String title; // 집행내역 제목
    private String execution_date = null;
    Connect_execution_Web3j Conn_web3j = null;
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_execution_vote);

        //Intent 수신
        Intent incomingIntent = getIntent();
        title = incomingIntent.getStringExtra("title");
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");

        try {
            Conn_web3j = new Connect_execution_Web3j(ID);
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
        }

        // 뒤로가기 버튼 클릭 시 메인 메뉴로 돌아가기.
        Button btn = (Button) findViewById(R.id.check_execution_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //뒤로가기 버튼 클릭 시 Intent를 통해 주소와 패스워드 값 넘겨줌
                Intent intent = new Intent(Check_Execution_Vote.this, Check_Execution.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });

        TextView textView_budget = findViewById(R.id.textView_budget);
        TextView textView_budget_result = findViewById(R.id.textView_budget_result);
        TextView textView_execution = findViewById(R.id.textView_execution);
        TextView textView_execution_result = findViewById(R.id.textView_execution_result);
        textView_budget.setText(String.format("<< [%s]의 예산안 내용 >>", title));
        textView_execution.setText(String.format("<< [%s]의 집행 결과 >>", title));

        Access_DB_Task task1 = new Access_DB_Task();
        String budget_result = null;
        try {
            budget_result = task1.execute("Lookup_DB","budget",title).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        textView_budget_result.setText("\n" + budget_result + "\n");

        Access_DB_Task task2 = new Access_DB_Task();
        String execution_result = null;
        try {
            execution_result = task2.execute("Lookup_DB","execution",title).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        textView_execution_result.setText("\n" + execution_result + "\n");

        Access_DB_Task task3 = new Access_DB_Task();
        try {
            execution_date = task3.execute("Lookup_DB","execution_date", title).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RadioGroup = findViewById(R.id.RadioGroup); //라디오 버튼을 담고있는 그룹
        Agree = findViewById(R.id.radioButton_agree); //라디오 버튼
        Disagree = findViewById(R.id.radioButton_Disagree); // 라디오 버튼

        RadioGroup.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radioButton_agree) {
                    Toast.makeText (Check_Execution_Vote.this, "Agree 선택",
                            Toast.LENGTH_SHORT).show ();
                    str_result = "Agree";
                }
                else if(i==R.id.radioButton_Disagree) {
                    Toast.makeText (Check_Execution_Vote.this,"Disagree 선택",
                            Toast.LENGTH_SHORT).show ();
                    str_result = "Disagree";
                }
            }
        });

        Button commitExecution_vote = (Button) findViewById(R.id.commitExecution_vote);
        commitExecution_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 투표 컨트랙트
                if("Agree".equals(str_result)) {
                    try {
                        // 현재 로그인한 학생 name, id  st_name, st_id에 넣어야함
                        Conn_web3j.voteToExecution(ID,ID,true);
                        Toast.makeText (Check_Execution_Vote.this,"투표완료", Toast.LENGTH_SHORT).show ();
                        Intent intent = new Intent(Check_Execution_Vote.this, Menu_Supervisor.class);
                        intent.putExtra("ID",ID);           //학번
                        intent.putExtra("Address",Address); //계좌
                        startActivity(intent);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else if("Disagree".equals(str_result)) {
                    try {
                        // 현재 로그인한 학생 name, id  st_name, st_id에 넣어야함
                        Conn_web3j.voteToExecution(ID,ID,false);
                        Toast.makeText (Check_Execution_Vote.this,"투표완료", Toast.LENGTH_SHORT).show ();
                        Intent intent = new Intent(Check_Execution_Vote.this, Menu_Supervisor.class);
                        intent.putExtra("ID",ID);           //학번
                        intent.putExtra("Address",Address); //계좌
                        startActivity(intent);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(Check_Execution_Vote.this, "의견을 선택하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
