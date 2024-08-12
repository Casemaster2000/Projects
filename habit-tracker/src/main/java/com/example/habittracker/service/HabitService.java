package com.example.habittracker.service;

import com.example.habittracker.exception.HabitNotFoundException;
import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class HabitService {

    private final HabitRepository habitRepository;

    @Autowired
    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public List<Habit> getAllHabits() {
        return habitRepository.findAll();
    }

    public Habit getHabitById(Long id) {
        return habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException(id));
    }

    public Habit createHabit(Habit habit) {
        habit.setCurrentStreak(0);
        habit.setLongestStreak(0);
        habit.setProgressToday(0);
        return habitRepository.save(habit);
    }

    @Transactional
    public Habit updateHabit(Long id, Habit habitDetails) {
        Habit habit = getHabitById(id);
        habit.setName(habitDetails.getName());
        habit.setDescription(habitDetails.getDescription());
        habit.setTargetFrequency(habitDetails.getTargetFrequency());
        return habitRepository.save(habit);
    }


    public void deleteHabit(Long id) {
        if (!habitRepository.existsById(id)) {
            throw new HabitNotFoundException(id);
        }
        habitRepository.deleteById(id);
    }

    @Transactional
    public Habit completeHabit(Long id) {
        Habit habit = getHabitById(id);
        LocalDate today = LocalDate.now();
        
        if (habit.getLastCompletedDate() == null || !habit.getLastCompletedDate().equals(today)) {
            habit.setProgressToday(1);
            habit.setCurrentStreak(habit.getCurrentStreak() + 1);
            if (habit.getCurrentStreak() > habit.getLongestStreak()) {
                habit.setLongestStreak(habit.getCurrentStreak());
            }
        } else {
            habit.setProgressToday(habit.getProgressToday() + 1);
        }

        habit.setLastCompletedDate(today);
        return habitRepository.save(habit);
    }

    public List<Habit> searchHabitsByName(String name) {
        return habitRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Habit> getHabitsByMinimumStreak(Integer streak) {
        return habitRepository.findByCurrentStreakGreaterThanEqual(streak);
    }
}