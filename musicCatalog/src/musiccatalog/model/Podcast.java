package musiccatalog.model;

import musiccatalog.model.enums.Categories;
import musiccatalog.model.enums.Genres;

import java.time.LocalDate;
import java.util.List;

public class Podcast extends CatalogItem {
    private final Categories category = Categories.PODCAST;

    public Categories getCategory() { return category; }

    public Podcast(String title, List<String> artists, Genres genre, int duration, LocalDate releaseDate) {
        super(title, artists, genre, duration, releaseDate);
    }
}
