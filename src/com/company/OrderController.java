package com.company;

import org.json.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class OrderController {
    private final Order order;
    private final OrderView view;
    private final String data_filename = "data.json";

    public OrderController(Order order, OrderView view){
        this.order = order;
        this.view = view;
    }

    public OrderView getView() {
        return view;
    }

    public void Serialize(){

        // create json from object order

        JSONObject data = new JSONObject(order);
        JSONObject address_data = new JSONObject(order.address);
        JSONObject product_data = new JSONObject(order.product);
        data.put("address", address_data);
        data.put("products", product_data);

        try{

            // if file not exist create new json array and put there new order
            // if file exist append to end of orders array and write new array to file

            File data_file = new File(data_filename);
            if(data_file.exists()){

                JSONArray prev_data = Deserialize(); // reading array from file
                prev_data.put(data);
                FileWriter fileWriter = new FileWriter(data_filename);
                fileWriter.write(prev_data.toString());
                fileWriter.close();
            }else {
                FileWriter fileWriter = new FileWriter(data_filename);
                JSONArray new_data = new JSONArray(); // creating new json array and writing it to file
                new_data.put(data);
                fileWriter.write(new_data.toString());
                fileWriter.close();
            }
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public JSONArray Deserialize(){

        try{

            // reading data from file to string to build json array

            File data_file = new File(data_filename);
            Scanner file_reader = new Scanner(data_file);
            StringBuilder data = new StringBuilder();

            while (file_reader.hasNextLine()){
                data.append(file_reader.nextLine());
            }

            file_reader.close();
            return new JSONArray(data.toString());

        }
        catch (FileNotFoundException exception){
            exception.printStackTrace();
            return new JSONArray();
        }
    }

    public boolean Validate(Order order){
        String[] available_currencies = {"EUR", "USD", "GBP", "PLN"};

        String email = order.getEmail();
        String zipcode = order.address.getZipcode();
        String city = order.address.getCity();
        String country = order.address.getCountry();
        String street = order.address.getStreet();
        String product_name = order.product.getName();
        // checks if email and zipcode is provided correctly
        String email_validator = "^[A-Za-z0-9+_.-]+@(.+)$";
        String zipcode_validator = "^([0-9]{5})$";

        Pattern email_pattern = Pattern.compile(email_validator);
        Pattern zipcode_pattern = Pattern.compile(zipcode_validator);
        if(!email_pattern.matcher(email).matches()){
            view.printNotification("Wrong email!", email, true);
            return false;
        }
        // checks if there is no violation on currencies
        else if(!Arrays.asList(available_currencies).contains(order.getCurrency().toUpperCase()) || order.getAmount() < 0){
            view.printNotification("Wrong currency or amount is below 0!", email, true);
            return false;
        }

        else if (!zipcode_pattern.matcher(zipcode).matches()){
            view.printNotification("Not valid zip code", email, true);
            return false;
        }
        // checks if other fields are not empty
        else if (city.isEmpty() || country.isEmpty() || street.isEmpty()){
            view.printNotification("Check address!", email, true);
            return false;
        }
        else if (product_name.isEmpty()){
            view.printNotification("Invalid name for product!", email, true);
            return false;
        }
        return true;
    }

    public boolean isClientExist(){
        JSONArray orderSystem = Deserialize();
        String email_to_find = "\"email\":\""+order.getEmail()+"\""; // looks for client with given email already exists
        return orderSystem.toString().contains(email_to_find);
    }

}
