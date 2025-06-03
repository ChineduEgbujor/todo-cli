package com.chinedu.todo.repository;

import com.chinedu.todo.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskRepositoryTest {
    private InMemoryTaskRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryTaskRepository();
    }

    @Test
    void testSaveAndFindById() {
        Task t = new Task("Write tests");
        Task saved = repo.save(t);
        assertTrue(saved.getId() > 0);
        Optional<Task> fetched = repo.findById(saved.getId());
        assertTrue(fetched.isPresent());
        assertEquals("Write tests", fetched.get().getDescription());
    }

    @Test
    void testFindAllAndDelete() {
        repo.save(new Task("Task1"));
        repo.save(new Task("Task2"));
        List<Task> all = repo.findAll();
        assertEquals(2, all.size());

        int idToDelete = all.get(0).getId();
        repo.delete(idToDelete);
        assertFalse(repo.findById(idToDelete).isPresent());
    }

    @Test
    void testUpdateTask() {
        Task t = repo.save(new Task("Initial"));
        t.setDescription("Updated");
        repo.save(t);
        Task updated = repo.findById(t.getId()).get();
        assertEquals("Updated", updated.getDescription());
    }



}
