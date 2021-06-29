package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class AntiFraudSystem extends Thread{

    private final OrderController startUpOrderController = new OrderController(null, null);
    private JSONArray jsonStartup;
    private final HashMap<Order, OrderController> orders = new HashMap<>();
    private boolean flag = true;

    private final Runnable r = () -> {
            while (flag){
                // checks if there are new orders
                if (!orders.isEmpty()) {
                    for (Order o : orders.keySet()) {
                        // running monitor on every new order
                        AntiFraudCheck(o, orders.get(o));
                    }
                    for (Order o : orders.keySet()) {
                        // deletes all checked orders
                        orderPop(o);
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException exception) {
                    System.out.println("Interrupted...");
                }


            }
        System.out.println("Exiting...");
    };
    public void start(){
        Thread thread = new Thread(r);
        jsonStartup = startUpOrderController.Deserialize(); // loads previous orders on startup
        thread.start();
    }

    public void Stop(){
        // last check is there are orders left
        if (!orders.isEmpty()) {
            for (Order o : orders.keySet()) {
                AntiFraudCheck(o, orders.get(o));
            }
        }
        this.flag = false;
    }

    public void orderAppend(Order newOrder, OrderController orderController){
        orders.put(newOrder, orderController);
    }

    public void orderPop(Order orderToDelete){
        if (orders.isEmpty()){
            startUpOrderController.getView().printNotification("Anti-fraud check: can't delete order.",
                    orderToDelete.getEmail(), true);
        }
        else {

            orders.remove(orderToDelete);

        }
    }

    public void AntiFraudCheck(Order order, OrderController orderController){
        String orderCountry = order.address.getCountry();

        // checks if this is the new client and the order country is Nigeria and amount is over 1000

        if (orderCountry.equalsIgnoreCase("Nigeria")
                && !orderController.isClientExist()
                && order.getAmount() > 1000){

            orderController.getView().printNotification("The order was suspended due to anti-fraud violation",
                    order.getEmail(), true);

        }
        // checks if the order amount is 5 times higher than the average amount of the previous orders
        else if (order.getAmount() > getAverage()){
            orderController.getView().printNotification("The order was suspended due to anti-fraud violation",
                    order.getEmail(), true);
        }
        else{
            // register new orders to json file and shows notification about success
            orderController.getView().printNotification("The order was processed successfully",
                    order.getEmail(), false);
            orderController.Serialize();
        }
    }

    // getting average amount of previous orders
    public double getAverage(){
        double sum = 0;
        for (int i = 0; i < jsonStartup.length(); ++i){
            JSONObject record = jsonStartup.getJSONObject(i);
            sum += record.getInt("amount");
        }
        double average = sum / jsonStartup.length();
        return average * 5;
    }
}
