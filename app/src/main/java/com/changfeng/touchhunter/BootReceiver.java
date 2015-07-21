package com.changfeng.touchhunter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by changfeng on 2015/4/2.
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, TAG + " Boot completed.", Toast.LENGTH_LONG).show();

        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);
    }
}
