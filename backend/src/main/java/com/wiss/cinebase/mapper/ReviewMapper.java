package com.wiss.cinebase.mapper;

// Importiert das Data Transfer Object für Reviews.
import com.wiss.cinebase.dto.ReviewDTO;
// Importiert die Entity-Klasse für Reviews.
import com.wiss.cinebase.entity.Review;

// Importiert Listen-Utilities.
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper für die Konvertierung zwischen Review Entity und ReviewDTO.
 * Quelle: Block 04A - Mapper Pattern
 */
public class ReviewMapper {

    /**
     * Wandelt eine Datenbank-Entity in ein API-DTO um.
     * Löst die Objekt-Beziehungen (User, Movie) in einfache IDs/Strings auf,
     * um Zirkelbezüge im JSON zu vermeiden.
     * @param review Die zu konvertierende Entity.
     * @return Das resultierende DTO oder null.
     */
    public static ReviewDTO toDTO(Review review) {
        if (review == null) return null;

        return new ReviewDTO(
                review.getId(),
                review.getUser().getUsername(), // ! Setzt den Namen des Autors statt des ganzen Objekts.
                review.getMovie().getId(),      // ! Setzt die ID des Films statt des ganzen Objekts.
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }

    /**
     * Wandelt ein API-DTO in eine Datenbank-Entity um.
     * @param dto Das zu konvertierende DTO.
     * @return Die resultierende Entity oder null.
     */
    public static Review toEntity(ReviewDTO dto) {
        if (dto == null) return null;

        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        // ! ID, CreatedAt, User und Movie werden im Service gesetzt/verwaltet.
        // Der Mapper überträgt nur den Nutzdaten-Teil.

        return review;
    }

    /**
     * Hilfsmethode für Listen.
     * Wandelt eine Liste von Review-Entities in eine Liste von ReviewDTOs um.
     * @param reviews Liste der Entities.
     * @return Liste der DTOs.
     */
    public static List<ReviewDTO> toDTOList(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewMapper::toDTO)
                .collect(Collectors.toList());
    }
}