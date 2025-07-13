
import javax.xml.transform.Result;
import java.lang.module.ResolutionException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

    private Connection con;
    private Scanner sc;


    AccountManager(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void debit_money(long account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter amount : ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter security pin : ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement st = con.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?");
                st.setLong(1, account_number);
                st.setString(2, security_pin);
                ResultSet rs = st.executeQuery();

                if (rs.next()) {
                    double current_balance = rs.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ? ";
                        PreparedStatement st2 = con.prepareStatement(debit_query);
                        st2.setDouble(1, amount);
                        st2.setLong(2, account_number);
                        int rowsAff = st2.executeUpdate();

                        if (rowsAff > 0) {
                            System.out.println("Rs." + amount + " debited successfully :)");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction failed !");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance !!");
                    }
                } else {
                    System.out.println("Invalid pin !");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }


    public void credit_money(long account_number) throws SQLException {

        sc.nextLine();
        System.out.print("Enter amount : ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter security pin : ");
        String security_pin = sc.nextLine();


        try {
            con.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement st = con.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?");
                st.setLong(1, account_number);
                st.setString(2, security_pin);
                ResultSet rs = st.executeQuery();

                if (rs.next()) {
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement st2 = con.prepareStatement(credit_query);
                    st2.setDouble(1, amount);
                    st2.setLong(2, account_number);
                    int rowsAff = st2.executeUpdate();
                    if (rowsAff > 0) {
                        System.out.println("Rs. " + amount + " credited successfully");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction failed !!");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Invalid security pin !");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void transfer_money(long sender_account_number) throws SQLException {

        sc.nextLine();
        System.out.print("Enter receiver account number : ");
        long receiver_account_number = sc.nextLong();
        System.out.print("Enter Amount : ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter security pin : ");
        String security_pin = sc.nextLine();
        try {
            con.setAutoCommit(false);
            if (sender_account_number != 0 && receiver_account_number != 0) {
                PreparedStatement st = con.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?");
                st.setLong(1, sender_account_number);
                st.setString(2, security_pin);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    double current_balance = rs.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

                        PreparedStatement creditSt = con.prepareStatement(credit_query);
                        PreparedStatement debitSt = con.prepareStatement(debit_query);

                        creditSt.setDouble(1, amount);
                        creditSt.setLong(2, receiver_account_number);
                        debitSt.setDouble(1, amount);
                        debitSt.setLong(2, sender_account_number);

                        int rowsAff1 = debitSt.executeUpdate();
                        int rowsAff2 = creditSt.executeUpdate();

                        if (rowsAff1 > 0 && rowsAff2 > 0) {
                            System.out.println("Transaction successful :)");
                            System.out.println("Rs." + amount + " Transferred successfully :)");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction failed !!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient balance !");
                    }
                } else {
                    System.out.println("Invalid security pin !");
                }
            } else {
                System.out.println("Invalid account number !!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        con.setAutoCommit(true);
    }

    public void getBalance(long account_number) {
        sc.nextLine();
        System.out.print("Enter security pin : ");
        String security_pin = sc.nextLine();
        try {
            PreparedStatement st = con.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
            st.setLong(1, account_number);
            st.setString(2, security_pin);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Balance : " + balance);
            } else {
                System.out.println("Invalid pin !");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}





