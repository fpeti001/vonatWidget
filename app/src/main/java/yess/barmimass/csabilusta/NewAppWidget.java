package yess.barmimass.csabilusta;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.csabilusta.R;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {



        // Instruct the widget manager to update the widget

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        Intent intent = new Intent(context, MainActivity.class);
        Intent intent2 = new Intent(context, Settings.class);
        //saveBoolean("widget",true,context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 3, intent2, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        views.setOnClickPendingIntent(R.id.refresh_button, pendingIntent);
        views.setOnClickPendingIntent(R.id.felso_fogaskerek, pendingIntent2);

        appWidgetManager.updateAppWidget(appWidgetIds, views);




        //saveBoolean("widget",true,context);





      /*  Intent intent = new Intent(context, HatterActivity.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setOnClickPendingIntent(R.id.refresh_button, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, views);*/



    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        saveBoolean("widget",true,context);

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        saveBoolean("widget",true,context);

    }
    public void saveBoolean (String milyenneven,Boolean mit,Context contextbe){
        PreferenceManager.getDefaultSharedPreferences(contextbe).edit().putBoolean(milyenneven+"boolean", mit).apply();

    }
}

