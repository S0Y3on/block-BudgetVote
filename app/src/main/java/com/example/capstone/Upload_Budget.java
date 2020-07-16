package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

import static com.example.capstone.Upload_Budget_Get_Hash_Task.getSHA512;

public class Upload_Budget extends AppCompatActivity {

    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌
    private Button commit_budget;        // 업로드 버튼
    // 사용자로부터 입력받은 내용
    private EditText[] upload_budget_date = new EditText[11];
    private EditText[] upload_budget_contents = new EditText[11];
    private EditText[] upload_budget_expenditure = new EditText[11];
    private EditText[] upload_budget_note = new EditText[11];
    private String[][] budget_data = new String[11][4];
    private String Hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_budget);

        //Intent 수신
        Intent incomingIntent = getIntent();
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");

        // 뒤로가기 버튼 클릭 시 메인 메뉴로 돌아가기.
        Button btn = (Button) findViewById(R.id.upload_budget_back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //뒤로가기 버튼 클릭 시 Intent를 통해 주소와 패스워드 값 넘겨줌
                Intent intent = new Intent(Upload_Budget.this, Menu_StudentCouncil.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });

        int[] date_idArray = {0, R.id.upload_budget_date1, R.id.upload_budget_date2, R.id.upload_budget_date3, R.id.upload_budget_date4, R.id.upload_budget_date5, R.id.upload_budget_date6, R.id.upload_budget_date7, R.id.upload_budget_date8, R.id.upload_budget_date9, R.id.upload_budget_date10};
        int[] contents_idArray = {0, R.id.upload_budget_contents1, R.id.upload_budget_contents2, R.id.upload_budget_contents3, R.id.upload_budget_contents4, R.id.upload_budget_contents5, R.id.upload_budget_contents6, R.id.upload_budget_contents7, R.id.upload_budget_contents8, R.id.upload_budget_contents9, R.id.upload_budget_contents10};
        int[] expenditure_idArray = {0, R.id.upload_budget_expenditure1, R.id.upload_budget_expenditure2, R.id.upload_budget_expenditure3, R.id.upload_budget_expenditure4, R.id.upload_budget_expenditure5, R.id.upload_budget_expenditure6, R.id.upload_budget_expenditure7, R.id.upload_budget_expenditure8, R.id.upload_budget_expenditure9, R.id.upload_budget_expenditure10};
        int[] note_idArray = {0, R.id.upload_budget_note1, R.id.upload_budget_note2, R.id.upload_budget_note3, R.id.upload_budget_note4, R.id.upload_budget_note5, R.id.upload_budget_note6, R.id.upload_budget_note7, R.id.upload_budget_note8, R.id.upload_budget_note9, R.id.upload_budget_note10};
        for(int i = 1 ; i < 11; i++){
            upload_budget_date[i] = (EditText) findViewById(date_idArray[i]);
            upload_budget_contents[i] = (EditText) findViewById(contents_idArray[i]);
            upload_budget_expenditure[i] = (EditText) findViewById(expenditure_idArray[i]);
            upload_budget_note[i] = (EditText) findViewById(note_idArray[i]);
        }

        commit_budget = findViewById(R.id.upload_budget_upload);
        commit_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i < 11; i++) {
                    // EditText 값을 String 배열에 저장
                    if (upload_budget_date[i].getText().toString().length() != 0) {
                        budget_data[i][0] = upload_budget_date[i].getText().toString();
                    }
                    if (upload_budget_contents[i].getText().toString().length() != 0) {
                        budget_data[i][1] = upload_budget_contents[i].getText().toString();
                    }
                    if (upload_budget_expenditure[i].getText().toString().length() != 0) {
                        budget_data[i][2] = upload_budget_expenditure[i].getText().toString();
                    }
                    if (upload_budget_note[i].getText().toString().length() != 0) {
                        budget_data[i][3] = upload_budget_note[i].getText().toString();
                    }
                }
                String result = "";
                for (int i = 1; i < 11; i++) {
                    if (budget_data[i][1] != null) {
                        Access_DB_Task task = new Access_DB_Task();
                        try {
                            result = task.execute("Upload_Budget", budget_data[i][0], budget_data[i][1], budget_data[i][2], budget_data[i][3]).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (result.equals(null)) {
                            Toast.makeText(Upload_Budget.this, i + "행 업로드 실패하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        continue;
                    }// 내용이 비어있으면 그 행은 업로드 진행x
                }
                Toast.makeText(Upload_Budget.this, "업로드 과정 마침.", Toast.LENGTH_SHORT).show();

                // 해시함수를 통해 업로드한 예산안의 해시값 얻어오기
                String inputValue = "";

                for (int i = 1; i < 11; i++) {
                    // NULL값을 제외한 EditText 값을 inputValue에 이어 붙이기
                    if (upload_budget_date[i].getText().toString().length() != 0) {
                        inputValue += upload_budget_date[i].getText().toString();
                    }
                    if (upload_budget_contents[i].getText().toString().length() != 0) {
                        inputValue += upload_budget_contents[i].getText().toString();
                    }
                    if (upload_budget_expenditure[i].getText().toString().length() != 0) {
                        inputValue += upload_budget_expenditure[i].getText().toString();
                    }
                    if (upload_budget_note[i].getText().toString().length() != 0) {
                        inputValue += upload_budget_note[i].getText().toString();
                    }
                }
                // 해시함수 실행
                String sha512 = getSHA512(inputValue);

                // 트랜잭션 발생
                Upload_Transaction_Task _upload_transaction_task = new Upload_Transaction_Task();
                Hash = _upload_transaction_task.Send_Transaction(sha512, Address, ID);

                // 주소 업로드
                Access_DB_Task task = new Access_DB_Task();
                task.execute("Upload_Transaction_Hash", "Budget_Hash", Hash);

                // 메인화면으로 돌아감
                Toast.makeText(Upload_Budget.this, "업로드 되었습니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Upload_Budget.this, Menu_StudentCouncil.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });
    }
}
