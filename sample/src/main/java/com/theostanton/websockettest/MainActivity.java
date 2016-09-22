package com.theostanton.websockettest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.theostanton.rxwebsocket.RxWebSocket;

import org.java_websocket.client.WebSocketClient;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "ws://young-garden-45653.herokuapp.com";

    private static final String TAG = "MainActivity";
    private EditText editText;

    WebSocketClient client;

    private String last = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);

        client = RxWebSocket.getClient(URL);

        RxTextView.textChanges(editText).debounce(1000, TimeUnit.MILLISECONDS).subscribe(new Subscriber<CharSequence>() {
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
                String current = editText.getText().toString();
                Log.d(TAG, "onTextChanged last=" + last + " current=" + current + " equals=" + last.equals(current));
                if (last.equals(current)) {
                    return;
                }
                last = current;
                client.send(MessageObject.create(current));
            }
        });

        RxWebSocket.git json(URL, MessageObject.class)
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
                        String current = editText.getText().toString();
                        final String message = messageObject.getMessage();
                        if (message.equals(current)) {
                            Log.d(TAG, "onNext ignore message=" + message);
                            return;
                        } else {
                            Log.d(TAG, "onNext update message=" + message);
                        }

                        last = message;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editText.setText(message);
                            }
                        });

                    }
                });

//        RxWebSocket.messages(URL)
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d(TAG, "onCompleted RxWebSocket");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, "onError RxWebSocket", e);
//                    }
//
//                    @Override
//                    public void onNext(final String message) {
//                        String current = editText.getText().toString();
//                        if (message.equals(current)) {
//                            Log.d(TAG, "onNext ignore message=" + message);
//                            return;
//                        } else {
//                            Log.d(TAG, "onNext update message=" + message);
//                        }
//
//                        last = message;
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                editText.setText(message);
//                            }
//                        });
//
//                    }
//                });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.close();
    }
}
