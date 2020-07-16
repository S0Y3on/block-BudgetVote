package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Login_Register extends AppCompatActivity {

    public String[] createWallet(final String password) {
        String[] result = new String[2];
        try {
            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/ethereum"); //다운로드 path 가져오기
            if (!path.exists()) {
                path.mkdir();
            }
            String fileName = WalletUtils.generateLightNewWalletFile(password, new File(String.valueOf(path))); //지갑생성
            result[0] = path+"/"+fileName;

            Credentials credentials = WalletUtils.loadCredentials(password,result[0]);

            result[1] = credentials.getAddress();

            return result;
        } catch (NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | IOException
                | CipherException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);

        final EditText idtext = (EditText)findViewById(R.id.editText_id);
        final EditText pwtext = (EditText)findViewById(R.id.editText_pw);
        final EditText nametext = (EditText)findViewById(R.id.editText_name);
        Button registerbutton = (Button)findViewById(R.id.button_register);

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login_Start.class);

                String id = idtext.getText().toString();
                String pw = pwtext.getText().toString();
                String name = nametext.getText().toString();

                if(id.length() != 0 && pw.length() != 0 && name.length() != 0) {
                    try{
                        String addr = createWallet(id)[1];
                        Access_DB_Task task = new Access_DB_Task();
                        String result = task.execute("Login_Check", "join", id, pw, name, addr).get();
                        if(result.equalsIgnoreCase("id")){
                            Toast.makeText(Login_Register.this,"이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                            idtext.setText("");
                            pwtext.setText("");
                            nametext.setText("");
                        }
                        else if(result.equalsIgnoreCase("ok")){
                            idtext.setText("");
                            pwtext.setText("");
                            nametext.setText("");
                            Toast.makeText(Login_Register.this,"회원가입에 성공 했습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }catch (Exception e){}
                }
                else {
                    Toast.makeText(getApplicationContext(), "모든 칸을 다 작성해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
