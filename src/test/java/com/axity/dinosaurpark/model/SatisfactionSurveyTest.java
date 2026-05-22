package com.axity.dinosaurpark.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class SatisfactionSurveyTest {
    
    @Test
    void testSurveyCreation() {
        LocalDateTime now = LocalDateTime.now();
        SatisfactionSurvey survey = new SatisfactionSurvey(1, 5, "Encierro VIP", 5, now);
        
        assertEquals(1, survey.id());
        assertEquals(5, survey.touristId());
        assertEquals("Encierro VIP", survey.enclosureName());
        assertEquals(5, survey.score());
        assertEquals(now, survey.respondedAt());
    }
    
    @Test
    void testIsExcellent() {
        SatisfactionSurvey survey1 = new SatisfactionSurvey(1, 1, "Test", 5, LocalDateTime.now());
        SatisfactionSurvey survey2 = new SatisfactionSurvey(2, 2, "Test", 4, LocalDateTime.now());
        SatisfactionSurvey survey3 = new SatisfactionSurvey(3, 3, "Test", 3, LocalDateTime.now());
        
        assertTrue(survey1.isExcellent());
        assertTrue(survey2.isExcellent());
        assertFalse(survey3.isExcellent());
    }
    
    @Test
    void testIsPoor() {
        SatisfactionSurvey survey1 = new SatisfactionSurvey(1, 1, "Test", 1, LocalDateTime.now());
        SatisfactionSurvey survey2 = new SatisfactionSurvey(2, 2, "Test", 2, LocalDateTime.now());
        SatisfactionSurvey survey3 = new SatisfactionSurvey(3, 3, "Test", 3, LocalDateTime.now());
        
        assertTrue(survey1.isPoor());
        assertTrue(survey2.isPoor());
        assertFalse(survey3.isPoor());
    }
    
    @Test
    void testInvalidScoreThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SatisfactionSurvey(1, 1, "Test", 0, LocalDateTime.now());
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new SatisfactionSurvey(1, 1, "Test", 6, LocalDateTime.now());
        });
    }
}