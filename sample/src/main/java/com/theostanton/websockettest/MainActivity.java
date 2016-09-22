package com.theostanton.websockettest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import org.java_websocket.client.WebSocketClient;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText editText;

    WebSocketClient client;

    private String last = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);


        RxTextView.textChanges(editText).debounce(1000, TimeUnit.MILLISECONDS).subscribe(new Subscriber<CharSequence>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CharSequence charSequence) {
                String current = editText.getText().toString();
                Log.d(TAG, "onTextChanged last=" + last + " current=" + current + " equals=" + last.equals(current));
                if(last.equals(current)){
                    return;
                }
                last = current;
                client.send(last);
            }
        });

//        client = new WebSocketClient(URI.create("ws://young-garden-45653.herokuapp.com")) {
//
//            private static final String TAG = "WebSocketClient";
//
//            @Override
//            public void onOpen(ServerHandshake handshakedata) {
//                Log.d(TAG, "onOpen");
//                send("lols");
//            }
//
//            @Override
//            public void onMessage(final String message) {
//                Log.d(TAG, "onMessage = " + message);
//                if(message.equals(editText.getText().toString())){
//                    return;
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        last = message;
//                        editText.setText(message);
//                    }
//                });
//
//            }
//
//            @Override
//            public void onClose(int code, String reason, boolean remote) {
//                Log.d(TAG, "onClose because " + reason);
//
//            }
//
//            @Override
//            public void onError(Exception ex) {
//                Log.e(TAG, "onError", ex);
//
//            }
//        };
//        client.connect();

//        WebsocketClientMessageSubscribe().
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
