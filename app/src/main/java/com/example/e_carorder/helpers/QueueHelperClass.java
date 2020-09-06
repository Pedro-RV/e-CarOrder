package com.example.e_carorder.helpers;

public class QueueHelperClass {

    private String queueUserId;

    public QueueHelperClass() {
    }

    public QueueHelperClass(String queueUserId) {
        this.queueUserId = queueUserId;
    }

    public String getQueueUserId() {
        return queueUserId;
    }

    public void setQueueUserId(String queueUserId) {
        this.queueUserId = queueUserId;
    }
}
