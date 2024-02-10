package com.jukebox.connectivity;


        import java.sql.Connection;
        import java.sql.DriverManager;

public class GetConnection {

    public static Connection connection;

    public static void createConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox", "root", "root");

            //System.out.println("Connection established successfully!");
        } catch (Exception exception) {
            System.out.println("Error establishing connection!"+exception);
        }
    }

    public static void main(String[] args) {
        GetConnection g1=new GetConnection();
        createConnection();
    }
}


