package com.hello1987.restful.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.hello1987.restful.util.LogUtil;

import static android.provider.BaseColumns._ID;
import static com.hello1987.restful.provider.MessageContract.AUTHORITY;


public class MessageProvider extends ContentProvider {

    private static final int MESSAGES = 1;
    private static final int MESSAGE_ID = 2;

    private static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hello1987.message";

    private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.hello1987.message";

    private ProviderDbHelper dbHelper;
    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "messages", MESSAGES);
        uriMatcher.addURI(AUTHORITY, "messages/#", MESSAGE_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new ProviderDbHelper(this.getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MESSAGES:
                return CONTENT_TYPE;
            case MESSAGE_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String orderBy) {
        if (uriMatcher.match(uri) == MESSAGE_ID) {
            long id = Long.parseLong(uri.getPathSegments().get(1));
            selection = appendRowId(selection, id);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(MessageContract.MessageTable.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (uriMatcher.match(uri) != MESSAGES) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        long id = db.insertOrThrow(MessageContract.MessageTable.TABLE_NAME, null, values);

        if (id > 0) {
            Uri newUri = ContentUris.withAppendedId(MessageContract.MessageTable.CONTENT_URI, id);

            LogUtil.i("New profile URI: " + newUri.toString());
            getContext().getContentResolver().notifyChange(newUri, null);

            return newUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (uriMatcher.match(uri) != MESSAGE_ID) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String recordId = Long.toString(ContentUris.parseId(uri));
        int affected = db.update(MessageContract.MessageTable.TABLE_NAME, values, BaseColumns._ID
                + "="
                + recordId
                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
                : ""), selectionArgs);

        LogUtil.i("Updated message URI: " + uri.toString());

        if (affected > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affected;
    }

    private String appendRowId(String selection, long id) {
        return _ID
                + "="
                + id
                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
                : "");
    }

}
