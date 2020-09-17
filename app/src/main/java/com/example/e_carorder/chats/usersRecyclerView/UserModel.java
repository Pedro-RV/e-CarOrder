package com.example.e_carorder.chats.usersRecyclerView;

public class UserModel {

    private String id, name;
    private Boolean newMessage;

    public UserModel(String id, String name, Boolean newMessage) {
        this.id = id;
        this.name = name;
        this.newMessage = newMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(Boolean newMessage) {
        this.newMessage = newMessage;
    }
}
