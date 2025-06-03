package com.chinedu.todo.cli;

import com.chinedu.todo.model.Task;
import com.chinedu.todo.repository.TaskRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.List;
import java.util.stream.Collectors;

/**
 * picocli command to list tasks, optionally filtered by status
 *
 * Usage examples:
 *  todo list               # all tasks (default)
 *  todo list --pending     # only incomplete tasks
 *  todo list --done        # only completed tasks
 *  todo list --tag work    # only tasks tagged 'work
 */
@Command(name = "list", description = "List tasks (all, pending, or done)")
public class ListCommand implements Runnable {
    private final TaskRepository repo;
    public ListCommand(TaskRepository repo) {
        this.repo = repo;
    }

    @Option(names = {"-p", "--pending"},
            description = "show only tasks not yet done",
            defaultValue = "false")
    private boolean pending;

    @Option(names = {"-d", "--done"},
            description = "show only tasks marked done",
            defaultValue = "false")
    private boolean done;

    @Option(names = {"-g", "--tag"},
    description = "Show only tasks with tis tag")
    private String tag;

    @Override
    public void run() {
        List<Task> tasks = repo.findAll();

        //1) Filter by done/pending flags
        if (pending && !done){
            tasks = tasks.stream()
                    .filter(t -> !t.isDone())
                    .collect(Collectors.toList());
        } else if (done && !pending) {
            tasks = tasks.stream()
                    .filter(Task::isDone)
                    .collect(Collectors.toList());
        } //if both or neither flag, show all

        // 2) Filter by tag if provided
        if (tag != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getTags().contains(tag))
                    .collect(Collectors.toList());
        }

        // 3) Print or indicate "no tasks"
        if (tasks.isEmpty()) {
            System.out.println("No tasks to show");
        } else {
            tasks.forEach(System.out::println);
        }
//        for (Task t : repo.findAll()){
//            System.out.println(t);
//        }
    }
}
