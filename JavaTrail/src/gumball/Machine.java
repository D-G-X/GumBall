package gumball;

import bootstrap.GumballLogs;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Machine implements Serializable {
    private static final long serialVersionUID = 24914;
    private int state = 0;
    private int      choice, cash;
    private double   inMoney;
    private double coins;
    private double soldSweetWorth;
    private ArrayList<String> sweets = new ArrayList<>();
    private static final Scanner  sc     = new Scanner(System.in);
    private Map<String,Double>  sweetCost   = new HashMap<>();
    private Map<String,Integer> sweetStock  = new HashMap<>();
    private Map<Double,Integer> changeStock = new HashMap<>();
    private static final GumballLogs gumballLogs = new GumballLogs();
    private static final Logger logger = gumballLogs.setupLogger();

    public int getState() {
        return state;
    }

    public void resetInMoney() {
        this.inMoney = 0;
        this.coins   = 0;
        this.cash    = 0;
    }

    public int getChoice() {
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

    public void menu(){

        System.out.println( "---------SWEETS-----------" );
        System.out.println( "1. Snickers\t\t-\t9.75 Rs"  );
        System.out.println( "2. TWIX\t\t\t-\t5.25 Rs"    );
        System.out.println( "3. Toblerone\t-\t8.5  Rs"   );
        System.out.println( "4. MARS\t\t\t-\t7.75 Rs"  );
        System.out.println( "5. Cancel Transaction"      );
        System.out.println( "0. Close Machine"           );
        System.out.println( "--------------------------" );
        System.out.println( "\nEnter your choice:\t"     );
        this.choice = this.sc.nextInt();

        if(choice != 5 && choice != 0){
            String sweet = this.sweets.get(choice - 1);
            if(this.sweetStock.get(sweet) <= 0){


                System.out.println("The sweet is not in stock.\n" +
                        "Please Select other sweet or Exit\n");
                logger.log(Level.SEVERE,"The sweet is not in stock.\n" +
                                "Please Select other sweet or Exit\n");

                menu();
            }
        } else if (choice == 5){
            System.out.println("Cancelling the Transaction");
            System.out.println("Returning the Money\n\n");

            logger.log(Level.SEVERE,"Cancelling the Transaction.\n" +
                    "Returning the Money"+"\n");

            resetInMoney();

            logger.log(Level.SEVERE,"New Transaction"+"\n");

            System.out.println("New Transaction\n");

            setInMoney();
            menu();
        } else if (choice == 0){

            System.out.println("Returning the money");
            System.out.println("Closing the Machine");
            this.state = 1;
            saveMachineState(this);

            logger.log(Level.SEVERE,"Returning the money\n" + "Closing the Machine."+"\n");

            logger.log(Level.SEVERE, "Total Sweets sold: Rs"+this.soldSweetWorth+"\n");
            System.exit(0);
        }
    }

    public void setInMoney(){
        System.out.println("Insert Money: \nTotal Cash:");

        this.cash = sc.nextInt();

        System.out.println("Total Coins:");

        this.coins = sc.nextDouble();

        this.inMoney = cash + coins;

        logger.log(Level.SEVERE,"Money Inserted: \t Cash: "+this.cash+"\tCoins: "+this.coins+"\t\tTotal Money: "+this.inMoney+"\n");
    }

    public boolean compareInCost(){
        String sweet = this.sweets.get((this.choice) - 1);
        return this.inMoney >= this.sweetCost.get(sweet);
    }

    public void decreaseStock(String sweet){
        int stock = this.sweetStock.get(sweet);
        stock -= 1;
        this.soldSweetWorth += this.sweetCost.get(sweet);
        this.sweetStock.put(sweet,stock);
    }

    public void saveMachineState(Machine gumball){
        try {
            File file = new File("D:/Clear/JavaTrail/Output.txt");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gumball);
            System.out.println("The state is Saved");
            logger.log(Level.SEVERE, "Machine State Saved"+"\n");
        } catch(Exception e){
            System.out.println(e);
            logger.log(Level.SEVERE,e+"\n");
        }
    }
}
