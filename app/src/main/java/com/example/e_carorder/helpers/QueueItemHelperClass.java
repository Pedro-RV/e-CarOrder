package com.example.e_carorder.helpers;

public class QueueItemHelperClass {

    private String queueItemUserId;

    public QueueItemHelperClass() {
    }

    public QueueItemHelperClass(String queueUserId) {
        this.queueItemUserId = queueUserId;
    }

    public String getQueueUserId() {
        return queueItemUserId;
    }

    public void setQueueUserId(String queueUserId) {
        this.queueItemUserId = queueUserId;
    }
}
