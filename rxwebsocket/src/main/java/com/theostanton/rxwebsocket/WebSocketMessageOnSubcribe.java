package com.theostanton.rxwebsocket;


import rx.Subscriber;

/**
 * Created by theostanton on 22/09/2016.
 */

public class WebSocketMessageOnSubcribe extends RxWebSocketOnSubscribe<String> {

    public WebSocketMessageOnSubcribe(RxWebSocketClient client) {
        super(client);
    }

    @Override
    void onCall(final Subscriber<? super String> subscriber) {
        client.setMessageListener(new RxWebSocketClient.MessageListener() {
            @Override
            public void onMessage(String message) {
                subscriber.onNext(message);
            }
        });
    }

    @Override
    void onUnSubscribe() {
        log("onUnSubscribe %s",this);
        client.removeMessageListener();
    }

}
