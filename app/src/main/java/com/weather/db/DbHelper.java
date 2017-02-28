package com.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.Locale;

public class DbHelper extends SQLiteOpenHelper implements IDbOperations {

    private static final String SQL_TABLE_CREATE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s);";

    private static final String SQL_NAME = "weatherForecast.db";

    public DbHelper(final Context context) {
        super(context, SQL_NAME, null, 1);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {

        String str = WeatherTable.DATE + " INTEGER, "
                + WeatherTable.TEMP + " REAL, "
                + WeatherTable.HUMIDITY + " INTEGER, "
                + WeatherTable.DESCRIPTION + " TEXT, "
                + WeatherTable.ICON + " TEXT, "
                + WeatherTable.SPEED + " INTEGER, "
                + WeatherTable.CLOUDS + " INTEGER";
        String sql = String.format(Locale.US, SQL_TABLE_CREATE_TEMPLATE, WeatherTable.WeatherTable, str);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }

    @Override
    public Cursor query(final String sql, final String... args) {

        final SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, args);
    }

    @Override
    public int delete(final String name, final String sql, final String... args) {

        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            int count = 0;

            try {
                database.beginTransaction();

                count = database.delete(name, sql, args);

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }

            return count;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public long insert(final String name, final ContentValues values) {

        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            long id;

            try {
                database.beginTransaction();

                id = database.insert(name, null, values);

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }

            return id;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public int bulkInsert(final String name, final List<ContentValues> values) {

        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            int count = 0;
            try {
                database.beginTransaction();

                for (final ContentValues value : values) {
                    database.insert(name, null, value);
                    count++;
                }
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }

            return count;
        } else {
            throw new RuntimeException();
        }
    }

}

