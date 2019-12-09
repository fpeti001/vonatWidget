package yess.barmimass.csabilusta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

public class AlertReceiver extends BroadcastReceiver {
    Context context1;
    String getExtra;
    String sendExtra;
    @Override
    public void onReceive(Context context, Intent intent) {
       String ize= BasicMethods.loadString("ido",context);
       ize+="idozitett:";
       BasicMethods.saveString("ido",ize,context);
        Intent serviceIntent = new Intent(context, HatterActivity.class);
        ContextCompat.startForegroundService(context, serviceIntent);

    }


}