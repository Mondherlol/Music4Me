package MusicPlayer.Model;

import java.io.File;

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

}

