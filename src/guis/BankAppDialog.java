package guis;

/*
* Display customer dialog for BankAppGui
* */


import db_objects.MyJDBC;
import db_objects.Transactions;
import db_objects.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

public class BankAppDialog extends JDialog implements ActionListener {
    private User user;
    private BankingAppGui bankingAppGui;
    private JLabel balanceLabel, enterAmountLabel, enterUserLabel;
    private JTextField enterAmountField, enterUserField;
    private JButton actionButton;
    private JPanel pastTransactionPanel;
    private ArrayList <Transactions> pastTransactions;


    public BankAppDialog(BankingAppGui bankingAppGui, User user) {
        // set Size
        setSize(400, 400);

        // add focus to dialog(limit user interaction until dialog closed)
        setModal(true);

        // loads in the center of banking gui
        setLocationRelativeTo(bankingAppGui);

        // user closes dialog, release resource being used
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // prevent dialog resize
        setResizable(false);

        // manually specify size and position of each component
        setLayout(null);

        // reference to gui to update the current balance
        this.bankingAppGui = bankingAppGui;

        // access user info to update DB
        this.user = user;
    }

    public void addCurrentBalanceAmount() {
        // Balance Label
        balanceLabel = new JLabel("Balance: R " + user.getCurrentBalance());
        balanceLabel.setBounds(0, 10, getWidth() - 20, 20);
        balanceLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        // Enter Amount Label
        enterAmountLabel = new JLabel("Enter Amount:");
        enterAmountLabel.setBounds(0, 50, getWidth() - 20, 20);
        enterAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountLabel);

        // Enter Amount Field
        enterAmountField = new JTextField();
        enterAmountField.setBounds(15, 80, getWidth() - 50, 40);
        enterAmountField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterAmountField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountField);
    }

    public void addActionButton(String actionButtonType) {
        actionButton = new JButton(actionButtonType);
        actionButton.setBounds(15, 300, getWidth() - 50, 40);
        actionButton.setFont(new Font("Dialog", Font.BOLD, 20));
        actionButton.addActionListener(this);
        add(actionButton);
    }

    public void addUserField() {
        // enter user label
        enterUserLabel = new JLabel("Enter Account Username:");
        enterUserLabel.setBounds(0, 160, getWidth() - 20, 20);
        enterUserLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserLabel);

        // enter user field
        enterUserField = new JTextField();
        enterUserField.setBounds(15, 190, getWidth() - 50, 40);
        enterUserField.setFont(new Font("Dialog", Font.BOLD, 20));
        //enterUserField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserField);
    }

    public void addPastTransactionComponents() {
        // container to store each transaction
    pastTransactionPanel = new JPanel();

        // layout 1x1
        pastTransactionPanel.setLayout(new BoxLayout(pastTransactionPanel, BoxLayout.Y_AXIS));

        // add scrolling to container
        JScrollPane scrollPane = new JScrollPane(pastTransactionPanel);

        // display vertical scroll only when required
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 20, getWidth() - 15, getHeight() - 15);

        // DB call to retrieve past transactions and store into array list
        pastTransactions = MyJDBC.getPastTransaction(user);

        // iterate through the list and add to GUI
        for(int i = 0; i < pastTransactions.size(); i++){
            // store current transaction
            Transactions pastTransaction = pastTransactions.get(i);

            // create a container to store an individual transaction
            JPanel pastTransactionContainer = new JPanel();
            pastTransactionContainer.setLayout(new BorderLayout());

            // create transaction type label
            JLabel transactionTypeLabel = new JLabel(pastTransaction.getTransactionType());
            transactionTypeLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // create transaction amount label
            JLabel transactionAmountLabel = new JLabel(String.valueOf(pastTransaction.getTransactionAmount()));
            transactionAmountLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // create transaction date label
            JLabel transactionDateLabel = new JLabel(String.valueOf(pastTransaction.getTransactionDate()));
            transactionDateLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // add to the container
            pastTransactionContainer.add(transactionTypeLabel, BorderLayout.WEST); // place this on the west side
            pastTransactionContainer.add(transactionAmountLabel, BorderLayout.EAST); // place this on the east side
            pastTransactionContainer.add(transactionDateLabel, BorderLayout.SOUTH); // place this on the south side

            // give a white background to each container
            pastTransactionContainer.setBackground(Color.WHITE);

            // give a black border to each transaction container
            pastTransactionContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // add transaction component to the transaction panel
            pastTransactionPanel.add(pastTransactionContainer);
        }

        // add to the dialog
        add(scrollPane);
    }

    private void handleTransaction(String transactionType, float amountVal) {
        Transactions transaction;

        if(transactionType.equalsIgnoreCase("Deposit")) {
            // deposit transaction type
            // add to current balance
            user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(amountVal)));

            // create Transaction
            // leave DATE null. Using NOW() in SQL to get present date
            transaction = new Transactions(user.getId(), transactionType, new BigDecimal(amountVal), null);
        } else {
            // withdraw transaction type
            // subtract from current balance
            user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amountVal)));

            // add negative sign for when withdrawing amount
            transaction = new Transactions(user.getId(), transactionType, new BigDecimal(-amountVal), null);

        }
        // update DB
        if(MyJDBC.addTransactionToDatabase(transaction) && MyJDBC.updateCurrentBalance(user)) {
            // show success dialog
            JOptionPane.showMessageDialog(this, transactionType + " Successful!");

            // reset the fields
            resetFieldsAndUpdateCurrentBalance();
        } else {
            // show failed dialog
            JOptionPane.showMessageDialog(this, transactionType + " Failed...");
        }
    }

            private void resetFieldsAndUpdateCurrentBalance() {
            // reset fields
                enterAmountField.setText("");

                if(enterUserField != null) {
                    enterAmountField.setText("");
                }
                // update current balance on dialog
                balanceLabel.setText("Balance: R" + user.getCurrentBalance());

                // update current balance on Main gui
                bankingAppGui.getCurrentBalanceField().setText("R" + user.getCurrentBalance());
            }

            private void handleTransfer(User user, String transferredUser, float amount) {
            // attempt to perform transfer
                if(MyJDBC.transfer(user, transferredUser, amount)) {
                    // show success dialog
                    JOptionPane.showMessageDialog(this, "Transfer Success!");
                    resetFieldsAndUpdateCurrentBalance();
                }else {
                    // show failure dialog
                    JOptionPane.showMessageDialog(this, "Transfer has failed...");
                }
            }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        // Get Value Amount
        float amountVal = Float.parseFloat(enterAmountField.getText());

        // Client clicked Deposit
        if(buttonPressed.equalsIgnoreCase("Deposit")) {
            // Handle transaction deposit
            handleTransaction(buttonPressed, amountVal);
        } else {
            // pressed withdraw or transfer

            // validate input by ensuring that withdraw or transfer amount is less than current_balance
            // if result -1 it means entered amount is more, 0 means they are equal and 1 means
            // the entered amount is less
            int result = user.getCurrentBalance().compareTo(BigDecimal.valueOf(amountVal));
            if(result < 0) {
                // display error dialog
                JOptionPane.showMessageDialog(this, "Error: Input value is more then current value");
                return;
            }

            // check if withdraw of transfer was clicked
            if(buttonPressed.equalsIgnoreCase("Withdraw")) {
                handleTransaction(buttonPressed, amountVal);
            } else {
                //transfer
                String transferredUser = enterUserField.getText();

                // handle transfer
                handleTransfer(user, transferredUser, amountVal);
            }
        }
    }
}
