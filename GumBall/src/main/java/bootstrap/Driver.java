package bootstrap;

import gumball.Machine;


import java.io.*;
import java.util.Formatter;
import java.util.logging.*;

public class Driver {
    private static final Logger logger = GumballLogs.setupLogger();

    public static Machine loadMachineState(String path){
        Machine gumball = new Machine();
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            gumball = (Machine) ois.readObject();
            logger.log(Level.INFO,"Previous State Loaded \n");
        } catch(Exception e){
            logger.log(Level.SEVERE,"ERROR:\t"+e.getLocalizedMessage()+"\n");
        }
        return gumball;
    }

    public static void main(String[] args) {

//        Machine gumball = loadMachineState("/home/drgx/Documents/workspace/GumBall/src/main/resources/Output.txt");
        Machine gumball = loadMachineState("D:/Hogwarts Engineering/GumBall/src/main/resources/Output.txt");

        if (gumball.getState() != 1) {
            logger.log(Level.INFO,"New State Created");
            gumball.setSweetAttributes("SNICKERS", 9.75, 10);
            gumball.setSweetAttributes("TWIX", 5.25, 10);
            gumball.setSweetAttributes("TOBLERONE", 7.75, 10);
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
                    String sweet = gumball.getChoice();
                    double diff_cost = gumball.getInMoney() - gumball.getSweetCost().get(sweet);
                    gumball.decreaseStock(sweet);

                    if (diff_cost <= 0) {
                        System.out.println("Transaction Successful.\n");
                        logger.log(Level.INFO, "Transaction Successful" + " 1 " + sweet +
                                " sold, remaining stock " + gumball.getSweetStock().get(sweet) + "\n");
                        gumball.resetInMoney();
                    } else {
                        //return money
                            //error if not enough changeStock
                        int changeRequired01, changeRequired05, changeRequired050, changeRequired0250;
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

                        System.out.println("\nChange Returned: " + formatter);
                        logger.log(Level.INFO, "Change Returned: " + formatter + "\n");
                        gumball.resetInMoney();
                        System.out.println("Transaction Successful.");
                        logger.log(Level.INFO, "Transaction Successful" + " 1 " + sweet +
                                " sold, remaining stock " + gumball.getSweetStock().get(sweet) + "\n");
                    }
                } else {
                    System.out.println("The money is insufficient for the transaction");
                    System.out.println("Money returned\n");

                    logger.log(Level.INFO, "The money is insufficient for the transaction\nMoney returned\n");
                    gumball.resetInMoney();
                    gumball.setInMoney();
                    gumball.menu();
                }
            }
        }

}
