package com.googleinterns.smb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.PropertyName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Order {

    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("date_of_order")
    private Date dateOfOrder;
    @JsonProperty("merchant_id")
    private String merchantId;
    @JsonProperty("merchant_name")
    private String merchantName;
    @JsonProperty("merchant_address")
    private String merchantAddress;
    @JsonProperty("offers")
    private String offersAvailed;

    private enum OrderStatus {
        PROCESSING, COMPLETED, ONGOING
    }

    @JsonProperty("order_status")
    private OrderStatus orderStatus;

    private static final Logger LOGGER = Logger.getLogger(Order.class.getName());

    public Order() {

    }

    public Order(String userId, String orderId) {
        this.userId = userId;
        this.orderId = orderId;
        this.orderStatus = OrderStatus.ONGOING;
        this.dateOfOrder = getDate();
    }

    public Order(String userId, String orderId, String merchantId, String merchantName, String merchantAddress,
            String offers) {
        this.userId = userId;
        this.orderId = orderId;
        this.merchantId = merchantId;
        this.dateOfOrder = getDate();
        this.merchantName = merchantName;
        this.merchantAddress = merchantAddress;
        this.offersAvailed = offers;
        this.orderStatus = OrderStatus.ONGOING;
    }

    @PropertyName("user_id")
    public String getUserId() {
        return userId;
    }

    @PropertyName("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @PropertyName("merchant_id")
    public String getMerchantId() {
        return merchantId;
    }

    @PropertyName("merchant_id")
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @PropertyName("order_id")
    public String getOrderId() {
        return orderId;
    }

    @PropertyName("order_id")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @PropertyName("date_of_order")
    public Date getDateOfOrder() {
        return dateOfOrder;
    }

    @PropertyName("date_of_order")
    public void setDateOfOrder(Date dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    @PropertyName("merchant_name")
    public String getMerchantName() {
        return merchantName;
    }

    @PropertyName("merchant_name")
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    @PropertyName("merchant_address")
    public String getMerchantAddress() {
        return merchantAddress;
    }

    @PropertyName("merchant_address")
    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    @PropertyName("offers_availed")
    public String getOffersAvailed() {
        return offersAvailed;
    }

    @PropertyName("offers_availed")
    public void setOffersAvailed(String offersAvailed) {
        this.offersAvailed = offersAvailed;
    }

    @PropertyName("order_status")
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    @PropertyName("order_status")
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    private Date getDate() {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
        Date date = Calendar.getInstance().getTime();
        try {
            date = format.parse(format.format(date));
        } catch (ParseException e) {
            LOGGER.log(Level.INFO, "Could not parse Date");
        }
        return date;
    }

}