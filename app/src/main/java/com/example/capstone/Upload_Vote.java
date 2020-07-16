package com.example.capstone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;

public class Upload_Vote extends AppCompatActivity {

    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_vote);

        //Intent 수신
        Intent incomingIntent = getIntent();
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");

        Button btn = (Button) findViewById(R.id.upload_vote_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //뒤로가기 버튼 클릭 시 Intent를 통해 주소와 패스워드 값 넘겨줌
                Intent intent = new Intent(Upload_Vote.this, Menu_StudentCouncil.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });

        Button btn2 = (Button) findViewById(R.id.upload_vote_budget);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Upload_Vote.this, Upload_Vote_Select_Time.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                intent.putExtra("type", "budget");
                startActivity(intent);
            }
        });
        Button btn3 = (Button) findViewById(R.id.upload_vote_execution);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Upload_Vote.this, Upload_Vote_Select_Execution.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                intent.putExtra("type", "execution");
                intent.putExtra("How","upload");
                startActivity(intent);
            }
        });
        Button btn4 = (Button) findViewById(R.id.upload_vote_budget_finish);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Upload_Vote.this);
                builder.setTitle("예산안 투표 종료 Warning!");
                builder.setMessage("정말로 종료하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // 예산안 컨트랙트 종료 함수
                                try {
                                    Connect_Web3j Conn_web3j = new Connect_Web3j(ID);
                                    String result = Conn_web3j.getResult();

                                    //result 트랜잭션 전송
                                    Upload_Transaction_Task upload_transaction_task = new Upload_Transaction_Task();
                                    String Hash = upload_transaction_task.Send_Transaction(result,Address,ID);

                                    //Hash DB 전송
                                    Access_DB_Task upload_transaction_hash_task = new Access_DB_Task();
                                    upload_transaction_hash_task.execute("Upload_Transaction_Hash", "Budget_Vote_Result_Hash", Hash);

                                    //메인화면으로 이동
                                    Intent intent = new Intent(Upload_Vote.this, Menu_StudentCouncil.class);
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
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });

                builder.show();
            }
        });
        Button btn5 = (Button) findViewById(R.id.upload_vote_execution_finish);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Upload_Vote.this, Upload_Vote_Select_Execution.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                intent.putExtra("type", "execution");
                intent.putExtra("How","delete");
                startActivity(intent);
            }
        });
    }
}
