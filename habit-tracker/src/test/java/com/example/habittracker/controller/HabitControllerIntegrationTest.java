package com.example.habittracker.controller;

import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HabitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Habit testHabit;

    @BeforeEach
    void setUp() {
        habitRepository.deleteAll();
        testHabit = new Habit();
        testHabit.setName("Test Habit");
        testHabit.setDescription("Test Description");
        testHabit.setTargetFrequency(1);
        testHabit = habitRepository.save(testHabit);
    }

    @Test
    void whenCreateHabit_thenStatus201AndHabitReturned() throws Exception {
        Habit newHabit = new Habit();
        newHabit.setName("New Habit");
        newHabit.setDescription("New Description");
        newHabit.setTargetFrequency(2);

        mockMvc.perform(post("/api/habits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newHabit)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Habit"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.targetFrequency").value(2));
    }

    @Test
    void whenGetAllHabits_thenStatus200AndHabitListReturned() throws Exception {
        mockMvc.perform(get("/api/habits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Test Habit"));
    }

    @Test
    void whenGetHabitById_thenStatus200AndHabitReturned() throws Exception {
        mockMvc.perform(get("/api/habits/{id}", testHabit.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Habit"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void whenGetNonExistentHabit_thenStatus404() throws Exception {
        mockMvc.perform(get("/api/habits/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdateHabit_thenStatus200AndUpdatedHabitReturned() throws Exception {
        Habit updatedHabit = new Habit();
        updatedHabit.setName("Updated Habit");
        updatedHabit.setDescription("Updated Description");
        updatedHabit.setTargetFrequency(3);

        mockMvc.perform(put("/api/habits/{id}", testHabit.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedHabit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Habit"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.targetFrequency").value(3));
    }

    @Test
    void whenDeleteHabit_thenStatus204() throws Exception {
        mockMvc.perform(delete("/api/habits/{id}", testHabit.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/habits/{id}", testHabit.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCompleteHabit_thenStatus200AndUpdatedHabitReturned() throws Exception {
        mockMvc.perform(post("/api/habits/{id}/complete", testHabit.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentStreak").value(1))
                .andExpect(jsonPath("$.lastCompletedDate").isNotEmpty());
    }

    @Test
    void whenCreateInvalidHabit_thenStatus400() throws Exception {
        Habit invalidHabit = new Habit();
        invalidHabit.setName(""); // Set an empty name instead of null
        invalidHabit.setDescription("Invalid Habit");
        invalidHabit.setTargetFrequency(1);
    
        mockMvc.perform(post("/api/habits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidHabit)))
                .andExpect(status().isBadRequest());
              //  .andExpect(jsonPath("$.errors").exists()) // Expect an errors field in the response
              //  .andExpect(jsonPath("$.errors.name").value("Name is required")); // Expect a specific error message
    }

    @Test
    void whenCreateHabitWithNullName_thenStatus400() throws Exception {
        Habit invalidHabit = new Habit();
        // Don't set the name at all
        invalidHabit.setDescription("Invalid Habit");
        invalidHabit.setTargetFrequency(1);
    
        mockMvc.perform(post("/api/habits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidHabit)))
                .andExpect(status().isBadRequest());
    }
}