package MusicPlayer.Model;

import java.io.File;

public class Song {
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int year;
    private int duration; // En secondes

    private File mp3;

    private String cover;

    private boolean isFavorite;


    public Song(String title, String artist, String album, String genre, int year, int duration, File mp3, String cover) {
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

