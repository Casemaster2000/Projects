package com.example.habittracker.web;

import com.example.habittracker.model.Habit;
import com.example.habittracker.service.HabitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/habits")
public class HabitWebController {

    private final HabitService habitService;

    public HabitWebController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping
    public String listHabits(Model model) {
        model.addAttribute("habits", habitService.getAllHabits());
        return "habit-list";
    }

    @GetMapping("/create")
    public String createHabitForm(Model model) {
        model.addAttribute("habit", new Habit());
        return "habit-form";
    }

    @PostMapping("/create")
    public String createHabit(@ModelAttribute Habit habit) {
        habitService.createHabit(habit);
        return "redirect:/habits";
    }

    @GetMapping("/edit/{id}")
    public String editHabitForm(@PathVariable Long id, Model model) {
        model.addAttribute("habit", habitService.getHabitById(id));
        return "habit-form";
    }

    @PostMapping("/edit/{id}")
    public String updateHabit(@PathVariable Long id, @ModelAttribute Habit habit) {
        habitService.updateHabit(id, habit);
        return "redirect:/habits";
    }

    @GetMapping("/delete/{id}")
    public String deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return "redirect:/habits";
    }

    @GetMapping("/complete/{id}")
    public String completeHabit(@PathVariable Long id) {
        habitService.completeHabit(id);
        return "redirect:/habits";
    }
}