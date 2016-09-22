RxWebSocket
===========

Incomplete / Work in progress. 

Currently holds instances accessable by URL.

Opens connection on any subscription, if not already open.

Can access WebSocketClient via 

```java 
WebSocketClient client = RxWebSocket.getClient(URL);
```

Snippets
--------

### Close connection

```java

RxWebSocket.disconnect(URL);

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
