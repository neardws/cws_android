package com.github.cqu_bdsc.collision_warning_system.udp;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.github.cqu_bdsc.collision_warning_system.DAO.Message;
import com.github.cqu_bdsc.collision_warning_system.MainActivity;
import com.github.cqu_bdsc.collision_warning_system.ntp.SntpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SendService extends IntentService  {//继承父类IntentService

    public static final int BUFF_SIZE = 8192;//最大数据长度

    public static final String ACTION_SEND_STRING = "SEND_STRING";
    public static final String ACTION_SEND_JSON = "SEND_JSON";
    public static final String ACTION_SEND_TCP = "SEND_TCP";

    public static final String EXTRAS_HOST      = "EXTRAS_HOST";//主机
    public static final String EXTRAS_PORT      = "EXTRAS_PORT";//端口
    public static final String EXTRAS_STRING    = "EXTRAS_STRING";//在子类中定义常量
    public static final String EXTRAS_JSON        = "EXTRAS_JSON";

    private static final String NTP_ALIYUN = "120.25.108.11";
    private static final int TIME_OUT = 200;

    private SntpClient sntpClient;

    public SendService(String name){
        super(name);
    }
    public SendService(){
        super("SendService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context context = getApplicationContext();
        sntpClient = new SntpClient();
        /**
         * 发送字符串
         */
        try {
            String action = (String) intent.getAction();
            String host = intent.getExtras().getString(EXTRAS_HOST);
            final String port = intent.getExtras().getString(EXTRAS_PORT);
            final InetAddress serverAddr = InetAddress.getByName(host);
            //InetAddress getAddress()   返回某台机器的IP地址，此数据报是将要发往该主机或是从该主机接收到的

            DatagramSocket udpSocket = null;
            DatagramPacket packet = null;


            /**
             *  发送字符串
             */
            if (action.equals(ACTION_SEND_STRING)){


                String extras_string = intent.getExtras().getString(EXTRAS_STRING);
                try {
                    if (udpSocket == null){
                        udpSocket = new DatagramSocket();
                    }
                    byte[] buff = (byte[]) extras_string.getBytes();

                    packet = new DatagramPacket(buff, buff.length,serverAddr,Integer.valueOf(port));
                    udpSocket.send(packet);
                } catch (SocketException e) {
                    e.printStackTrace();
                }  finally {
                    if (udpSocket != null){
                        udpSocket.close();
                    }
                }
            } else if (action.equals(SendService.ACTION_SEND_JSON)){
                final Message message = (Message) intent.getExtras().get(EXTRAS_JSON);
                if (sntpClient.requestTime(NTP_ALIYUN,TIME_OUT)) {
                    long now = sntpClient.getNtpTime()+ SystemClock.elapsedRealtime()- sntpClient.getNtpTimeReference();
                    message.setTimeStamp(now);
                } else {
                    message.setTimeStamp(-666);
                }
                final JSONObject jsonObject = message.toJSON();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DatagramSocket udpSocket = null;
                        DatagramPacket packet = null;
                        try {
                            if (udpSocket == null){
                                udpSocket = new DatagramSocket();
                            }


                            //构造数据包，用来将length长度的数据包发送到指定主机（指定mac地址的主机）上的指定端口号
                            //udpSocket.send(packet);

                            for (int i = 1; i <= 51; i++){
                                jsonObject.put("num", String.valueOf(i));
                                byte[] buff = (byte[]) jsonObject.toString().getBytes("UTF-8");

                                packet = new DatagramPacket(buff, buff.length,serverAddr,Integer.valueOf(port));
                                udpSocket.send(packet);
                            }
                        } catch (SocketException e) {
                            e.printStackTrace();
                        }  catch (IOException e){
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            if (udpSocket != null){
                                udpSocket.close();
                            }
                        }
                    }
                }).start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //1.创建客户端Socket，指定服务器地址和端口
                            Socket socket=new Socket("120.78.167.211", 4040);
                            //2.获取输出流，向服务器端发送信息
                            OutputStream os=socket.getOutputStream();//字节输出流
                            PrintWriter pw=new PrintWriter(os);//将输出流包装为打印流
                            int i = 1;
                            while (i <= 51){
                                jsonObject.put("num", i);
                                byte[] buff = (byte[]) jsonObject.toString().getBytes("UTF-8");
                                String str = new String(buff);
                                pw.write(str);
                                pw.flush();
                                i = i + 1;
                            }

//                            byte[] buff = (byte[]) jsonObject.toString().getBytes("UTF-8");
//                            String str = new String(buff);
//                            pw.write(str);
//                            pw.flush();
                            socket.shutdownOutput();//关闭输出流
                            //4.关闭资源
                            pw.close();
                            os.close();
                            socket.close();
                        } catch (UnknownHostException e) {//
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
//                try {
//                    if (udpSocket == null){
//                        udpSocket = new DatagramSocket();
//                    }
//                    byte[] buff = (byte[]) jsonObject.toString().getBytes("UTF-8");
//
//                    packet = new DatagramPacket(buff, buff.length,serverAddr,Integer.valueOf(port));
//                    //构造数据包，用来将length长度的数据包发送到指定主机（指定mac地址的主机）上的指定端口号
//                    udpSocket.send(packet);
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }  catch (IOException e){
//                    e.printStackTrace();
//                }finally {
//                    if (udpSocket != null){
//                        udpSocket.close();
//                    }
//                }


//                try {
//                    //1.创建客户端Socket，指定服务器地址和端口
//                    Socket socket=new Socket("120.78.167.211", 4040);
//                    //2.获取输出流，向服务器端发送信息
//                    OutputStream os=socket.getOutputStream();//字节输出流
//                    PrintWriter pw=new PrintWriter(os);//将输出流包装为打印流
//                    byte[] buff = (byte[]) jsonObject.toString().getBytes("UTF-8");
//                    String str = new String(buff);
//                    pw.write(str);
//                    pw.flush();
//                    socket.shutdownOutput();//关闭输出流
//                    //4.关闭资源
//                    pw.close();
//                    os.close();
//                    socket.close();
//                } catch (UnknownHostException e) {//
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            } else if (action.equals(SendService.ACTION_SEND_TCP)){
//                Message message = (Message) intent.getExtras().get(EXTRAS_JSON);
//                JSONObject jsonObject = message.toJSON();
//                try {
//                    //1.创建客户端Socket，指定服务器地址和端口
//                    Socket socket=new Socket("120.78.167.211", 4040);
//                    //2.获取输出流，向服务器端发送信息
//                    OutputStream os=socket.getOutputStream();//字节输出流
//                    PrintWriter pw=new PrintWriter(os);//将输出流包装为打印流
//                    byte[] buff = (byte[]) jsonObject.toString().getBytes("UTF-8");
//                    String str = new String(buff);
//                    pw.write(str);
//                    pw.flush();
//                    socket.shutdownOutput();//关闭输出流
//                    //4.关闭资源
//                    pw.close();
//                    os.close();
//                    socket.close();
//                } catch (UnknownHostException e) {//
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            } else {

            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
