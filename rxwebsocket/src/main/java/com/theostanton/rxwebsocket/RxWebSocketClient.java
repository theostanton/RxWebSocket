package com.theostanton.rxwebsocket;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;


/**
 * Created by theostanton on 22/09/2016.
 */

class RxWebSocketClient extends WebSocketClient {

    private static final String TAG = "RxWebSocketClient";

    private MessageListener messageListener;
    private MessageListener jsonListener;
    private StateListener stateListener;
    private final String url;

    public RxWebSocketClient(String url) {
        super(URI.create(url));
        this.url = url;
    }

    public void open() {
        log("open()");
        try {
            connect();
        } catch (IllegalStateException e) {
            if (e.getMessage().equals("WebSocketClient objects are not reuseable")) {
                System.out.println("already open");
            } else {
                throw e;
            }
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log("onOpen() %s", handshakedata.getHttpStatusMessage());
        if (stateListener != null) {
            stateListener.onOpen(handshakedata);
        }
    }

    @Override
    public void onMessage(String message) {
        log("onMessage() %s", message);
        if (messageListener != null) {
            messageListener.onMessage(message);
        }
        if (jsonListener != null) {
            jsonListener.onMessage(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log("onClose()");
        if (stateListener != null) {
            stateListener.onClose(code, reason, remote);
        }
    }

    @Override
    public void onError(Exception exception) {
        if (stateListener != null) {
            stateListener.onError(exception);
        }
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void removeMessageListener() {
        this.messageListener = null;
    }

    public void setJsonListener(MessageListener jsonListener) {
        this.jsonListener = jsonListener;
    }

    public void removeJsonListener() {
        this.jsonListener = null;
    }


    public void setStateListener(StateListener stateListener) {
        this.stateListener = stateListener;
    }

    public void removeStateListener() {
        this.stateListener = null;
    }

    interface StateListener {
        void onOpen(ServerHandshake handshakedata);

        void onClose(int code, String reason, boolean remote);

        void onError(Exception exception);
    }

    interface MessageListener {
        void onMessage(String message);
    }

    private void log(String message, Object... args) {
        Log.d(TAG, String.format(message, args));
    }

    @Override
    public String toString() {
        return "RxWebSocketClient{" +
                "url=" + url +
                "messageListener=" + messageListener +
                ", stateListener=" + stateListener +
                "} " + super.toString();
    }
}
