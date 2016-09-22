package com.theostanton.rxwebsocket;

import org.java_websocket.handshake.ServerHandshake;

import rx.Subscriber;

/**
 * Created by theostanton on 22/09/2016.
 */

public class WebSocketStateOnSubscribe extends RxWebSocketOnSubscribe<State> {

    public WebSocketStateOnSubscribe(RxWebSocketClient client) {
        super(client);
    }

    @Override
    void onCall(final Subscriber<? super State> subscriber) {
        client.setStateListener(new RxWebSocketClient.StateListener() {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                log("onOpen %s",this);
                if (subscriber.isUnsubscribed()) return;
                subscriber.onNext(new State(
                        true,
                        handshakedata.getHttpStatusMessage(),
                        handshakedata.getHttpStatus()
                ));
            }



            @Override
            public void onClose(int code, String reason, boolean remote) {
                log("onClose %s",this);
                if (subscriber.isUnsubscribed()) return;
                subscriber.onNext(new State(
                        false,
                        reason,
                        code
                ));
            }

            @Override
            public void onError(Exception exception) {
                log("onError %s",this);
                subscriber.onError(exception);
            }
        });
    }

    public void onUnSubscribe(){
        log("onUnSubscribe %s",this);
        RxWebSocket.disconnect(client.getURI().toString());
        client.removeStateListener();
    }

    @Override
    public String toString() {
        return "WebSocketStateOnSubscribe{} " + super.toString();
    }
}
