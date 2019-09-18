package com.faziz.exercise.tradeledger.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "event")
public class Event implements BaseEntity {

    public Event() {
    }

    public Event(String id, EventType type, 
            long time, String user, String ip) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.user = user;
        this.ip = ip;
    }

    @Id
    private String id;
    private EventType type;
    private long time;
    private String user;
    private String ip;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Event{id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", time=").append(time);
        sb.append(", user=").append(user);
        sb.append(", ip=").append(ip);
        sb.append('}');
        return sb.toString();
    }
}
