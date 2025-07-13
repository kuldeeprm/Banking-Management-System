
import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection con;
    private Scanner sc;

    public Accounts(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public long open_account(String email) {
        if (!account_exist(email)) {
            String open_account_query = "INSERT INTO Accounts(account_number,full_name,email,balance,security_pin)VALUES( ?, ?, ?, ?, ?)";
            sc.nextLine();
            System.out.print("Enter full name : ");
            String full_name = sc.nextLine();
            System.out.print("Enter initial amount : ");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter security pin : ");
            String security_pin = sc.nextLine();

            try{
                long account_number = generateAccountNumber();
                PreparedStatement st = con.prepareStatement(open_account_query);
                st.setLong(1,account_number);
                st.setString(2,full_name);
                st.setString(3,email);
                st.setDouble(4,balance);
                st.setString(5,security_pin);
                int rowsAff = st.executeUpdate();
                if(rowsAff > 0){
                    return account_number;
                }else{
                    throw new RuntimeException("Account creation failed !!");
                }

            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        throw new RuntimeException("Account already exists");

    }

    public long getAccount_number(String email){
        String query = "SELECT account_number FROM Accounts WHERE email = ?";
        try{
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1,email);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getLong("account_number");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account number doesn't exists !!!");
    }


    private long generateAccountNumber(){
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT account_number FROM Accounts ORDER BY account_number DESC LIMIT 1");
            if(rs.next()){
                long last_account_number = rs.getLong("account_number");
                return last_account_number + 1;
            }else{
                return 10000100;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 10000100 ;
    }


    public boolean account_exist(String email){
        String query = "SELECT * FROM Accounts WHERE email = ?";
        try{
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1,email);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

}
