package com.example.habittracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDate;


@Entity
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @NotNull(message = "Target frequency is required")
    @Min(value = 1, message = "Target frequency must be at least 1")
    @Max(value = 100, message = "Target frequency must be at most 100")
    private Integer targetFrequency;

    @Min(value = 0, message = "Current streak cannot be negative")
    private Integer currentStreak;

    @Min(value = 0, message = "Longest streak cannot be negative")
    private Integer longestStreak;

    private LocalDate lastCompletedDate;

    @Min(value = 0, message = "Progress today cannot be negative")
    @Max(value = 100, message = "Progress today must be at most 100")
    private Integer progressToday;

    // Default constructor
    public Habit() {
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.progressToday = 0;
    }

    // Constructor with name and target frequency
    public Habit(String name, Integer targetFrequency) {
        this();
        this.name = name;
        this.targetFrequency = targetFrequency;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTargetFrequency() {
        return targetFrequency;
    }

    public void setTargetFrequency(Integer targetFrequency) {
        this.targetFrequency = targetFrequency;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }

    public LocalDate getLastCompletedDate() {
        return lastCompletedDate;
    }

    public void setLastCompletedDate(LocalDate lastCompletedDate) {
        this.lastCompletedDate = lastCompletedDate;
    }

    public Integer getProgressToday() {
        return progressToday;
    }

    public void setProgressToday(Integer progressToday) {
        this.progressToday = progressToday;
    }

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", targetFrequency=" + targetFrequency +
                ", currentStreak=" + currentStreak +
                ", longestStreak=" + longestStreak +
                ", lastCompletedDate=" + lastCompletedDate +
                ", progressToday=" + progressToday +
                '}';
    }
}