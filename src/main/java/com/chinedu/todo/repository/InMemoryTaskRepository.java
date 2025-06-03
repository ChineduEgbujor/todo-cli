package com.chinedu.todo.repository;

import com.chinedu.todo.model.Task;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryTaskRepository implements TaskRepository{
    private final Map<Integer, Task> store = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

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
        if (task.getId() == 0){
            int id = idCounter.getAndIncrement();
            task.setId(id);
        }
        store.put(task.getId(), task);
        return task;
    }

    @Override
    public void delete(int id){
        store.remove(id);
    }
}
