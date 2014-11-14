package edu.nyit.csci455.geocircuit.util;

import android.content.Context;

import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;

import edu.nyit.csci455.geocircuit.Interface.Constants;

/**
 * <p>Title: GeoCircuitDbHelper.java </p>
 * <p>Description:</p>
 *
 * @author jasonscott
 */
public class GeoCircuitDbHelper extends SQLiteOpenHelper {

    private static GeoCircuitDbHelper sInstance;

    private Context mContext;

    private final static String DATABASE_NAME = "geocircuit.db";
    private final static int DATABASE_VERSION = 1;

    // SQLite create statements
    public static final String SQL_CREATE_LOCATION_ENTRIES =
            " ("
                    + Constants.LOCATION_ID
                    + Constants.INTEGER_TYPE
                    + Constants.PRIMARY_KEY
                    + Constants.COMMA_SEP
                    + Constants.DATE_TIME
                    + Constants.INTEGER_TYPE
                    + Constants.COMMA_SEP
                    + Constants.LATITUDE
                    + Constants.REAL_TYPE
                    + Constants.COMMA_SEP
                    + Constants.LONGITUDE
                    + Constants.REAL_TYPE
                    + " )";

    public static final String SQL_CREATE_CIRCUIT_ENTRIES =
            " ("
                    + Constants.CIRCUIT_ID
                    + Constants.INTEGER_TYPE
                    + Constants.PRIMARY_KEY
                    + Constants.COMMA_SEP
                    + Constants.CIRCUIT_NAME
                    + Constants.TEXT_TYPE
                    + Constants.COMMA_SEP
                    + Constants.START_LOCATION
                    + Constants.INTEGER_TYPE
                    + Constants.COMMA_SEP
                    + Constants.END_LOCATION
                    + Constants.INTEGER_TYPE
                    + " )";

    /**
     * @param context
     * @return
     */
    public static GeoCircuitDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GeoCircuitDbHelper(context);
        }

        return sInstance;
    }

    /**
     * @param context
     */
    private GeoCircuitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQLiteDatabase.loadLibs(mContext);
        SQLiteDatabase database = super.getWritableDatabase(Constants.SECRET);

        database.execSQL(Constants.CREATE_TABLE
                + Constants.LOCATION_TABLE
                + SQL_CREATE_LOCATION_ENTRIES);

        database.execSQL(Constants.CREATE_TABLE
                + Constants.CIRCUITS_TABLE
                + SQL_CREATE_CIRCUIT_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
