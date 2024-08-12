package com.example.habittracker.controller;

import com.example.habittracker.model.Habit;
import com.example.habittracker.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;

    @Autowired
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping
    public ResponseEntity<List<Habit>> getAllHabits() {
        return ResponseEntity.ok(habitService.getAllHabits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habit> getHabitById(@PathVariable Long id) {
        return ResponseEntity.ok(habitService.getHabitById(id));
    }

    @PostMapping
    public ResponseEntity<?> createHabit(@Valid @RequestBody Habit habit, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(habitService.createHabit(habit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHabit(@PathVariable Long id, @Valid @RequestBody Habit habitDetails, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(habitService.updateHabit(id, habitDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Habit> completeHabit(@PathVariable Long id) {
        return ResponseEntity.ok(habitService.completeHabit(id));
    }
}