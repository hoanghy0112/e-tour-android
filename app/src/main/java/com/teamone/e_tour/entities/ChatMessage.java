package com.teamone.e_tour.entities;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {
    public String uid;
    public String content;
    public String _id;
    public Date createdAt;
}
