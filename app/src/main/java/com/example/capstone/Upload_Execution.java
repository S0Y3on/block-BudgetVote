package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutionException;

public class Upload_Execution extends AppCompatActivity {
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌
    private Button commit_execution;        // 업로드 버튼
    // 사용자로부터 입력받은 내용
    private EditText execution_date, execution_contents, execution_income, execution_expenditure, execution_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_execution);

        //Intent 수신
        Intent incomingIntent = getIntent();
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");

        Button btn = (Button) findViewById(R.id.upload_execution_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //뒤로가기 버튼 클릭 시 Intent를 통해 주소와 패스워드 값 넘겨줌
                Intent intent = new Intent(Upload_Execution.this, Menu_StudentCouncil.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });
        execution_date = findViewById(R.id.execution_date);
        execution_contents = findViewById(R.id.execution_contents);
        execution_income = findViewById(R.id.execution_income);
        execution_expenditure = findViewById(R.id.execution_expenditure);
        execution_note = findViewById(R.id.execution_note);
        commit_execution = findViewById(R.id.commitExecution);
        commit_execution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = execution_date.getText().toString();
                String contents = execution_contents.getText().toString();
                String income = execution_income.getText().toString();
                String expenditure = execution_expenditure.getText().toString();
                String note = execution_note.getText().toString();

                Access_DB_Task task = new Access_DB_Task();
                String result = null;
                try {
                    result = task.execute("Upload_Execution",date,contents,income,expenditure,note).get();
                    if(result.equals(null)) {
                        Toast.makeText(Upload_Execution.this, "업로드 실패하였습니다.", Toast.LENGTH_LONG).show();
                    } else {
                        // 메인화면으로 돌아감
                        Toast.makeText(Upload_Execution.this, "업로드 되었습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Upload_Execution.this, Menu_StudentCouncil.class);
                        intent.putExtra("ID",ID);           //학번
                        intent.putExtra("Address",Address); //계좌
                        startActivity(intent); }
                } catch (ExecutionException e) {e.printStackTrace();}
                catch (InterruptedException e) {e.printStackTrace();}
            }
        });
    }
}
