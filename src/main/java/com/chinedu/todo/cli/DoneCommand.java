package com.chinedu.todo.cli;

import com.chinedu.todo.model.Task;
import com.chinedu.todo.repository.TaskRepository;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(name = "Done", description = "Mark a task as done")
public class DoneCommand implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(DoneCommand.class);
    private final TaskRepository repo;
    @Parameters(index = "0", description = "ID of the task to be mark done")
    private int id;

    public DoneCommand(TaskRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run() {
        Task t = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No task with ID" + id));
        t.setDone(true);
        repo.save(t);
        log.info("Marked done: {}", t);
        System.out.println("Marked done: " + t);
    }
}
