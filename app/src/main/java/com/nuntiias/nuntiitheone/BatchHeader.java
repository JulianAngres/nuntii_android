package com.nuntiias.nuntiitheone;

import com.google.gson.annotations.SerializedName;

public class BatchHeader {

    private String payout_batch_id, batch_status;
    @SerializedName("sender_batch_header")
    private SenderBatchHeader senderBatchHeader;

    public BatchHeader(String payout_batch_id, String batch_status, SenderBatchHeader senderBatchHeader) {
        this.payout_batch_id = payout_batch_id;
        this.batch_status = batch_status;
        this.senderBatchHeader = senderBatchHeader;
    }

    public String getPayout_batch_id() {
        return payout_batch_id;
    }

    public String getBatch_status() {
        return batch_status;
    }

    public SenderBatchHeader getSenderBatchHeader() {
        return senderBatchHeader;
    }
}
