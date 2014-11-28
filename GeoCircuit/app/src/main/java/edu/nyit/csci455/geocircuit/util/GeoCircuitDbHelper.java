package edu.nyit.csci455.geocircuit.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

//import net.sqlcipher.SQLException;
//import net.sqlcipher.database.SQLiteOpenHelper;
//import net.sqlcipher.database.SQLiteDatabase;


import java.util.ArrayList;

import edu.nyit.csci455.geocircuit.Interface.Constants;
import edu.nyit.csci455.geocircuit.normalized.Circuit;
import edu.nyit.csci455.geocircuit.normalized.GeoLocation;

/**
 * <p>GeoCircuitDbHelper.java </p>
 * <p>Database helper class for creating and managing the applications SQLite database.</p>
 *
 * @author jasonscott
 */
public class GeoCircuitDbHelper extends SQLiteOpenHelper {

    private static GeoCircuitDbHelper sInstance;

//    private Context mContext;

    private final static String DATABASE_NAME = "geocircuit.db";
    private final static int DATABASE_VERSION = 1;

    // SQLite create statements
    public static final String SQL_CREATE_LOCATION_ENTRIES =
            " ("
                    + Constants.LOCATION_ID
                    + Constants.INTEGER_TYPE
                    + Constants.PRIMARY_KEY
                    + Constants.COMMA_SEP
                    + Constants.CIRCUIT_ID
                    + Constants.INTEGER_TYPE
                    + Constants.COMMA_SEP
                    + Constants.DATE_TIME
                    + Constants.INTEGER_TYPE
                    + Constants.COMMA_SEP
                    + Constants.SPEED
                    + Constants.REAL_TYPE
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
     * Returns and instance of this.
     *
     * @param context Application context.
     * @return GeoCircuitDbHelper
     */
    public static GeoCircuitDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GeoCircuitDbHelper(context);
        }

        return sInstance;
    }

    /**
     * Private constructor only to be called by getInstance.
     *
     * @param context Application context.
     */
    private GeoCircuitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        mContext = context;
//        getWritableDatabase(Constants.SECRET);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        SQLiteDatabase.loadLibs(mContext);
        db.execSQL(Constants.CREATE_TABLE
                + Constants.LOCATION_TABLE
                + SQL_CREATE_LOCATION_ENTRIES);

        db.execSQL(Constants.CREATE_TABLE
                + Constants.CIRCUITS_TABLE
                + SQL_CREATE_CIRCUIT_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // Insertions

    /**
     * Inserts the specified location into the database.
     *
     * @param geoLocation Specified location.
     */
    public void insertLocation(GeoLocation geoLocation) {
        try {
            insertLocationIntoDb(geoLocation);
        } catch (SQLException e) {
            //TODO (jasonscott) Log any errors.
            System.err.println(e.getMessage());
        }

    }

    /**
     * Inserts the specified location into the location table using insert or throw.
     *
     * @param geoLocation Specified location.
     * @throws SQLiteException For errors inserting into the table.
     */
    private void insertLocationIntoDb(GeoLocation geoLocation) throws SQLiteException {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.LOCATION_ID, geoLocation.getLocationId());
        contentValues.put(Constants.CIRCUIT_ID, geoLocation.getCircuitId());
        contentValues.put(Constants.DATE_TIME, geoLocation.getDate());
        contentValues.put(Constants.SPEED, geoLocation.getSpeed());
        contentValues.put(Constants.LATITUDE, geoLocation.getLatitude());
        contentValues.put(Constants.LONGITUDE, geoLocation.getLongitude());

//      getWritableDatabase(Constants.SECRET)
        getWritableDatabase()
                .insertOrThrow(Constants.LOCATION_TABLE,
                        null,
                        contentValues);
    }

    /**
     * Inserts the specified circuit with the specified name
     * into the database.
     *
     * @param circuit     Specified circuit.
     */
    public void insertCircuit(Circuit circuit) {
        try {
            insertCircuitIntoDb(circuit);
        } catch (SQLException e) {
            //TODO (jasonscott) Log any errors.
            System.err.println(e.getMessage());
        }
    }

    /**
     * Inserts the specified circuit with the specified name
     * into the circuits table using insert or throw.
     *
     * @param circuit     Specified circuit.
     * @throws SQLiteException For errors inserting into the table.
     */
    private void insertCircuitIntoDb(Circuit circuit) throws SQLiteException {

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.CIRCUIT_ID, circuit.getCircuitId());
        contentValues.put(Constants.CIRCUIT_NAME, circuit.getCircuitName());

//      getWritableDatabase(Constants.SECRET)
        getWritableDatabase()
                .insertOrThrow(Constants.CIRCUITS_TABLE,
                        null,
                        contentValues);

    }

    // Retrieval

    /**
     * Returns the location with the specified locationId.
     *
     * @param circuitId Specified Location ID
     * @return Returns Location
     */
    public ArrayList retrieveLocationByCircuitId(int circuitId) {
        ArrayList geoLocations = new ArrayList();

        String[] columns = {Constants.LOCATION_ID,
                Constants.CIRCUIT_ID,
                Constants.DATE_TIME,
                Constants.SPEED,
                Constants.LATITUDE,
                Constants.LONGITUDE};

        String selection = Constants.CIRCUIT_ID + "=?";
        String[] selectionArgs = {String.valueOf(circuitId)};

//        Cursor cursor = getReadableDatabase(Constants.SECRET).query(
        Cursor cursor = getReadableDatabase().query(
                Constants.LOCATION_TABLE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                GeoLocation geoLocation = new GeoLocation();

                geoLocation.setLocationId(cursor.getInt(0));
                geoLocation.setCircuitId(cursor.getInt(1));
                geoLocation.setDate(cursor.getLong(2));
                geoLocation.setSpeed(cursor.getLong(3));
                geoLocation.setLatitude(cursor.getFloat(4));
                geoLocation.setLongitude(cursor.getFloat(5));

                geoLocations.add(geoLocation);

            } while (cursor.moveToNext());
            cursor.close();
        }

        return geoLocations;
    }

    /**
     * Returns a List of Location for all rows in the locations table.
     *
     * @return ArrayList of Location
     */
    public ArrayList retrieveAllLocations() {
        ArrayList geoLocations = new ArrayList();

        Cursor cursor = getReadableDatabase().query(
                Constants.LOCATION_TABLE,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                GeoLocation geoLocation = new GeoLocation();

                geoLocation.setLocationId(cursor.getInt(0));
                geoLocation.setCircuitId(cursor.getInt(1));
                geoLocation.setDate(cursor.getLong(2));
                geoLocation.setSpeed(cursor.getLong(3));
                geoLocation.setLatitude(cursor.getFloat(4));
                geoLocation.setLongitude(cursor.getFloat(5));

                geoLocations.add(geoLocation);

            } while (cursor.moveToNext());
            cursor.close();
        }

        return geoLocations;
    }

    /**
     * Returns a List of Circuit for all rows in the circuits table.
     *
     * @return ArrayList of Circuit.
     */
    public ArrayList retrieveAllCircuits() {
        ArrayList circuits = new ArrayList();

        Cursor cursor = getReadableDatabase().query(
                Constants.CIRCUITS_TABLE,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                Circuit circuit = new Circuit();
                int circuitId = cursor.getInt(0);
                circuit.setCircuitId(circuitId);
                circuit.setCircuitName(cursor.getString(1));
                ArrayList geoLocations = retrieveLocationByCircuitId(circuitId);
                circuit.setGeoLocations(geoLocations);
                circuits.add(circuit);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return circuits;
    }

    // Modification

    // Deletion

    // Count

    /**
     * Returns the number of rows in the locations table.
     *
     * @return int number of rows.
     */
    public int getNumberOfLocations() {
        return (int) DatabaseUtils.queryNumEntries(getReadableDatabase(),
                Constants.LOCATION_TABLE);
    }

    /**
     * Returns the number of rows in the circuits table.
     *
     * @return int number of rows.
     */
    public int getNumberOfCircuits() {
        return (int) DatabaseUtils.queryNumEntries(getReadableDatabase(),
                Constants.CIRCUITS_TABLE);
    }
}
