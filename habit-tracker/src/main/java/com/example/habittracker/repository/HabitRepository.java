package com.example.habittracker.repository;

import com.example.habittracker.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    // Find habits by name (case-insensitive)
    List<Habit> findByNameContainingIgnoreCase(String name);

    // Find habits with a current streak greater than or equal to a given value
    List<Habit> findByCurrentStreakGreaterThanEqual(Integer streak);

    // You can add more custom query methods here if needed
}
