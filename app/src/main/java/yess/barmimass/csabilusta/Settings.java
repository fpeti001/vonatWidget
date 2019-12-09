package yess.barmimass.csabilusta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.csabilusta.R;

import static android.app.PendingIntent.FLAG_NO_CREATE;

public class Settings extends AppCompatActivity {

    TextView depTextView;
    TextView depRealTextView;
    TextView honnanIndul;
    TextView hovaMegy;
    Switch autoUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        IntentFilter filter = new IntentFilter("com.toxy.LOAD_URL");
        this.registerReceiver(new Receiver(), filter);

        honnanIndul=findViewById(R.id.honnan_indul);
        honnanIndul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AllomasKereso.class);
                intent.putExtra("extra","honnan");
                startActivity(intent);
            }
        });

        hovaMegy=findViewById(R.id.hova_megy);
        hovaMegy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AllomasKereso.class);
                intent.putExtra("extra","hova");
                startActivity(intent);
            }
        });

        Button button =findViewById(R.id.frissites_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(getApplicationContext(), HatterActivity.class);


                ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);

            }
        });

       String honnan= BasicMethods.loadString("honnan",this);
        honnanIndul.setText(honnan);

        String hova=BasicMethods.loadString("hova",this);
        hovaMegy.setText(hova);

         depTextView=findViewById(R.id.dep_text_view);
        String depString=BasicMethods.loadString("depLista",this);
        depTextView.setText(depString);

         depRealTextView=findViewById(R.id.dep_real_text_view);
        String depRealString=BasicMethods.loadString("depRealLista",this);
        depRealTextView.setText(depRealString);

autoUpdate=findViewById(R.id.switch_auto_update_settings);
autoUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        BasicMethods.saveBoolean("autoUpdate",isChecked,getApplicationContext());
        if (!isChecked)stopService();
    }
});

    }
    @Override
    public void onPause() {
        super.onPause();
        this.finish();
    }


    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent arg1) {

            depTextView=findViewById(R.id.dep_text_view);
            String depString=BasicMethods.loadString("depLista",context);
            depTextView.setText(depString);

            depRealTextView=findViewById(R.id.dep_real_text_view);
            String depRealString=BasicMethods.loadString("depRealLista",context);
            depRealTextView.setText(depRealString);
        }
    }
    public void stopService() {


        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 1001, intent, FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager!= null&&sender!=null) {
            alarmManager.cancel(sender);
        }
        if (sender!=null) {
            sender.cancel();
        }


    }
}
