package com.company;


public class OrderView {
    public void printNotification(String message, String customer, boolean error){
        if (error) System.out.println("Oops... There is an error for " + customer + "!");
        System.out.println(message);
    }
}
