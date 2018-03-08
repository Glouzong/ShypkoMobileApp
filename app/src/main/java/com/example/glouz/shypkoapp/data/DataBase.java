package com.example.glouz.shypkoapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.glouz.shypkoapp.launcher.ItemLauncher;

import java.util.ArrayList;
import java.util.HashMap;


public class DataBase {
    private DBHelper connection;

    interface TableInfo {
        String TABLE_NAME = "table_frequency";

        interface Columns extends BaseColumns {
            String FIELD_FREQUENCY = "frequency";
            String FIELD_PACKAGE_NAME = "package_name";
        }

        String CREATE_TABLE_SCRIPT =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        "(" +
                        Columns.FIELD_FREQUENCY + " NUMBER, " +
                        Columns.FIELD_PACKAGE_NAME + " TEXT" +
                        ")";

        String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    private static class DBHelper extends SQLiteOpenHelper {
        static final int VERSION = 1;
        static final String DB_NAME = "launcher.db";

        DBHelper(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TableInfo.CREATE_TABLE_SCRIPT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(TableInfo.DROP_TABLE_SCRIPT);
            onCreate(db);
        }
    }

    public DataBase(Context context) {
        connection = new DBHelper(context);
    }

    public HashMap<String, Integer> getFrequencies() {
        HashMap<String, Integer> packageFrequencies = new HashMap<>();

        SQLiteDatabase db = connection.getReadableDatabase();
        Cursor cursor = db.query(TableInfo.TABLE_NAME,
                new String[]{TableInfo.Columns.FIELD_PACKAGE_NAME, TableInfo.Columns.FIELD_FREQUENCY},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            String packageName = cursor.getString(cursor.getColumnIndex(TableInfo.Columns.FIELD_PACKAGE_NAME));
            Integer frequency = cursor.getInt(cursor.getColumnIndex(TableInfo.Columns.FIELD_FREQUENCY));
            packageFrequencies.put(packageName, frequency);
        }
        cursor.close();
        db.close();
        return packageFrequencies;
    }

    public void replace(ArrayList<ItemLauncher> data) {
        SQLiteDatabase db = connection.getWritableDatabase();
        db.delete(TableInfo.TABLE_NAME, null, null);
        for (int i = 0; i < data.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableInfo.Columns.FIELD_PACKAGE_NAME, data.get(i).getPackageName());
            contentValues.put(TableInfo.Columns.FIELD_FREQUENCY, data.get(i).getFrequency());
            db.insert(TableInfo.TABLE_NAME, null, contentValues);
        }
        db.close();
    }
}
