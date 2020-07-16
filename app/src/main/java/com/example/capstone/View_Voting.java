package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;

public class View_Voting extends AppCompatActivity{
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌

        private RadioGroup voteGroup;
        private Button voting_button;
        Connect_Web3j Conn_web3j = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.view_voting);
            Intent incomingIntent = getIntent();
            ID = incomingIntent.getStringExtra("ID");
            Address = incomingIntent.getStringExtra("Address");

            //라디오 그룹 설정
            voteGroup = (RadioGroup) findViewById(R.id.voteGroup);
            voteGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
            voting_button = (Button) findViewById(R.id.voting);
            final Button back_button = (Button) findViewById(R.id.back);

            try {
                Conn_web3j = new Connect_Web3j(ID);
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

            back_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            voting_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(View_Voting.this, "하나를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        //라디오 그룹 클릭 리스너
        RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.disagree){
                    voting_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(View_Voting.this, "반대", Toast.LENGTH_SHORT).show();
                            try {
                                // 현재 로그인한 학생 name, id  st_name, st_id에 넣어야함
                                Conn_web3j.voteToBudget(ID,ID,false);

                                Toast.makeText(View_Voting.this, "투표 성공", Toast.LENGTH_SHORT).show();
                                // 투표가 끝나면 메인메뉴로 복귀
                                Access_DB_Task lookup_db_task = new Access_DB_Task();
                                String role = lookup_db_task.execute("Lookup_DB","login_role",ID).get();
                                if("0".equals(role)) {
                                    Intent intent = new Intent(View_Voting.this, Menu_NormalStudent.class);
                                    intent.putExtra("ID",ID);           //학번
                                    intent.putExtra("Address",Address); //계좌
                                    startActivity(intent);
                                }
                                else if("1".equals(role)) {
                                    Intent intent = new Intent(View_Voting.this, Menu_StudentCouncil.class);
                                    intent.putExtra("ID",ID);           //학번
                                    intent.putExtra("Address",Address); //계좌
                                    startActivity(intent);
                                }
                                else if("2".equals(role)) {
                                    Intent intent = new Intent(View_Voting.this, Menu_Supervisor.class);
                                    intent.putExtra("ID",ID);           //학번
                                    intent.putExtra("Address",Address); //계좌
                                    startActivity(intent);
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else{
                    voting_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(View_Voting.this, "찬성", Toast.LENGTH_SHORT).show();
                            try {
                                Conn_web3j.voteToBudget(ID,ID,true);

                                Toast.makeText(View_Voting.this, "투표 성공", Toast.LENGTH_SHORT).show();
                                // 투표가 끝나면 메인메뉴로 복귀
                                Access_DB_Task lookup_db_task = new Access_DB_Task();
                                String role = lookup_db_task.execute("Lookup_DB","login_role",ID).get();
                                if("0".equals(role)) {
                                    Intent intent = new Intent(View_Voting.this, Menu_NormalStudent.class);
                                    intent.putExtra("ID",ID);           //학번
                                    intent.putExtra("Address",Address); //계좌
                                    startActivity(intent);
                                }
                                else if("1".equals(role)) {
                                    Intent intent = new Intent(View_Voting.this, Menu_StudentCouncil.class);
                                    intent.putExtra("ID",ID);           //학번
                                    intent.putExtra("Address",Address); //계좌
                                    startActivity(intent);
                                }
                                else if("2".equals(role)) {
                                    Intent intent = new Intent(View_Voting.this, Menu_Supervisor.class);
                                    intent.putExtra("ID",ID);           //학번
                                    intent.putExtra("Address",Address); //계좌
                                    startActivity(intent);
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        };
}
