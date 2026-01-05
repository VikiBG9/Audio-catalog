package musiccatalog;

import musiccatalog.app.CatalogApp;

import java.nio.file.Path;

public class Main{

    public static void main(String[] args) {
        Path filePath = Path.of("data", "catalog.txt");
        new CatalogApp(filePath).run();
    }
}
