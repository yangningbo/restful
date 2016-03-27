package com.hello1987.restful.processor;

public interface ResultCode {

    public static final int NO_ERROR = 200; //
    public static final int AUTH_ERROR = 401; //
    public static final int SERVER_ERROR = 500; //

    public static final int UNKNOWN_ERROR = 1000; //
    public static final int TIMEOUT_ERROR = 1001;//
    public static final int PARSE_ERROR = 1002;//

}
