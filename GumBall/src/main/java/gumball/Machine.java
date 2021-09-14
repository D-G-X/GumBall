package gumball;

import bootstrap.GumballLogs;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Machine implements Serializable {
    private static final long serialVersionUID = 24914;
    private int state = 0;
    private int cash;
    private double   inMoney, coins, soldSweetWorth;
    private String choice;
    private final ArrayList<String> sweets = new ArrayList<>();
    private static final Scanner  sc     = new Scanner(System.in);
    private final Map<String,Double>  sweetCost   = new HashMap<>();
    private final Map<String,Integer> sweetStock  = new HashMap<>();
    private final Map<Double,Integer> changeStock = new HashMap<>();
    private static final Logger logger = GumballLogs.setupLogger();

    public int getState() {
        return state;
    }

    public void resetInMoney() {
        this.inMoney = 0;
        this.coins   = 0;
        this.cash    = 0;
    }

    public String getChoice() {
        return choice;
    }

    public ArrayList<String> getSweets() {
        return sweets;
    }

    public double getInMoney() {
        return inMoney;
    }

    public Map<String, Double> getSweetCost() {
        return sweetCost;
    }

    public Map<String, Integer> getSweetStock() {
        return sweetStock;
    }

    public double getSoldSweetWorth() {
        return soldSweetWorth;
    }

    public void setChangeStock(double changeValue, int stock){
        this.changeStock.put(changeValue,stock);
    }

    public void setSweetAttributes(String sweetName, double cost, int stock){
        this.sweets.add(sweetName);
        this.sweetStock.put(   sweetName   ,   stock   );
        this.sweetCost.put (   sweetName   ,   cost    );
    }

    public Map<Double, Integer> getChangeStock() {
        return changeStock;
    }

    public void decreaseChangeStock(Double change, int required){
        int stock = this.changeStock.get(change);
        stock -= required;
        this.changeStock.put(change,stock);
    }

    private boolean validInput(String sweet){

        return this.sweets.contains(sweet);
    }


    public void menu(){
        System.out.println( "---------SWEETS-----------" );
        System.out.println( "Snickers\t\t-\t9.75 Rs"  );
        System.out.println( "TWIX\t\t\t-\t5.25 Rs"    );
        System.out.println( "Toblerone\t\t-\t8.5  Rs"   );
        System.out.println( "MARS\t\t\t-\t7.75 Rs"  );
        System.out.println( "5. Cancel Transaction"      );
        System.out.println( "0. Close Machine"           );
        System.out.println( "--------------------------" );
        System.out.println( "\nEnter your choice:\t"     );
        this.choice = sc.nextLine();
        this.choice = this.choice.toUpperCase();
        if((!this.choice.equals("5")) && (!this.choice.equals("0")) && validInput(this.choice)){
            String sweet = this.choice;
            if(this.sweetStock.get(sweet) <= 0){
                System.out.println("The sweet is not in stock.\n" +
                        "Please Select other sweet or Exit\n");
                logger.log(Level.INFO,"The sweet is not in stock.\n" +
                                "Please Select other sweet or Exit\n");
                menu();
            }
        } else if (this.choice.equals("5")){
            System.out.println("Cancelling the Transaction");
            System.out.println("Returning the Money\n\n");
            logger.log(Level.INFO,"Cancelling the Transaction.\n" +
                    "Returning the Money"+"\n");
            resetInMoney();
            logger.log(Level.INFO,"New Transaction"+"\n");
            System.out.println("New Transaction\n");
            setInMoney();
            menu();
        } else if (this.choice.equals("0")){
            System.out.println("Returning the money");
            System.out.println("Closing the Machine");
            this.state = 1;
            saveMachineState(this);
            logger.log(Level.INFO,"Returning the money\n" + "Closing the Machine."+"\n");
            logger.log(Level.INFO, "Total Sweets sold: Rs"+this.soldSweetWorth+"\n");
            System.exit(0);
        } else{
            System.out.println("Invalid Input: menu");
            logger.log(Level.SEVERE,"Input Error\t Invalid Input: "+this.choice+"\n");
            menu();
        }
    }

    public void setInMoney(){
        try{
            System.out.println("Insert Money: \n");

            while (true){
                try {
                    System.out.println("Total Cash:");
                    this.cash = Integer.parseInt(sc.nextLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.print("Invalid Input: \n");
                }}

            while (true){
                try {
                    System.out.println("Total Coins:");
                    this.coins = Double.parseDouble(sc.nextLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.print("Invalid Input: \n");
                }
            }
            this.inMoney = this.cash + this.coins;
            logger.log(Level.SEVERE,"Money Inserted: \t Cash: "+this.cash+"\tCoins: "+this.coins+"\t\tTotal Money: "+this.inMoney+"\n");
        } catch(Exception e){
            logger.log(Level.SEVERE,"ERROR:\t"+e.getLocalizedMessage());
        }
    }

    public boolean compareInCost(){
//        String sweet = this.sweets.get((this.choice) - 1);
        String sweet = this.choice;
        return this.sweetCost.get(sweet) <= this.inMoney;
    }

    public void decreaseStock(String sweet){
        int stock = this.sweetStock.get(sweet);
        stock -= 1;
        this.soldSweetWorth += this.sweetCost.get(sweet);
        this.sweetStock.put(sweet,stock);
    }

    public void saveMachineState(Machine gumball){
        try {
//            File file = new File("/home/drgx/Documents/workspace/GumBall/src/main/resources/Output.txt");
            File file = new File("D:/Hogwarts Engineering/GumBall/src/main/resources/Output.txt");

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gumball);
            System.out.println("The state is Saved");
            logger.log(Level.INFO, "Machine State Saved"+"\n");
        } catch(Exception e){
            System.out.println(e);
            logger.log(Level.SEVERE,"ERROR:\t"+e+"\n");
        }
    }
}
