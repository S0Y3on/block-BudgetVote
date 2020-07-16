package com.example.capstone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;
import java.util.concurrent.ExecutionException;

import static com.example.capstone.Upload_Budget_Get_Hash_Task.getSHA512;

public class View_Now_Budget extends AppCompatActivity {
    private String ID;      //학번 겸 계좌 비밀번호
    private String Address; //계좌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_now_budget);
        TableLayout stk = init();
        //Intent 수신
        Intent incomingIntent = getIntent();
        ID = incomingIntent.getStringExtra("ID");
        Address = incomingIntent.getStringExtra("Address");
        String year = "2020";

        try {
            Access_DB_Task task = new Access_DB_Task();
            String result = task.execute("View","budget",year).get();
            Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.0.2:8545"));  // defaults to http://localhost:8545/

            String inputdata = "";
            Access_DB_Task task2 = new Access_DB_Task();
            String result2 = task2.execute("View","addr",year).get();
            EthTransaction tx = null;
            String txAddr = result2.substring(5, result2.length()-1);

            try {
                tx = web3j.ethGetTransactionByHash(txAddr).sendAsync().get();
                inputdata = tx.getTransaction().getInput();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            String budgetHash = hexToString(inputdata.substring(2));
            String[] array = result.split("\n");
            String tmp = "";
            for(int i=6; i<array.length;i++){
                if(!array[i].equalsIgnoreCase("null") && (i%5 != 0))
                    tmp += array[i];
            }

            String sha512 = getSHA512(tmp);

            if(result.equals("false") || !sha512.equalsIgnoreCase(budgetHash)){
                Toast.makeText(View_Now_Budget.this, "예산안 읽어오기 실패", Toast.LENGTH_SHORT).show();
                result = "";
            }
            else{

                for(int i=5; i<array.length;){
                    TextView t1v = new TextView(this);
                    TextView t2v = new TextView(this);
                    TextView t3v = new TextView(this);
                    TextView t4v = new TextView(this);
                    TextView t5v = new TextView(this);

                    TableRow tbrow = new TableRow(this);
                    t1v.setText(array[i++]+"   ");
                    t1v.setTextColor(Color.WHITE);
                    t1v.setGravity(Gravity.CENTER);
                    t1v.setTextSize(15);

                    tbrow.addView(t1v);
                    t2v.setText(" "+array[i++]);
                    t2v.setTextColor(Color.WHITE);
                    t2v.setGravity(Gravity.CENTER);
                    t2v.setTextSize(20);

                    tbrow.addView(t2v);
                    t3v.setText(" "+array[i++]);
                    t3v.setTextColor(Color.WHITE);
                    t3v.setGravity(Gravity.CENTER);
                    t3v.setTextSize(20);

                    tbrow.addView(t3v);
                    t4v.setText(" "+array[i++]);
                    t4v.setTextColor(Color.WHITE);
                    t4v.setGravity(Gravity.CENTER);
                    t4v.setTextSize(20);

                    tbrow.addView(t4v);

                    t5v.setText(" "+array[i++]);
                    t5v.setTextColor(Color.WHITE);
                    t5v.setGravity(Gravity.CENTER);
                    t5v.setTextSize(20);

                    tbrow.addView(t5v);
                    stk.addView(tbrow);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public TableLayout init() {
        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("uploadDate");
        tv0.setTextSize(20);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" date");
        tv1.setTextSize(20);

        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" contents ");
        tv2.setTextSize(20);

        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(" expenditure ");
        tv3.setTextSize(20);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText(" note");
        tv4.setTextSize(20);
        tbrow0.addView(tv4);

        stk.addView(tbrow0);

        return stk;

    }

    // 헥사 -> 문자열 변환하는 메서드
    public static String hexToString(String hex) {
        hex = hex.replaceAll("^(00)+", "");
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return new String(bytes);
    }
}
