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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Check_Execution extends AppCompatActivity {
    private ListView listView;
    private ListViewAdapter adapter;
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_execution);

        //Intent 수신
        Intent incomingIntent = getIntent();
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");

        // 뒤로가기 버튼 클릭 시 메인 메뉴로 돌아가기.
        Button btn = (Button) findViewById(R.id.check_execution_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Check_Execution.this, Menu_Supervisor.class);
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
            String result = task.execute("View", "execution").get();
            String[] array = result.split("\n");
            if(array[5].equals("false")){
                Toast.makeText(Check_Execution.this, "집행내역이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(getApplicationContext(), Check_Execution_Vote.class); // 다음넘어갈 화면
                intent.putExtra("title",title.get(position));
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
           }
        });

        listView.setOnItemLongClickListener (new AdapterView.OnItemLongClickListener () {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder (Check_Execution.this);
                builder.setTitle ("투표 결과");
                builder.setMessage ("결과를 확인하시겠습니까?");
                builder.setPositiveButton ("예", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), Check_Execution_Vote_Result.class); // 다음넘어갈 화면
                        intent.putExtra("title",title.get(position));
                        intent.putExtra("ID",ID);           //학번
                        intent.putExtra("Address",Address); //계좌
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton ("아니오", null);
                builder.show ();
                return true;
            }
        });
    }
}

