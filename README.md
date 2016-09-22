RxWebSocket
===========

Incomplete / Work in progress. 

Download
--------

Gradle:
```groovy
compile 'com.theostanton.rxwebsocket:rxwebsocket:0.1'
```



Summary
-------

An RX wrapper for java-websocket library. 

Currently holds instances accessible by URL.

Opens connection on any subscription, if not already open.



Snippets
--------

### Get WebSocketClient 

```java 
WebSocketClient client = RxWebSocket.getClient(URL);
```


### Close connection

```java

RxWebSocket.disconnect(URL);

// or

stateSubscription.unsubscribe(); 

```

### State subscription

```java
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
```

### Message subscription

```java
RxWebSocket.messages(URL).subscribe(new Subscriber<String>() {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        System.out.println("onError " + e.getMessage());
    }

    @Override
    public void onNext(String message) {
        System.out.println("Message received : " + message);
    }
});
```

### Json subscription

Converts JSON string to POJO

```java
RxWebSocket.json(URL, MessageObject.class)
    .subscribe(new Subscriber<MessageObject>() {
        @Override
        public void onCompleted() {
    
        }
    
        @Override
        public void onError(Throwable e) {
            System.out.println("onError " + e.getMessage());
        }
    
        @Override
        public void onNext(MessageObject messageObject) {
            System.out.println("Message received : " + messageObject.getMessage());
        }
});
```
