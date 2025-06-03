package com.chinedu.todo.repository;

import com.chinedu.todo.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileTaskRepository implements TaskRepository {
    private static final Logger log = LoggerFactory.getLogger(FileTaskRepository.class);
    private final Path filePath;
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final Map<Integer, Task> store = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger();

    public FileTaskRepository(Path filePath){
        this.filePath = filePath;
        load();
        //set idCounter to max existing ID +1
        idCounter.set(store.keySet().stream().max(Integer::compareTo).orElse(0) + 1);
    }

    private void load() {
        File file = filePath.toFile();
        if (!file.exists() || file.length() == 0) return;
        try {
            List<Task> tasks = mapper.readValue(file, new TypeReference<List<Task>>(){});
            for (Task t: tasks) {
                store.put(t.getId(), t);
            }
        }catch (JsonProcessingException e) {
            // malformed JSON: log a warning and continue with empty store
            System.err.println("Warning: could not parse tasks.json, starting fresh: " + e.getMessage());
        }catch (IOException e){
            throw new RuntimeException("Failed to load tasks", e);
        }
    }

    private void persist() {
        try {
            List<Task> tasks = new ArrayList<>(store.values());
            mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), tasks);
            log.debug("Persisted {} tasks to {}", store.size(), filePath);
        } catch (IOException e){
            log.error("Failed to save tasks", e);
            throw new RuntimeException("Failed to save tasks", e);
        }
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Task> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Task save(Task task) {
        if (task.getId() == 0) {
            task.setId(idCounter.getAndIncrement());
        }
        store.put(task.getId(), task);
        persist();
        return task;
    }

    @Override
    public void delete(int id) {
        store.remove(id);
        persist();
    }

}
