package com.nuntiias.nuntiitheone;

public class SenderBatchHeader {

    private String sender_batch_id;
    private String email_subject;

    public SenderBatchHeader(String sender_batch_id, String email_subject) {
        this.sender_batch_id = sender_batch_id;
        this.email_subject = email_subject;
    }

    public String getSender_batch_id() {
        return sender_batch_id;
    }

    public String getEmail_subject() {
        return email_subject;
    }
}
