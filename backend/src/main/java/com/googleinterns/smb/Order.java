package com.googleinterns.smb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class Order {
    public String user_id;
    public String order_id;
    public Date date_of_order;
    public String merchant_name;
    public String merchant_address;
    public String offers_availed;
    public String order_status;
    private static Random rand = new Random();

    public Order(){

    }

    public Order(String userID){
        this.user_id = userID;
        this.order_id = generateOrderID();
        this.order_status = "PROCESSING";
    }

    public Order(String userID, String merchantName, String merchantAddress, String offers){
        this.user_id = userID;
        this.order_id = generateOrderID();
        this.merchant_name = merchantName;
        this.merchant_address = merchantAddress;
        this.offers_availed = offers;
        this.order_status = "PROCESSING";
    }

    private String generateOrderID(){
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat currentTimeStamp = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
 

		String currentTime = currentTimeStamp.format(today);
 
		try {
 
            Date date = currentTimeStamp.parse(currentTime);
            this.date_of_order = date;
 
			long epochTime = date.getTime();
 
            int randomDigits[] = new int[5];
            
            for (int i = 0; i<5; i++){
                randomDigits[i] = rand.nextInt(10);
            }

            StringBuilder sb = new StringBuilder(5);

            for (int i = 0; i < 5; i++) sb.append(randomDigits[i]);
            String randomID = epochTime + ":" + sb.toString();
            return randomID;
 
		} catch (ParseException e) {
			return "ERROR";
		}
    }

    
}