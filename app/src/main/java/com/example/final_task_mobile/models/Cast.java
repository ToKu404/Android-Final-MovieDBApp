package com.example.final_task_mobile.models;

public class Cast {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    String name;
    String profile_path;

    public String getCharacter() {
        return "("+character+")";
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    String character;
}
