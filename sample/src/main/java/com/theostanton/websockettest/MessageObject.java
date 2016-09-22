package com.theostanton.websockettest;

import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by theostanton on 22/09/2016.
 */

public class MessageObject {

    private String message;
    private long timestamp;

    private MessageObject(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    private MessageObject(String message) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public static String create(String message) {
        return new MessageObject(message).toJson();
    }

    public String getMessage() {
        return message;
    }

    public boolean isFresher(long than) {
        return new Date(than).before(new Date(timestamp));
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return "MessageObject{" +
                "message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
