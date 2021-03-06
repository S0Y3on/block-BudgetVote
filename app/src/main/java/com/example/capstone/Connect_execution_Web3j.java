package com.example.capstone;

import android.os.Environment;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.lang.*;

public class Connect_execution_Web3j {

    //컨트랙트주소
    private String contract_addr = "0xbC31b53a5a5d139484812AA1BF2f616488F40Cb7";
    //패스워드 db 로그인정보 가져와야함.
    private String pwd;
    private Web3j web3j;
    private Credentials credentials;
    private Vote_Executions vote_execution;
    private static final String EXTENSIONS = ".json";

    public Connect_execution_Web3j(String password) throws IOException, CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {

        this.pwd = password;
        web3j = Web3jFactory.build(new HttpService("http://192.168.0.2:8545"));

        LoadWallet();
        vote_execution = Vote_Executions.load(contract_addr,web3j,credentials,BigInteger.valueOf(50000),BigInteger.valueOf(1000000));
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initVote_execution(String vote_date) throws ExecutionException, InterruptedException {
        byte[] date;
        date = StringToByte32(vote_date);
        TransactionReceipt transactionReceipt = vote_execution._initExecution(date).sendAsync().get();
    }

    public String getResult_execution() throws ExecutionException, InterruptedException {
        countVoted();
        TransactionReceipt transactionReceipt = vote_execution._getResult_Execution().sendAsync().get();
        ArrayList<Vote_Executions.ResultEventResponse> responses = (ArrayList<Vote_Executions.ResultEventResponse>) vote_execution.getResultEvents(transactionReceipt);

        BigInteger agree = responses.get(0)._agree;
        BigInteger disagree = responses.get(0)._disagree;
        BigInteger total_vote = responses.get(0)._total_vote;

        String result_execution = "agree:"+agree.toString()+" disagree:"+disagree.toString()+" total_vote:"+total_vote;
        return result_execution;
    }

    public void voteToExecution(String st_name, String st_id, boolean vote_option) throws ExecutionException, InterruptedException {
        byte[] name,id;
        name = StringToByte32(st_name);
        id = StringToByte32(st_id);

        vote_execution._voteToExecution(name,id,vote_option).sendAsync().get();
        web3j.ethMining();
    }

    public void countVoted() throws ExecutionException, InterruptedException {
        TransactionReceipt transactionReceipt = vote_execution._countVoted_Execution().sendAsync().get();
    }

    //    각 안드로이드 모바일 기기 당 1개의 계좌만 있어야함.
    private Credentials LoadWallet() throws IOException, CipherException {
        String[] result = new String[2];
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/ethereum"); //다운로드 path 가져오기
        result[0] = extensionFilter(path);

        credentials = WalletUtils.loadCredentials(pwd,result[0]);
        result[1] = credentials.getAddress();

        return credentials;
    }

    private static String extensionFilter(File folder) {
        String result = null;
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (EXTENSIONS.contains(file.getName().substring(file.getName().lastIndexOf(".")))) {
                    result = file.toString();
                    return result;
                }
            }
        }
        return result;
    }

    private static byte[] StringToByte32(String str){
        byte [] date = new byte[32];
        byte [] tmp = str.getBytes();

        for(int i=0;i<tmp.length; i++){
            date[i] = tmp[i];
        }
        return date;
    }

    private static String ByteToString32(Byte b){
        String result = b.toString();
        return result;
    }
}
