package com.jukebox.impl;

import com.jukebox.connectivity.GetConnection;
import com.jukebox.entity.User;
import com.jukebox.menu.Menu;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class UserImpl {

    Menu menu = new Menu();

    public UserImpl () {
        GetConnection.createConnection();
    }
    public Map<String,Integer> logInUser(User user) {

        String loggedInUser = "";

        int user_id = 0;

        Map<String, Integer> map = new HashMap<>();

        try {
            String query = "SELECT user_id, first_name FROM user where user_name = ? AND password = ?";
            PreparedStatement ps = GetConnection.connection.prepareStatement(query);
            ps.setString(1, user.getUser_name());
            ps.setString(2, user.getPassword());

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                user_id = resultSet.getInt("user_id");
                //System.out.println("Logged In User ID: " + user_id);
                loggedInUser = resultSet.getString("first_name");

                map.put(loggedInUser, user_id);

                System.out.println(resultSet.getString("first_name"));

            } else {
                System.out.println("User not found. Please check your username/password or consider Signing up");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, Integer> registerUser(User user) throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        int user_id = 0;

        Map<String, Integer> map = new HashMap<>();

        int rowAffected = 0;
        try {
            String query = "INSERT INTO user (first_name, last_name, user_name, password) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = GetConnection.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFirst_name());
            ps.setString(2, user.getLast_name());

            ps.setString(3, user.getUser_name());

            ps.setString(4, user.getPassword());

            rowAffected = ps.executeUpdate();

            if (rowAffected > 0) {

                ResultSet generatedKeys = ps.getGeneratedKeys();

                if (generatedKeys.next()) {
                    user_id = generatedKeys.getInt(1);
                    //System.out.println("User ID: " + user_id);
                }
                map.put(user.getFirst_name(), user_id);
                return map;
            }

        } catch (Exception e) {

            System.out.println("User already exist by the username.Please consider logging in");

        } finally {
            menu.startApplication();
        }
        return map;
    }
}
