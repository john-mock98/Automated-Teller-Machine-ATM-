import java.text.DecimalFormat;
import java.util.Random;

/**
 *
 * @author John P Mock
 */
public class ATMAccount {
    private int accountID;
    public String accountName;
    private int accountPin;
    private double accountBalance;
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
    
    
    public String getName(){
        return accountName;
    }
    public double getBalance(){
        
        return accountBalance;
    }
    protected void setDeposit(double deposit){
        this.accountBalance += deposit;
    }
    protected void setWithdraw(double withdraw){
        this.accountBalance -= withdraw;
    }
    public int getID(){
        return accountID;
    }
    public String parseString(){
        String str;
        str = accountID +"    "+accountName + "    "+ accountBalance
                + "    "+ accountPin;
        return str;
    }
    public String toString(){
        String message;
        message = "\nAccount ID: " + accountID + "\nAcccount Name: "
                + accountName+"\nAccount Balance: " +
                balanceFormatter.format(accountBalance) + "\nAccount Pin: " +accountPin+"\n";
        return message;
    }
    
}
