package com.chinedu.todo.cli;

import com.chinedu.todo.model.Task;
import com.chinedu.todo.repository.InMemoryTaskRepository;
import picocli.CommandLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ExportCommandTest {
    private InMemoryTaskRepository repo;
    private Path tempFile;

    @BeforeEach
    void setup() throws IOException {
        repo = new InMemoryTaskRepository();
        repo.save(new Task("A"));
        Task t2 = new Task("B");
        t2.setDone(true);
        t2.getTags().add("x");
        repo.save(t2);
        tempFile = Files.createTempFile("tasks", ".csv");
    }

    @Test
    void testExportAll() throws IOException {
        new CommandLine(new ExportCommand(repo))
                .execute("--out", tempFile.toString());
        String content = Files.readString(tempFile);
        assertTrue(content.contains("ID,Description,Done,CreatedAt,Tags"));
        assertTrue(content.contains("A"));
        assertTrue(content.contains("B"));
    }

    @Test
    void testExportFilterDone() throws IOException {
        new CommandLine(new ExportCommand(repo))
                .execute("--done", "--out", tempFile.toString());
        String content = Files.readString(tempFile);
        assertFalse(content.contains("A,"));
        //assertEquals("B", content);
        assertTrue(content.contains("B"));
    }
}
