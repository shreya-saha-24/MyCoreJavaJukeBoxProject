package com.jukebox.menu;

import com.jukebox.entity.User;
import com.jukebox.exceptions.NoSongsFoundException;
import com.jukebox.impl.PlaySongImpl;
import com.jukebox.impl.SongsImpl;
import com.jukebox.impl.UserImpl;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.exit;

public class Menu {

    int choice;
    Scanner read = new Scanner(System.in);
    //boolean goBack = false;

    User userLogin;


    int userId;

    //String responses;
    Map<String, Integer> getUserDetails = new HashMap<>();

    int playListId;

    String playlistName;
    public void startApplication() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        int registrationChoice;
        String username;
        String firstname;

        String lastname;
        String password;

        System.out.println("Welcome to Juke Box \uD83C\uDFB8");

        System.out.println("Login. Press 1");
        System.out.println("Sign Up. Press 2");
        registrationChoice = read.nextInt();

        //read.nextLine();

        switch (registrationChoice) {
            case 1:
                System.out.println("User name");
                username = read.next();
                System.out.println("Password");
                password = read.next();

                userLogin = new User(username, password);
                //userId = userLogin.getUser_id();

                UserImpl userImplLogin = new UserImpl();

                //String loggedInUser = userImplLogin.logInUser(userLogin);
                getUserDetails = userImplLogin.logInUser(userLogin);

//                if (!loggedInUser.isEmpty()) {
                if(!getUserDetails.isEmpty()) {

                    Map.Entry<String, Integer> firstEntry = getUserDetails.entrySet().iterator().next();

                    userId = firstEntry.getValue();
                    System.out.print("Your user id is:"+ userId);
                    System.out.println(" ");
                    System.out.println("Welcome Back " + firstEntry.getKey());

                    System.out.println(" ");
                    displayMenu();
                } else {
//                    System.out.println("You have not registered! Please go back and register!");
                    System.out.println(" ");
                    startApplication();
                }
                break;

            case 2:
                System.out.println("First name");
                firstname = read.next();
                System.out.println("Last name: ");
                lastname = read.next();

                System.out.println("User name: ");
                username = read.next();

                System.out.println("Password: ");
                password = read.next();

                User userRegistration = new User(firstname, lastname, username, password);
                //userId = userRegistration.getUser_id();

                UserImpl userImplRegister = new UserImpl();

                getUserDetails = userImplRegister.registerUser(userRegistration);

                if (!getUserDetails.isEmpty()) {

                    Map.Entry<String, Integer> firstEntry = getUserDetails.entrySet().iterator().next();
                    System.out.println("Welcome to Jukebox " + firstEntry.getKey() + "!");

                    userId = firstEntry.getValue();
                    System.out.print("Your user id is:"+ userId);

                    System.out.println(" ");
                    //System.out.println("Logged in successfully.");
                    displayMenu();
                }
                break;
        }
    }


    public void displayMenu() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        while (true) {
            System.out.println("*************MAIN MENU**************");
            System.out.println("Welcome to Juke Box \uD83C\uDFB8");


            System.out.println("1. Search songs");
            System.out.println("2. Show all available songs");
            System.out.println("3. Play songs");
            System.out.println("4. Create a playlist");
            System.out.println("5. Add songs to playlist");
            System.out.println("6. View songs in a Playlist");
            System.out.println("7. Exit");

            System.out.println("Enter your choice:");
            choice = read.nextInt();
            //read.nextLine();

            switch (choice) {
                case 1:
                    searchSongs();
                    break;
                case 2:
                    showAllSongs();
                    break;
                case 3:
                    playSong();
                    break;

                case 4:
                    //System.out.println("For cross verification: "+ userId);
                    createAPlaylist(userId);
                    break;
                case 5:
                    addSongToPlaylist();
                    break;

                case 6:
                    viewListOfSongsForAPlaylist(userId);
                    break;

                case 7:
                    System.out.println("See you soon!");
                    exit(0);

                default:
                    System.out.println("Please select a valid option!");
                    System.out.println(" ");
            }
        }
    }

    private void showAllSongs() {
        SongsImpl songsImpl = new SongsImpl();
        songsImpl.showAvailableSongs();
    }

    private void searchSongs() {

        //System.out.println("\uD83D\uDD0D");

        //System.out.printf("%50s", "\uD83D\uDD0D");
        int searchByCategory;

        String artistName;
        String genre;
        String album;
        SongsImpl songsImpl = new SongsImpl();

        try {
            System.out.println(" ");
            System.out.printf("%80s", "1. Search by artist");
            System.out.println(" ");
            System.out.printf("%80s", "2. Search by Genre");
            System.out.println(" ");
            System.out.printf("%80s", "3. Search by Album");
            System.out.println(" ");

            System.out.println("Enter your choice: ");
            searchByCategory = read.nextInt();

            //Doing, so empties the input buffer.
            read.nextLine();

            switch (searchByCategory) {
                case 1:
                    System.out.println("Enter the name of the artist.");
                    artistName = read.nextLine();

                    try {
                        // calling code
                        List<String> songName = songsImpl.searchSongByArtistName(artistName);

                        // process the result
                        if (songName.isEmpty()) {
                            System.out.println("No songs available for the artist: " + artistName);
                        } else {
                            System.out.println("Here is what we found!");
                            for (String song : songName) {
                                System.out.println(song + " ");
                            }
                            System.out.println(" ");
                        }
                    } catch (NoSongsFoundException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e);
                    }

                    break;
                case 2:
                    System.out.println("Enter genre:");
                    genre = read.nextLine();

                    try {
                        List<String> songsBasedOnGenre = songsImpl.searchSongsBasedOnGenre(genre);

                        if (songsBasedOnGenre.isEmpty()) {
                            System.out.println("No songs available for the genre: " + genre);
                        } else {
                            System.out.println("Here is what we found!");
                            for (String song : songsBasedOnGenre) {
                                System.out.println(song + " ");
                            }
                            System.out.println(" ");
                        }
                    } catch (NoSongsFoundException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e);
                    }
                    break;

                case 3:
                    System.out.println("Enter Album:");
                    album = read.nextLine();

                    try {
                        List<String> songsBasedOnAlbum = songsImpl.searchSongsBasedOnAlbum(album);

                        if (songsBasedOnAlbum.isEmpty()) {
                            System.out.println("No songs available for the album: " + album);
                        } else {
                            System.out.println("Here is what we found!");
                            for (String song : songsBasedOnAlbum) {
                                System.out.println(song + " ");
                            }
                            System.out.println(" ");
                        }
                    } catch (NoSongsFoundException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e);
                    }
                    break;
            }

            //songsImpl.showAvailableSongs();

            System.out.println("Do you wish to go back to the main menu? (yes/no)");
            String response = read.next();

            if (response.equalsIgnoreCase("yes")) {
                displayMenu();
            } else {
                System.out.println("See you soon!");
                exit(0);
            }
        } catch (Exception e) {
            System.out.println("Error while displaying songs. Possible cause: " + e);
        }
    }

    private void playSong() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        Map<String, String> songDetails;

        SongsImpl songsImpl = new SongsImpl();
        System.out.println("Playing songs...\uD83C\uDFB9");

        String filePath="";

        System.out.println("Fetching songs from database...");

        Map<Integer, String> songs = songsImpl.fetchSongNames();

        //songs = songsImpl.fetchSongNames();

        for(Map.Entry<Integer, String> entry: songs.entrySet()) {
            System.out.println(entry.getKey()+ " "+ entry.getValue());
        }

        System.out.println("Please select song id's from above.");

        int songId = read.nextInt();

        songDetails = songsImpl.fetchSongPath(songId);

        if(!songDetails.isEmpty()) {
            Map.Entry<String, String> firstEntry = songDetails.entrySet().iterator().next();
            filePath = firstEntry.getValue();
            System.out.println("Now Playing: "+ firstEntry.getKey());
            System.out.println(" ");
        }
        PlaySongImpl.initializeApplication(filePath);

        System.out.println("Going back to menu");
        displayMenu();
    }



    private void createAPlaylist(int userId) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        String input;
        SongsImpl songsImpl = new SongsImpl();
        Map<String, Integer> playlistStatus = songsImpl.createPlaylist(userId);

        if (!playlistStatus.isEmpty()) {
            Map.Entry<String, Integer> firstEntry = playlistStatus.entrySet().iterator().next();

            playListId = firstEntry.getValue();
            System.out.println("Playlist ID: " + playListId);
            playlistName = firstEntry.getKey();
            System.out.println("Playlist " + playlistName + " successfully created with id: " + playListId);

            System.out.println("Do you want to add songs to it? Type yes or no");
            input = read.next();

            if (input.equalsIgnoreCase("yes")) {
                addSongToPlaylist();
            } else if (input.equalsIgnoreCase("no")) {
                displayMenu();
            } else {
                System.out.println("Please provide a correct response and try again later");
            }
        } else {
            System.out.println("Error creating the playlist.");
        }
    }

    private void addSongToPlaylist() {
        SongsImpl songsImpl = new SongsImpl();

        //GET ME THE PLAYLIST ID OF THE USER BY HITTING THE PLAYLIST TABLE AD THEN PASS THE DATA.

        int playListIdForAUser = songsImpl.getPlaylistIdOfAnExistingUser(userId);
        //System.out.println("Your userId is: "+ updatedUserId+ " Your playlistId is: "+ playListIdForAUser);


        viewListOfSongsForAPlaylist(userId);

        Map<Integer, Integer> map =  songsImpl.addSongsToPlaylist(userId, playListIdForAUser);
        if(!map.isEmpty()) {
            //System.out.println("Songs added successfully in the playlist");

        } else {
            System.out.println("Some error occurred!");
        }
    }

    private void viewListOfSongsForAPlaylist(int updatedUserId) {

//        System.out.println("Your playlist id in the viewListOfPlaylist() method is: "+ updatedUserId);

        //SELECT PLAYLIST_ID, PLAYLIST_NAME FROM PLAYLIST WHERE USERID = ?
        SongsImpl songsImpl = new SongsImpl();
        List<String> listOfSongsForAPlaylist = songsImpl.viewListOfSongsForAPlaylist(updatedUserId);

        if (!listOfSongsForAPlaylist.isEmpty()) {
            System.out.println("Here are the songs created for the playlist: ");
            System.out.println(" ");
            System.out.printf("%-15s%n", "Song");
            System.out.println("---------");

            for(String song: listOfSongsForAPlaylist) {
                System.out.printf("%-15s%n", song);
            }
            System.out.println(" ");
        } else {
            System.out.println("There are no songs created for the playlist");
        }
        System.out.println(" ");
    }
}