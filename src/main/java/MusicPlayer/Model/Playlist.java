package MusicPlayer.Model;

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
        this.songs = null;
        this.cover = "";
    }


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
}
