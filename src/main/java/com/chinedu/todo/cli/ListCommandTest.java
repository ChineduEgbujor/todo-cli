package com.chinedu.todo.cli;

import com.chinedu.todo.model.Task;
import com.chinedu.todo.repository.InMemoryTaskRepository;
import com.chinedu.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class ListCommandTest {
    private TaskRepository repo;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        repo = new InMemoryTaskRepository();

        // Task 1: no tags, not done
        Task t1 = new Task ("Task A");
        repo.save(t1);

        //Task 2: done, tag "work
        Task t2 = new Task("Task B");
        t2.setDone(true);
        t2.getTags().add("work");
        repo.save(t2);

        //Task 3: not done, tags "home" and "urgent"
        Task t3 = new Task("Task C");
        t3.getTags().add("home");
        t3.getTags().add("urgent");
        repo.save(t3);

        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    private String runList(String... args) {
        new CommandLine(new ListCommand(repo)).execute(args);
        return out.toString().trim();
    }

    @Test
    void testListAll() {
        String output = runList();
        assertTrue(output.contains("❏ Task A"));
        assertTrue(output.contains("✔ Task B (work)"));
        assertTrue(output.contains("❏ Task C (urgent,home)"));
    }

    @Test
    void testFilterPending() {
        String output = runList("--pending");
        assertTrue(output.contains("❏ Task A"));
        assertFalse(output.contains("✔ Task B"));
        assertTrue(output.contains("❏ Task C"));
    }

    @Test
    void testFilterDone() {
        String output = runList("--done");
        assertFalse(output.contains("❏ Task A"));
        assertTrue(output.contains("✔ Task B (work)"));
        assertFalse(output.contains("❏ Task C"));
    }

    @Test
    void testFilterByTag_Work() {
        String output = runList("--tag", "work");
        assertFalse(output.contains("❏ Task A"));
        assertTrue(output.contains("✔ Task B (work)"));
        assertFalse(output.contains("❏ Task C"));
    }

    @Test
    void testFilterByTag_Home() {
        String output = runList("--tag", "home");
        assertFalse(output.contains("❏ Task A"));
        assertFalse(output.contains("✔ Task B (work)"));
        assertTrue(output.contains("❏ Task C (urgent,home)"));
    }

    @Test
    void testFilterByTag_NoMatch() {
        String output = runList("--tag", "nonexistent");
        assertEquals("No tasks to show", output);
    }


    @Test
    void testListPending() {
        String output = runList("--pending");
        assertTrue(output.contains("❏ A"));
        assertFalse(output.contains("✔ B"));
    }

    @Test
    void testListDone() {
        String output = runList("--done");
        assertFalse(output.contains("❏ A"));
        assertTrue(output.contains("✔ B"));

    }
}
