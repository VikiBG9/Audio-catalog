package musiccatalog.service;

import musiccatalog.model.CatalogItem;
import musiccatalog.model.enums.Categories;
import musiccatalog.model.enums.Genres;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CatalogService {
    public List<CatalogItem> searchByTitle(List<CatalogItem> items, String wantedTitle) {
        List<CatalogItem> foundItems = new ArrayList<>();
        if (wantedTitle == null || wantedTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        for (CatalogItem item : items) {
            if (item.getTitle().toLowerCase().contains(wantedTitle.trim().toLowerCase())) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }

    public List<CatalogItem> searchByArtist(List<CatalogItem> items, String wantedArtists) {
        List<CatalogItem> foundItems = new ArrayList<>();
        if (wantedArtists == null || wantedArtists.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        for (CatalogItem item : items) {
            for (String artist : item.getArtists()) {
                if (artist.toLowerCase().contains(wantedArtists.trim().toLowerCase())) {
                    foundItems.add(item);
                    break;
                }
            }
        }
        return foundItems;
    }

    public List<CatalogItem> searchByGenre(List<CatalogItem> items, Genres wantedGenre) {
        List<CatalogItem> foundItems = new ArrayList<>();

        for (CatalogItem item : items) {
            if(item.getGenre() == wantedGenre) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }

    public List<CatalogItem> searchByDuration(List<CatalogItem> items, int wantedDuration) {
        List<CatalogItem> foundItems = new ArrayList<>();

        for (CatalogItem item : items) {
            if(item.getDuration() <= wantedDuration) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }

    public List<CatalogItem> searchByCategory(List<CatalogItem> items, Categories wantedCategory) {
        List<CatalogItem> foundItems = new ArrayList<>();

        for (CatalogItem item : items) {
            if(item.getCategory() == wantedCategory) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }

    public List<CatalogItem> searchByReleaseDate(List<CatalogItem> items, LocalDate wantedFirstDate, LocalDate wantedSecondDate) {
        List<CatalogItem> foundItems = new ArrayList<>();

        for (CatalogItem item : items) {
            if(item.getReleaseDate().isAfter(wantedFirstDate) && item.getReleaseDate().isBefore(wantedSecondDate)) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }

    public void resultAfterSearch(List<CatalogItem> result) {
        if (result == null || result.isEmpty()) {
            System.out.println("No items found");
            return;
        }
        System.out.println("Found " + result.size() + " items");
        System.out.println("Matches:");
        for (CatalogItem item : result) {
            System.out.println(item);
        }
    }

    public CatalogItem getWantedItem(List<CatalogItem> items, Categories wantedCategory, String wantedTitle, List<String> wantedArtists) {
        for(CatalogItem item : items) {
            if(item.getCategory() != wantedCategory) continue;
            if(!item.getTitle().equals(wantedTitle.trim())) continue;
            if(!exactArtists(item.getArtists(), wantedArtists)) continue;
            return item;
        }
        return null;
    }

    public boolean exactArtists(List<String> artistA, List<String> artistB) {
        List<String> artistALowered =  new ArrayList<>();
        for(String a : artistA) artistALowered.add(a.toLowerCase().trim());

        List<String> artistBLowered =  new ArrayList<>();
        for(String b : artistB) artistBLowered.add(b.toLowerCase().trim());

        if(artistALowered.size() != artistBLowered.size()) return false;

        for(String artist : artistALowered) {
            if (!artistBLowered.contains(artist)) return false;
        }
        return true;
    }
}
