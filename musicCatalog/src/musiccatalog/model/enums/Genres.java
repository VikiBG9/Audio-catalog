package musiccatalog.model.enums;

import java.util.EnumSet;
import java.util.Set;

public enum Genres {
    //Song/album genres
    POP,
    ROCK,
    ELECTRONIC,
    HIPHOP,
    JAZZ,
    HYPERPOP,
    METAL,
    CLASSICAL,
    COUNTRY,
    FOLK,
    RAP,
    LATIN,
    KPOP,
    RNB,
    SOUL,

    //Podcast/audiobooks genres
    COMEDY,
    DRAMA,
    HEALTH,
    GAMESHOW,
    NEWS,
    POLITICS,
    POPCULTURE,
    RELIGION,
    SPORTS,
    TRUECRIME,
    FANTASY,
    ROMANCE,
    SCIFI,
    MYSTERY,
    HORROR,
    HISTORICAL,
    BIOGRAPHY,
    BUSINESS,

    //Playlist genres
    CHILL,
    PARTY,
    STUDY,
    ROADTRIP,
    WORKOUT,
    NOSTALGIA,
    PERSONALIZED,
    MIXED;

    public static final Set<Genres> PLAYLISTGENRES = EnumSet.of(
            CHILL, PARTY, STUDY, ROADTRIP, WORKOUT, NOSTALGIA, PERSONALIZED, MIXED
    );

    public static final Set<Genres> SONGALBUMGENRES = EnumSet.of(
            POP, ROCK, ELECTRONIC, HIPHOP, JAZZ, HYPERPOP, METAL, CLASSICAL, COUNTRY, FOLK, RAP, LATIN, KPOP, RNB, SOUL
    );

    public static final Set<Genres> PODCASTAUDIOBOOKGENRES = EnumSet.of(
            COMEDY, DRAMA, HEALTH, GAMESHOW, NEWS, POLITICS, POPCULTURE, RELIGION, SPORTS,
            TRUECRIME, FANTASY, ROMANCE, SCIFI, MYSTERY, HORROR, HISTORICAL, BIOGRAPHY, BUSINESS
    );
}
