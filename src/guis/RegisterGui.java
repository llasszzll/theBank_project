package guis;

import db_objects.MyJDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGui extends BaseFrame{

    public RegisterGui() {
        super("Banking App Register");
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
        passwordLabel.setBounds(20, 220, getWidth() - 50, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        // create password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 260, getWidth() - 50, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(passwordField);

        // re-type password label
        JLabel rePassword = new JLabel("Re-type password:");
        rePassword.setBounds(20, 320, getWidth() - 50, 40);
        rePassword.setFont(new Font("Dialog", Font.PLAIN, 18));
        add(rePassword);

        // create re-type password field
        JPasswordField rePasswordField = new JPasswordField();
        rePasswordField.setBounds(20, 360, getWidth() - 50, 40);
        rePasswordField.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(rePasswordField);

        // create register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(20, 460, getWidth() - 50, 40);
        registerButton.setFont(new Font("Dialog", Font.BOLD, 20));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get Username
                String username = usernameField.getText();

                // get Password
                String password = String.valueOf(passwordField.getPassword());
                // get ReType Password
                String rePassword = String.valueOf(rePasswordField.getPassword());

                // validate user input
                if(validateUserInput(username, password, rePassword)) {
                    // attempt to register user to DB
                    if(MyJDBC.register(username,password)) {
                        //register success
                        // dispose of gui
                        RegisterGui.this.dispose();

                        // launch Login Gui
                        LoginGui loginGui = new LoginGui();
                        loginGui.setVisible(true);

                        // create result dialog
                        JOptionPane.showMessageDialog(loginGui, "Registered Account Successfully!");
                    } else
                        // Register fails
                         {
                             JOptionPane.showMessageDialog(RegisterGui.this, "Error: Username Already Taken.");
                        }
                    } else {
                        // invalid user input
                        JOptionPane.showMessageDialog(RegisterGui.this,
                                "Error: Username must be at least 6 characters\n" + "and/or Password must match.");
                    }
                }

        });
        add(registerButton);

        // Create Login label
        JLabel loginLabel = new JLabel("<html><a href=\"#\">Have an account? Sign in here</a></html>");
        loginLabel.setBounds(0, 510, getWidth() - 10, 30);
        loginLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // dispose of GUI
                RegisterGui.this.dispose();

                // Launch Login GUI
                new LoginGui().setVisible(true);
            }
        });
        add(loginLabel);
    }

    private boolean validateUserInput(String username, String password, String rePassword) {
        // all fields must have value
        if (username.length() == 0 || password.length() == 0 || rePassword.length() == 0)
            return false;

        // username 6+ characters
        if (username.length() < 6) return false;

        // Password and RePassword must return the same
        if (!password.equals(rePassword)) return false;

        // passed Validation
        return true;
    }
}
