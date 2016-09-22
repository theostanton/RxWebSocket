package com.theostanton.websockettest;

/**
 * Created by theostanton on 22/09/2016.
 */

import com.theostanton.rxwebsocket.RxWebSocket;
import com.theostanton.rxwebsocket.State;

import rx.Subscriber;
import rx.Subscription;

public class Main {

    private static String url = "ws://young-garden-45653.herokuapp.com";


    public static void main(String... args) {
        System.out.println("main()");
        Subscription subscription = RxWebSocket.connect(url).subscribe(new Subscriber<State>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
                System.out.println(e);

            }

            @Override
            public void onNext(State state) {
                if (state.isOpen()) {
                    System.out.println("onOpen");
                } else {
                    System.out.println("onClose");
                }
                System.out.println(state);
            }
        });
        RxWebSocket.messages(url).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
                System.out.println(e.getMessage());

            }

            @Override
            public void onNext(String message) {
                System.out.println("onNext");
                System.out.println(message);
            }
        });
        try {
            Thread.sleep(5000);
            subscription.unsubscribe();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
