package com.example.e_carorder.chats.usersRecyclerView;

public class UserModel {

    private String id, name;
    private int imageUser;

    public UserModel(String id, String name, int imageUser) {
        this.id = id;
        this.name = name;
        this.imageUser = imageUser;
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

    public int getImageUser() {
        return imageUser;
    }

    public void setImageUser(int imageUser) {
        this.imageUser = imageUser;
    }

}
