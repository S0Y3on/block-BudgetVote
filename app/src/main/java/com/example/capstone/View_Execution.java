package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class View_Execution extends AppCompatActivity {
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌
    private ListView listView;
    private ListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_execution);
        final ArrayList<String> title = new ArrayList<String>();
        adapter = new ListViewAdapter();

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        //Intent 수신
        Intent incomingIntent = getIntent();
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");
        try {
            Access_DB_Task task = new Access_DB_Task();
            String result = task.execute("View","execution").get();
            String[] array = result.split("\n");
            if(array[5].equals("false")){
                Toast.makeText(View_Execution.this, "집행내역이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(getApplicationContext(), View_detail.class); // 다음넘어갈 화면
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                intent.putExtra("title",title.get(position));
                startActivity(intent);
            }
        });
    }
}
