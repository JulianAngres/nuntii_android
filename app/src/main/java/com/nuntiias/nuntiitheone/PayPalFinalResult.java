package com.nuntiias.nuntiitheone;

import com.google.gson.annotations.SerializedName;

public class PayPalFinalResult {

    @SerializedName("batch_header")
    private BatchHeader batchHeader;

    public PayPalFinalResult(BatchHeader batchHeader) {

        this.batchHeader = batchHeader;
    }

    public BatchHeader getBatchHeader() {
        return batchHeader;
    }
}
