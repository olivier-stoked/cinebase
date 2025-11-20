package com.wiss.cinebase.repository;

import com.wiss.cinebase.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Spring generiert automatisch diese Standard-Methoden:
    // - Question save(Question question)           ← CREATE/UPDATE automatisch
    // - Optional<Question> findById(Long id)       ← READ by ID
    // - List<Question> findAll()                   ← READ all
    // - void deleteById(Long id)                   ← DELETE by ID
    // - boolean existsById(Long id)                ← EXISTS check
    // - long count()                               ← COUNT all
    // Plus noch viele mehr: saveAll, findAllById, deleteAll... +20 Methoden

    // Custom Query Methods (basierend auf Methoden-Namen):
    List<Question> findByCategory(String category);
    List<Question> findByDifficulty(String difficulty);

    // Spring übersetzt automatisch:
    // findByCategory → SELECT * FROM questions WHERE category = ?
    // findByDifficulty → SELECT * FROM questions WHERE difficulty = ?

    // Kombinierte Queries:
    List<Question> findByCategoryAndDifficulty(String category, String difficulty);
    List<Question> findByCategoryOrDifficulty(String category, String difficulty);

    // Text-Suche:
    List<Question> findByQuestionContaining(String keyword);
    List<Question> findByQuestionContainingIgnoreCase(String keyword);

    // Counting:
    long countByCategory(String category);
    long countByDifficulty(String difficulty);
    long countByCategoryAndDifficulty(String category, String difficulty);

    // Sortierung:
    List<Question> findByCategoryOrderByQuestionAsc(String category);
    List<Question> findByDifficultyOrderByIdDesc(String difficulty);

    // Existenz prüfen:
    boolean existsByQuestionAndCategory(String question, String category);

    // Top N Results:
    List<Question> findTop5ByCategory(String category);
    List<Question> findFirst3ByDifficultyOrderByIdAsc(String difficulty);

    // Random
    @Query(value = "SELECT * FROM questions WHERE category = :category ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true)
    List<Question> findRandomByCategory(@Param("category") String category, @Param("limit") int limit);

    @Query(value = "SELECT * FROM questions ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true)
    List<Question> findRandomQuestions(@Param("limit") int limit);

}
