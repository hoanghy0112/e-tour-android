package com.teamone.e_tour.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Company implements Serializable {
    public String _id;
    public String name;
    public String description;
    public String email;
    public String image;
    public boolean isFollowing;
    public ArrayList<String> previewImages;
}
