package com.github.cqu_bdsc.collision_warning_system.udp;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import com.github.cqu_bdsc.collision_warning_system.DAO.Result;
import com.github.cqu_bdsc.collision_warning_system.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveThread extends Thread {

    private Context context;

    private boolean stop = false;

    public static final String ACTION_STRING = "ACTION_STRING";
    public static final String ACTION_JSON     = "ACTION_JSON";

    public static final String  JSON_CONTEXT = "JSON_CONTEXT";

    public static final String STRING_CONTEXT = "STRING_CONTEXT";

    private static final String SERVER_IP = "192.168.42.129";
    private static final String RECEIVE_PORT = "4040";

    private String serverIp = null;//流，可以装东西

    public ReceiveThread(){
        this.serverIp = SERVER_IP;
    }
    public ReceiveThread(Context context,String serverIp){
        this.context = context;
        this.serverIp = serverIp;
    }

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    @Override
    public void run() {//线程的main函数

        try {
            DatagramPacket reveivePacket;
            DatagramSocket reveiveSocket;

            reveiveSocket = new DatagramSocket(Integer.valueOf(RECEIVE_PORT));//把接收端口号的字符串变成整数
            /*

             */

            byte[] buff = new byte[SendService.BUFF_SIZE];//发送过来的数据的长度范围

            String dataType = null;//先不限定数据的类型
            while (!stop){
                reveivePacket = new DatagramPacket(buff, buff.length);
                System.out.print("收到消息");
                try {//try和catch合作，如果出错可以马上看到是哪个地方的错误
                    reveiveSocket.receive(reveivePacket);
                    String msg = new String(reveivePacket.getData(),0,reveivePacket.getLength());
                    HandleData(msg);
                    try {
                        JSONObject jsonObject = new JSONObject(msg);
                        HandleData(jsonObject);
                        //HandleData(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();//IO即输入输出错误，错误地方用e来表示，在哪里出错就在哪里打印出e
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void stopMe(){
        this.stop = true;
    }

    private void HandleData(JSONObject jsonObject){//处理数据函数
        Result result = new Result();
        result.fromJSON(jsonObject);
        SendIntent( result);
    }

    private void HandleData(String string){//处理数据函数
        SendIntent(string);

    }


    private void SendIntent( String something){
        Intent intent = new Intent();
        intent.setAction(ReceiveThread.ACTION_STRING);
        intent.putExtra(ReceiveThread.STRING_CONTEXT, something);
        context.sendBroadcast(intent);
    }

    private void SendIntent(Result something){
        Intent intent = new Intent();
        intent.setAction(ReceiveThread.ACTION_JSON);
        intent.putExtra(ReceiveThread.JSON_CONTEXT,something);
        context.sendBroadcast(intent);
    }
}

