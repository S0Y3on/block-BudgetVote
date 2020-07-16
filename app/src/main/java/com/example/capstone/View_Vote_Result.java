package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.ExecutionException;

import static com.example.capstone.View_Now_Budget.hexToString;

public class View_Vote_Result extends AppCompatActivity {
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vote_result);
        final Button current_button = (Button) findViewById(R.id.current);
        Intent incomingIntent = getIntent();
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");

        current_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), View_Now_Budget.class);
                intent.putExtra("ID",ID);           //학번
                intent.putExtra("Address",Address); //계좌
                startActivity(intent);
            }
        });
        final Button back_button = (Button) findViewById(R.id.back);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final TextView agree_result = (TextView) findViewById(R.id.agree_result);
        final TextView disagree_result = (TextView) findViewById(R.id.disagree_result);
        final TextView vote_result = (TextView) findViewById(R.id.vote_result);

        Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.0.2:8545"));  // defaults to http://localhost:8545/
        String year = "2020";

        String inputdata = "";

        try {
            Access_DB_Task task = new Access_DB_Task();
            String result = task.execute("View","result",year).get();
            EthTransaction tx = null;
            String txAddr = result.substring(5, result.length()-1);

            tx = web3j.ethGetTransactionByHash(txAddr).sendAsync().get();
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
