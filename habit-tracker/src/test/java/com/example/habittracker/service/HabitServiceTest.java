package com.example.habittracker.service;

/*import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.exception.HabitNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;*/


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.exception.HabitNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class HabitServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @InjectMocks
    private HabitService habitService;

    private Habit testHabit;

    @BeforeEach
    void setUp() {
        testHabit = new Habit();
        testHabit.setId(1L);
        testHabit.setName("Test Habit");
        testHabit.setDescription("Test Description");
        testHabit.setTargetFrequency(1);
        testHabit.setCurrentStreak(0);
        testHabit.setLongestStreak(0);
        testHabit.setProgressToday(0);
    }

    @Test
    void testGetAllHabits() {
        when(habitRepository.findAll()).thenReturn(Arrays.asList(testHabit));
        List<Habit> habits = habitService.getAllHabits();
        assertFalse(habits.isEmpty());
        assertEquals(1, habits.size());
        assertEquals("Test Habit", habits.get(0).getName());
    }

    @Test
    void testGetHabitById() {
        when(habitRepository.findById(1L)).thenReturn(Optional.of(testHabit));
        Habit habit = habitService.getHabitById(1L);
        assertNotNull(habit);
        assertEquals("Test Habit", habit.getName());
    }

    @Test
    void testGetHabitByIdNotFound() {
        when(habitRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(HabitNotFoundException.class, () -> habitService.getHabitById(1L));
    }

    @Test
    void testCreateHabit() {
        when(habitRepository.save(any(Habit.class))).thenReturn(testHabit);
        Habit createdHabit = habitService.createHabit(testHabit);
        assertNotNull(createdHabit);
        assertEquals("Test Habit", createdHabit.getName());
        assertEquals(0, createdHabit.getCurrentStreak());
        assertEquals(0, createdHabit.getLongestStreak());
        assertEquals(0, createdHabit.getProgressToday());
    }

    @Test
    void testUpdateHabit() {
        Habit updatedHabit = new Habit();
        updatedHabit.setName("Updated Habit");
        updatedHabit.setDescription("Updated Description");
        updatedHabit.setTargetFrequency(2);

        when(habitRepository.findById(1L)).thenReturn(Optional.of(testHabit));
        when(habitRepository.save(any(Habit.class))).thenReturn(updatedHabit);

        Habit result = habitService.updateHabit(1L, updatedHabit);
        assertNotNull(result);
        assertEquals("Updated Habit", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(2, result.getTargetFrequency());
    }

    @Test
    void testCompleteHabit() {
        when(habitRepository.findById(1L)).thenReturn(Optional.of(testHabit));
        when(habitRepository.save(any(Habit.class))).thenReturn(testHabit);

        Habit completedHabit = habitService.completeHabit(1L);
        assertNotNull(completedHabit);
        assertEquals(1, completedHabit.getCurrentStreak());
        assertEquals(1, completedHabit.getLongestStreak());
        assertEquals(1, completedHabit.getProgressToday());
        assertEquals(LocalDate.now(), completedHabit.getLastCompletedDate());
    }

    @Test
    void testDeleteHabit() {
        when(habitRepository.existsById(1L)).thenReturn(true);
        doNothing().when(habitRepository).deleteById(1L);
        assertDoesNotThrow(() -> habitService.deleteHabit(1L));
        verify(habitRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteHabitNotFound() {
        when(habitRepository.existsById(1L)).thenReturn(false);
        assertThrows(HabitNotFoundException.class, () -> habitService.deleteHabit(1L));
    }
}