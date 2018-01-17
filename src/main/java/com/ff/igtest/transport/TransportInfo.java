package com.ff.igtest.transport;

public class TransportInfo {
    private String username;
    private String password;
    private String url;
    private String name;
    private boolean queue;
    private String clientId;
    private boolean producer;

    public TransportInfo(String username, String password, String url, String name, boolean queue, String clientId, boolean producer){
        this.username = username;
        this.password = password;
        this.url = url;
        this.name = name;
        this.queue = queue;
        this.clientId = clientId;
        this.producer = producer;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public boolean isQueue() {
        return queue;
    }

    public String getClientId() {
        return clientId;
    }

    public boolean isProducer() {
        return producer;
    }
}
