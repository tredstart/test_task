package com.company;


import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.Character.toUpperCase;

public class Main {

    public static void main(String[] args) {
        AntiFraudSystem antiFraudSystem = new AntiFraudSystem();
        antiFraudSystem.start(); // start anti-fraud monitor in a new thread

        char flag = 'Y';
        Scanner scanner = new Scanner(System.in);
        while(toUpperCase(flag) != 'N'){
            Order order = readOrder();
            // checks if order was created
            if (order != null){
                OrderView orderView = new OrderView();

                OrderController orderController = new OrderController(order, orderView);
                // checks if client input is correct and registers in anti-fraud system
                if (orderController.Validate(order)) antiFraudSystem.orderAppend(order, orderController);

            }
            System.out.println("Wanna place another order? [Y/n]");

            flag = scanner.next().charAt(0);
        }
        antiFraudSystem.Stop();


    }

    public static Order readOrder(){
        String email;
        int amount;
        String currency;
        String street;
        String zipcode;
        String city;
        String country;
        String name;
        int quantity;

        // little text interface to get input from user

        Scanner scanner = new Scanner(System.in);
        try{
            System.out.println("Email: ");
            email = scanner.nextLine();
            System.out.println("Amount: ");
            amount = Integer.parseInt(scanner.nextLine());
            System.out.println("Currency: ");
            currency = scanner.nextLine();
            System.out.println("Address ");
            System.out.println("Street: ");
            street = scanner.nextLine();
            System.out.println("Zipcode: ");
            zipcode = scanner.nextLine();
            System.out.println("City: ");
            city = scanner.nextLine();
            System.out.println("Country: ");
            country = scanner.nextLine();
            System.out.println("Products ");
            System.out.println("Name: " );
            name = scanner.nextLine();
            System.out.println("Quantity: " );
            quantity = Integer.parseInt(scanner.nextLine());

            System.out.println("Confirm order? [Y/n] ");
            char answer = scanner.next().charAt(0);
            if (toUpperCase(answer) == 'N'){
                return null;
            }
        }
        catch (NumberFormatException exception){
            System.out.println("Use numbers for amount or quantity");
            return null;
        }
        catch (InputMismatchException exception){
            System.out.println("Check your input");
            return null;
        }


        Order order = new Order();
        order.setEmail(email);
        order.setAmount(amount);
        order.setCurrency(currency);

        order.address.setStreet(street);
        order.address.setZipcode(zipcode);
        order.address.setCity(city);
        order.address.setCountry(country);

        order.product.setProducts_name(name);
        order.product.setProducts_quantity(quantity);

        return order;
    }
}
