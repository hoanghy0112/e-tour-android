package com.teamone.e_tour.entities;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {
    public String uid;
    public String content;
    public String _id;
    public Date createdAt;

    public ChatMessage(String uid, String content, Date createdAt) {
        this.uid = uid;
        this.content = content;
        this.createdAt = createdAt;
    }
}
