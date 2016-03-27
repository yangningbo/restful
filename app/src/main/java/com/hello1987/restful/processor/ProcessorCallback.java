package com.hello1987.restful.processor;

import com.hello1987.restful.rest.resource.Resource;

public interface ProcessorCallback {

    void send(int resultCode, Resource resource);

}
