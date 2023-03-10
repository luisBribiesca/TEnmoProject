package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {
    private AccountService acc = new AccountService();
    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = "1";
        while (true) {
            username = promptForString("Username: ");
            if (Character.isDigit(username.charAt(0))) {
                System.out.println("Username must not start with a number. Please try again.");
            } else {
                break;
            }
        }
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printCurrentBalance(int userId) {
        //Goal of this method is to use accountservice to return the balance with the user_id.
        Account account = acc.getAccount(userId);
        System.out.println("\nYour account balance is: " +
                account.getBalance());
    }

    public void printListOfUsers(int currentUserId) {
        User[] users = acc.getAllUsers();
        for (User user : users) {
            if (user.getId() == currentUserId) {
                continue;
            } else {
                System.out.println(user.toString());
            }

        }

    }

    public void printTransferHistory(int currentUserId) {
        //Goal of this function is print the Transfer history to the console
        Transfer[] transfersList = acc.getTransferHistory(currentUserId);
        int i = 1;
        System.out.println("\n\t\t\t\t\t\tTransfer History\n#:\t\tPayer:\t\t\tAmount:\t\tPayee:\t\t\tStatus:\t\t\tID:");
        for (Transfer transactions : transfersList) {
            if (currentUserId == acc.findUser(Integer.toString(transactions.getAccountFrom())).getId()) {
                User to = acc.findUser(Integer.toString(transactions.getAccountTo()));
                if (transactions.getTransferAmount().compareTo(new BigDecimal(1000)) < 0) {
                    System.out.printf("%d:\t\tYou(%d)\t\t$%.2f\t\t%s(%d)\t\t%s\t\t%d\n", i, currentUserId,
                            transactions.getTransferAmount(),
                            to.getUsername(), to.getId(),
                            (transactions.getTransferStatusId() == 2) ? "Approved" : "Rejected",
                            transactions.getTransferId());
                } else {
                    System.out.printf("%d:\t\tYou(%d)\t\t$%.2f\t%s(%d)\t\t%s\t\t%d\n", i, currentUserId,
                            transactions.getTransferAmount(),
                            to.getUsername(), to.getId(),
                            (transactions.getTransferStatusId() == 2) ? "Approved" : "Rejected",
                            transactions.getTransferId());
                }
                i++;
            } else {
                User from = acc.findUser(Integer.toString(transactions.getAccountFrom()));
                if (transactions.getTransferAmount().compareTo(new BigDecimal(1000)) < 0) {
                    System.out.printf("%d:\t\t%s(%d)\t\t$%.2f\t\tYou(%d)\t\t%s\t\t%d\n", i,
                            from.getUsername(), from.getId(),
                            transactions.getTransferAmount(), currentUserId,
                            (transactions.getTransferStatusId() == 2) ? "Approved" : "Rejected",
                            transactions.getTransferId());
                } else {
                    System.out.printf("%d:\t\t%s(%d)\t\t$%.2f\tYou(%d)\t\t%s\t\t%d\n", i,
                            from.getUsername(), from.getId(),
                            transactions.getTransferAmount(), currentUserId,
                            (transactions.getTransferStatusId() == 2) ? "Approved" : "Rejected",
                            transactions.getTransferId());
                }
                i++;
            }
        }
    }

    public void printPendingRequests(int currentUserId) {
        //Goal of this function is print the pending requests to the console
        Transfer[] pendingList = acc.getPendingRequests(currentUserId);
        //Need to add logic to be able to get transfer status.
        System.out.println("\n\t\t\t\t\tTransfers Pending Approval");
        if (pendingList == null) {
            System.out.println("There are no pending approvals.");
            return;
        }
        System.out.println("#:\t\tRecipient:\t\tSender:\t\t\tAmount:\t\tStatus:\t\tID:");
        int i = 1;
        for (Transfer pending : pendingList) {
            User to = acc.findUser(Integer.toString(pending.getAccountTo()));
            User from = acc.findUser(Integer.toString(pending.getAccountFrom()));
            System.out.printf("%d:\t\t%s(%d)\t\t%s(%d)\t\t%.2f\t\t%s\t\t%d\n", i,
                    to.getId() == currentUserId ? "You" : to.getUsername(),
                    pending.getAccountTo(), from.getId() == currentUserId ? "You" : from.getUsername(),
                    pending.getAccountFrom(), pending.getTransferAmount(),
                    (pending.getTransferStatusId() == 1 ? "Pending"
                            : pending.getTransferStatusId() == 2 ? "Approved" : "Rejected"),
                    pending.getTransferId());
            i++;
        }
        promptToAcceptPendingRequests(currentUserId);
    }

    public void sendMoneyRequest(int currentUserId) {
        //Basic functionality : Pull current user ID. Prompt for user to send money to
        System.out.println("\n");
        printListOfUsers(currentUserId);
        int userTo = promptForInt("\nPlease enter the user_id you are sending money to: ");
        int userFrom = currentUserId;
        BigDecimal amount = promptForBigDecimal("How much would you like to send? ");
        acc.sendMoney(userTo, userFrom, amount);

    }

    public void requestMoneyFrom(int currentUserId) {
        System.out.println("\n");
        printListOfUsers(currentUserId);
        int userRequesting = currentUserId;
        int userFrom = promptForInt("Please enter the user_id you are requesting money from: ");
        BigDecimal amount = promptForBigDecimal("How much would you like to request? ");
        acc.requestMoney(userRequesting, userFrom, amount);

    }

    public void promptToAcceptPendingRequests(int currentUserId) {
        boolean hasOne = false;
        try {
            Transfer[] allForUser = acc.getTransfer(Integer.toString(currentUserId));
            for (Transfer t : allForUser) {
                if ((t.getAccountTo() == acc.getAccount(currentUserId).getAccountId() || t.getAccountFrom() == acc
                        .getAccount(currentUserId).getAccountId()) && t.getTransferStatusId() == 1) {
                    hasOne = true;
                    break;
                }
            }
            if (hasOne) {
                String grabTransfer = promptForString(
                        "Please select a transaction ID to approve or reject an incoming request. ");
                System.out.println();
                Transfer[] transfers = acc.getTransfer(grabTransfer);
                try {
                    Transfer transferToAcceptDecline = transfers[0];
                    String isYesNo = promptForString("Please accept, decline, or ignore the request for money (Y/N/I)");
                    acc.acceptPendingRequest(transferToAcceptDecline, isYesNo, currentUserId);
                } catch (NullPointerException n) {
                    System.out.println("No such transaction ID has been made.");
                }
            } else {
                System.out.println("\n\t\t\t\tThere are no transactions pending approval.");
                return;
            }
        } catch (NullPointerException e) {
            System.out.println("\n\t\t\t\tThere are no transactions pending approval.");
            return;
        }
    }
}
