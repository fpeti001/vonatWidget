package yess.barmimass.csabilusta;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

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
import java.util.Locale;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static yess.barmimass.csabilusta.App.CHANNEL_ID;


public class HatterActivity extends Service {
    Boolean siker=false;
    String lista="";
    String listaSchedule="";
    String elviraurl="https://apiv2.oroszi.net/elvira/leaderboard?station=ferencvaros";
    String elviraFerencToKispest = "https://apiv2.oroszi.net/elvira?from=Ferencv%C3%A1ros&to=K%C5%91b%C3%A1nya-Kispest";
    String honnanHovaURL;
    String odaURL;
    String visszaURL;
    TextView kiiro;
    Date tegnap;
    int keszAletoltes;

    List<Date> realTimeArray = new ArrayList<Date>();
    List<Date> scheduleTimeArray = new ArrayList<Date>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        String eddigJegyzett=BasicMethods.loadString("ido",getApplicationContext());
        eddigJegyzett+=egyszeruIdo()+"\n";
        BasicMethods.saveString("ido",eddigJegyzett,getApplicationContext());
        if (BasicMethods.loadBoolean("autoUpdate",getApplicationContext())&&vanMarAlarm()==false){
        ismetles();
        }
     //   Toast.makeText(HatterActivity.this, "Frissítés", Toast.LENGTH_SHORT).show();
        String honnan=BasicMethods.loadString("honnan",getApplicationContext());
        String hova=BasicMethods.loadString("hova",getApplicationContext());
        honnanHovaURL="https://apiv2.oroszi.net/elvira?from="+honnan+"&to="+hova;
        odaURL="https://apiv2.oroszi.net/elvira?from="+honnan+"&to="+hova;
        visszaURL="https://apiv2.oroszi.net/elvira?from="+hova+"&to="+honnan;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        tegnap=cal.getTime();


        listaLetolto(odaURL,"oda");
        listaLetolto(visszaURL,"vissza");

































        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void listaLetolto(String url, final String melyikIrany){

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {


                try {
                    String depRealLista="";
                    String depLista="";
                    JSONObject reader = new JSONObject(string);
                    JSONArray items = reader.getJSONArray("timetable");
                    List<Date> realTimeArrayHelyi = new ArrayList<Date>();
                    List<Date> scheduleTimeArrayHelyi = new ArrayList<Date>();


             /*           for (int i = 0; i < items.length(); i++) {

                            JSONObject elso = items.getJSONObject(i);
                            String name = elso.getString("name");
                            lista = lista + name + "\n";
                        }*/


/*RÉGI JÓL MŰKÖDŐ CSAK FERENCVÁROSTOL
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


                    }*/


                    for (int i = 0; i < items.length(); i++) {
                        String depReal;
                        String dep;
                        JSONObject egyJarat = items.getJSONObject(i);
                        JSONArray details=egyJarat.getJSONArray("details");
                        JSONObject elsoObj =details.getJSONObject(0);
                            if (elsoObj.isNull("dep_real") == false) {

                                dep=elsoObj.getString("dep");
                                depReal=elsoObj.getString("dep_real");


                                if (depReal.equals("")){
                                    depReal=dep;
                                }
                                if (dep.equals("")){
                                    depReal="00:01";
                                    dep="00:01";
                                }


                                    realTimeArrayHelyi.add(stringDatumraAlakit2(depReal));
                                    scheduleTimeArrayHelyi.add(stringDatumraAlakit2(dep));

                                if (melyikIrany.equals("oda")) {
                                    depLista+=dep+"\n";

                                }else{
                                    depRealLista += dep + "\n";

                                }
                            }

                    }

                    if (melyikIrany.equals("oda")) {
                        BasicMethods.saveString("depLista",depLista,getApplicationContext());

                    }else{
                        BasicMethods.saveString("depRealLista",depRealLista,getApplicationContext());

                    }
                    keszAletoltes++;

                    if (keszAletoltes==2)receiverKuldes("asd");

                    valaszto(scheduleTimeArrayHelyi,realTimeArrayHelyi,melyikIrany);
                } catch (JSONException e) {
                    widgetNagyValto("try error",Color.RED,R.id.real_text);
                    widgetKicsiValto("try error",Color.RED,R.id.widget_kicsi_text);

                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                widgetNagyValto("try error",Color.RED,R.id.real_text);
                widgetKicsiValto("try error",Color.RED,R.id.widget_kicsi_text);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(HatterActivity.this);
        rQueue.add(request);

        //   return listdata;
    }

    public void kiiroKetto(Date schedule,Date real,String melyikIrany){

        int color;
        Date date = real;
        Date dateSchedule = schedule;
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH:mm");
        String kiiirasra = simpleDateFormat2.format(date);
        String kiiirasraSchedule = simpleDateFormat3.format(dateSchedule);
        if (melyikIrany.equals("oda")){
            if (date.after(dateSchedule)){
                color=Color.RED;
            }else {
                color=Color.GREEN;
            }
            widgetKicsiValto("Oda:\n"+kiiirasra,color,R.id.widget_kicsi_text);
            widgetNagyValto(kiiirasra,color,R.id.schedule_text);
        }else {
            if (date.after(dateSchedule)){
                color=Color.RED;
            }else {
                color=Color.GREEN;
            }
            widgetKicsiValto("Vissza:\n"+kiiirasra,color,R.id.widget_kicsi_text2);
            widgetNagyValto(kiiirasra,color,R.id.real_text);
        }






        //valtsdat(kiiirasra,kiiirasraSchedule);

      // String kozos="Menetrend: "+kiiirasra+" Igazábóll: "+kiiirasraSchedule+ "sorszam: " +sorszam;

       // kiiro.setText(kiiro.getText()+kozos);
    }





    public Date  stringDatumraAlakit(String time){
        SimpleDateFormat simpleDate=new SimpleDateFormat("hh:mm");
        Date date;
        try {

            date=simpleDate.parse(time);

        } catch (ParseException e) {
            date=tegnap;
        }


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

    public void valaszto(List<Date> schedule,List<Date> real,String melyikIrany){
        Date mostido=ido();
        int sorszam=9999;
        for (int i=0;i<real.size();i++){
            if (real.get(i)!=null) {
                if (real.get(i).after(mostido)) {
                    sorszam = i;
                    break;
                }
            }


        }
        if (sorszam!=9999) {


            kiiroKetto(schedule.get(sorszam),real.get(sorszam),melyikIrany);
        }else {  Toast.makeText(HatterActivity.this, "ERRROOORRR", Toast.LENGTH_LONG).show();}



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
    public void widgetNagyValto(String text,int color,int melyikText){
        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        ComponentName thisWidget = new ComponentName(context, NewAppWidget.class);
        remoteViews.setTextColor(melyikText,color);
        remoteViews.setTextViewText(melyikText, text );
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);




        siker=true;
    }
    public void widgetKicsiValto(String text,int color,int melyikText){
        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_kicsi);
        ComponentName thisWidget = new ComponentName(context, WidgetKicsi.class);
        remoteViews.setTextColor(melyikText,color);
        remoteViews.setTextViewText(melyikText, text );
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);




        siker=true;
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
    public void receiverKuldes(String extra) {
        Intent intent = new Intent();
        intent.setAction("com.toxy.LOAD_URL");
        intent.putExtra("extra", extra);
        sendBroadcast(intent);
    }
    public void ismetles() {


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1001, intent, FLAG_CANCEL_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 5);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 35);
        calendar.set(Calendar.SECOND, 00);




        long startUpTime = cal.getTimeInMillis();

        alarmManager.setRepeating(AlarmManager.RTC, startUpTime, 1000*60*10, pendingIntent);
    }
    public String egyszeruIdo() {
        String date = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        return date;
    }
    public  Boolean vanMarAlarm(){
        Boolean visszaad=true;
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 1001, intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        boolean isWorking =(alarmManager!= null);
        boolean isWorking2 =(sender!= null);
        if (isWorking&&isWorking2){}else {visszaad=false;}
        return visszaad;
    }
}