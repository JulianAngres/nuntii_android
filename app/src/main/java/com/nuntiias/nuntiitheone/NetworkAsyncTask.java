package com.nuntiias.nuntiitheone;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class NetworkAsyncTask extends AsyncTask<Void, Void, String> {

    private String sender_batch_id, email_subject, email_message, recipient_type, value, currency, note, sender_item_id, receiver;

    public NetworkAsyncTask(String sender_batch_id, String email_subject, String email_message, String recipient_type, String value, String currency, String note, String sender_item_id, String receiver) {
        super();
        this.sender_batch_id = sender_batch_id;
        this.email_subject = email_subject;
        this.email_message = email_message;
        this.recipient_type = recipient_type;
        this.value = value;
        this.currency = currency;
        this.note = note;
        this.sender_item_id = sender_item_id;
        this.receiver = receiver;
    }

    /*protected <params> String doInBackground(String sender_batch_id, String email_subject, String email_message, String recipient_type, String value, String currency, String note, String sender_item_id, String receiver... params) {
        String result = null;
        //protected String doInBackground(Void... params) {
        /*this.sender_batch_id = sender_batch_id;
        this.email_subject = email_subject;
        this.email_message = email_message;
        this.recipient_type = recipient_type;
        this.value = value;
        this.currency = currency;
        this.note = note;
        this.sender_item_id = sender_item_id;
        this.receiver = receiver;!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        URL url = null;
        try {
            url = new URL("https://api-m.sandbox.paypal.com/v1/oauth2/token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            http.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        http.setDoOutput(true);
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Accept-Language", "en_US");
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        http.setRequestProperty("Authorization", "Basic QVl2RGJqNEI5YUhGSVdxNHYwQ3Yyam5ITkYwY1pwbmEyWDg5V3NqcTFldjZXYWdzU0cydGhXZGgyLTVLRnZSOFVWNnZ5THNQNHhIWDdGTjc6RU0xelpSZlZaSWwtQXVVaTk4U1pYbUtnMXVWdkQ0dHJmbnNfQ2pjUzBOSHl5MlZ6RkM4OFVYX1JKa3Z6WV9ralJ2c3hWRHlEVjU3di0xajM=");

        String data = "grant_type=client_credentials";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = null;
        try {
            stream = http.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            stream.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            try (InputStream in = http.getInputStream()) {

                try (Scanner scanner = new Scanner(in)) {
                    scanner.useDelimiter("\\A");

                    boolean hasInput = scanner.hasNext();
                    if (hasInput) {

                        Gson gson = new Gson();

                        String json = scanner.next();
                        PayPalTokenResult payPalTokenResult = gson.fromJson(json, PayPalTokenResult.class);
                        String token = payPalTokenResult.getAccess_token();


                        Log.d("token", token);

                        URL FinalUrl = new URL("https://api-m.sandbox.paypal.com/v1/payments/payouts");
                        HttpURLConnection httpNew = (HttpURLConnection) FinalUrl.openConnection();
                        httpNew.setRequestMethod("POST");
                        httpNew.setDoOutput(true);
                        httpNew.setRequestProperty("Content-Type", "application/json");
                        httpNew.setRequestProperty("Authorization", "Bearer " + token);

                        Log.d("data", sender_batch_id + " | " + email_subject + " | " + email_message + " | " + recipient_type + " | " + value + " | " + currency + " | " + note + " | " + sender_item_id + " | " + receiver);

                        String dataNew = "{\"sender_batch_header\": {\"sender_batch_id\": " + sender_batch_id + ",\"email_subject\": " + email_subject + ",\"email_message\": " + email_message + " },\"items\": [ {\"recipient_type\": " + recipient_type + ",\"amount\": {\"value\": " + value + ",\"currency\": " + currency + " },\"note\": " + note + ",\"sender_item_id\": " + sender_item_id + ",\"receiver\": " + receiver + " }] }";

                        byte[] outNew = dataNew.getBytes(StandardCharsets.UTF_8);

                        OutputStream streamNew = httpNew.getOutputStream();
                        streamNew.write(outNew);

                        try {
                            InputStream inNew = httpNew.getInputStream();

                            Scanner scannerNew = new Scanner(inNew);
                            scannerNew.useDelimiter("\\A");

                            boolean hasInputNew = scannerNew.hasNext();
                            if (hasInputNew) {

                                String jsonNew = scannerNew.next();
                                Log.d("jsonNew", jsonNew);
                                Gson gson1 = new Gson();
                                PayPalFinalResult payPalFinalResult = gson1.fromJson(jsonNew, PayPalFinalResult.class);

                                Log.d("sender_batch_id", payPalFinalResult.getBatchHeader().getSenderBatchHeader().getSender_batch_id());
                                Log.d("email_subject", payPalFinalResult.getBatchHeader().getSenderBatchHeader().getEmail_subject());
                                Log.d("payout_batch_id", payPalFinalResult.getBatchHeader().getPayout_batch_id());
                                Log.d("batch_status", payPalFinalResult.getBatchHeader().getBatch_status());

                            }
                        } finally {
                            httpNew.disconnect();
                        }


                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            http.disconnect();
        }

        /*try {
            Log.d("Response", http.getResponseCode() + " " + http.getResponseMessage());
            Log.d("Content", http.getContent().toString());
            Log.d("Header Fields", http.getHeaderFields().toString());
            Log.d("Content type", http.getContentType());
        } catch (IOException e) {
            e.printStackTrace();
        }!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


        return result;
    }*/

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;
        //protected String doInBackground(Void... params) {
        /*this.sender_batch_id = sender_batch_id;
        this.email_subject = email_subject;
        this.email_message = email_message;
        this.recipient_type = recipient_type;
        this.value = value;
        this.currency = currency;
        this.note = note;
        this.sender_item_id = sender_item_id;
        this.receiver = receiver;*/

        URL url = null;
        try {
            url = new URL("https://api-m.sandbox.paypal.com/v1/oauth2/token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            http.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        http.setDoOutput(true);
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Accept-Language", "en_US");
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        http.setRequestProperty("Authorization", "Basic QVl2RGJqNEI5YUhGSVdxNHYwQ3Yyam5ITkYwY1pwbmEyWDg5V3NqcTFldjZXYWdzU0cydGhXZGgyLTVLRnZSOFVWNnZ5THNQNHhIWDdGTjc6RU0xelpSZlZaSWwtQXVVaTk4U1pYbUtnMXVWdkQ0dHJmbnNfQ2pjUzBOSHl5MlZ6RkM4OFVYX1JKa3Z6WV9ralJ2c3hWRHlEVjU3di0xajM=");

        String data = "grant_type=client_credentials";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = null;
        try {
            stream = http.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            stream.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            try (InputStream in = http.getInputStream()) {

                try (Scanner scanner = new Scanner(in)) {
                    scanner.useDelimiter("\\A");

                    boolean hasInput = scanner.hasNext();
                    if (hasInput) {

                        Gson gson = new Gson();

                        String json = scanner.next();
                        PayPalTokenResult payPalTokenResult = gson.fromJson(json, PayPalTokenResult.class);
                        String token = payPalTokenResult.getAccess_token();


                        Log.d("token", token);

                        URL FinalUrl = new URL("https://api-m.sandbox.paypal.com/v1/payments/payouts");
                        HttpURLConnection httpNew = (HttpURLConnection) FinalUrl.openConnection();
                        httpNew.setRequestMethod("POST");
                        httpNew.setDoOutput(true);
                        httpNew.setRequestProperty("Content-Type", "application/json");
                        httpNew.setRequestProperty("Authorization", "Bearer " + token);

                        String dataNew = "{\"sender_batch_header\": {\"sender_batch_id\": \"" + sender_batch_id + "\",\"email_subject\": \"" + email_subject + "\",\"email_message\": \"" + email_message + "\" },\"items\": [ {\"recipient_type\": \"" + recipient_type + "\",\"amount\": {\"value\": \"" + value + "\",\"currency\": \"" + currency + "\" },\"note\": \"" + note + "\",\"sender_item_id\": \"" + sender_item_id + "\",\"receiver\": \"" + receiver + "\" }] }";

                        Log.d("dataJson", dataNew);

                        byte[] outNew = dataNew.getBytes(StandardCharsets.UTF_8);

                        OutputStream streamNew = httpNew.getOutputStream();
                        streamNew.write(outNew);

                        try {
                            InputStream inNew = httpNew.getInputStream();

                            Scanner scannerNew = new Scanner(inNew);
                            scannerNew.useDelimiter("\\A");

                            boolean hasInputNew = scannerNew.hasNext();
                            if (hasInputNew) {

                                String jsonNew = scannerNew.next();
                                Log.d("jsonNew", jsonNew);
                                Gson gson1 = new Gson();
                                PayPalFinalResult payPalFinalResult = gson1.fromJson(jsonNew, PayPalFinalResult.class);

                                Log.d("sender_batch_id", payPalFinalResult.getBatchHeader().getSenderBatchHeader().getSender_batch_id());
                                Log.d("email_subject", payPalFinalResult.getBatchHeader().getSenderBatchHeader().getEmail_subject());
                                Log.d("payout_batch_id", payPalFinalResult.getBatchHeader().getPayout_batch_id());
                                Log.d("batch_status", payPalFinalResult.getBatchHeader().getBatch_status());

                            }
                        } finally {
                            httpNew.disconnect();
                        }


                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            http.disconnect();
        }

        /*try {
            Log.d("Response", http.getResponseCode() + " " + http.getResponseMessage());
            Log.d("Content", http.getContent().toString());
            Log.d("Header Fields", http.getHeaderFields().toString());
            Log.d("Content type", http.getContentType());
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        return null;
    }
}
