package com.example.habittracker.web;

import com.example.habittracker.model.Habit;
import com.example.habittracker.service.HabitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/habits")
public class HabitWebController {

    private final HabitService habitService;

    public HabitWebController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping
    public String listHabits(Model model) {
        List<Habit> habits = habitService.getAllHabits();
        int totalEngagement = habits.stream().mapToInt(Habit::getCurrentStreak).sum();
        model.addAttribute("habits", habits);
        model.addAttribute("totalEngagement", totalEngagement);
        return "habit-dashboard";
    }

    @PostMapping("/create")
    @ResponseBody
    public Habit createHabit(@RequestBody Habit habit) {
        return habitService.createHabit(habit);
    }

    @PostMapping("/complete/{id}")
    @ResponseBody
    public Habit completeHabit(@PathVariable Long id) {
        return habitService.completeHabit(id);
    }

    @PostMapping("/edit/{id}")
    @ResponseBody
    public Habit editHabit(@PathVariable Long id, @RequestBody Habit habitDetails) {
        return habitService.updateHabit(id, habitDetails);
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public void deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
    }

    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}