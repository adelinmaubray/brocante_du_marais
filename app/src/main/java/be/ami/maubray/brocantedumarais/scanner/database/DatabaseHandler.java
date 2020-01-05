package be.ami.maubray.brocantedumarais.scanner.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {


    // Emplacement
    static final String EMPLACEMENT_NUMBER = "number";
    static final String EMPLACEMENT_ENTRY = "entry";
    static final String EMPLACEMENT_SCAN = "scan";
    static final String EMPLACEMENT_CODE = "code";
    static final String EMPLACEMENT_REFUSAL = "refusal";
    // Entry
    static final String ENTRY_ID = "id";
    static final String ENTRY_DESCRIPTION = "description";
    static final String ENTRY_VALUE = "value";

    public static final String TABLE_NAME_EMPLACEMENT = "emplacement";
    // General information about the database
    private static final String DATABASE_NAME = "database.database";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_CREATE_EMPLACEMENT =
            "CREATE TABLE " + TABLE_NAME_EMPLACEMENT + " (" +
                    EMPLACEMENT_NUMBER + " TEXT PRIMARY KEY, " +
                    EMPLACEMENT_CODE + " TEXT, " +
                    EMPLACEMENT_ENTRY + " TEXT, " +
                    EMPLACEMENT_SCAN + " TEXT, " +
                    EMPLACEMENT_REFUSAL + " INTEGER);";
    private static final String TABLE_DROP_EMPLACEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME_EMPLACEMENT + ";";
    private static final String TABLE_CREATE_ENTRY =
            "CREATE TABLE " + TABLE_NAME_ENTRY + " (" +
                    ENTRY_ID + " TEXT PRIMARY KEY, " +
                    ENTRY_DESCRIPTION + " TEXT, " +
                    ENTRY_VALUE + " INTEGER);";

    public static final String TABLE_NAME_ENTRY = "entry";
    private static final String TABLE_DROP_ENTRY = "DROP TABLE IF EXISTS " + TABLE_NAME_ENTRY + ";";

    DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE_EMPLACEMENT);
        sqLiteDatabase.execSQL(TABLE_CREATE_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(TABLE_DROP_EMPLACEMENT);
        sqLiteDatabase.execSQL(TABLE_DROP_ENTRY);
        onCreate(sqLiteDatabase);
    }
}
