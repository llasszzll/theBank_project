package guis;

/*
* This gui will allow user to login or launch the register GUI
* */

import db_objects.MyJDBC;
import db_objects.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGui extends BaseFrame{
    public LoginGui() {
        super("Banking App Login");
    }


    @Override
    protected void addGuiComponents() {
        // create banking app label
        JLabel bankingAppLabel = new JLabel("Banking Application");

        // set location and size of Gui component
        bankingAppLabel.setBounds(0, 20, super.getWidth(), 40);

        // change font style
        bankingAppLabel.setFont(new Font("Dialog", Font.BOLD, 32));

        // center text in JLabel
        bankingAppLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add to Gui
        add(bankingAppLabel);

        // Username Label
        JLabel usernameLabel = new JLabel("Username: ");

        // getWidth() returns the width of frame
        usernameLabel.setBounds(20,120, getWidth() -30, 24);
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(usernameLabel);

        // create username field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(20, 160, getWidth() -50, 40);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(usernameField);

        // create password field
        JLabel passwordLabel = new JLabel("Password: ");

        passwordLabel.setBounds(20, 280, getWidth() - 50, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        // create password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 320, getWidth() - 50, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(passwordField);

        // create login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20, 460, getWidth() - 50, 40);
        loginButton.setFont(new Font("Dialog", Font.BOLD, 20));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get Username
                String username = usernameField.getText();

                // Get Password
                String password = String.valueOf(passwordField.getPassword());

                // Validate Login
                User user = MyJDBC.validateLogin(username, password);

                // if user is null, is invalid, otherwise valid account
                if(user != null) {
                    // valid login

                    // dispose GUI
                    LoginGui.this.dispose();

                    // Launch BANKING GUI
                    BankingAppGui bankingAppGui = new BankingAppGui(user);
                    bankingAppGui.setVisible(true);

                    // show success dialog
                    JOptionPane.showMessageDialog(bankingAppGui, "Login Successful");
                } else {
                    // invalid login
                    JOptionPane.showMessageDialog(LoginGui.this, "Login Failed");
                }
            }
        });
        add(loginButton);

        // Create Register label
        JLabel registerLabel = new JLabel("<html><a href=\"#\">Don't have an account? Register Here</a></html>");
        registerLabel.setBounds(0, 510, getWidth() - 10, 30);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // add listener when mouse is clicked, Bring Register Gui forward
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            // dispose of the gui
                LoginGui.this.dispose();

                //launch Register Gui
                new RegisterGui().setVisible(true);
            }
        });

        add(registerLabel);
    }
}
