package edu.nyit.csci455.geocircuit.Interface;

/**
 * Created by Matt on 10/26/2014.
 */
public interface Constants {
    public static final int TEXT_SIZE_SMALL = 15;
    public static final int TEXT_SIZE_LARGE = 80;
    public static final int INDEX_KM = 0;
    public static final int INDEX_MILES = 1;
    public static final int DEFAULT_SPEED_LIMIT_MPH = 55;
    public static final int DEFAULT_SPEED_LIMIT_KM = 86;
    public static final int HOUR_MULTIPLIER = 3600;   // 1 sec=1/3600 hr
    public static final double UNIT_MULTIPLIERS[] = {0.001, 0.000621371192};

    public static final String[] FEATURES = {"Dashboard", "Circuit Manager", "Near Me" };
    public static final int DASHBOARD = 0;
    public static final int CIRCUIT_MANAGER = 1;
    public static final int NEAR_ME = 2;

    public static final String STATUS_BAR_HEIGHT = "status_bar_height";
    public static final String DIMEN = "dimen";
    public static final String ANDROID = "android";

    // SQLite Database

    // Table names
    public static final String LOCATION_TABLE = "locations";
    public static final String CIRCUITS_TABLE = "circuits";

    // Column names
    public static final String LOCATION_ID = "location_id";
    public static final String DATE_TIME = "date_time";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    public static final String CIRCUIT_ID = "circuit_id";
    public static final String CIRCUIT_NAME = "circuit_name";
    public static final String START_LOCATION = "start_location";
    public static final String END_LOCATION = "end_location";

    // SQLite types
    public static final String INTEGER_TYPE = "INTEGER";
    public static final String REAL_TYPE = "REAL";
    public static final String TEXT_TYPE = "TEXT";
    public static final String COMMA_SEP = ",";
    public static final String PRIMARY_KEY = "PRIMARY KEY";

    public static final String CREATE_TABLE = "CREATE TABLE ";
    public static final String SECRET = "secret";
}
