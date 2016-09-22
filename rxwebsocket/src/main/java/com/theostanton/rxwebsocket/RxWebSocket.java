package com.theostanton.rxwebsocket;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by theostanton on 22/09/2016.
 */

public class RxWebSocket {

    private RxWebSocketClient client;
    private static HashMap<String,RxWebSocket> instances = new HashMap<>();

    private RxWebSocket(String url){
        client = new RxWebSocketClient(url);
    }


    public RxWebSocketClient getClient(){
        return client;
    }

    public static Observable<State> connect(String url){
        RxWebSocket websocket = get(url);
        return Observable.create(new WebSocketStateOnSubscribe(websocket.getClient()));
    }

    public static void disconnect(String url){
        if(instances.containsKey(url)){
            log("disconnect() exists=true url=%s",url);
            instances.get(url).getClient().close();
            instances.remove(url);
        } else {
            log("disconnect() exists=false url=%s",url);
        }
    }

    private static RxWebSocket get(String url){
        if(instances.containsKey(url)){
            log("get() exists=true url=%s",url);
            return instances.get(url);
        } else {
            log("get() exists=false url=%s",url);
            instances.put(url,new RxWebSocket(url));
            return instances.get(url);
        }
    }

    @Nullable
    public static RxWebSocketClient getClient(String url){
        return get(url).getClient();
    }

    public static Observable<String> messages(String url) {
        log("messages() url=%s",url);
        return Observable.create(new WebSocketMessageOnSubcribe(get(url).getClient()));

    }

    public static <T> Observable<T> json(String url, Class<T> clazz) {
        log("messages() url=%s",url);
        return Observable.create(new WebSocketJsonOnSubscribe<T>(get(url).getClient(),clazz));

    }

    private static void log(String message, Object... args){
        System.out.println(String.format(message,args));
        Log.d("RxWebSocket",String.format(message,args));
    }

}
