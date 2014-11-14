package edu.nyit.csci455.geocircuit.util;

import android.content.Context;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;

/**
 * <p>Title: GeoDbHelper.java </p>
 * <p>Description:</p>
 *
 * @author jasonscott
 */
public class GeocircuitDbHelper extends SQLiteOpenHelper {

    private static GeocircuitDbHelper sInstance;

    private final static String DATABASE_NAME = "geocircuit.db";
    private final static int DATABASE_VERSION = 1;


    /**
     * @param context
     * @return
     */
    public static GeocircuitDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GeocircuitDbHelper(context);
        }

        return sInstance;
    }

    /**
     * @param context
     */
    private GeocircuitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
