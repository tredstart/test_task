package com.company;


public class Order {
    // declaring order details variables

    private String email;
    private int amount;
    private String currency;
    protected Address address = new Address();
    protected Product product = new Product();

    public static class Address{
        private String street;
        private String zipcode;
        private String city;
        private String country;

        public void setStreet(String street){
            this.street = street;
        }
        public void setZipcode(String zipcode){
            this.zipcode = zipcode;
        }
        public void setCity(String city){
            this.city = city;
        }
        public void setCountry(String country){
            this.country = country;
        }

        public String getStreet(){
            return street;
        }
        public String getZipcode(){
            return zipcode;
        }
        public String getCity(){
            return city;
        }
        public String getCountry(){
            return country;
        }
    }
    public static class Product{

        private String name;
        private int quantity;

        public void setProducts_name(String name){
            this.name = name;
        }
        public void setProducts_quantity(int quantity){
            this.quantity = quantity;
        }

        public String getName(){
            return name;
        }
        public int getQuantity(){
            return quantity;
        }

    }


    // setters for order details variables
    public void setEmail(String email){
        this.email = email;
    }
    public void setAmount(int amount){
        this.amount = amount;
    }
    public void setCurrency(String currency){
        this.currency = currency;
    }



    // getters for order details variables
    public String getEmail(){
        return email;
    }
    public int getAmount(){
        return amount;
    }
    public String getCurrency(){
        return currency;
    }



}
