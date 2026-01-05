package musiccatalog.model;
import musiccatalog.model.enums.Categories;
import musiccatalog.model.enums.Genres;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Playlist extends CatalogItem {
    private final Categories category = Categories.PLAYLIST;
    private final List<Song> songs = new ArrayList<>();
    private final List<String> pendingKeys = new ArrayList<>();
    public Categories getCategory() { return category; }

    public Playlist(String title, List<String> artists, Genres genre, int duration, LocalDate releaseDate) {
        super(title, artists, genre, duration, releaseDate);
    }

    public List<Song> getSongs() { return new ArrayList<>(songs); }

    public void addSong(Song song) {
        if (song == null) throw new IllegalArgumentException("musiccatalog.model.Song is null");
        if (!songs.contains(song)) songs.add(song);
    }

    public boolean removeSong(Song song) {
        if (song == null) throw new IllegalArgumentException("musiccatalog.model.Song is null");
        return songs.remove(song);
    }

    public List<String> getSongKeys() {
        List<String> keys = new ArrayList<>();
        for (Song song : songs) {
            keys.add(song.songKey());
        }
        return keys;
    }
    public void addPendingKeys(String key) {
        if(key == null) return;
        if(!key.trim().isEmpty()) pendingKeys.add(key.trim());
    }

    public List<String> getPendingKeys() { return new ArrayList<>(pendingKeys); }
    public void clearPendingKeys() { pendingKeys.clear(); }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        sb.append("\nSongs in playlist:\n");

        if (songs.isEmpty()) {
            sb.append("(empty)\n");
        } else {
            for (int i = 0; i < songs.size(); i++) {
                Song s = songs.get(i);
                sb.append(i + 1).append(") ")
                        .append(s.getTitle())
                        .append(" - ")
                        .append(String.join(", ", s.getArtists()))
                        .append(" [").append(s.getGenre()).append("]\n");
            }
        }
        return sb.toString();
    }
}
