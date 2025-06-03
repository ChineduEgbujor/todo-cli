package com.chinedu.todo.repository;

import com.chinedu.todo.model.Task;
import java.util.List;
import java.util.Optional;

/**
 * Defines CRUD operations for storing and retrieving tasks.
 * Implementations may keep data in memory, on disk, or in a database.
 *
 * @since 1.0
 */
public interface TaskRepository {
    /**
     * Returns all tasks, in no particular order.
     *
     * @return a List of all stored Task objects; never null
     */
    List<Task> findAll();

    /**
     * Finds a task by its unique ID.
     *
     * @param id the ID of the task to find
     * @return an Optional containing the Task if found, or empty if not found
     */
    Optional<Task> findById(int id);

    /**
     * Saves a new task or updates an existing one.
     * If the {@code task.getId()} is zero, assigns a new unique ID.
     *
     * @param task the Task to save or update; must not be null
     * @return the perisisted Task instance, with ID and timestamps set
     * @throws IllegalArgumentException if {@code task} is null
     */
    Task save(Task task);//returns saved task (with ID set)

    /**
     * Deletes the task with the given ID, if it exists.
     * Does nothing if no such task is present
     *
     * @param id the ID of the task to delete
     */
    void delete(int id);
}
