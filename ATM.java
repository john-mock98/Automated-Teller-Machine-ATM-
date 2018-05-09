
/**
 *
 * @author John Mock
 */
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * 						 PROJECT COMPLETED (5/8/18 @ 9:25 PM)
 */

public class ATM {
	static DecimalFormat moneyFormatter = new DecimalFormat("$" + "#,###,###.##");
	static File accountsdb;
	static Scanner accountsdbReader;
	static Scanner userInput = new Scanner(System.in);

	/*
	*	Creates new account 
	*
	*
	*
	*/
	public static ATMAccount createAccount() {
		String userName;
		double userBalance;
		int digit = -1;

		System.out.println("Please enter a name for the account: ");
		userInput.nextLine();
		userName = userInput.nextLine();

		//Requests digits 1 by 1 for the pin number
		String accPin = "";
		for (int i = 1; i < 5; i++) {
			digit = -1;
			System.out.println("Please enter digit #" + i + " of" + "your pin.");
			do {
				digit = userInput.nextInt();
			} while (digit < 0 || digit > 9);
			accPin += digit;
		}

		System.out.println("Please put the amount of money you would like to " + "initially deposit into the account: ");
		userBalance = userInput.nextDouble();
		Random accountIDGenerator = new Random();
		int userID = accountIDGenerator.nextInt(2147483647);
		ATMAccount account = new ATMAccount(userID, userName, Integer.parseInt(accPin), userBalance);
		System.out.println("Acount Information: \n"+account.toString());
		return account;
	}

	//Presents user with action options
	//ATMAccount account - passes the account either created previously or logged in
	public static void userChoice(ATMAccount account) throws FileNotFoundException, IOException, InterruptedException{
		int selection = 0;
		do {
			System.out.println("Please select one of the following: " + "\n1 = Request ID \n2 = Balance Inquiry"
					+ "\n3 = Deposit Money\n4 = Withdraw Money " + " \n5 = Go Back to Start Screen"+
					"\n6 = Exit");
			selection = userInput.nextInt();
			switch (selection) {
			case 1:
				System.out.println("Requesting ID number...");
				System.out.println("Account ID: "+ account.getID());
				break;
			case 2:
				System.out.println("Your balance is: " + moneyFormatter.format(account.getBalance()));
				break;
			case 3:
				depositMoney(account);
				break;
			case 4:
				withdrawMoney(account);
				break;
			case 5:
				ATMStartScreen();
				break;
			case 6:
				System.exit(0);
				break;
			default:
				System.out.println("What are you doing mate?");
				break;
			}

		} while (selection != 5);

	}

	//Simulates an ATM Idle Screen i.e. before a credit card is inserted
	public static void ATMStartScreen() throws FileNotFoundException, IOException, InterruptedException {
		System.out.println("Welcome to Maze Bank Automated Teller Machine!");
		System.out.println("1 = Open a new account." + "\n2 = Log In to a current account." + "\n3 = Exit.");
		int select = 0;

		do {
			select = userInput.nextInt();
			switch (select) {
			case 1:
				ATMAccount accountNew = createAccount();
				parseAccountDB(accountNew);
				userChoice(accountNew);
				break;
			case 2:
				ATMAccount accountExisting = LogIn();
				userChoice(accountExisting);
				break;
			case 3:
				System.exit(0);
				break;
			default:
				break;

			}
		} while (select != 3);
	}

	//Processes a log in with the account ID that a user enters
	public static ATMAccount LogIn() throws FileNotFoundException, IOException, InterruptedException {
		ATMAccount account = null;
		System.out.println("Enter your account ID:");
		int intaccID;
		intaccID = userInput.nextInt();
		String accID = intaccID + "";
		String[] dataGrabbed = new String[4];
		File thisFile = new File("accountsdb.txt");
		accountsdbReader = new Scanner(thisFile);

		if (thisFile.exists()) {
			while (accountsdbReader.hasNextLine()) {
				String accountIDHolder = accountsdbReader.findInLine(accID);
				if (accountIDHolder == null) {

					accountsdbReader.nextLine();
				} else if (accountIDHolder.equals(accID)) {
					for (int i = 1; i < dataGrabbed.length; i++) {
						if (accountsdbReader.hasNext())
							dataGrabbed[i] = accountsdbReader.next();
					}
				} else if ((!(accountsdbReader.hasNextLine()) && account == null)) {
					System.out.println("This account does not exist!");
					ATMStartScreen();
				}

				dataGrabbed[0] = accID;

			}
			int validationLoop = 3;
			String pinValidity;
			userInput.nextLine();
			do {
				System.out.println("Please enter your 4 digit pin in order to access your account:");
				pinValidity = userInput.nextLine();
				if (pinValidity.equals(dataGrabbed[3])) {
					System.out.println("PIN Confirmed!");
					System.out.println("Displaying account options...");
					account = new ATMAccount(Integer.parseInt(dataGrabbed[0]), dataGrabbed[1],
							Integer.parseInt(dataGrabbed[3]), Double.parseDouble(dataGrabbed[2]));

					return account;
				} else {
					System.out.println("PIN Declined!");
					System.out.println("You have " + validationLoop + " attempts remaining.");

				}
				validationLoop--;
			} while (!pinValidity.equals(dataGrabbed[3]) && validationLoop != -1);
			System.out.println("You have exhausted all of your attempts you will now return to the main menu.");
		} else {
			// Return to StartScreen before being able to return null account
			System.out.println("File was not found!");
			System.out.println("Returning to main menu...");
			ATMStartScreen();
		}
		System.out.println("Returning to main menu...");
		ATMStartScreen();
		return null;

	}

	//Enters account information into accountsdb.txt
	public static void parseAccountDB(ATMAccount accountNew) throws FileNotFoundException, IOException {

		try {
			FileWriter fileWriter = new FileWriter("accountsdb.txt", true);
			BufferedWriter fileBuffer = new BufferedWriter(fileWriter);
			PrintWriter fileOutput = new PrintWriter(fileBuffer);
			fileOutput.println(accountNew.parseString());

			if (fileOutput != null) {
				fileOutput.close();
			}
			if (fileBuffer != null) {
				fileBuffer.close();
			}
			if (fileWriter != null) {
				fileWriter.close();
			}
		} catch (Exception e) {
			System.out.println("Error!");
		}
	}


	//Deposit function for balance manipulation
	public static void depositMoney(ATMAccount account) throws FileNotFoundException {
		accountsdb = new File("accountsdb.txt");
		//Use of two dimensional array to account for line number
		String[][] accountInfo = null;

		if (accountsdb.exists()) {
			accountsdbReader = new Scanner(accountsdb);
			int y = 0;
			while (accountsdbReader.hasNextLine()) {
				y++;
				accountInfo = new String[y][4];
				accountsdbReader.nextLine();
			}
			accountsdbReader.close();
		}

		double depositAmount;
		do {
			System.out.println("Enter the amount you want to deposit:");
			depositAmount = userInput.nextDouble();
			if (depositAmount < 0) {
				System.out.println("You cannot deposit a negative amount!");
			}

		} while (depositAmount < 0);
		account.setDeposit(depositAmount);
		System.out.println("Your deposit has been made.");

		System.out.println(accountInfo.length);

		String s = "";
		if (accountsdb.exists()) {
			int y = 0;
			accountsdbReader = new Scanner(accountsdb);

			while (accountsdbReader.hasNextLine()) {
				s = accountsdbReader.nextLine();
				StringTokenizer spl = new StringTokenizer(s, "    ");
				for (int x = 0; x != 4; x++) {
					accountInfo[y][x] = spl.nextToken();

				}
				y++;
			}
		}
/**											 VALIDATION PROOF
 *		System.out.println("Line #1" + "\n Index #0: " + accountInfo[0][0] + "\t Index #1: " + accountInfo[0][1]
 *				+ "\t Index #2: " + accountInfo[0][2] + "\t Index #3: " + accountInfo[0][3]);
 *		System.out.println("Line #2" + "\n Index #0: " + accountInfo[1][0] + "\t Index #1: " + accountInfo[1][1]
 *				+ "\t Index #2: " + accountInfo[1][2] + "\t Index #3: " + accountInfo[1][3]);
 *		System.out.println("Line #3" + "\n Index #0: " + accountInfo[2][0] + "\t Index #1: " + accountInfo[2][1]
 *				+ "\t Index #2: " + accountInfo[2][2] + "\t Index #3: " + accountInfo[2][3]);
 *		System.out.println("Line #4" + "\n Index #0: " + accountInfo[3][0] + "\t Index #1: " + accountInfo[3][1]
 *				+ "\t Index #2: " + accountInfo[3][2] + "\t Index #3: " + accountInfo[3][3]);
 *				
 */
		int line = -1;
		try {

			int accID;
			for (int y = 0; y != accountInfo.length; y++) {
				accID = Integer.parseInt(accountInfo[y][0]);
				if (accID == account.getID())
					line = y;

			}
			accountInfo[line][2] = "" + account.getBalance();
			System.out.println("Depositing...");
			System.out.println("New Balance: " + accountInfo[line][2]);
		} catch (Exception e) {
			System.out.println("Account was not found in database array!");
		}
		try {
			PrintWriter scrubFile = new PrintWriter(accountsdb);
			scrubFile.write("");
			scrubFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileWriter fileWriter = new FileWriter(accountsdb, true);
			BufferedWriter fileBuffer = new BufferedWriter(fileWriter);
			PrintWriter fileOutput = new PrintWriter(fileBuffer);
			for (int i = 0; i != accountInfo.length; i++) {
				fileOutput.println(accountInfo[i][0] + "    " + accountInfo[i][1] + "    " + accountInfo[i][2] + "    "
						+ accountInfo[i][3]);
			}

			if (fileOutput != null) {
				fileOutput.close();
			}
			if (fileBuffer != null) {
				fileBuffer.close();
			}
			if (fileWriter != null) {
				fileWriter.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		accountsdbReader.close();

	}

	//Withdraw function for balance manipulation
	public static void withdrawMoney(ATMAccount account) throws FileNotFoundException {

		// File initialization
		accountsdb = new File("accountsdb.txt");
		// #ADDED THE 2-D ARRAY
		String[][] accountInfo = null;

		if (accountsdb.exists()) {
			accountsdbReader = new Scanner(accountsdb);
			int y = 0;
			while (accountsdbReader.hasNextLine()) {
				y++;
				accountInfo = new String[y][4];
				accountsdbReader.nextLine();
			}
			accountsdbReader.close();
		}

		double withdrawAmount;
		do {
			System.out.println("Enter the amount you want to withdraw:");
			withdrawAmount = userInput.nextDouble();
			if (account.getBalance() < 0 || withdrawAmount > account.getBalance()) {
				System.out.println("Insufficient balance!");
			} else {
				account.setWithdraw(withdrawAmount);
			}

		} while (withdrawAmount < 0);

		String s = "";
		if (accountsdb.exists()) {
			int y = 0;
			accountsdbReader = new Scanner(accountsdb);

			while (accountsdbReader.hasNextLine()) {
				s = accountsdbReader.nextLine();
				StringTokenizer spl = new StringTokenizer(s, "    ");
				for (int x = 0; x != 4; x++) {
					accountInfo[y][x] = spl.nextToken();

				}
				y++;
			}
		}
		int line = -1;
		try {

			int accID;
			for (int y = 0; y != accountInfo.length; y++) {
				accID = Integer.parseInt(accountInfo[y][0]);
				if (accID == account.getID())
					line = y;

			}
			accountInfo[line][2] = "" + account.getBalance();
			System.out.println("Withdrawing...");
			System.out.println("New Balance: " + accountInfo[line][2]);
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		try {
			PrintWriter scrubFile = new PrintWriter(accountsdb);
			scrubFile.write("");
			scrubFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileWriter fileWriter = new FileWriter(accountsdb, true);
			BufferedWriter fileBuffer = new BufferedWriter(fileWriter);
			PrintWriter fileOutput = new PrintWriter(fileBuffer);
			for (int i = 0; i != accountInfo.length; i++) {
				fileOutput.println(accountInfo[i][0] + "    " + accountInfo[i][1] + "    " + accountInfo[i][2] + "    "
						+ accountInfo[i][3]);
			}

			if (fileOutput != null) {
				fileOutput.close();
			}
			if (fileBuffer != null) {
				fileBuffer.close();
			}
			if (fileWriter != null) {
				fileWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		accountsdbReader.close();

	}

	//Main method initializes ATMStartScreen method
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		ATMStartScreen();
	}

}
