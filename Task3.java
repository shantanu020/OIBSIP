

import java.util.ArrayList;
import java.util.Scanner;

// Class representing a financial transaction
class Transaction {
    private String transactionType;
    private double transactionAmount;

    public Transaction(String transactionType, double transactionAmount) {
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
    }

    @Override
    public String toString() {
        return transactionType + ": $" + transactionAmount;
    }
}

// Class representing a user's bank account
class BankAccount {
    private double balance;
    private ArrayList<Transaction> transactionHistory;

    public BankAccount() {
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void addDeposit(double amount) {
        balance += amount;
        transactionHistory.add(new Transaction("Deposit", amount));
        System.out.println("Deposit successful: $" + amount);
    }

    public void processWithdrawal(double amount) {
        if (amount > balance) {
            System.out.println("Error: Insufficient balance!");
        } else {
            balance -= amount;
            transactionHistory.add(new Transaction("Withdraw", amount));
            System.out.println("Withdrawal successful: $" + amount);
        }
    }

    public void recordTransaction(String type, double amount) {
        transactionHistory.add(new Transaction(type, amount));
    }

    public void displayTransactionHistory() {
        System.out.println("Transaction History:");
        for (Transaction transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }
}

// Class representing a user of the ATM system
class ATMUser {
    private String userId;
    private String userPin;
    private BankAccount bankAccount;

    public ATMUser(String userId, String userPin) {
        this.userId = userId;
        this.userPin = userPin;
        this.bankAccount = new BankAccount();
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPin() {
        return userPin;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }
}

// Class representing the ATM interface and its functionalities
class ATMSystem {
    private ArrayList<ATMUser> registeredUsers;
    private ATMUser loggedInUser;
    private Scanner scanner;

    public ATMSystem() {
        registeredUsers = new ArrayList<>();
        scanner = new Scanner(System.in);
        setupInitialUsers();
    }

    private void setupInitialUsers() {
        registeredUsers.add(new ATMUser("user1", "1234"));
        registeredUsers.add(new ATMUser("user2", "5678"));
    }

    public void startATM() {
        System.out.println("=== Welcome to the Secure ATM System ===");
        authenticateUser();
    }

    private void authenticateUser() {
        System.out.print("Please enter your User ID: ");
        String enteredUserId = scanner.nextLine();
        System.out.print("Please enter your PIN: ");
        String enteredPin = scanner.nextLine();

        for (ATMUser user : registeredUsers) {
            if (user.getUserId().equals(enteredUserId) && user.getUserPin().equals(enteredPin)) {
                loggedInUser = user;
                System.out.println("Login successful. Welcome, " + user.getUserId() + "!");
                showATMMenu();
                return;
            }
        }

        System.out.println("Login failed. Invalid User ID or PIN. Please try again.");
        authenticateUser();
    }

    private void showATMMenu() {
        while (true) {
            System.out.println("\n=== ATM Main Menu ===");
            System.out.println("1. View Transaction History");
            System.out.println("2. Withdraw Cash");
            System.out.println("3. Deposit Cash");
            System.out.println("4. Transfer Funds");
            System.out.println("5. Exit");
	    System.out.print("Select an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    loggedInUser.getBankAccount().displayTransactionHistory();
                    break;
                case 2:
                    executeWithdrawal();
                    break;
                case 3:
                    executeDeposit();
                    break;
                case 4:
                    executeTransfer();
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM System. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid selection. Please choose a valid option.");
            }
        }
    }

    private void executeWithdrawal() {
        System.out.print("Enter the amount to withdraw: ");
        double withdrawalAmount = scanner.nextDouble();
        loggedInUser.getBankAccount().processWithdrawal(withdrawalAmount);
    }

    private void executeDeposit() {
        System.out.print("Enter the amount to deposit: ");
        double depositAmount = scanner.nextDouble();
        loggedInUser.getBankAccount().addDeposit(depositAmount);
    }

    private void executeTransfer() {
        System.out.print("Enter the recipient's User ID for the transfer: ");
        String recipientId = scanner.nextLine();
        System.out.print("Enter the amount to transfer: ");
        double transferAmount = scanner.nextDouble();

        ATMUser recipientUser = findUserById(recipientId);

        if (recipientUser != null) {
            if (loggedInUser.getBankAccount().getBalance() >= transferAmount) {
                loggedInUser.getBankAccount().processWithdrawal(transferAmount);
                recipientUser.getBankAccount().addDeposit(transferAmount);
                loggedInUser.getBankAccount().recordTransaction("Transfer to " + recipientId, transferAmount);
                recipientUser.getBankAccount().recordTransaction("Transfer from " + loggedInUser.getUserId(), transferAmount);
                System.out.println("Transfer successful: $" + transferAmount + " to user " + recipientId);
            } else {
                System.out.println("Error: Insufficient balance for the transfer.");
            }
        } else {
            System.out.println("Error: Recipient User ID not found.");
        }
    }

    private ATMUser findUserById(String userId) {
        for (ATMUser user : registeredUsers) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}

// Main class to initiate the ATM application
public class ATMInterfaceMain {
    public static void main(String[] args) {
        ATMSystem atmSystem = new ATMSystem();
        atmSystem.startATM();
    }
}