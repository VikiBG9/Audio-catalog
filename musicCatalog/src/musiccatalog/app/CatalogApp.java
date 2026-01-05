package musiccatalog.app;
import musiccatalog.model.CatalogItem;
import musiccatalog.model.Playlist;
import musiccatalog.model.Song;
import musiccatalog.model.enums.Categories;
import musiccatalog.model.enums.Genres;
import musiccatalog.persistence.CatalogWR;
import musiccatalog.service.CatalogService;
import musiccatalog.ui.ConsoleInput;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class CatalogApp {
    private final Path filePath;
    private final Scanner scanner = new Scanner(System.in);
    private final ConsoleInput input = new ConsoleInput();
    private final CatalogService service = new CatalogService();

    private List<CatalogItem> catalog = new ArrayList<>();

    public CatalogApp(Path filePath) {
        this.filePath = filePath;
    }

    public void run() {
        loadCatalog();

        boolean running = true;
        while (running) {
            System.out.println("""
                    ===== AUDIO CATALOG =====
                    1) Add item
                    2) Show catalog
                    3) Save catalog
                    4) Filter catalog
                    5) Search for exact object
                    6) Sort catalog
                    7) Remove item
                    8) Playlists
                    0) Exit
                    """);

            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addItem();
                    break;
                case "2":
                    showCatalog(catalog);
                    break;
                case "3":
                    saveCatalog();
                    break;
                case "4":
                    searchMenu();
                    break;
                case "5":
                    getObjectMenu();
                    break;
                case "6":
                    sortCatalog();
                    break;
                case "7":
                    removeItem();
                    break;
                case "8":
                    playlistsMenu();
                    break;
                case "0":
                    saveCatalog();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }

        System.out.println("Goodbye!");
    }

    private void loadCatalog() {
        try {
            catalog = CatalogWR.load(filePath);
            System.out.println("Audio catalog loaded. Number of items = " + catalog.size());
        }catch (IOException e){
            System.err.println("Could not find an existing audio catalog. Starting with an empty one.");
            catalog = new ArrayList<>();
        }
    }

    private void saveCatalog() {
        try {
            System.out.println("Saving items...");
            CatalogWR.save(filePath, catalog);
            System.out.println("Catalog saved.");
        } catch (Exception e) {
            System.out.println("Error saving catalog: " + e.getMessage());
        }
    }

    private void showCatalog(List<CatalogItem> catalog) {
        if (catalog.isEmpty()) {
            System.out.println("Catalog is empty.");
            return;
        }
        System.out.println("\n--- Catalog ---");
        for (int i = 0; i < catalog.size(); i++) {
            System.out.println((i + 1) + ".");
            System.out.println(catalog.get(i));
        }
    }

    private void addItem() {
        Categories category = input.readCategoryChoice(scanner);
        if (category == null) return;

        try {
            CatalogItem item = input.readItem(scanner, category);
            catalog.add(item);
            saveCatalog();
            System.out.println("Item added successfully!");

        } catch (Exception e) {
            System.out.println("Error creating item: " + e.getMessage());
        }
    }

    private void removeItem() {
        System.out.print("Title to delete: ");
        String title = scanner.nextLine();
        boolean removed = false;
        try {
            Iterator<CatalogItem> iterator = catalog.iterator();
            while(iterator.hasNext()) {
                CatalogItem item = iterator.next();
                if(item.getTitle().equalsIgnoreCase(title)) {
                    iterator.remove();
                    removed = true;
                    break;
                }
            }

            if(removed) {
                saveCatalog();
                System.out.println("Item deleted successfully!");
            }else  {
                System.out.println("Item not found!");
            }

        }catch (Exception e){
            System.out.println("Error deleting item: " + e.getMessage());
        }
    }

    private void searchMenu() {
        while (true) {
            String field = input.conditionSearch(scanner);
            if (field == null) return;

            List<CatalogItem> results;
            switch (field) {
                case "TITLE":
                    System.out.println("Enter wanted title:");
                    String titleSearch = input.readTitle(scanner);
                    results = service.searchByTitle(catalog, titleSearch);
                    break;
                case "ARTIST":
                    System.out.println("Enter wanted artist name:");
                    System.out.print("Artist/Artists: ");
                    String wantedArtist = scanner.nextLine();
                    results = service.searchByArtist(catalog, wantedArtist);
                    break;
                case "GENRE":
                    Categories c = input.readCategoryChoice(scanner);
                    if (c == null) continue;
                    System.out.println("Enter wanted genre:");
                    Genres genreSearch = input.readGenre(scanner, c);
                    results = service.searchByGenre(catalog, genreSearch);
                    break;
                case "DURATION":
                    System.out.println("Enter wanted duration:");
                    int durationSearch = input.readDuration(scanner);
                    results = service.searchByDuration(catalog, durationSearch);
                    break;
                case "CATEGORY":
                    System.out.println("Enter wanted category:");
                    Categories cat = input.readCategoryChoice(scanner);
                    if (cat == null) continue;
                    results = service.searchByCategory(catalog, cat);
                    break;
                case "RELEASE DATE":
                    System.out.println("Enter first date:");
                    LocalDate date1 = input.readReleaseDate(scanner);
                    System.out.println("Enter second date:");
                    LocalDate date2 = input.readReleaseDate(scanner);
                    results = service.searchByReleaseDate(catalog, date1, date2);
                    break;
                default:
                    results = List.of();
                    break;
            }

            service.resultAfterSearch(results);
            break;
        }
    }

    private void getObjectMenu() {
        System.out.println("See if we have a desired audio item! Press");
        Categories wantedCategory = input.readCategoryChoice(scanner);
        String wantedTitle = input.readTitle(scanner);
        List<String> wantedArtists = input.readArtists(scanner);

        CatalogItem foundItem = service.getWantedItem(catalog, wantedCategory, wantedTitle, wantedArtists);

        if(foundItem != null) {
            System.out.println("Audio object found!");
            System.out.println(foundItem);
        }else {
            System.out.println("Audio object not found! No matches.");
        }
    }

    private void sortCatalog() {
        List<CatalogItem> sortedItems = new ArrayList<>(catalog);
        System.out.println("Sorted catalog items by title:");
        sortedItems.sort((item1, item2) -> item1.getTitle().compareToIgnoreCase(item2.getTitle()));
        showCatalog(sortedItems);
    }

    private void playlistsMenu() {
        while (true) {
            System.out.println("""
                --- PLAYLISTS ---
                1) Create playlist
                2) Show all playlists
                3) Open playlist
                0) Back
                """);

            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    Playlist p = input.readPlaylist(scanner);
                    catalog.add(p);
                    saveCatalog();
                    System.out.println("Playlist created!");
                }
                case "2" -> showAllPlaylists();
                case "3" -> {
                    Playlist p = choosePlaylist();
                    if (p != null) {
                        openPlaylistMenu(p);
                    }
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void showAllPlaylists() {
        int count = 0;
        for (CatalogItem item : catalog) {
            if (item.getCategory() == Categories.PLAYLIST) {
                count++;
                Playlist p = (Playlist) item;
                System.out.println(count + ") " + p.getTitle() + " (Creator: " + String.join(", ", p.getArtists()) + ")");
            }
        }
        if (count == 0) {
            System.out.println("No playlists yet.");
        }
    }

    private Playlist choosePlaylist() {
        List<Playlist> playlists = new ArrayList<>();
        for (CatalogItem item : catalog) {
            if (item.getCategory() == Categories.PLAYLIST) {
                playlists.add((Playlist) item);
            }
        }

        if (playlists.isEmpty()) {
            System.out.println("No playlists yet.");
            return null;
        }

        System.out.println("--- Choose playlist ---");
        for (int i = 0; i < playlists.size(); i++) {
            System.out.println((i + 1) + ") " + playlists.get(i).getTitle());
        }

        int index = input.readChoices(scanner);
        if (index == 0) return null;
        if (index < 1 || index > playlists.size()) {
            System.out.println("Invalid number.");
            return null;
        }

        return playlists.get(index - 1);
    }

    private void openPlaylistMenu(Playlist p) {
        while (true) {
            System.out.println("""
                --- PLAYLIST: %s ---
                1) Show playlist details (and songs)
                2) Add song from catalog
                3) Remove song from playlist
                0) Back
                """.formatted(p.getTitle()));

            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> System.out.println(p);
                case "2" -> addSongToPlaylist(p);
                case "3" -> removeSongFromPlaylist(p);
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addSongToPlaylist(Playlist p) {
        System.out.println("Adding SONG to playlist (must exist in catalog)...");
        String title = input.readTitle(scanner);
        List<String> artists = input.readArtists(scanner);
        LocalDate date = input.readReleaseDate(scanner);

        Song song = (Song)service.getWantedItem(catalog, Categories.SONG, title, artists);
        if (song == null) {
            System.out.println("No such SONG in catalog.");
            return;
        }

        p.addSong(song);
        saveCatalog();
        System.out.println("Song added to playlist.");
    }

    private void removeSongFromPlaylist(Playlist p) {
        System.out.println("Removing SONG from playlist...");
        String title = input.readTitle(scanner);
        List<String> artists = input.readArtists(scanner);

        Song song = (Song)service.getWantedItem(catalog, Categories.SONG, title, artists);
        if (song == null) {
            System.out.println("No such SONG in catalog.");
            return;
        }

        boolean removed = p.removeSong(song);
        if (removed) {
            saveCatalog();
            System.out.println("Song removed from playlist.");
        } else {
            System.out.println("That song is not in this playlist.");
        }
    }

}
