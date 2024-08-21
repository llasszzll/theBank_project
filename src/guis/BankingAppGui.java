package guis;
import db_objects.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
*  Add banking functions such as:
*  - Deposit
*  - Withdraw
*  - Past Transactions
*  - Transfer funds
* */



public class BankingAppGui extends BaseFrame implements ActionListener {

    private JTextField currentBalanceField;
    public JTextField getCurrentBalanceField() {return currentBalanceField;};
    public BankingAppGui(User user) {
        super("Banking App", user);

    }

    @Override
    protected void addGuiComponents() {
        // create Welcome Message
        String welcomeMessage = "<html>" +
                "<body style='text-align:center'>" + "<b>Hello " + user.getUsername() + "</b><br>" +
                "What would you like to do today?</body></html>";

        JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
        welcomeMessageLabel.setBounds(0, 20, getWidth() - 10, 40);
        welcomeMessageLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        welcomeMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeMessageLabel);

        // create current balance label
        JLabel currentBalanceLabel = new JLabel("Current Balance");
        currentBalanceLabel.setBounds(0, 80, getWidth() - 10, 30);
        currentBalanceLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        currentBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(currentBalanceLabel);

        // create Current balance field
        currentBalanceField = new JTextField("R " + user.getCurrentBalance());
        currentBalanceField.setBounds(15, 120, getWidth() - 50, 40);
        currentBalanceField.setFont(new Font("Dialog", Font.BOLD, 28));
        currentBalanceField.setHorizontalAlignment(SwingConstants.RIGHT);
        currentBalanceField.setEditable(false);
        add(currentBalanceField);

        //deposit button
        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(15, 180, getWidth() - 50, 50);
        depositButton.setFont(new Font("Dialog", Font.BOLD, 22));
        // depositButton.setHorizontalAlignment(SwingConstants.CENTER);
        depositButton.addActionListener(this);
        add(depositButton);

        // withdraw button
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(15, 250, getWidth() - 50, 50);
        withdrawButton.setFont(new Font("Dialog", Font.BOLD, 22));
        withdrawButton.addActionListener(this);
        add(withdrawButton);

        // past transaction button
        JButton pastTransactionButton = new JButton("Previous Transactions");
        pastTransactionButton.setBounds(15, 320, getWidth() - 50, 50);
        pastTransactionButton.setFont(new Font("Dialog", Font.BOLD, 22));
        pastTransactionButton.addActionListener(this);
        add(pastTransactionButton);

        // Transfer Button
        JButton transferButton = new JButton("Transfer");
        transferButton.setBounds(15, 390, getWidth() - 50, 50);
        transferButton.setFont(new Font("Dialog", Font.BOLD, 22));
        transferButton.addActionListener(this);
        add(transferButton);

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(15, 500, getWidth() - 50, 50);
        logoutButton.setFont(new Font("Dialog", Font.BOLD, 22));
        logoutButton.addActionListener(this);
        add(logoutButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        // user pressed LOGOUT
        if(buttonPressed.equalsIgnoreCase("Logout")) {
            // return user to Login gui
            new LoginGui().setVisible(true);

            // dispose of gui
            this.dispose();

            // Don't run code any further
            return;
        }

        // other functions
        BankAppDialog bankAppDialog = new BankAppDialog(this, user);

        // set title in the dialog header to action
        bankAppDialog.setTitle(buttonPressed);

        // if button pressed is Deposit, Withdraw or Transfer (IF || OR)
        if(buttonPressed.equalsIgnoreCase("Deposit") || buttonPressed.equalsIgnoreCase("Withdraw")
        || buttonPressed.equalsIgnoreCase("Transfer")) {

            // Add Balance and Amount GUI components to the dialog
            bankAppDialog.addCurrentBalanceAmount();

            // Add Action Button
            bankAppDialog.addActionButton(buttonPressed);

            // Transfer action requires more components
            if(buttonPressed.equalsIgnoreCase("Transfer")) {
                bankAppDialog.addUserField();
            }

            bankAppDialog.setVisible(true);
        }else if(buttonPressed.equalsIgnoreCase("Past Transactions")) {
            bankAppDialog.addPastTransactionComponents();
        }

        // make app dialog visible
        bankAppDialog.setVisible(true);
    }
}
