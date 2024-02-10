CREATE DATABASE jukebox;

use jukebox;

CREATE TABLE songs (
    song_id INT AUTO_INCREMENT PRIMARY KEY,
    song_name VARCHAR(255),
    artist_name VARCHAR(255),
    album_name VARCHAR(255),
    release_year INT,
    genre VARCHAR(255),
    duration_seconds INT,
    file_path VARCHAR(255)
);
INSERT INTO songs (song_name, artist_name, album_name, release_year, genre, duration_seconds, file_path)
VALUES ('Reminder', 'The Weeknd', 'Starboy', 2016, 'Soul', 222, '/Users/shubham/Documents/NIIT-Capstone-Project/songs/The_Weeknd-Reminder.wav');

UPDATE songs SET file_path='/Users/shubham/Desktop/Mitraz-Khayaal.wav' where song_id=5;

UPDATE songs SET file_path='/Users/shubham/Documents/NIIT-Capstone-Project/songs/The_Weeknd-Call_Out_My_Name.wav' where song_id=3;





INSERT INTO songs (song_name, artist_name, album_name, release_year, genre, duration_seconds, file_path)
VALUES ('Call Out My Name', 'The Weeknd', 'After Hours', 2019, 'Pop', 245, '/path/to/call_out_my_name.mp3');

INSERT INTO songs (song_name, artist_name, album_name, release_year, genre, duration_seconds, file_path)
VALUES ('Call Out My Name', 'The Weeknd', 'After Hours', 2019, 'Pop', 245, '/path/to/call_out_my_name.mp3');

delete from songs where song_id=4;

SELECT * FROM songs;


CREATE TABLE user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50), 
    last_name VARCHAR(50),
    user_name VARCHAR(25) UNIQUE,
    password VARCHAR(255)
);

INSERT INTO user (first_name, last_name, user_name, password)
VALUES ('John', 'Doe', "john@123.com", "123456");

INSERT INTO user (first_name, last_name, user_name, password)
VALUES ('John', 'Doe', "john@1234.com", "123456");

SELECT * FROM user;




CREATE TABLE playlist (
    playlist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    playlist_name VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

select * from playlist;

delete from playlist where playlist_id=11;
delete from playlist where playlist_id=12;
delete from playlist where playlist_id=13;
delete from playlist where playlist_id=17;
delete from playlist where playlist_id=18;
delete from playlist where playlist_id=27;

delete from user where user_id=13;


CREATE TABLE playlist_song (
    playlist_id INT,
    song_id INT,
    PRIMARY KEY (playlist_id, song_id),
    FOREIGN KEY (playlist_id) REFERENCES playlist(playlist_id),
    FOREIGN KEY (song_id) REFERENCES songs(song_id)
);

select * from playlist_song;
delete from playlist_song where playlist_id=14;

SELECT * from playlist where user_id=14;

delete from playlist_song where playlist_id=10;
delete from playlist_song where playlist_id=8;
delete from playlist_song where playlist_id=18;
delete from playlist_song where playlist_id=27;

delete from playlist_song where song_id=1;

SELECT playlist_id, playlist_name from playlist where user_id=2;


