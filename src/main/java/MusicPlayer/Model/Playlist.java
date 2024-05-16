package MusicPlayer.Model;


import MusicPlayer.Utils.DBConnexion;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Playlist {
    private int id;
    private String name;
    private String description;
    private List<Song> songs;
    private String cover;

    public Playlist(int id, String name, String description, List<Song> songs, String cover) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.songs = songs;
        this.cover = cover;
    }

    public Playlist() {
        this.id = 0;
        this.name = "";
        this.description = "";
        this.songs = new ArrayList<>();
        this.cover = "";
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    // Méthodes pour interagir avec la base de données

    public boolean addSongToPlaylist(int songId) {
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return false;
        }

        try {
            String query = "INSERT INTO playlist_songs (playlist_id, song_id) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.id);
            statement.setInt(2, songId);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeSongFromPlaylist(int songId) {
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return false;
        }

        try {
            String query = "DELETE FROM playlist_songs WHERE playlist_id = ? AND song_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.id);
            statement.setInt(2, songId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePlaylist() {
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return false;
        }

        try {
            String query = "DELETE FROM playlists WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Playlist supprimée avec succès.");
                return true;
            } else {
                System.out.println("Aucune playlist supprimée. Playlist introuvable.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePlaylist() {
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return false;
        }

        try {
            String query = "UPDATE playlists SET name = ?, description = ?, cover = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, this.name);
            statement.setString(2, this.description);
            statement.setString(3, this.cover);
            statement.setInt(4, this.id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Playlist mise à jour avec succès.");
                return true;
            } else {
                System.out.println("Aucune playlist mise à jour. Playlist introuvable.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Playlist getPlaylistById(int playlistId) {
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return null;
        }

        try {
            String query = "SELECT * FROM playlists WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, playlistId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String cover = resultSet.getString("cover");

                // Récupérer la liste des chansons de la playlist
                List<Song> songs = getPlaylistSongs(playlistId);

                return new Playlist(id, name, description, songs, cover);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Song> getPlaylistSongs(int playlistId) {
        List<Song> songs = new ArrayList<>();
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return songs;
        }

        try {
            String query = "SELECT * FROM songs INNER JOIN playlist_songs ON songs.id = playlist_songs.song_id WHERE playlist_songs.playlist_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, playlistId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                String album = resultSet.getString("album");
                String genre = resultSet.getString("genre");
                int year = resultSet.getInt("year");
                int duration = resultSet.getInt("duration");
                File mp3 =  new File(resultSet.getString("mp3"));
                String cover = resultSet.getString("cover");
                boolean isFavorite = resultSet.getBoolean("isFavorite");

                songs.add(new Song(id, title, artist, album, genre, year, duration, mp3, cover, isFavorite));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }
    public static List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return playlists;
        }

        try {
            String query = "SELECT * FROM playlists";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String cover = resultSet.getString("cover");

                // Récupérer la liste des chansons de la playlist
                List<Song> songs = getPlaylistSongs(id);

                playlists.add(new Playlist(id, name, description, songs, cover));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlists;
    }
    public boolean addPlaylist() {
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return false;
        }

        try {
            String query = "INSERT INTO playlists (name, description, cover) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, this.name);
            statement.setString(2, this.description);
            statement.setString(3, this.cover);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}