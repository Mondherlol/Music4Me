package MusicPlayer.Model;

import MusicPlayer.Utils.DBConnexion;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Song {
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int year;
    private int duration;
    private File mp3;
    private String cover;
    private boolean isFavorite;

    private int id;

    public Song(int id, String title, String artist, String album, String genre, int year, int duration, File mp3, String cover, boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.duration = duration;
        this.mp3 = mp3;
        this.cover = cover;
        this.isFavorite = isFavorite;
    }


    // Autres propriétés existantes

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Song(int id,String title, String artist, String album, String genre, int year, int duration, File mp3, String cover) {
        System.out.println("Song path : "+ mp3.toString());
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.duration = duration;
        this.mp3 = mp3;
        this.cover = cover;
        this.isFavorite = false;
    }

    public Song(){

        this.title = "";
        this.artist = "";
        this.album = "";
        this.genre = "";
        this.year = 0;
        this.duration = 0;
        this.mp3 = null;
        this.cover = "";
        this.isFavorite = false;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public int getDuration() {
        return duration;
    }

    public File getMp3() {
        return mp3;
    }

    public String getCover() {
        return cover;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setMp3(File mp3) {
        this.mp3 = mp3;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean getIsFavorite(){
        return isFavorite;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", genre='" + genre + '\'' +
                ", year='" + year + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

    public static Song getSongById(int songId) {
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return null;
        }

        try {
            String query = "SELECT * FROM songs WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, songId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                String album = resultSet.getString("album");
                String genre = resultSet.getString("genre");
                int year = resultSet.getInt("year");
                int duration = resultSet.getInt("duration");
                File mp3 = new File(resultSet.getString("mp3"));
                String cover = resultSet.getString("cover");
                boolean isFavorite = resultSet.getBoolean("isFavorite");

                Song song = new Song(id, title, artist, album, genre, year, duration, mp3, cover);


                return song;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateSong(Song song) {
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return false;
        }

        try {
            String query = "UPDATE songs SET title = ?, artist = ?, album = ?, genre = ?, year = ?, duration = ?, mp3 = ?, cover = ?, isFavorite = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, song.getTitle());
            statement.setString(2, song.getArtist());
            statement.setString(3, song.getAlbum());
            statement.setString(4, song.getGenre());
            statement.setInt(5, song.getYear());
            statement.setInt(6, song.getDuration());
            statement.setString(7, song.getMp3().toString());
            statement.setString(8, song.getCover());
            statement.setBoolean(9, song.getIsFavorite());
            statement.setInt(10, song.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;


    };

    public static List<Song> getFavoriteSongs(){
        List<Song> songs = new ArrayList<Song>();
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return songs;
        }

        try {
            String query = "SELECT * FROM songs WHERE isFavorite = 1";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                String album = resultSet.getString("album");
                String genre = resultSet.getString("genre");
                int year = resultSet.getInt("year");
                int duration = resultSet.getInt("duration");
                File mp3 = new File(resultSet.getString("mp3"));
                String cover = resultSet.getString("cover");
                boolean isFavorite = resultSet.getBoolean("isFavorite");

                songs.add(new Song(id, title, artist, album, genre, year, duration, mp3, cover, isFavorite));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }
}

