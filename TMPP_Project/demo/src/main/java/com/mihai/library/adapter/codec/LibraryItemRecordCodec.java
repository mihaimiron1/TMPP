package com.mihai.library.adapter.codec;

import com.mihai.library.domain.Book;
import com.mihai.library.domain.Dvd;
import com.mihai.library.domain.LibraryItem;
import com.mihai.library.domain.Magazine;

public final class LibraryItemRecordCodec {
    public String encode(LibraryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("item null");
        }

        if (item instanceof Book book) {
            return String.join("|",
                    "BOOK",
                    Base64Codec.encode(book.getId()),
                    Base64Codec.encode(book.getTitle()),
                    Base64Codec.encode(book.getAuthor()),
                    Base64Codec.encode(book.getIsbn()));
        }

        if (item instanceof Magazine magazine) {
            return String.join("|",
                    "MAGAZINE",
                    Base64Codec.encode(magazine.getId()),
                    Base64Codec.encode(magazine.getTitle()),
                    String.valueOf(magazine.getIssueNumber()));
        }

        if (item instanceof Dvd dvd) {
            return String.join("|",
                    "DVD",
                    Base64Codec.encode(dvd.getId()),
                    Base64Codec.encode(dvd.getTitle()),
                    String.valueOf(dvd.getDurationMinutes()));
        }

        throw new IllegalArgumentException("Unsupported item type: " + item.getClass().getSimpleName());
    }

    public LibraryItem decode(String record) {
        if (record == null || record.isBlank()) {
            throw new IllegalArgumentException("record invalid");
        }

        String[] parts = record.split("\\|", -1);
        String type = parts[0];

        return switch (type) {
            case "BOOK" -> decodeBook(parts);
            case "MAGAZINE" -> decodeMagazine(parts);
            case "DVD" -> decodeDvd(parts);
            default -> throw new IllegalArgumentException("Unknown item type in record: " + type);
        };
    }

    private static Book decodeBook(String[] parts) {
        requireLength(parts, 5, "BOOK");
        return Book.builder()
                .id(Base64Codec.decode(parts[1]))
                .title(Base64Codec.decode(parts[2]))
                .author(Base64Codec.decode(parts[3]))
                .isbn(Base64Codec.decode(parts[4]))
                .build();
    }

    private static Magazine decodeMagazine(String[] parts) {
        requireLength(parts, 4, "MAGAZINE");
        return Magazine.builder()
                .id(Base64Codec.decode(parts[1]))
                .title(Base64Codec.decode(parts[2]))
                .issueNumber(parsePositiveInt(parts[3], "issueNumber"))
                .build();
    }

    private static Dvd decodeDvd(String[] parts) {
        requireLength(parts, 4, "DVD");
        return Dvd.builder()
                .id(Base64Codec.decode(parts[1]))
                .title(Base64Codec.decode(parts[2]))
                .durationMinutes(parsePositiveInt(parts[3], "durationMinutes"))
                .build();
    }

    private static int parsePositiveInt(String rawValue, String fieldName) {
        try {
            int parsed = Integer.parseInt(rawValue);
            if (parsed <= 0) {
                throw new IllegalArgumentException(fieldName + " must be > 0");
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid integer for " + fieldName + ": " + rawValue, ex);
        }
    }

    private static void requireLength(String[] parts, int expectedLength, String type) {
        if (parts.length != expectedLength) {
            throw new IllegalArgumentException("Invalid record for " + type + ": expected " + expectedLength + " fields but got " + parts.length);
        }
    }
}
