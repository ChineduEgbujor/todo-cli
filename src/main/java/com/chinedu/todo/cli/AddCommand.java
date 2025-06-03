package com.chinedu.todo.cli;

import com.chinedu.todo.model.Task;
import com.chinedu.todo.repository.TaskRepository;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * picocli command to add a new task.
 *
         * <p>Usage example:
        * <pre>
 *   todo add "Buy milk"
        * </pre>
        * </p>
 *
 */
@Command(name = "add", description = "Add a new task")
public class AddCommand implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(AddCommand.class);
    private final TaskRepository repo;

    /** Words forming the task’s description. */
    @Parameters(index = "0..*", description = "The task description")
    private  String[] descriptionWords;

    /**
     * Optional comma-separated tags (e.g. --tags work,urgent).
     */
    @Option(names = {"-t", "--tag"}, split = ",", description = "Comma-separated tags")
    private List<String> tagList;


    /**
     * @param repo the repository used to persist tasks; must not be null
     */
    public AddCommand(TaskRepository repo){
        this.repo = repo;
    }

    /**
     * Builds the description string, creates a new Task, saves it,
     * and prints the result.
     */
    @Override
    public void run() {
        String description = String.join(" ", descriptionWords);
        Task t = new Task(description);

        //if user passed tags, add them to the Task
        if (tagList != null) {
            t.getTags().addAll(tagList);
        }
        Task saved = repo.save(t);
        log.info("Adding new task: {}", description);
        System.out.println("Added: "+ saved);
    }
}
