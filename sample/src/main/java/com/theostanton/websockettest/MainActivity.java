package com.theostanton.websockettest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.theostanton.rxwebsocket.RxWebSocket;
import com.theostanton.rxwebsocket.State;

import org.java_websocket.client.WebSocketClient;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "ws://young-garden-45653.herokuapp.com";

    private static final String TAG = "MainActivity";
    private EditText editText;

    WebSocketClient client;

    private long latest = 0L;
    private String lastMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);

        client = RxWebSocket.getClient(URL);

        RxTextView.textChanges(editText).debounce(400, TimeUnit.MILLISECONDS).subscribe(new Subscriber<CharSequence>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted RxTextView");

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError RxTextView", e);
            }

            @Override
            public void onNext(CharSequence charSequence) {
                Log.d(TAG,"onNext");
                send(charSequence.toString());
            }
        });


        RxWebSocket.connect(URL).subscribe(new Subscriber<State>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError " + e.getMessage());
            }

            @Override
            public void onNext(State state) {
                if(state.isOpen()){
                    System.out.println("Connection opened " + state.getMessage());
                } else {
                    System.out.println("Connection closed " + state.getMessage());
                }
            }
        });

        RxWebSocket.json(URL, MessageObject.class)
                .subscribe(new Subscriber<MessageObject>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted RxWebSocket");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError RxWebSocket", e);
                    }

                    @Override
                    public void onNext(final MessageObject messageObject) {
                        Log.d(TAG, "onNext " + messageObject);

                        set(messageObject);
                    }
                });

    }

    private void set(final MessageObject messageObject) {
        Log.d(TAG,"set " + messageObject);
        if(messageObject.getTimestamp() > latest){
            latest = messageObject.getTimestamp();
            lastMessage = messageObject.getMessage();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    editText.setText(messageObject.getMessage());
                }
            });
        }
    }

    private void send(String message) {
        Log.d(TAG,"send message=" + message);
        if (lastMessage.equals(message)) {
            return;
        }
        MessageObject messageObject = MessageObject.create(message);
        latest = messageObject.getTimestamp();
        if(client.getConnection().isOpen()) {
            Log.d(TAG,"send " + messageObject);
            client.send(messageObject.toJson());
        } else {
            Log.e(TAG,"connection is closed");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.close();
    }
}
