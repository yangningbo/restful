package com.hello1987.restful.rest.resource;

/**
 * Created by yangningbo on 2016/3/27.
 */
public class Message implements Resource {

    private long created;

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
