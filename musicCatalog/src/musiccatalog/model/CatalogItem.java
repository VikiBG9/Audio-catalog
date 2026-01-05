package musiccatalog.model;

import musiccatalog.model.enums.Categories;
import musiccatalog.model.enums.Genres;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class CatalogItem {
    private String title;
    private List<String> artists;
    private Genres genre;
    private int duration;
    private LocalDate releaseDate;

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
    }
    public List<String> getArtists() { return new ArrayList<>(artists); }
    public void setArtists(List<String> artists) {
        if (artists == null || artists.isEmpty()) {
            throw new IllegalArgumentException("There needs to be an artist");
        }
        for (String artistName : artists) {
            if (artistName == null || artistName.trim().isEmpty()) {
                throw new IllegalArgumentException("Artist name cannot be null");
            }
        }
        this.artists = new ArrayList<>(artists);
    }
    public Genres getGenre() { return genre; }
    public void setGenre(Genres genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        this.genre = genre;
    }
    public int getDuration() { return duration; }
    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("The duration of the song cannot be zero or negative");
        }
        this.duration = duration;
    }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) {
        if (releaseDate == null) {
            throw new IllegalArgumentException("Release date cannot be null");
        }
        this.releaseDate = releaseDate;
    }

    public abstract Categories getCategory();

    protected CatalogItem(String title, List<String> artists, Genres genre, int duration, LocalDate releaseDate) {
        setTitle(title);
        setArtists(artists);
        setGenre(genre);
        setDuration(duration);
        setReleaseDate(releaseDate);
    }

    protected String artistSoloOrMore() {
        return (artists.size() == 1) ? "Artist" : "Artists";
    }
    protected String formattedTime() {
        return String.format("%02d:%02d:%02d", duration / 3600, (duration % 3600) / 60, duration % 60);
    }

    public String songKey() {
        return getTitle().trim().toLowerCase() + "~" + String.join(",", getArtists()).trim().toLowerCase() + "~" + getReleaseDate().toString();
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
                Released: %s
                -------------""", getCategory(), title, artistSoloOrMore(),String.join(", ", artists), genre, formattedTime(), releaseDate);
    }
}
