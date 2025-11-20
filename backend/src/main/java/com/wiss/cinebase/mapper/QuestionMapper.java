package com.wiss.cinebase.mapper;

import com.wiss.cinebase.dto.QuestionDTO;
import com.wiss.cinebase.dto.QuestionFormDTO;
import com.wiss.cinebase.entity.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mapper-Klasse für die Konvertierung zwischen Question Entity und verschiedenen DTOs.
 * <p>
 *     Diese Utility-Klasse stellt statische Methoden zur Verfügung, um Question-Objekte
 *     zwischen verschiedenen Repräsentationen zu konvertieren:
 * </p>
 * <ul>
 *     <li>{@link Question} - JPA Entity für die Datenbank</li>
 *     <li>{@link QuestionDTO} - DTO für Quiz-Spiel mit gemischten Antworten</li>
 *     <li>{@link QuestionFormDTO} - DTO für Admin-Formulare mit getrennten Antworten</li>
 * </ul>
 *
 * @author Johnny Krup
 * @version 1.0
 * @since 2025.06.01
 */
public class QuestionMapper {

    /**
     * Konvertiert eine Question Entity in ein QuestionDTO für das Quiz-Spiel.
     * <p>
     * Diese Methode sammelt alle Antworten (korrekte + falsche) und mischt sie
     * zufällig, damit die korrekte Antwort nicht immer an derselben Position steht.
     * Dies ist wichtig für eine faire Quiz-Experience im Frontend.
     *
     * @param entity Die Question Entity aus der Datenbank
     * @return Das QuestionDTO mit gemischten Antworten für das Quiz-Spiel,
     *         oder {@code null} falls die Entity {@code null} ist
     *
     * @see QuestionDTO
     * @see Collections#shuffle(List)
     */
    public static QuestionDTO toDTO(Question entity) {
        if (entity == null) {
            return null;
        }

        // Alle Antworten sammeln
        List<String> allAnswers = new ArrayList<>();
        allAnswers.addAll(entity.getIncorrectAnswers());
        allAnswers.add(entity.getCorrectAnswer());

        // Antworten mischen - wichtig für Frontend!
        Collections.shuffle(allAnswers);

        return new QuestionDTO(
                entity.getId(),               // ← ID hinzufügen!
                entity.getQuestion(),
                entity.getCorrectAnswer(),
                allAnswers,
                entity.getCategory(),
                entity.getDifficulty()
        );
    }

    /**
     * Konvertiert eine Question Entity in ein QuestionFormDTO für Admin-Formulare.
     * <p>
     * Im Gegensatz zu {@link #toDTO(Question)} werden hier die Antworten NICHT gemischt,
     * da in Admin-Formularen die korrekte und falschen Antworten getrennt
     * bearbeitet werden sollen.
     *
     * @param entity Die Question Entity aus der Datenbank
     * @return Das QuestionFormDTO mit getrennten Antworten für Bearbeitungsformulare,
     *         oder {@code null} falls die Entity {@code null} ist
     *
     * @see QuestionFormDTO
     */
    public static QuestionFormDTO toFormDTO(Question entity) {
        if (entity == null) {
            return null;
        }

        return new QuestionFormDTO(
                entity.getId(),
                entity.getQuestion(),
                entity.getCorrectAnswer(),
                entity.getIncorrectAnswers(),
                entity.getCategory(),
                entity.getDifficulty()
        );
    }

    /**
     * Konvertiert ein QuestionDTO zurück in eine Question Entity.
     * <p>
     * Diese Methode filtert die korrekte Antwort aus der gemischten Antwortliste
     * heraus, um die ursprüngliche Struktur mit getrennten korrekten und
     * falschen Antworten wiederherzustellen.
     *
     * @param dto Das QuestionDTO vom Frontend (z.B. aus einem REST-Request)
     * @return Die Question Entity für die Datenbank-Persistierung,
     *         oder {@code null} falls das DTO {@code null} ist
     *
     * @see QuestionDTO
     * @see Question
     */
    public static Question toEntity(QuestionDTO dto) {
        if (dto == null) {
            return null;
        }

        // correctAnswer aus answers herausfiltern
        List<String> incorrectAnswers = dto.getAnswers().stream()
                .filter(answer -> !answer.equals(dto.getCorrectAnswer()))
                .toList();

        return new Question(
                dto.getQuestion(),
                dto.getCorrectAnswer(),
                incorrectAnswers,
                dto.getCategory(),
                dto.getDifficulty()
        );
    }

    /**
     * Konvertiert eine Liste von Question Entities in eine Liste von QuestionDTOs.
     * <p>
     * Utility-Methode für die Batch-Konvertierung, z.B. beim Laden aller Fragen
     * einer Kategorie für das Quiz-Spiel.
     *
     * @param entities Liste von Question Entities aus der Datenbank
     * @return Liste von QuestionDTOs mit gemischten Antworten,
     *         leere Liste, falls Input {@code null} oder leer ist
     *
     * @see #toDTO(Question)
     */
    public static List<QuestionDTO> toDTOList(List<Question> entities) {
        return entities.stream()
                .map(QuestionMapper::toDTO)
                .toList();
    }

    /**
     * Konvertiert eine Liste von Question Entities in eine Liste von QuestionFormDTOs.
     * <p>
     * Utility-Methode für die Batch-Konvertierung, z.B. beim Laden aller Fragen
     * für die Admin-Übersicht.
     *
     * @param entities Liste von Question Entities aus der Datenbank
     * @return Liste von QuestionFormDTOs mit getrennten Antworten,
     *         leere Liste, falls Input {@code null} oder leer ist
     *
     * @see #toFormDTO(Question)
     */
    public static List<QuestionFormDTO> toFormDTOList(List<Question> entities) {
        return entities.stream()
                .map(QuestionMapper::toFormDTO)
                .toList();
    }
}
