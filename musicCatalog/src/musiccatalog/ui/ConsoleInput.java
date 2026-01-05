package musiccatalog.ui;

import musiccatalog.model.*;
import musiccatalog.model.enums.Categories;
import musiccatalog.model.enums.Genres;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ConsoleInput {
    public String readTitle(Scanner scanner) {
        while (true) {
            System.out.print("Title: ");
            String s = scanner.nextLine();

            if (s != null && !s.trim().isEmpty()) {
                return s.trim();
            }

            System.out.println("Invalid input. Please enter a non-empty value.");
        }
    }

    public List<String> readArtists(Scanner scanner) {
        while (true) {
            System.out.print("Artist/Artists: ");
            String line = scanner.nextLine();
            if (line == null) {
                System.out.println("Invalid input. Please enter at least one artist.");
                continue;
            }
            String[] parts = line.split(",");
            List<String> artists = new ArrayList<>();
            for (String p : parts) {
                String name = p.trim();
                if (!name.isEmpty()) {
                    artists.add(name);
                }
            }
            if (!artists.isEmpty()) {
                return artists;
            }
            System.out.println("Invalid input. Please enter at least one artist.");
        }
    }

    public int readDuration(Scanner scanner) {
        while (true) {
            System.out.print("Duration: ");
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input.trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Invalid number. It must be > 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please type a whole number.");
            }
        }
    }

    public int readNumberOfSongsForAlbum(Scanner scanner) {
        while (true) {
            System.out.print("Number of songs in album: ");
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input.trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Invalid number. It must be > 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please type a whole number.");
            }
        }
    }

    public LocalDate readReleaseDate(Scanner scanner) {
        while (true) {
            System.out.print("Release date (yyyy-MM-dd): ");
            String input = scanner.nextLine();

            try {
                LocalDate date = LocalDate.parse(input.trim());
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use yyyy-MM-dd");
            }
        }
    }

    public Genres readGenre(Scanner scanner, Categories typeOfItem) {
        Set<Genres> specificGenres = null;
        if (typeOfItem == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }

        switch (typeOfItem) {
            case SONG:
            case ALBUM:
                specificGenres = Genres.SONGALBUMGENRES;
                break;
            case PODCAST:
            case AUDIOBOOK:
                specificGenres = Genres.PODCASTAUDIOBOOKGENRES;
                break;
            case PLAYLIST:
                specificGenres = Genres.PLAYLISTGENRES;
                break;
            default:
                System.out.println("Invalid input. Please enter a non-empty value.");
                return null;
        }
        while (true) {
            System.out.println("Available genres:");
            for (Genres g : specificGenres) {
                System.out.print(g + ", ");
            }

            System.out.print("\nGenre: ");
            String input = scanner.nextLine();

            if (input == null || input.trim().isEmpty()) {
                System.out.println("Invalid input. Please enter a genre from the list.");
                continue;
            }
            try {
                Genres genre = Genres.valueOf(input.trim().toUpperCase());
                if (!specificGenres.contains(genre)) {
                    System.out.println("This genre is not allowed for " + typeOfItem + ". Try again.");
                    continue;
                }
                return genre;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid genre. Please type one of the listed values exactly (case-insensitive).");
            }
        }
    }

    public Categories readCategoryChoice(Scanner scanner) {
        while (true) {
            System.out.println("""
                Choose?
                1) Song
                2) Album
                3) Podcast
                4) Audiobook
                0) Back
                """);
            System.out.print("Choice: ");
            String input = scanner.nextLine();

            if (input == null) continue;

            switch (input.trim()) {
                case "1": return Categories.SONG;
                case "2": return Categories.ALBUM;
                case "3": return Categories.PODCAST;
                case "4": return Categories.AUDIOBOOK;
                case "0": return null;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public CatalogItem readItem(Scanner scanner, Categories category) {
        String title = readTitle(scanner);
        List<String> artists = readArtists(scanner);
        Genres genre = readGenre(scanner, category);
        int duration = readDuration(scanner);
        LocalDate date = readReleaseDate(scanner);

        switch (category) {
            case SONG: return new Song(title, artists, genre, duration, date);
            case PODCAST: return new Podcast(title, artists, genre, duration, date);
            case AUDIOBOOK: return new Audiobook(title, artists, genre, duration, date);
            case ALBUM:
                int numberOfSongs = readNumberOfSongsForAlbum(scanner);
                return new Album(title, artists, genre, duration, numberOfSongs, date);
            default: throw new IllegalArgumentException("Unknown category: " + category);
        }
    }

    public int readChoices(Scanner scanner) {
        while (true) {
            System.out.print("Choice: ");
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input.trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Invalid number. It must be > 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please type a whole number.");
            }
        }
    }

    public String conditionSearch(Scanner scanner) {
        while (true) {
            System.out.println("""
                What do you want to filter by?
                1) Title
                2) Artist/artists
                3) Genre
                4) Duration
                5) Category
                6) Release date
                0) Back
                """);
            System.out.print("Choice: ");
            String input = scanner.nextLine();

            if (input == null) continue;

            switch (input.trim()) {
                case "1": return "TITLE";
                case "2": return "ARTIST";
                case "3": return "GENRE";
                case "4": return "DURATION";
                case "5": return "CATEGORY";
                case "6": return "RELEASE DATE";
                case "0": return null;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public Playlist readPlaylist(Scanner scanner) {
        System.out.println("Creating PLAYLIST...");
        String title = readTitle(scanner);

        System.out.println("Creator name:");
        List<String> creators = readArtists(scanner);

        Genres genre = readGenre(scanner, Categories.PLAYLIST);

        LocalDate date = LocalDate.now();

        return new Playlist(title, creators, genre, 1, date);
    }
}
