package com.example.capstone;


import android.os.Environment;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class Upload_Transaction_Task {

    private String Address;
    private String pwd;
    private Credentials credentials;
    private static final String EXTENSIONS = ".json";
    private String Transaction_Hash;

    public String Send_Transaction(String... strings) {
        //strings[0]은 input_data, strings[1]은 Address, strings[2]는 비밀번호

        Address = strings[1];
        pwd = strings[2];
        String sendMsg = stringToHex(strings[0]);

        //Geth 연결
        Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.0.2:8545"));  // defaults to http://localhost:8545/

        try {
            LoadWallet();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }

        // get the next available nonce
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(
                    Address, DefaultBlockParameterName.LATEST).sendAsync().get();
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        BigInteger value = BigInteger.valueOf(Long.MAX_VALUE);

        // create transaction
        RawTransaction rawTransaction  = RawTransaction.createTransaction(
                nonce,
                BigInteger.valueOf(2000000020L), //gasPrice
                BigInteger.valueOf(500000L), //gasLimit
                Address,
                value,
                sendMsg);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethCall = null;
        try {
            ethCall = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Transaction_Hash = ethCall.getTransactionHash();
        return Transaction_Hash;

    }

    // 문자열을 헥사 스트링으로 변환하는 메서드
    public String stringToHex(String s) {
        String result = "0x";
        for (int i = 0; i < s.length(); i++) {
            result += String.format("%02X", (int) s.charAt(i));
        }
        return result;
    }

    //    각 안드로이드 모바일 기기 당 1개의 계좌만 있어야함.
    private Credentials LoadWallet() throws IOException, CipherException {
        String[] result = new String[2];
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/ethereum"); //다운로드 path 가져오기
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
}