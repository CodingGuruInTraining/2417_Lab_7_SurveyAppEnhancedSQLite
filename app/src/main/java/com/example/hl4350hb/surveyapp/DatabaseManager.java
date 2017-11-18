package com.example.hl4350hb.surveyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */

public class DatabaseManager {

    private Context context;
    private SQLHelper helper;
    private SQLiteDatabase db;

    // Static strings of database info.
    protected static final String DB_NAME = "surveys.db";

    protected static final int DB_VERSION = 1;
    protected static final String DB_TABLE = "results";

    private static final String ID_COL = "_id";
    protected static final String QUESTION_COL = "question";
    protected static final String OPT1_COL = "option1";
    protected static final String OPT2_COL = "option2";
    protected static final String OPT1_COUNT_COL = "opt1_count";
    protected static final String OPT2_COUNT_COL = "opt2_count";


    // Constructor
    public DatabaseManager(Context c) {
        this.context = c;
        helper = new SQLHelper(c);
        this.db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    // Select all query.
    public Cursor getCursorAll() {
        Cursor cursor = db.query(DB_TABLE, null, null, null, null, null, QUESTION_COL);
        return cursor;
    }

    // Add new survey after removing previous one.
    public boolean addSurvey(String qu, String opt1, String opt2, int count1, int count2) {
        ContentValues values = new ContentValues();
        values.put(QUESTION_COL, qu);
        values.put(OPT1_COL, opt1);
        values.put(OPT2_COL, opt2);
        values.put(OPT1_COUNT_COL, count1);
        values.put(OPT2_COUNT_COL, count2);

        try {
            db.delete(DB_TABLE, null, null);
            db.insertOrThrow(DB_TABLE, null, values);
            return true;
        } catch (SQLiteConstraintException err) {
            return false;
        }
    }

    // Update query for increasing counts.
    public void updateSurvey(String question, int yes, int no) {
        ContentValues updateCounts = new ContentValues();
        updateCounts.put(OPT1_COUNT_COL, yes);
        updateCounts.put(OPT2_COUNT_COL, no);
        String[] whereArgs = {question};
        String where = QUESTION_COL + " = ?";

        int rowsChanged = db.update(DB_TABLE, updateCounts, where, whereArgs);
    }



    // Class for handling database setup.
    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper(Context c) {
            super(c, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTable = "CREATE TABLE " + DB_TABLE + " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUESTION_COL + " TEXT, " + OPT1_COL + " TEXT, " + OPT2_COL + " TEXT, " + OPT1_COUNT_COL +
                    " INTEGER, " + OPT2_COUNT_COL + " INTEGER);";

            db.execSQL(createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }
}
