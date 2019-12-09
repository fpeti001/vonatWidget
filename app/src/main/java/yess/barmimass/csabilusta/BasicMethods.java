package yess.barmimass.csabilusta;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BasicMethods {



    public static void saveBoolean (String milyenneven,Boolean mit,Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(milyenneven+"boolean", mit).apply();

    }
    public static Boolean loadBoolean (String milyenNeven,Context context){
        Boolean mit= PreferenceManager.getDefaultSharedPreferences(context).getBoolean(milyenNeven+"boolean", false);
        return mit;
    }



    public static void saveString(String milyenneven, String mit,Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(milyenneven + "string", mit).apply();

    }

    public static String loadString(String milyenNeven, Context context) {
        String mit = PreferenceManager.getDefaultSharedPreferences(context).getString(milyenNeven + "string", " ");
        return mit;
    }

    public static  void saveData(List<String> arrayList, String milyenneven,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(milyenneven, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("task list", json);
        editor.apply();
    }

    public static List<String> loadData(String melyiket,Context context) {
        List<String> arrayList = new ArrayList<String>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(melyiket, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        arrayList = gson.fromJson(json, type);

        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        return arrayList;
    }

}
