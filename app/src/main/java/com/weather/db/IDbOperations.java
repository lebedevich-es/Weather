package com.weather.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

public interface IDbOperations {

    Cursor query(String sql, String... args);

    int delete(final String name, String sql, String... args);

    long insert(final String name, ContentValues values);

    int bulkInsert(final String name, List<ContentValues> values);
}
