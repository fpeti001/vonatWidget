package yess.barmimass.csabilusta;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

public class Reciver extends BroadcastReceiver {
    Context context1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, HatterActivity.class);

        ContextCompat.startForegroundService(context, serviceIntent);
    }


}
