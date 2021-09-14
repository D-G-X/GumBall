package bootstrap;

import gumball.Machine;


import java.io.*;
import java.util.Formatter;
import java.util.logging.*;

public class Driver {
    private static GumballLogs gumballLogs = new GumballLogs();
    private static Logger logger = gumballLogs.setupLogger();

    public static Machine loadMachineState(String path){
        Machine gumball = new Machine();
        try {

            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            gumball = (Machine) ois.readObject();
            logger.log(Level.SEVERE,"Previous State Loaded");

        } catch(Exception e){
            logger.log(Level.SEVERE,"ERROR:\t"+e);
        }
        return gumball;
    }

    public static void main(String args[]) throws IOException {

        Machine gumball = loadMachineState("D:/Clear/JavaTrail/Output.txt");

        if (gumball.getState() != 1) {
            logger.log(Level.SEVERE,"New State Created");
            gumball.setSweetAttributes("Snickers", 9.75, 10);
            gumball.setSweetAttributes("TWIX", 5.25, 10);
            gumball.setSweetAttributes("Toblerone", 7.75, 10);
            gumball.setSweetAttributes("MARS", 6.75, 10);

            gumball.setChangeStock(0.25, 50);
            gumball.setChangeStock(0.50, 50);
            gumball.setChangeStock(1.00, 50);
            gumball.setChangeStock(5.00, 50);
        }
            while (true) {
                gumball.setInMoney();
                gumball.menu();

                if (gumball.compareInCost()) {

                    String sweet = gumball.getSweets().get(gumball.getChoice() - 1);
                    double diff_cost = gumball.getInMoney() - gumball.getSweetCost().get(sweet);
                    gumball.decreaseStock(sweet);

                    if (diff_cost <= 0) {
//                        System.out.println("Transaction Successful" + " 1 " + sweet +
//                                " sold, remaining stock " + gumball.getSweetStock().get(sweet));
                        System.out.println("Transaction Successful.");

                        logger.log(Level.SEVERE, "Transaction Successful" + " 1 " + sweet +
                                " sold, remaining stock " + gumball.getSweetStock().get(sweet) + "\n");

//                    System.out.println(diff_cost);

                        gumball.resetInMoney();
                    } else {

                        //return the change//
                        int changeRequired01 = 0, changeRequired05 = 0, changeRequired050 = 0, changeRequired0250 = 0;
                        int integer_part = (int) diff_cost;
                        double fractional_part = diff_cost % 10;

                        while (fractional_part > 0) {
                            if (fractional_part >= 0.5) {
                                changeRequired050 = (int) (fractional_part / 0.50);
                                gumball.decreaseChangeStock(0.50, changeRequired050);
                                fractional_part -= changeRequired050 * (0.50);
                            } else if (fractional_part >= 0.25) {
                                changeRequired0250 = (int) (fractional_part / 0.25);
                                gumball.decreaseChangeStock(0.25, changeRequired0250);
                                fractional_part -= changeRequired0250 * (0.25);
                            } else if (fractional_part < 0.25) {
                                gumball.decreaseChangeStock(0.25, 1);
                                fractional_part = 0.00;
                            }
                        }


                        if (integer_part >= 5) {
                            changeRequired01 = integer_part % 5;
                            changeRequired05 = integer_part / 5;
                            gumball.decreaseChangeStock(1.0, changeRequired01);
                            gumball.decreaseChangeStock(5.0, changeRequired05);

                        } else {
                            changeRequired01 = integer_part;
                            gumball.decreaseChangeStock(1.0, changeRequired01);
                        }
                        Formatter formatter = new Formatter();
                        formatter.format("%.2f", diff_cost);

                        System.out.println("\nChange Returned: " + formatter.toString());

                        logger.log(Level.SEVERE, "Change Returned: " + formatter.toString() + "\n");

                        gumball.resetInMoney();

                        System.out.println("Transaction Successful.");

                        logger.log(Level.SEVERE, "Transaction Successful" + " 1 " + sweet +
                                " sold, remaining stock " + gumball.getSweetStock().get(sweet) + "\n");

//                        System.out.println("Transaction Successful" + " 1 " + sweet +
//                                " sold, remaining stock " + gumball.getSweetStock().get(sweet));

                    }
                } else {
                    System.out.println("The money is insufficient for the transaction");
                    System.out.println("Money returned\n");

                    logger.log(Level.SEVERE, "The money is insufficient for the transaction\nMoney returned\n");

                    gumball.resetInMoney();
                    gumball.setInMoney();
                    gumball.menu();
                }
            }
        }

}
