package com.hello1987.restful.provider;

import android.net.Uri;

public class MessageContract {


    public static final String AUTHORITY = "com.hello1987.restful.provider.messageprovider";

    public static final class MessageTable implements ResourceTable {

        public static final String TABLE_NAME = "message";

        static final String SCHEME = "content://";
        public static final String URI_PREFIX = SCHEME + AUTHORITY;
        private static final String URI_PATH_MESSAGE = "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.parse(URI_PREFIX + URI_PATH_MESSAGE);

        public static final String CREATED = "created";

        private MessageTable() {
        }
    }

    private MessageContract() {
    }

}
