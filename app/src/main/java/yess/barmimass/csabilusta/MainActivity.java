package yess.barmimass.csabilusta;

import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.csabilusta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

public class MainActivity extends Activity {
    final ScreenActionReceiver screenactionreceiver = new ScreenActionReceiver();
    String lista="";
    String listaSchedule="";
    String elviraurl="https://apiv2.oroszi.net/elvira/leaderboard?station=ferencvaros";
    String napiurl = "https://fortnite-public-api.theapinetwork.com/prod09/store/get";
TextView kiiro;
TextView kiiro2;
Date tegnap;

    List<Date> realTimeArray = new ArrayList<Date>();
    List<Date> scheduleTimeArray = new ArrayList<Date>();

    @Override
    public void onPause() {


        super.onPause();

      //  this.finish();
        unregisterReceiver(screenactionreceiver);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(screenactionreceiver, screenactionreceiver.getFilter());
  /*     kiiro=findViewById(R.id.ki_iro);
        kiiro2=findViewById(R.id.ki_iro2);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR,20 );
        tegnap=cal.getTime();*/

  //   listaLetolto();
/*Boolean widgetIndit=loadBoolean("widget");
if (widgetIndit) {
        startService();


}else {
    Intent intent = new Intent(this, Settings.class);
    startActivity(intent);
}
        saveBoolean("widget",false);*/





        startService();
finish();








      /* CountDownTimer visszaszamolo=new CountDownTimer(5000, 5000) { // adjust the milli seconds here


            public void onTick(long millisUntilFinished) {


                invalidateOptionsMenu();
            }

            public void onFinish() {
                kiiro.setText(lista);
            }
        }.start();*/

    }
    public void saveBoolean (String milyenneven,Boolean mit){
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(milyenneven+"boolean", mit).apply();

    }
    public  Boolean loadBoolean (String milyenNeven){
        Boolean mit= PreferenceManager.getDefaultSharedPreferences(this).getBoolean(milyenNeven+"boolean", false);
        return mit;
    }

    public void listaLetolto(){

            StringRequest request = new StringRequest(elviraurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String string) {


                    try {


                        JSONObject reader = new JSONObject(string);
                        JSONArray items = reader.getJSONArray("trains");


             /*           for (int i = 0; i < items.length(); i++) {

                            JSONObject elso = items.getJSONObject(i);
                            String name = elso.getString("name");
                            lista = lista + name + "\n";
                        }*/





                        for (int i = 0; i < items.length(); i++) {
                            String erkezes;
                            String erkezesSchedule;
                            JSONObject elso = items.getJSONObject(i);
                            String neve = elso.getString("line");
                            neve=neve.substring(neve.lastIndexOf('-') + 1);
                            if (neve.contains("Kispest")) {
                                if (elso.isNull("real") == false) {
                                    JSONObject schedule = elso.getJSONObject("schedule");
                                    erkezesSchedule = schedule.getString("arrival");
                                    JSONObject real = elso.getJSONObject("real");
                                    erkezes = real.getString("arrival");

                                    realTimeArray.add(stringDatumraAlakit2(erkezes));
                                    scheduleTimeArray.add(stringDatumraAlakit2(erkezesSchedule));
                                } else {


                                }
                            }


                        }





                  kiiro();
                             valaszto();


                    } catch (JSONException e) {
                     //   kiiro.setText(lista);
                        kiiro.setText("Error letoltes try");

                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                kiiro.setText("Error letoltes");

                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
            rQueue.add(request);

            //   return listdata;
    }
    public void kiiro(){
        for (int i=0;i<realTimeArray.size();i++){
            if (realTimeArray.get(i)!=null) {
                Date date = realTimeArray.get(i);
                Date dateSchedule = scheduleTimeArray.get(i);
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH:mm");
                String kiiirasra = simpleDateFormat2.format(date);
                String kiiirasraSchedule = simpleDateFormat3.format(dateSchedule);
                lista = lista +kiiirasra +"\n" ;
                listaSchedule = listaSchedule  +kiiirasraSchedule +"\n" ;;

            }else{
                lista = lista + "sajt" + "*" + i + "*";
                listaSchedule = listaSchedule + "sajt" + "*" + i + "*";
            }

        }



String kozos=lista+listaSchedule;

        kiiro.setText(listaSchedule);
        kiiro2.setText(lista);
    }
    public void kiiroKetto(int sorszam){


                Date date = realTimeArray.get(sorszam);
                Date dateSchedule = scheduleTimeArray.get(sorszam);
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH:mm");
                String kiiirasra = simpleDateFormat2.format(date);
                String kiiirasraSchedule = simpleDateFormat3.format(dateSchedule);









        String kozos="Menetrend: "+kiiirasra+"\n Igazábóll: "+kiiirasraSchedule+ "sorszam: " +sorszam;

        kiiro.setText(kiiro.getText()+kozos);
    }




    public Date  stringDatumraAlakit(String time){
        SimpleDateFormat simpleDate=new SimpleDateFormat("hh:mm");
        Date date;
        try {
            date=Calendar.getInstance().getTime();
            date=simpleDate.parse(time);

        } catch (ParseException e) {
            date=tegnap;
        }

     //   date=Calendar.getInstance();
        return date;
    }

    public Date  stringDatumraAlakit2(String time){
        String[] reszek=time.split(":");
        int ora=Integer.parseInt(reszek[0]);
        int perc=Integer.parseInt(reszek[1]);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,ora);
        calendar.set(Calendar.MINUTE,perc);
        Date date=calendar.getTime();
        //   date=Calendar.getInstance();
        return date;
    }


    public void valaszto(){
        Boolean voltBrake=false;
        Date mostido=ido();
        int sorszam=9999;
for (int i=0;i<realTimeArray.size();i++){
    Date datumm=realTimeArray.get(i);
    if (realTimeArray.get(i)!=null) {
        if (realTimeArray.get(i).before(mostido)) {
            sorszam = i;
        } else {
            sorszam=i;
            voltBrake=true;
            break;
        }
    }


}
if (voltBrake) {
    if (sorszam != 9999) {
        kiiroKetto(sorszam);
    } else {
        kiiro.setText("error");
    }
}else {    kiiro.setText(kiiro.getText()+"error nincs brake");}


}
    public Date ido() {
     /*   Date date;
        String dateString = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
        SimpleDateFormat simpleDate=new SimpleDateFormat("hh:mm");
        date=simpleDate.parse(dateString);
        return date;*/
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime;

    }


    public void asd(View view){
       // valtsdat();
       // startService();
        startReviever();
    }
    public void startService() {

      /*  String input = editTextInput.getText().toString();

        Intent serviceIntent = new Intent(this, ExampleService.class);
        //serviceIntent.putExtra("inputExtra", input);

   //   ContextCompat.startForegroundService(this, serviceIntent);
        this.startService(serviceIntent);*/


        Intent serviceIntent = new Intent(this, HatterActivity.class);


        ContextCompat.startForegroundService(this, serviceIntent);




    }
    public void startReviever(){
        Intent intent = new Intent(this, Reciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1001, intent, FLAG_CANCEL_CURRENT);

        this.startService(new Intent(this, Reciver.class));

    }

}
