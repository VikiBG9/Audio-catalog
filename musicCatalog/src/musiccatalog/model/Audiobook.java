package musiccatalog.model;

import musiccatalog.model.enums.Categories;
import musiccatalog.model.enums.Genres;

import java.time.LocalDate;
import java.util.List;

public class Audiobook extends CatalogItem {
    private final Categories category = Categories.AUDIOBOOK;

    public Categories getCategory() { return category; }

    public Audiobook(String title, List<String> artists, Genres genre, int duration, LocalDate releaseDate) {
        super(title, artists, genre, duration, releaseDate);
    }
}
