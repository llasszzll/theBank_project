package guis;

import db_objects.User;

import javax.swing.*;

/*
* Creating abstract class assists with setup of the blueprint that GUI's will follow
* */

public abstract class BaseFrame extends JFrame {

    // Store user info
    protected User user;

    public BaseFrame(String title) { initialize(title); }
    public BaseFrame(String title, User user) {
        // initialize user
        this.user = user;

        initialize(title);
    }

    private void initialize(String title) {
        // instantiate JFrame properties, Add title, Add title bar

        setTitle(title);

        // Size set in Pixels
        setSize(400, 600);

        // Close program
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set layout to NULL to have absolute layout
        // that allows to manually specify the size and position of each gui component
        setLayout(null);

        // prevent GUI resize
        setResizable(false);

        // center of screen launch
        setLocationRelativeTo(null);

        // call subclass addGuiComponent()

        addGuiComponents();

    }

    // Method will need to be defined by subclasses when this class is being inherited from
    protected abstract void addGuiComponents();
}
