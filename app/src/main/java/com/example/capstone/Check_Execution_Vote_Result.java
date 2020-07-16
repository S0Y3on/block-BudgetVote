package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;

import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;

import static com.example.capstone.View_Now_Budget.hexToString;

public class Check_Execution_Vote_Result extends AppCompatActivity {
    private String title; // 집행내역 제목
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌
    Connect_execution_Web3j Conn_web3j = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_execution_vote_result);

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

        TextView result = findViewById(R.id.result);
        result.setText(String.format("[%s]의 투표결과", title));

        final TextView agree_result = (TextView) findViewById(R.id.agree_result);
        final TextView disagree_result = (TextView) findViewById(R.id.disagree_result);
        final TextView vote_result = (TextView) findViewById(R.id.vote_result);

        Button btn = (Button) findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //뒤로가기 버튼 클릭 시 Intent를 통해 주소와 패스워드 값 넘겨줌
                Intent intent = new Intent(Check_Execution_Vote_Result.this, Check_Execution.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });

        Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.0.2:8545"));  // defaults to http://localhost:8545/

        String inputdata = "";

        try {
            Access_DB_Task task = new Access_DB_Task();
            String result_execution = task.execute("Lookup_DB","execution_vote_result_address",title).get();
            EthTransaction tx = null;
            tx = web3j.ethGetTransactionByHash(result_execution).sendAsync().get();
            inputdata = tx.getTransaction().getInput();

            String result_data = hexToString(inputdata.substring(2));

            String[] array = result_data.split(" ");
            String[] agree = array[0].split(":");;
            String[] disagree = array[1].split(":");;
            String[] total_vote = array[2].split(":");;

            agree_result.setText("찬성 : "+(Integer.parseInt(agree[1])/Integer.parseInt(total_vote[1]))*100+" %");
            disagree_result.setText("반대 : "+(Integer.parseInt(disagree[1])/Integer.parseInt(total_vote[1]))*100+" %");
            vote_result.setText("총 참여자 수 : "+total_vote[1]);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
