package com.theostanton.rxwebsocket;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import rx.Subscriber;

/**
 * Created by theostanton on 22/09/2016.
 */

public class WebSocketJsonOnSubscribe<T> extends RxWebSocketOnSubscribe<T> {

    protected static final String TAG = "WebSocketJsonOnSubscribe";

    private Gson gson = new Gson();
    private Type type;
    private Class clazz;

    public WebSocketJsonOnSubscribe(RxWebSocketClient client, Class clazz) {
        super(client);
        this.clazz = clazz;
//        type = new TypeToken<T>(){}.getType();
//        log("type=%s",type);
    }

    @Override
    void onCall(final Subscriber<? super T> subscriber) {
        client.setJsonListener(new RxWebSocketClient.MessageListener() {
            @Override
            public void onMessage(String message) {
                log("onMessage message=%s",message);
//                if(subscriber.isUnsubscribed())return;
                T object = (T) gson.fromJson(message,clazz);
                log("onMessage object=%s",object);
                subscriber.onNext(object);
            }
        });
    }

    @Override
    void onUnSubscribe() {
        client.removeJsonListener();
    }
}
