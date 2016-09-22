package com.theostanton.rxwebsocket;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by theostanton on 22/09/2016.
 */

abstract class RxWebSocketOnSubscribe<T> implements Observable.OnSubscribe<T> {

    private static final boolean log = true;
    protected final RxWebSocketClient client;

    public RxWebSocketOnSubscribe(RxWebSocketClient client) {
        this.client = client;
    }

    abstract void onCall(final Subscriber<? super T> subscriber);

    abstract void onUnSubscribe();

    @Override
    public void call(Subscriber<? super T> subscriber) {
        onCall(subscriber);
        subscriber.add(new Subscription() {
            @Override
            public void unsubscribe() {
                log("unsubscribe");
                onUnSubscribe();
            }

            @Override
            public boolean isUnsubscribed() {
                return false;
            }
        });
        client.open();
    }

    protected void log(String message, Object... args) {
        if (log) System.out.println (String.format(message, args));
    }

    @Override
    public String toString() {
        return "RxWebSocketOnSubscribe{" +
                "client=" + client +
                '}';
    }
}
