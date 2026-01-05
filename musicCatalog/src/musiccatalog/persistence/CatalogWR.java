package musiccatalog.persistence;
import musiccatalog.model.*;
import musiccatalog.model.enums.Categories;
import musiccatalog.model.enums.Genres;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CatalogWR {
    public static void save(Path file, List<CatalogItem> items) throws IOException {
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (CatalogItem item : items) {
                writer.write(serialize(item));
                writer.newLine();
            }
        }
    }
    private static String serialize(CatalogItem item) {
        String category = item.getCategory().name();
        String title = item.getTitle();
        String artists = String.join(", ", item.getArtists());
        String genre = item.getGenre().name();
        String duration = String.valueOf(item.getDuration());
        String date = item.getReleaseDate().toString();

        if (item instanceof Album albumItem) {
            return String.join("|", category, title, artists, genre, duration, String.valueOf(albumItem.getNumberOfSongs()), date);
        }
        if (item instanceof Playlist playlistItem) {
            String songKeys = String.join(";", playlistItem.getSongKeys());
            return String.join("|", category, title, artists, genre, duration, date, songKeys);
        }
        return String.join("|", category, title, artists, genre, duration, date);
    }

    public static List<CatalogItem> load(Path file) throws IOException {
        List<CatalogItem> items = new ArrayList<>();
        if (!Files.exists(file)) {
            return items;
        }
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                CatalogItem item = deserialize(line);
                if (item != null) items.add(item);
            }
        }
        linkPlaylist(items);
        return items;
    }

    private static CatalogItem deserialize(String line) {
        String[] inputs = line.split("\\|", -1);
        if (inputs.length < 6) return null;

        Categories category = Categories.valueOf(inputs[0]);
        String title = inputs[1];

        String allArtists = inputs[2];
        List<String> artists = new ArrayList<>();
        if (!allArtists.trim().isEmpty()) {
            String[] artistsArray = allArtists.split(",");
            for (String artist : artistsArray) {
                String artistName = artist.trim();
                if (!artistName.isEmpty()) {
                    artists.add(artistName);
                }
            }
        }

        Genres genre = Genres.valueOf(inputs[3]);
        int duration = Integer.parseInt(inputs[4]);

        if (category == Categories.ALBUM) {
            if (inputs.length < 7) return null;
            int numberOfSongs = Integer.parseInt(inputs[5]);
            LocalDate date = LocalDate.parse(inputs[6]);
            return new Album(title, artists, genre, duration, numberOfSongs, date);
        }

        if (category == Categories.PLAYLIST) {
            LocalDate date = LocalDate.parse(inputs[5]);
            Playlist p = new Playlist(title, artists, genre, duration, date);

            if (inputs.length >= 7 && !inputs[6].trim().isEmpty()) {
                String[] keys = inputs[6].split(";");
                for (String k : keys) {
                    p.addPendingKeys(k);
                }
            }
            return p;
        }
        LocalDate date = LocalDate.parse(inputs[5]);

        switch (category) {
            case SONG: { return new Song(title, artists, genre, duration, date); }
            case PODCAST: { return new Podcast(title, artists, genre, duration, date); }
            case AUDIOBOOK: { return new Audiobook(title, artists, genre, duration, date); }
            default: { return null; }
        }
    }

    public static void linkPlaylist(List<CatalogItem> items) {
        List<Song> songs = new ArrayList<>();
        for (CatalogItem item : items) {
            if (item.getCategory() == Categories.SONG) {
                songs.add((Song)item);
            }
        }

        for (CatalogItem item : items) {
            if (item.getCategory() != Categories.PLAYLIST) {
                continue;
            }
            Playlist pl = (Playlist)item;
            for (String key : pl.getPendingKeys()) {
                Song song = findSongByKey(songs, key);
                if (song != null) {
                    pl.addSong(song);
                }
            }
            pl.clearPendingKeys();
        }
    }
    private static Song findSongByKey(List<Song> songs, String key) {
        if (key == null) return null;

        for (Song song : songs) {
            if (song.songKey().equals(key.trim().toLowerCase())) return song;
        }
        return null;
    }
}



