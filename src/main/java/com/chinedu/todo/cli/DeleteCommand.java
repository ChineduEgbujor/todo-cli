package com.chinedu.todo.cli;

import com.chinedu.todo.repository.TaskRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(name = "delete", description = "Delete a task")
public class DeleteCommand implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(DeleteCommand.class);
    private final TaskRepository repo;
    @Parameters(index = "0", description = "ID of the task to delete")
    private int id;

    public DeleteCommand(TaskRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run() {
        repo.delete(id);
        log.info("Deleting task {}", id);
        System.out.println("Deleted task " + id);
    }
}
