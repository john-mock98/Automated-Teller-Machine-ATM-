import java.text.DecimalFormat;
import java.util.Random;

/**
 *
 * @author John P Mock
 */
public class ATMAccount {
    private int accountID; //ID cannot be changed once generated and only accessed by this object
    public String accountName;
    private int accountPin; //Only ATMAccount object needs access to this variable 
    private double accountBalance; //Only ATMAccount object and package are going to have access to this variable *SEE SETTERS (protected)
    public DecimalFormat balanceFormatter = new DecimalFormat("$" + "#,###.##");

    public ATMAccount(int accID, String accName, int accPin, double accBalance){
        this.accountID = accID;
        this.accountName = accName;
        this.accountPin = accPin;
        this.accountBalance = accBalance;
        
    }
    public ATMAccount(int accID, String accName, int accPin){
        this.accountID = accID;
        this.accountName = accName;
        this.accountPin = accPin;
        this.accountBalance = 0;
        
    }
    public ATMAccount(int accID, String accName){
        this.accountID = accID;
        this.accountName = accName;
        this.accountPin = 0000;
        this.accountBalance = 0;
    }
    public ATMAccount(){
        Random accountIDGenerator = new Random();
        this.accountID = accountIDGenerator.nextInt(2147483647);
        this.accountName = null;
        this.accountPin = 0000;
        this.accountBalance = 0;
                
    }
    
    //Getters and Setters
    public String getName(){
        return accountName;
    }
    public double getBalance(){
        
        return accountBalance;
    }
    //Demonstrate security
    protected void setDeposit(double deposit){
        this.accountBalance += deposit;
    }
    //Demonstrate security
    protected void setWithdraw(double withdraw){
        this.accountBalance -= withdraw;
    }
    public int getID(){
        return accountID;
    }
    //Used in entering the account information into the accountsdb.txt
    public String parseString(){
        String str;
        str = accountID +"    "+accountName + "    "+ accountBalance
                + "    "+ accountPin;
        return str;
    }
    //Used for presenting information to user
    //Overriding default toString method 
    public String toString(){
        String message;
        message = "\nAccount ID: " + accountID + "\nAcccount Name: "
                + accountName+"\nAccount Balance: " +
                balanceFormatter.format(accountBalance) + "\nAccount Pin: " +accountPin+"\n";
        return message;
    }
    
}
