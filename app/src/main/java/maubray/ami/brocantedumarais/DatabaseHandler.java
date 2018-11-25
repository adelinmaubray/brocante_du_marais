package maubray.ami.brocantedumarais;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "database.db";
    public static final int DATABASE_VERSION = 2;

    // Ce qui concerne les emplacements
    public static final String EMPLACEMENT_NUMBER = "number";
    public static final String EMPLACEMENT_ENTRY = "entry";
    public static final String EMPLACEMENT_SCAN = "scan";
    public static final String EMPLACEMENT_CODE = "code";
    public static final String EMPLACEMENT_REFUS = "refus";

    public static final String TABLE_NAME_EMPLACEMENT = "emplacement";
    public static final String TABLE_CREATE_EMPLACEMENT =
            "CREATE TABLE " + TABLE_NAME_EMPLACEMENT + " (" +
                    EMPLACEMENT_NUMBER + " TEXT PRIMARY KEY, " +
                    EMPLACEMENT_CODE + " TEXT, " +
                    EMPLACEMENT_ENTRY + " TEXT, " +
                    EMPLACEMENT_SCAN + " TEXT, " +
                    EMPLACEMENT_REFUS + " INTEGER);";

    public static final String TABLE_DROP_EMPLACEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME_EMPLACEMENT + ";";


    // Ce qui concerne les entrées
    public static final String ENTRY_ID = "id";
    public static final String ENTRY_LIBELLE = "libelle";
    public static final String ENTRY_VALUE = "value";

    public static final String TABLE_NAME_ENTRY = "entry";
    public static final String TABLE_CREATE_ENTRY =
            "CREATE TABLE " + TABLE_NAME_ENTRY + " (" +
                    ENTRY_ID + " TEXT PRIMARY KEY, " +
                    ENTRY_LIBELLE + " TEXT, " +
                    ENTRY_VALUE + " INTEGER);";

    public static final String TABLE_DROP_ENTRY = "DROP TABLE IF EXISTS " + TABLE_NAME_ENTRY + ";";


    public DatabaseHandler(Context context) {
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
