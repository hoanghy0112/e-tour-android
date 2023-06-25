package com.teamone.e_tour.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ChatRoom implements Serializable {
    public String userId;
    public Staff staffId;
    public String routeId;
    public String _id;
    public Date createdAt;
    public ArrayList<ChatMessage> chats;
}
