package musiccatalog.model;

import musiccatalog.model.enums.Categories;
import musiccatalog.model.enums.Genres;

import java.time.LocalDate;
import java.util.List;

public class Album extends CatalogItem {
    private int numberOfSongs;
    private final Categories category = Categories.ALBUM;

    public int getNumberOfSongs() { return numberOfSongs; }
    public void setNumberOfSongs(int numberOfSongs) {
        if (numberOfSongs <= 0) {
            throw new IllegalArgumentException("musiccatalog.model.Album must have at least one song");
        }
        this.numberOfSongs = numberOfSongs;
    }
    public Categories getCategory() { return category; }

    public Album(String title, List<String> artists, Genres genre, int duration, int numberOfSongs, LocalDate releaseDate) {
        super(title, artists, genre, duration, releaseDate);
        this.numberOfSongs = numberOfSongs;
    }

    @Override
    public String toString() {
        return String.format("""
                -------------
                Category: %s,
                Title: %s,
                %s: %s,
                Genre: %s,
                Duration: %s,
                Number of songs: %d,
                Released: %s
                -------------""", getCategory(), getTitle(), artistSoloOrMore(), String.join(", ", getArtists()), getGenre(), formattedTime(),getNumberOfSongs(), getReleaseDate());
    }
}
