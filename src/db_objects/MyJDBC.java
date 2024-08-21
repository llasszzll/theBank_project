package db_objects;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

/*
*
* JDBC class used to interact with MySQL database
* Retrieving and updating
* */

public class MyJDBC {
    // database configuration
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/bank_app";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Lategansql18";

    // if valid, return object with user information
    public static User validateLogin(String username, String password) {

        // make connection to database using configuration
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            // create SQL query

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?"
            );

            // parameter index referring to iteration of the ? so 1 is the first ? and 2 is the second
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // execute query and store into result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // next() returns true or false
            // true - query returned data and result set now points to the first row
            // false - query returned no data, result set equals to null
            if(resultSet.next()) {
            // success
                // get id
                int userId = resultSet.getInt("id");

                // get balance
                BigDecimal currentBalance = resultSet.getBigDecimal("current_balance");

                // return user object
                return new User(userId, username, password, currentBalance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // not valid user
        return null;
    }

    public static boolean register(String username, String password) {
        try {
            // first check if username exists

            if(!checkUser(username)) {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO users(username, password, current_balance) " +
                                "VALUES(?,?,?)"
                );

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setBigDecimal(3, new BigDecimal(0));

                preparedStatement.executeUpdate();
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // check if username exists in DB
    // true - user exists
    // false - user does not exist
    private static boolean checkUser(String username) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            // if query returns no data, username is available
            if(!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    // true -  update to DB success
    // false - update to DB failed
    public static boolean addTransactionToDatabase(Transactions transaction) {
        try {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        PreparedStatement insertTransaction = connection.prepareStatement(
                "INSERT transactions(user_id, transaction_type, transaction_amount, transaction_date) " +
                        "VALUES(?, ?, ?, NOW())"
        );

            // NOW() to put in current data
            insertTransaction.setInt(1, transaction.getUserId());
            insertTransaction.setString(2, transaction.getTransactionType());
            insertTransaction.setBigDecimal(3, transaction.getTransactionAmount());

            // update database
            insertTransaction.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // true - update balance successful
    // false - update balance fail
    public static boolean updateCurrentBalance (User user) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement updateBalance = connection.prepareStatement(
                    "UPDATE users SET current_balance = ? WHERE id = ?"
            );

            updateBalance.setBigDecimal(1, user.getCurrentBalance());
            updateBalance.setInt(2, user.getId());

            updateBalance.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // true - transfer was success
    // false - transfer has failed
    public static boolean transfer(User user, String transferredUsername, float transferAmount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement queryUser = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );

            queryUser.setString(1, transferredUsername);
            ResultSet resultSet = queryUser.executeQuery();

            while (resultSet.next()) {
                // perform transfer
                User transferredUser = new User(
                        resultSet.getInt("id"),
                        transferredUsername,
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance")
                );

                // create Transaction
                Transactions transferTransaction= new Transactions(
                        user.getId(),
                        "Transfer",
                        new BigDecimal(-transferAmount),
                        null
                );
                // transaction will belong to transferred user
                Transactions receivedTransaction = new Transactions(
                        transferredUser.getId(),
                        "Transfer",
                        new BigDecimal(transferAmount),
                        null
                );

                // update transfer user
                transferredUser.setCurrentBalance(transferredUser.getCurrentBalance().add(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(transferredUser);

                // update user current balance
                user.setCurrentBalance(user.getCurrentBalance().subtract(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(user);

                // add transaction to DB
                addTransactionToDatabase(transferTransaction);
                addTransactionToDatabase(receivedTransaction);

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // get transactions (past transactions)
    public static ArrayList<Transactions> getPastTransaction (User user) {
        ArrayList<Transactions> pastTransactions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement selectAllTransaction = connection.prepareStatement(
                    "SELECT * FROM transactions WHERE user_id = ?"
            );
            selectAllTransaction.setInt(1, user.getId());

            ResultSet resultSet = selectAllTransaction.executeQuery();

            // iterate through result
            while (resultSet.next()) {
                // create transaction object
                Transactions transaction = new Transactions(
                        user.getId(),
                        resultSet.getString("transaction_type"),
                        resultSet.getBigDecimal("transaction_amount"),
                        resultSet.getDate("transaction_date")
                );

                // store into array list
                pastTransactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pastTransactions;
    }
}

// Connection connection = DriverManager.getConnection(
//              "jdbc:mysql://127.0.0.1:3306/bank_app",
//                    "root",
//                    "Lategansql18"
