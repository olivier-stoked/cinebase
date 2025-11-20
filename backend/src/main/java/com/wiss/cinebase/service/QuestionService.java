package com.wiss.cinebase.service;

import com.wiss.cinebase.dto.QuestionDTO;
import com.wiss.cinebase.dto.QuestionFormDTO;
import com.wiss.cinebase.entity.Question;
import com.wiss.cinebase.exception.CategoryNotFoundException;
import com.wiss.cinebase.exception.DifficultyNotFoundException;
import com.wiss.cinebase.exception.QuestionNotFoundException;
import com.wiss.cinebase.mapper.QuestionMapper;
import com.wiss.cinebase.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    // Neue DTO-basierte Methoden
    public List<QuestionDTO> getAllQuestionsAsDTO() {
        List<Question> entities = repository.findAll();
        return QuestionMapper.toDTOList(entities);
    }

    public List<QuestionFormDTO> getAllQuestionsAsFormDTO() {
        List<Question> entities = repository.findAll();
        return QuestionMapper.toFormDTOList(entities);
    }

    public QuestionDTO getQuestionByIdAsDTO(Long id){
        if(id == null) {
            throw new IllegalArgumentException("Question ID cannot be null");
        }

        if (!repository.existsById(id)) {
            throw new QuestionNotFoundException(id);  // ✅ Exception werfen
        }

        Question entity = getQuestionById(id);
        return QuestionMapper.toDTO(entity);
    }

    public QuestionFormDTO getQuestionByIdAsFormDTO(Long id){
        if(id == null) {
            throw new IllegalArgumentException("Question ID cannot be null");
        }

        if (!repository.existsById(id)) {
            throw new QuestionNotFoundException(id);  // ✅ Exception werfen
        }

        Question entity = getQuestionById(id);
        return QuestionMapper.toFormDTO(entity);
    }

    public List<QuestionDTO> getQuestionsByCategoryAsDTO(String category) {
        List<Question> entities = getQuestionsByCategory(category);
        return QuestionMapper.toDTOList(entities);
    }

    public List<QuestionDTO> getQuestionsByDifficultyAsDTO(String difficulty){
        List<Question> entities = getQuestionsByDifficulty(difficulty);
        return QuestionMapper.toDTOList(entities);
    }

    public List<Question> getAllQuestions() {
        return repository.findAll();
    }

    public Question getQuestionById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        Optional<Question> optionalQuestion = repository.findById(id);
        if (optionalQuestion.isEmpty()) {
            throw new QuestionNotFoundException(id);
        }

        return optionalQuestion.get();
    }

    public List<Question> getQuestionsByCategory(String category) {
        validateCategory(category);
        return repository.findByCategory(category.toLowerCase());
    }

    public List<Question> getQuestionsByDifficulty(String difficulty) {
        validateDifficulty(difficulty);
        return repository.findByDifficulty(difficulty.toLowerCase());
    }

    public long getTotalQuestionsCount() {
        return repository.count();
    }

    /**
     * Create a new question
     * using save method without existing ID
     * @param questionDTO
     * @return
     */
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        // Validierung
        if (questionDTO.getQuestion() == null || questionDTO.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("Question text is required");
        }
        if (questionDTO.getCorrectAnswer() == null || questionDTO.getCorrectAnswer().trim().isEmpty()) {
            throw new IllegalArgumentException("Correct answer is required");
        }

        // 1. DTO zu Entity konvertieren (ohne ID - ID bleibt null)
        Question entity = QuestionMapper.toEntity(questionDTO);
        // 2. Repository.save() aufrufen (erkennt automatisch CREATE)
        Question newQuestion = repository.save(entity);
        // 3. Gespeicherte Entity zu DTO konvertieren
        QuestionDTO newDTO = QuestionMapper.toDTO(newQuestion); // <- Java gibt dir hier den Hinweis, dass dies gleich als Return zurückgegeben werden kann
        // 4. DTO zurückgeben
        return newDTO; // aus Demozwecken in 2 Zeilen als ein direktes return statement
    }

    /**
     * Create a new Question from the Frontend-Form
     *
     * @param question The question designed for collecting Question data
     * @return A question Form DTO that can be used in the Question Manager
     */
    public QuestionFormDTO createQuestionFromForm(Question question) {
        Question saved = repository.save(question);
        return QuestionMapper.toFormDTO(saved);
    }

    /**
     * Update a question
     * using save method with an existing ID
     * @param id
     * @param questionDTO
     * @return
     */
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
        // 1. Prüfen ob Frage existiert (repository.existsById())
        if(!repository.existsById(id)){
            throw new QuestionNotFoundException(id);
        }
        // 2. DTO zu Entity konvertieren UND ID setzen
        Question entity = QuestionMapper.toEntity(questionDTO);
        entity.setId(id); // ← Wichtig: ID setzen für UPDATE-Erkennung
        // 3. Repository.save() aufrufen (erkennt automatisch UPDATE)
        Question updatedEntity = repository.save(entity);
        // 4. Aktualisierte Entity zu DTO konvertieren
        return QuestionMapper.toDTO(updatedEntity);
    }

    public QuestionFormDTO updateQuestionFromForm(Long id, Question question){
        // Prüfen ob Frage existiert
        if(!repository.existsById(id)){
            throw new QuestionNotFoundException(id);
        }
        question.setId(id);

        Question updated = repository.save(question);
        return QuestionMapper.toFormDTO(updated);

    }

    /**
     * Delete a Question
     * using the id to identify the question
     * @param id
     * @return
     */
    public void deleteQuestion(Long id) {
        // 1. Prüfen ob Frage existiert
        if(!repository.existsById(id)){
            throw new QuestionNotFoundException(id);
        }
        // 2. Repository.deleteById() aufrufen
        // 3. Ergebnis zurückgeben
        repository.deleteById(id);
    }

    public List<QuestionDTO> getQuestionsByCategoryAndDifficulty(String category, String difficulty) {
        validateCategory(category);
        validateDifficulty(difficulty);

        List<Question> entities = repository.findByCategoryAndDifficulty(category, difficulty);
        return QuestionMapper.toDTOList(entities);
    }

    public List<QuestionDTO> searchQuestions(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }

        List<Question> entities = repository.findByQuestionContainingIgnoreCase(keyword.trim());
        return QuestionMapper.toDTOList(entities);
    }

    public long getQuestionCountByCategory(String category) {
        validateCategory(category);
        return repository.countByCategory(category);
    }

    public List<QuestionDTO> getRandomQuestions(int limit) {
        if (limit <= 0 || limit > 50) {
            throw new IllegalArgumentException("Limit must be between 1 and 50");
        }

        List<Question> entities = repository.findRandomQuestions(limit);
        return QuestionMapper.toDTOList(entities);
    }

    public List<QuestionDTO> getRandomQuestionsByCategory(String category, int limit) {
        validateCategory(category);
        if (limit <= 0 || limit > 50) {
            throw new IllegalArgumentException("Limit must be between 1 and 50");
        }

        List<Question> entities = repository.findRandomByCategory(category, limit);
        return QuestionMapper.toDTOList(entities);
    }

    private void validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }

        List<String> validCategories = List.of("sports", "games", "movies", "geography", "science", "history");
        if (!validCategories.contains(category.toLowerCase())) {
            throw new CategoryNotFoundException(category);
        }
    }

    private void validateDifficulty(String difficulty) {
        if (difficulty == null || difficulty.trim().isEmpty()) {
            throw new IllegalArgumentException("Difficulty cannot be null or empty");
        }

        List<String> validDifficulties = List.of("easy", "medium", "hard");
        if (!validDifficulties.contains(difficulty.toLowerCase())) {
            throw new DifficultyNotFoundException(difficulty);
        }
    }
}
