package edu.nyit.csci455.geocircuit.Settings;

/**
 * Created by Matt on 11/15/2014.
 */


        import android.content.Context;
        import android.content.SharedPreferences;

public class AppSettings {
    private static final String UNIT_STRING = "MeasureUnit";
    private static final String PREF_NAME = "Speedometer";

    public static int getMeasureUnit(Context context){
        return getInt(context,AppSettings.UNIT_STRING);
    }

    public static void setMeasureUnit(Context context,int limit){
        putInt(context,AppSettings.UNIT_STRING,limit);
    }

    private static int getInt(Context context, String tag) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, 0);

        return pref.getInt(tag, 0);
    }

    public static void putInt(Context context, String tag, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(tag, value);
        editor.commit();
    }
}
