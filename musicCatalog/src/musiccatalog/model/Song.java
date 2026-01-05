package musiccatalog.model;

import musiccatalog.model.enums.Categories;
import musiccatalog.model.enums.Genres;

import java.time.LocalDate;
import java.util.List;

public class Song extends CatalogItem {
    private final Categories category = Categories.SONG;

    public Categories getCategory() { return category; }

    public Song(String title, List<String> artists, Genres genre, int duration, LocalDate releaseDate) {
        super(title, artists, genre, duration, releaseDate);
    }
}
