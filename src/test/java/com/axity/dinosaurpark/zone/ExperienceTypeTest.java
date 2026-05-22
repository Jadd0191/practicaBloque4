package com.axity.dinosaurpark.zone;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ExperienceTypeTest {
    
    @Test
    void testBasicRange() {
        Random rng = new Random();
        for (int i = 0; i < 100; i++) {
            int score = ExperienceType.BASIC.generateRandomScore(rng);
            assertTrue(score >= 1 && score <= 3);
        }
    }
    
    @Test
    void testPremiumRange() {
        Random rng = new Random();
        for (int i = 0; i < 100; i++) {
            int score = ExperienceType.PREMIUM.generateRandomScore(rng);
            assertTrue(score >= 2 && score <= 4);
        }
    }
    
    @Test
    void testVipRange() {
        Random rng = new Random();
        for (int i = 0; i < 100; i++) {
            int score = ExperienceType.VIP.generateRandomScore(rng);
            assertTrue(score >= 3 && score <= 5);
        }
    }
    
    @Test
    void testMinMaxScores() {
        assertEquals(1, ExperienceType.BASIC.getMinScore());
        assertEquals(3, ExperienceType.BASIC.getMaxScore());
        assertEquals(2, ExperienceType.PREMIUM.getMinScore());
        assertEquals(4, ExperienceType.PREMIUM.getMaxScore());
        assertEquals(3, ExperienceType.VIP.getMinScore());
        assertEquals(5, ExperienceType.VIP.getMaxScore());
    }
}