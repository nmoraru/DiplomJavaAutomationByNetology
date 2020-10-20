package ru.netology.web.data;

import lombok.Data;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.DriverManager;
import java.sql.SQLException;

@Data
public class SQLHelper {
    static String URL = "jdbc:mysql://localhost:3306/app";
    //static String URL = "jdbc:postgresql://localhost:5432/app";
    static String user = "app";
    static String password = "pass";


    public static void cleanData() {
        val runner = new QueryRunner();
        val creditPayment = "DELETE FROM credit_request_entity;";
        val order = "DELETE FROM order_entity;";
        val debitPayment = "DELETE FROM payment_entity;";

        try {
            val connection = DriverManager.getConnection(
                    URL, user, password);
            runner.update(connection, creditPayment);
            runner.update(connection, order);
            runner.update(connection, debitPayment);

        } catch (SQLException ex) {
            System.out.println("SQLException message:" + ex.getMessage());
        }
    }

    public static String searchOperationStatusByDebitCard() {
        val selectStatus = "SELECT status FROM payment_entity WHERE id is not null;";
        try {
            val connection = DriverManager.getConnection(
                    URL, user, password);
            val countStmt = connection.createStatement();
            val result = countStmt.executeQuery(selectStatus);

            if (result.next()) {
                val status = result.getString("status");
                return status;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException message:" + ex.getMessage());
        }
        return null;
    }

    public static String searchOperationStatusByCreditCard() {
        val selectStatus = "SELECT status FROM credit_request_entity WHERE id is not null;";
        try {
            val connection = DriverManager.getConnection(
                    URL, user, password);
            val countStmt = connection.createStatement();
            val result = countStmt.executeQuery(selectStatus);

            if (result.next()) {
                val status = result.getString("status");
                return status;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException message:" + ex.getMessage());
        }
        return null;
    }

    public static String searchOperationByOrderTableForDebitCard() {
        val selectPaymentId = "SELECT payment_id FROM order_entity WHERE id is not null;";
        try {
            val connection = DriverManager.getConnection(
                    URL, user, password);
            val countStmt = connection.createStatement();
            val result = countStmt.executeQuery(selectPaymentId);

            if (result.next()) {
                val paymentId = result.getString("payment_id");
                return paymentId;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException message:" + ex.getMessage());
        }
        return null;
    }

    public static String searchOperationByOrderTableForCreditCard() {
        val selectCreditId = "SELECT credit_id FROM order_entity WHERE id is not null;";
        try {
            val connection = DriverManager.getConnection(
                    URL, user, password);
            val countStmt = connection.createStatement();
            val result = countStmt.executeQuery(selectCreditId);

            if (result.next()) {
                val creditId = result.getString("credit_id");
                return creditId;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException message:" + ex.getMessage());
        }
        return null;
    }
}
