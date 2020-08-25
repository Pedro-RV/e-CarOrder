package com.example.e_carorder.chats.messagesRecyclerView;

public class ChatModel {

    private String sender;
    private String receiver;
    private String message;
    private Boolean status;

    public ChatModel(){

    }

    public ChatModel(String sender, String receiver, String message, Boolean status) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
