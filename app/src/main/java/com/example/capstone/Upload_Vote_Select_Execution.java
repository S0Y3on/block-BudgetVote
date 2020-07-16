package com.example.capstone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Upload_Vote_Select_Execution extends AppCompatActivity {
    private ListView listView;
    private ListViewAdapter adapter;
    private String type;
    private String How;
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_vote_select_execution);

        //Intent 수신
        Intent incomingIntent = getIntent();
        type = incomingIntent.getStringExtra("type");
        How = incomingIntent.getStringExtra("How");
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");

        // 뒤로가기 버튼 클릭 시 메인 메뉴로 돌아가기.
        Button btn = (Button) findViewById(R.id.check_execution_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Upload_Vote_Select_Execution.this, Menu_StudentCouncil.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });
        final ArrayList<String> title = new ArrayList<String>();
        adapter = new ListViewAdapter();

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        try {
            Access_DB_Task task = new Access_DB_Task();
            String result = task.execute("View","execution").get();
            String[] array = result.split("\n");
            if(array[5].equals("false")){
                Toast.makeText(Upload_Vote_Select_Execution.this, "집행내역이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                result = "";
            }
            else{
                for(int i=5; i<array.length;i++){
                    adapter.addItem(i-4+". " + array[i]);
                    title.add(array[i]);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter = new ListViewAdapter();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if ("execution".equals(type) && "upload".equals(How)) {
                    Intent intent = new Intent(getApplicationContext(), Upload_Vote_Select_Time.class); // 다음넘어갈 화면
                    intent.putExtra("type", "execution");
                    intent.putExtra("title", title.get(position));
                    intent.putExtra("ID",ID);           //학번
                    intent.putExtra("Address",Address); //계좌
                    startActivity(intent);
                } else if ("execution".equals(type) && "delete".equals(How)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Upload_Vote_Select_Execution.this);
                    builder.setTitle(title.get(position) + " 투표 종료 Warning!");
                    builder.setMessage("정말로 종료하시겠습니까?");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // 집행내역 컨트랙트 종료 함수
                                    try {
                                        Connect_execution_Web3j Conn_web3j_execution = new Connect_execution_Web3j(ID);
                                        String result_execution = Conn_web3j_execution.getResult_execution();

                                        Upload_Transaction_Task upload_transaction_task = new Upload_Transaction_Task();
                                        String Hash = upload_transaction_task.Send_Transaction(result_execution,Address,ID);

                                        //Hash DB 전송
                                        Access_DB_Task access_db_task = new Access_DB_Task();
                                        access_db_task.execute("Upload_Transaction_Hash","Execute_Vote_Result_Hash",title.get(position),Hash);

                                        Toast.makeText(Upload_Vote_Select_Execution.this, title.get(position) + "투표 종료 과정 마침.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Menu_StudentCouncil.class); // 다음넘어갈 화면
                                        intent.putExtra("ID",ID);           //학번
                                        intent.putExtra("Address",Address); //계좌
                                        startActivity(intent);
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
            }
        });
    }
}
