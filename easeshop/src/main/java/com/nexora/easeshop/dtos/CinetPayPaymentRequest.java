package com.nexora.easeshop.dtos;

public class CinetPayPaymentRequest {
    public String apikey;
    public String site_id;
    public String transaction_id;
    public int amount;
    public String currency;
    public String description;
    public String notify_url;
    public String return_url;
    public String channels;
    public String lang = "en";
}

