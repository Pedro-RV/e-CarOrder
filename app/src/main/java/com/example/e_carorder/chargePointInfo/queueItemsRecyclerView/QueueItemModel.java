package com.example.e_carorder.chargePointInfo.queueItemsRecyclerView;

public class QueueItemModel {

    private String queueItemUserId;

    public QueueItemModel(String queueItemUserId) {
        this.queueItemUserId = queueItemUserId;
    }

    public String getQueueItemUserId() {
        return queueItemUserId;
    }

    public void setQueueItemUserId(String queueItemUserId) {
        this.queueItemUserId = queueItemUserId;
    }
}
