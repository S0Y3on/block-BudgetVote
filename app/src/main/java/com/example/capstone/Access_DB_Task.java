package com.example.capstone;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Access_DB_Task extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;
    private String IP = "192.168.0.2";
    private String PortNum = "8080";
    private URL url;

    @Override
    protected String doInBackground(String... strings) {
        //strings[0] = 작업구분 , strings[1] = 세부요청사항 or strings[1,2,3,4~~ = 작업에 필요한 값들
        try {
            String str;
            if("Login_Check".equals(strings[0])) {
                url = new URL("http://"+ IP + ":" +PortNum +"/Capstone/login.jsp");
            }
            else if("Upload_Budget".equals(strings[0])) {
                url = new URL("http://"+ IP + ":" +PortNum +"/Capstone/DB_Insert.jsp");
            }
            else if("Upload_Execution".equals(strings[0])) {
                url = new URL("http://"+ IP + ":" +PortNum +"/Capstone/DB_Insert.jsp");
            }
            else if("Upload_Transaction_Hash".equals(strings[0])) {
                url = new URL("http://"+ IP + ":" +PortNum +"/Capstone/DB_Insert.jsp");
            }
            else if("View".equals(strings[0])) {
                url = new URL("http://"+ IP + ":" +PortNum +"/Capstone/budget.jsp");
            }
            else if("Lookup_DB".equals(strings[0])) {
                url = new URL("http://"+ IP + ":" +PortNum +"/Capstone/DB_Lookup.jsp");
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            if("Login_Check".equals(strings[0])) {
                if(strings[1].equalsIgnoreCase("login")){
                    //task.execute("Login_Check", "login", id, pw).get();
                    sendMsg = "type="+strings[1]+"&id="+strings[2]+"&pwd="+strings[3];
                }
                else if(strings[1].equalsIgnoreCase("join")){
                    //task.execute("Login_Check", "join", id, pw, name, addr).get();
                    sendMsg = "type="+strings[1]+"&id="+strings[2]+"&pwd="+strings[3]+"&name="+strings[4]+"&addr="+strings[5];
                }
            }
            else if("Upload_Budget".equals(strings[0])) {
                //task.execute("Upload_Budget", budget_data[i][0], budget_data[i][1], budget_data[i][2], budget_data[i][3]).get()
                sendMsg = "check=budget&date=" + strings[1] + "&contents=" + strings[2] + "&expenditure=" + strings[3] + "&note=" +strings[4];
            }
            else if("Upload_Execution".equals(strings[0])) {
                //task.execute("Upload_Execution",date,contents,income,expenditure,note).get();
                sendMsg = "check=execution&date=" + strings[1] + "&contents=" + strings[2] + "&budget=" + strings[3] +
                        "&expenditure=" + strings[4] + "&note=" + strings[5];
            }
            else if("Upload_Transaction_Hash".equals(strings[0])) {
                if("Budget_Hash".equals(strings[1])) {
                    //task.execute("Upload_Transaction_Hash", "Budget_Hash", Hash);
                    sendMsg = "check=BudgetHash&address=" + strings[2];
                }
                else if("Budget_Vote_Result_Hash".equals(strings[1])) {
                    //task.execute("Upload_Transaction_Hash", "Budget_Vote_Result_Hash", Hash);
                    sendMsg = "check=Budget_Vote_Result_Hash&address=" + strings[2];
                }
                else if("Execute_Vote_Result_Hash".equals(strings[1])) {
                    //task.execute("Upload_Transaction_Hash","Execute_Vote_Result_Hash",title.get(position),Hash);
                    sendMsg = "check=Execute_Vote_Result_Hash&contents="+ strings[2] + "&address=" +strings[3];
                }
            }
            else if("View".equals(strings[0])) {
                if("budget".equals(strings[1])){
                    //task.execute("View","budget",year).get();
                    sendMsg = "type="+strings[1]+"&year="+strings[2];
                }
                else if("execution".equals(strings[1])){
                    //task.execute("View","execution").get();
                    sendMsg = "type="+strings[1];
                }
                else if("detail".equals(strings[1])){
                    //task.execute("View","detail",title).get();
                    sendMsg = "type="+strings[1]+"&title="+strings[2];
                }
                else if("addr".equals(strings[1])){
                    //task2.execute("View","addr",year).get();
                    sendMsg = "type="+strings[1]+"&year="+strings[2];
                }
                else if("result".equals(strings[1])){
                    //task.execute("View","result",year).get();
                    sendMsg = "type="+strings[1]+"&year="+strings[2];
                }
            }
            else if("Lookup_DB".equals(strings[0])) {
                if("execution".equals(strings[1])) {
                    //task2.execute("Lookup_DB","execution",title).get();
                    sendMsg = "check=Lookup_execution&contents=" + strings[2];
                }
                else if("budget".equals(strings[1])) {
                    //task1.execute("Lookup_DB","budget",title).get();
                    sendMsg = "check=Lookup_budget&contents=" + strings[2];
                }
                else if("execution_date".equals(strings[1])) {
                    //task3.execute("Lookup_DB","execution_date", title).get();
                    sendMsg = "check=Lookup_execution_date&contents=" + strings[2];
                }
                else if("execution_vote_result_address".equals(strings[1])) {
                    //task.execute("Lookup_DB","execution_vote_result_address",title).get();
                    sendMsg = "check=Lookup_execution_vote_result_address&contents=" + strings[2];
                }
                else if("login_address".equals(strings[1])) {
                    //task.execute("Lookup_DB","login_address",id).get();
                    sendMsg = "check=Lookup_address&id=" + strings[2];
                }
                else if("login_role".equals(strings[1])) {
                    //task.execute("Lookup_DB","login_role",ID).get();
                    sendMsg = "check=Lookup_role&id=" + strings[2];
                }
            }
            osw.write(sendMsg);
            osw.flush();

            if("View".equals(strings[0])) {
                System.setProperty("http.keepAlive", "false");
            }
            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    if("View".equals(strings[0])) {
                        buffer.append(str+"\n");
                    }
                    else {
                        buffer.append(str);
                    }
                }
                receiveMsg = buffer.toString();

            } else {
                Log.i("통신 결과", conn.getResponseCode()+"에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }
}