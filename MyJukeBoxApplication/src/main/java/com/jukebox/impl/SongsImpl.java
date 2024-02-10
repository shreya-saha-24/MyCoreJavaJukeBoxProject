package com.jukebox.impl;

import com.jukebox.connectivity.GetConnection;
import com.jukebox.exceptions.NoSongsFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SongsImpl {

    Scanner read = new Scanner(System.in);

    public SongsImpl() {
        GetConnection.createConnection();
    }

    public void showAvailableSongs() {
        try {
            Statement statement = GetConnection.connection.createStatement();

            String query = "SELECT * FROM songs";
            ResultSet resultSet = statement.executeQuery(query);

            // Print headers outside the loop
            System.out.printf("%-20s %-20s %-30s %-15s%n",
                    "Song Name", "Artist Name", "Album Name",
                    "Genre");
            System.out.println("------------------------------------------------------------------------------");
            while (resultSet.next()) {
                // Print data for each row
                System.out.printf("%-20s %-20s %-30s %-15s%n",
                        resultSet.getString("song_name"),
                        resultSet.getString("artist_name"),
                        resultSet.getString("album_name"),
                        resultSet.getString("genre"));
            }

        } catch (Exception e) {
            System.out.println("Error encountered in Implementation class!" + getClass());
        }
    }


    public List<String> searchSongByArtistName(String artistName) throws NoSongsFoundException {
        List<String> songNamesList = new ArrayList<>();
        try {
            String query = "SELECT song_name FROM songs WHERE artist_name = ?";
            PreparedStatement ps = GetConnection.connection.prepareStatement(query);
            ps.setString(1, artistName);

            ResultSet resultSet = ps.executeQuery();

            // The isAfterLast() method checks whether the cursor is positioned after the last row.
            // If the cursor is after the last row, it means there are no rows in the result set.
            if (resultSet.isAfterLast()) {
                throw new NoSongsFoundException("No songs found for the artist: " + artistName);
            } else {
                while (resultSet.next()) {
                    String songName = resultSet.getString("song_name");
                    songNamesList.add(songName);
                }
            }
        } catch (SQLException e) {
            // Handle SQL-related exceptions
            System.out.println("SQL Exception occurred while searching records by artist name: " + e);
        } catch (Exception e) {
            // Handle other exceptions
            System.out.println("Exception occurred while searching records by artist name: " + e);
        }
        return songNamesList;
    }


    public List<String> searchSongsBasedOnGenre(String genre) throws NoSongsFoundException{

        List<String> songsBasedOnGenre = new ArrayList<>();
        try {
            String query = "SELECT song_name FROM songs WHERE genre = ?";
            PreparedStatement ps = GetConnection.connection.prepareStatement(query);
            ps.setString(1, genre);

            ResultSet resultSet = ps.executeQuery();


            //The isAfterLast() method checks whether the cursor is positioned after the last row.
            // If the cursor is after the last row, it means there are no rows in the result set.

            if (resultSet.isAfterLast()) {
                throw new NoSongsFoundException("No songs found for the genre");
            } else {
                while (resultSet.next()) {
                    String songName = resultSet.getString("song_name");
                    songsBasedOnGenre.add(songName);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred while searching records by genre: " + e);
        }
        return songsBasedOnGenre;
    }

    public List<String> searchSongsBasedOnAlbum(String album) throws NoSongsFoundException{

        List<String> songsBasedOnAlbum = new ArrayList<>();
        try {
            String query = "SELECT song_name FROM songs WHERE album_name = ?";
            PreparedStatement ps = GetConnection.connection.prepareStatement(query);
            ps.setString(1, album);

            ResultSet resultSet = ps.executeQuery();

            //The isAfterLast() method checks whether the cursor is positioned after the last row.
            // If the cursor is after the last row, it means there are no rows in the result set.

            if (resultSet.isAfterLast()) {
                throw new NoSongsFoundException("No song found for the album!");
            } else {
                while (resultSet.next()) {
                    String songName = resultSet.getString("song_name");
                    songsBasedOnAlbum.add(songName);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred while searching records by album: " + e);
        }
        return songsBasedOnAlbum;
    }

    public Map<Integer, String> fetchSongNames() {

        Map<Integer, String> listOfSongs = new HashMap<>();
        try {
            Statement statement = GetConnection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT song_name, song_id FROM songs");

            while (resultSet.next()) {
                Integer songId = resultSet.getInt("song_id");
                String song = resultSet.getString("song_name");

                listOfSongs.put(songId, song);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return listOfSongs;
    }

    public Map<String, Integer> createPlaylist(int userId) {
        Scanner read = new Scanner(System.in);

        Map<String, Integer> map = new HashMap<>();

        int rowAffected = 0;

        //System.out.printf("%-10s %-20s %-20s %-20s %-15s %-15s %-10s %-30s%n",
        //                        "Song ID", "Song Name", "Artist Name", "Album Name",
        //                        "Release Year", "Genre", "Duration", "File Path");


        System.out.printf("%-50s%n" ,"Please provide a playlist name: ");
        String playlistName = read.nextLine();

        try {
            String checkIfUserHasCreatedPlaylist = "SELECT playlist_id from playlist where user_id=? AND playlist_name=?";
            PreparedStatement ps1 = GetConnection.connection.prepareStatement(checkIfUserHasCreatedPlaylist);
            ps1.setInt(1, userId);
            ps1.setString(2, playlistName);
            ResultSet resultSet = ps1.executeQuery();


            // Check if the user has already created a playlist
            if (resultSet.next()) {
                System.out.println("You already have a playlist by that name.");
                return map; // or handle the case as per your requirements
            }


            // User hasn't created a playlist, proceed to insert
            String query = "INSERT INTO playlist (user_id, playlist_name) VALUES (?, ?)";
            PreparedStatement ps2 = GetConnection.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps2.setInt(1, userId);
            ps2.setString(2, playlistName);

            rowAffected = ps2.executeUpdate();

            if (rowAffected > 0) {
                ResultSet generatedKeys = ps2.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int playListId = generatedKeys.getInt(1);
                    map.put(playlistName, playListId);
                }

            }
        } catch (Exception e) {
            System.out.println("Error creating the playlist." + e);
        }
        return map;
    }

    public Map<Integer, Integer> addSongsToPlaylist(int userId, int playListId) {
        Map<Integer, String> availableSongs = fetchSongNames();
        List<Integer> songIds = new ArrayList<>();
        int songIdFromList;
        Scanner read = new Scanner(System.in);

        System.out.println("Playlist id: "+ playListId);


        System.out.println("Here are the songs we have: ");
        System.out.printf("%-15s %-20s%n", "Song Id", "Song Name");

        for (Map.Entry<Integer, String> entry : availableSongs.entrySet()) {
            System.out.printf("%-15s %-20s%n", entry.getKey(), entry.getValue());
        }

        System.out.println("Select song id's from the list to add songs to playlist. Type -1 to exit.");

        while (true) {
            songIdFromList = read.nextInt();
            if (songIdFromList == -1) {
                break;
            } else {
                songIds.add(songIdFromList);
            }
        }

        Map<Integer, Integer> map = new HashMap<>();
        int rowAffected;
        int individualSongId;

        try {
            for (int i = 0; i < songIds.size(); i++) {
                individualSongId = songIds.get(i);
                String query = "INSERT INTO playlist_song (playlist_id, song_id) VALUES (?, ?)";

                System.out.println(" ");
                System.out.println(" ");
                //System.out.println("From addSongsToPlaylist() method. Playlist id: "+ playListId+" song id: "+ individualSongId);

                System.out.println(" ");
                System.out.println(" ");

                PreparedStatement ps = GetConnection.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                ps.setInt(1, playListId);
                ps.setInt(2, individualSongId);

                rowAffected = ps.executeUpdate();

                if (rowAffected > 0) {
                    map.put(playListId, individualSongId);
                }

                else {
                    System.out.println("You already have that song in your playlist!");
                    return map;
                }
            }

            // After successfully adding songs, ask if the user wants to view the playlist
            System.out.println("Do you want to view the songs in the playlist? Type yes or no");
            String flag = read.next();

            if (flag.equalsIgnoreCase("yes")) {
                viewSongsInPlaylist(playListId);
            }
        }
        catch (Exception e) {
            System.out.println("You already have that song in your playlist!" + e);
        }


        return map;
    }

    // Separate method to view songs in a playlist
    private void viewSongsInPlaylist(int playListId) {
        List<String> listOfSongsFromPlaylist = getSongsInAPlaylist(playListId);

        if (!listOfSongsFromPlaylist.isEmpty()) {
            System.out.println("Here are the songs for the playlist: ");

            System.out.println(" ");

            System.out.printf("%-20s%n", "Song");
            System.out.println("----------");
            for (String song : listOfSongsFromPlaylist) {
                System.out.printf("%-20s%n", song);
            }

            System.out.println(" ");
        } else {
            System.out.println("No songs in the playlist yet.");
        }
    }

    public Map<Integer, String> getUserPlaylists(int updatedUserId) {
        Map<Integer, String> userPlayListInfo = new HashMap<>();
        try {
            String query = "SELECT playlist_id, playlist_name from playlist where user_id=?";
            PreparedStatement ps1 = GetConnection.connection.prepareStatement(query);
            ps1.setInt(1, updatedUserId);

            ResultSet resultSet = ps1.executeQuery();

            while(resultSet.next()) {
                Integer playlistId = resultSet.getInt("playlist_id");
                String playlistName = resultSet.getString("playlist_name");

                userPlayListInfo.put(playlistId, playlistName);
            }

        } catch (Exception e) {
            System.out.println("Error fetching user playlist. Possible Cause: "+ e);
        }

        return userPlayListInfo;
    }


    //FETCHES SONGS FOR THE SELECTED PLAYLIST ID.
    public List<String> getSongsInAPlaylist(int playlistId) {
        List<String> songListFromPlaylist = new ArrayList<>();

        try {
            String query = "SELECT s.song_name FROM songs s " +
                    "JOIN playlist_song ps ON s.song_id = ps.song_id " +
                    "WHERE ps.playlist_id = ?";

            PreparedStatement ps = GetConnection.connection.prepareStatement(query);
            ps.setInt(1, playlistId);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String songName = resultSet.getString("song_name");
                songListFromPlaylist.add(songName);
            }

            return songListFromPlaylist;

        } catch (Exception e) {
            System.out.println("Error occurred in viewing playlist."+ e);
        }

        return songListFromPlaylist;
    }


    //GETS ME THE SONGS IN A PLAYLIST FOR AN EXISTING USER/ON THE BASIS OF USER_ID.
    public List<String> viewListOfSongsForAPlaylist(int userId) {

        List<String> songListFromPlaylist = new ArrayList<>();

        int selectedPlaylistId;

        //System.out.println("Your userId in the viewPlayList method is: "+ userId);

        try {

            Map<Integer, String> userCreatedPlaylist = getUserPlaylists(userId);
            System.out.println("Here are the playlist's/ playlist u have created.");

            System.out.printf("%-15s %-20s%n", "Playlist Id", "Playlist Name");
            for (Map.Entry<Integer, String> entry: userCreatedPlaylist.entrySet()) {

                System.out.printf("%-15s %-20s%n", entry.getKey(), entry.getValue());
            }

            System.out.println(" ");
            System.out.println("Select the playlist ID");
            selectedPlaylistId = read.nextInt();

            //String nameOfThePlaylist = userCreatedPlaylist.get(selectedPlaylistId);

            //System.out.println("You have selected: "+ nameOfThePlaylist);

            String query = "SELECT s.song_name FROM songs s " +
                    "JOIN playlist_song ps ON s.song_id = ps.song_id " +
                    "WHERE ps.playlist_id = ?";

            PreparedStatement ps = GetConnection.connection.prepareStatement(query);
            ps.setInt(1, selectedPlaylistId);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String songName = resultSet.getString("song_name");
                songListFromPlaylist.add(songName);
            }

            return songListFromPlaylist;

        } catch (Exception e) {
            System.out.println("Error occurred in viewing playlist."+ e);
        }
        return songListFromPlaylist;
    }

    public Map<String, String> fetchSongPath(int songId) {

        Map<String, String> songDetails = new HashMap<>();

        String filePath="";
        try {
            String query = "SELECT song_name, artist_name, file_path from songs where song_id=?";
            PreparedStatement ps = GetConnection.connection.prepareStatement(query);
            ps.setInt(1, songId);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                String artistName = resultSet.getString("artist_name");
                String songName = resultSet.getString("song_name");

                String songMetaData = songName+" by "+artistName;
                filePath = resultSet.getString("file_path");

                songDetails.put(songMetaData, filePath);

                return songDetails;
            }

        } catch (Exception e) {
            System.out.println("Exception occured in fetching song path. "+e);
        }
        return songDetails;
    }

    public int getPlaylistIdOfAnExistingUser(int userId) {

        int playListIdForAUser=0;
        try {
            String query = "SELECT playlist_id from playlist where user_id=?";
            PreparedStatement ps = GetConnection.connection.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                playListIdForAUser = resultSet.getInt("playlist_id");
            }

        } catch (Exception e) {
            System.out.println("Error occurred in getPlaylistIdOfAnExistingUser() method. "+e);
        }
        return playListIdForAUser;
    }
}


