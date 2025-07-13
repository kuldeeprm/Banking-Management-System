
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private Connection con;
    private Scanner sc;


    User(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void register() {
        sc.nextLine();
        System.out.print("Full Name : ");
        String full_name = sc.nextLine();
        System.out.print("Email : ");
        String email = sc.nextLine();
        System.out.print("Password : ");
        String password = sc.nextLine();

        if (user_exist(email)) {
            System.out.println("User already exists for this email address !!");
            return;
        }
        String register_query = "INSERT INTO users(full_name,email,password) VALUES (?,?,?)";

        try {
            PreparedStatement st = con.prepareStatement(register_query);
            st.setString(1, full_name);
            st.setString(2, email);
            st.setString(3, password);
            int affRows = st.executeUpdate();
            if (affRows > 0) {
                System.out.println("Registration Successful :)");
            } else {
                System.out.println("Registration failed !!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String login(){
        sc.nextLine();
        System.out.print("Email : ");
        String email = sc.nextLine();
        System.out.print("Password : ");
        String password = sc.nextLine();
        String login_query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try{
            PreparedStatement st = con.prepareStatement(login_query);
            st.setString(1,email);
            st.setString(2,password);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return email;
            }else{
                return null;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean user_exist(String email){
        String query = "SELECT * FROM users WHERE email = ?";
        try{
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1,email);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return true;
            }else {
                return false;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
