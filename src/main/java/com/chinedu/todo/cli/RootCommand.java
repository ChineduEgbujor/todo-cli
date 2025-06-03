package com.chinedu.todo.cli;

import com.chinedu.todo.repository.TaskRepository;
import picocli.CommandLine.Command;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Command(
        name = "todo",
        mixinStandardHelpOptions = true,
        version = "todo-cli 1.0",
        description = {
                "A simple CLI for managing tasks.",
                "Subcommands:",
                " add       Add a new task (you can use --tags to assign tags)",
                " list      List tasks (filter by --pending, --done, or --tag)",
                " done      Mark a task as done",
                " delete    Delete a task"
        }
)
public class RootCommand implements Runnable {
    private final TaskRepository repo;
    public RootCommand(TaskRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run() {
        System.out.println("Use subcommands: add, list, done, delete. See `todo --help`.");
    }
}
