package com.changfeng.touchhunter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class LongRunningService extends Service {

    private static final String TAG = "LongRunningService";
    private static final int ONE_SECOND = 1000;

    private static final int MSG_WHAT_START_SLIDER = 1;
    String outputFile;

    private int postInterval = 30;




    long startTime;
    long elapsed_time;


    Handler startActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_START_SLIDER:
                    Intent i = new Intent();
                    i.setClassName("com.changfeng.touchhunter", "com.changfeng.touchhunter.ImageSliderActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        postInterval = pref.getInt(MainActivity.SLIDER_INTERVAL, 30);

        startTime = System.nanoTime(); // 开始时间

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(LongRunningService.this, "Service中子线程启动！", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
        outputFile =  Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "event.txt";
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        elapsed_time = (System.nanoTime() - startTime) / 1000 /1000 /1000;



        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, ONE_SECOND);
                }
//            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run(){
            if (!Shell.shellExecCommand("ps | grep getevent", "su")) {
                Toast.makeText(getApplicationContext(), "ps | prep getevent  failed", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Thread.sleep(ONE_SECOND);

                    try {
                        if (!MyFile.containString(outputFile, "/dev/input/event2:")) {
                            Log.i(TAG, "start activity Slider");
                            startActivityHandler.sendEmptyMessage(MSG_WHAT_START_SLIDER);
                        } else {
                            Log.i(TAG, "target string hunted, so not to start slider");
                        }

                    } catch (IOException e) {
//                        Toast.makeText(getApplicationContext(), "read file failed.", Toast.LENGTH_SHORT);
                    }
                } catch (InterruptedException e) {
//                    Toast.makeText(getApplicationContext(), "LongRunningService InterruptedException", Toast.LENGTH_SHORT).show();
                }
            }

            File file = new File(outputFile);
            if (file.exists()){
                file.delete();
            }


            if (getSharedPreferences("data", MODE_PRIVATE).getBoolean(MainActivity.STOP_SERVICE, false)) {
                return;
            }

            if (!Shell.shellExecCommand("getevent > " + outputFile + " &", "su")){
//                Toast.makeText(getApplicationContext(), "getevent > " + outputFile + " fail", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(getApplicationContext(), "getevent >" + outputFile + " success", Toast.LENGTH_SHORT).show();
            }

            handler.postDelayed(this, postInterval * ONE_SECOND);
        }
    };
}
