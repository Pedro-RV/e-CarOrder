package com.example.e_carorder.helpers;

import java.io.Serializable;

public class QueueItemHelperClass implements Serializable {

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
