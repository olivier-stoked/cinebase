package com.wiss.cinebase.mapper;

import com.wiss.cinebase.dto.ReviewDTO;
import com.wiss.cinebase.entity.Review;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper für die Konvertierung zwischen Review Entity und ReviewDTO.
 *
 * Quelle: Block 04A - Mapper Pattern
 */
public class ReviewMapper {

    /**
     * Wandelt eine Datenbank-Entity in ein API-DTO um.
     * Löst die Objekt-Beziehungen (User, Movie) in einfache IDs/Strings auf.
     */
    public static ReviewDTO toDTO(Review review) {
        if (review == null) return null;

        return new ReviewDTO(
                review.getId(),
                review.getUser().getUsername(), // Wir zeigen den Namen des Autors an
                review.getMovie().getId(),      // Wir zeigen die ID des Films an
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }

    /**
     * Wandelt ein API-DTO in eine Datenbank-Entity um.
     * Der Mapper übersetzt zwischen der Datenbank-Entity und dem DTO.
     * Hinweis: User und Movie werden hier NICHT gesetzt (das macht der Service).
     */
    public static Review toEntity(ReviewDTO dto) {
        if (dto == null) return null;

        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        // ID, CreatedAt, User und Movie werden im Service gesetzt/verwaltet

        return review;
    }

    public static List<ReviewDTO> toDTOList(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewMapper::toDTO)
                .collect(Collectors.toList());
    }
}
