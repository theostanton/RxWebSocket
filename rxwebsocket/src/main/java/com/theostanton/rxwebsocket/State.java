package com.theostanton.rxwebsocket;

public class State {
    private boolean open;
    private String message;
    private int code;

    public State(boolean open, String message, int code) {
        this.open = open;
        this.message = message;
        this.code = code;
    }

    public boolean isOpen() {
        return open;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "State{" +
                "open=" + open +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}