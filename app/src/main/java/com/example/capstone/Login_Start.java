package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Login_Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_start);

        Button button = (Button)findViewById(R.id.login);
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout loginlayout = (LinearLayout) vi.inflate(R.layout.login, null);
        final EditText idtext = (EditText) loginlayout.findViewById(R.id.id);
        final EditText pwtext = (EditText) loginlayout.findViewById(R.id.pw);
        final Button loginbutton = (Button) loginlayout.findViewById(R.id.loginbutton);
        final Button registerbutton = (Button) loginlayout.findViewById(R.id.registerbutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "버튼이 클릭 되었습니다.", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder mydlg = new AlertDialog.Builder(Login_Start.this);

                if (loginlayout.getParent() != null) {
                    ((ViewGroup)loginlayout.getParent()).removeView(loginlayout);
                }

                mydlg.setView(loginlayout);
                mydlg.show();

                loginbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = idtext.getText().toString();
                        String pw = pwtext.getText().toString();
                        if(id.length() != 0 && pw.length() != 0) {
                            Access_DB_Task task = new Access_DB_Task();
                            try {
                                String result = task.execute("Login_Check", "login", id, pw).get();
                                //로그인 실패
                                if (result.equals("false")) {
                                    Toast.makeText(Login_Start.this, "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    idtext.setText("");
                                    pwtext.setText("");
                                    result = "";
                                }
                                else { //로그인 성공
                                    Toast.makeText(Login_Start.this, "로그인", Toast.LENGTH_SHORT).show();
                                    Access_DB_Task lookup_db_task = new Access_DB_Task();
                                    String address = lookup_db_task.execute("Lookup_DB","login_address",id).get();
                                    if(result.equals("0")) {
                                        Intent intent = new Intent(getApplicationContext(), Menu_NormalStudent.class);
                                        intent.putExtra("ID",id);           //학번
                                        intent.putExtra("Address",address); //계좌

                                        startActivity(intent);
                                        finish();}
                                    else if(result.equals("1")){
                                        Intent intent = new Intent(getApplicationContext(), Menu_StudentCouncil.class);
                                        intent.putExtra("ID",id);           //학번
                                        intent.putExtra("Address",address); //계좌
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Intent intent = new Intent(getApplicationContext(), Menu_Supervisor.class);
                                        intent.putExtra("ID",id);           //학번
                                        intent.putExtra("Address",address); //계좌
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } catch (Exception e) {
                                Toast.makeText(Login_Start.this, "오류", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "모든 칸을 다 작성해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                registerbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Login_Register.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
