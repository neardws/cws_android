package com.github.cqu_bdsc.collision_warning_system.infor;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.github.cqu_bdsc.collision_warning_system.DAO.Message;
import com.github.cqu_bdsc.collision_warning_system.MainActivity;
import com.github.cqu_bdsc.collision_warning_system.ntp.SntpClient;

public class InfoThread extends Thread {
    private static final String NTP_ALIYUN = "120.25.108.11";
    private static final int TIME_OUT = 200;

    public final static String ACTION_INFORMATION = "ACTION_INFORMATION";

    public final static String EXTRAR_INFORMATION = "EXTRAR_INFORMATION;";

    private final static int FREQUENCY = 100;
    private boolean stop;
    private InfoTool infoTool;
    private SntpClient sntpClient;
    private Context context;
    public InfoThread(Context context){
        this.context = context;
        infoTool = new InfoTool(context);
        sntpClient = new SntpClient();
    }

    @Override
    public void run() {
        super.run();
        stop = false;
        while (!stop){
            Message infor = infoTool.getInfo();
            if (sntpClient.requestTime(NTP_ALIYUN,TIME_OUT)) {
                long now = sntpClient.getNtpTime() + SystemClock.elapsedRealtime() - sntpClient.getNtpTimeReference();
                infor.setTimeStamp(now);
            } else {
                infor.setTimeStamp(-666);
            }
            sendIntentMessage(infor);//将数据广播出去
            try {
                sleep(FREQUENCY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopMe(){
        this.stop = true;
    }

    public void sendIntentMessage(Message message){
        Intent intent = new Intent();
        intent.setAction(InfoThread.ACTION_INFORMATION);//告诉android将要执行什么功能
        intent.putExtra(InfoThread.EXTRAR_INFORMATION, message);//信息
        context.sendBroadcast(intent);//广播信息
    }
}