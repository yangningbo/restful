package com.hello1987.restful.processor;

import java.util.Map;

public interface Processor {

    void process(Map<String, Object> params, ProcessorCallback callback);

}
