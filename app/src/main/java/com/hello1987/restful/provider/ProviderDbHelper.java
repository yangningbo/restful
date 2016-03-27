package com.hello1987.restful.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hello1987.restful.provider.MessageContract.MessageTable;
import com.hello1987.restful.util.LogUtil;

public class ProviderDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restful.db";
    private static final int DATABASE_VERSION = 1;

    public ProviderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // CREATE MESSAGE TABLE
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE " + MessageTable.TABLE_NAME + " (");
        sqlBuilder.append(MessageTable._ID + " INTEGER, ");
        sqlBuilder.append(MessageTable._STATUS + " TEXT, ");
        sqlBuilder.append(MessageTable.CREATED + " INTEGER, ");
        sqlBuilder.append(");");
        String sql = sqlBuilder.toString();
        LogUtil.i("Creating DB table with string: '" + sql + "'");
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
