package com.chinedu.todo.cli;

import com.chinedu.todo.model.Task;
import com.chinedu.todo.repository.TaskRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Export tasks to CSV. Honors the same filters as `list`.
 *
 * Usage:
 *  todo export [--pending] [--done] [--tag TAG] [--out FILE]
 *
 */
@Command(name = "export", description = "Export tasks to CSV file")
public class ExportCommand implements Runnable{
    private final TaskRepository repo;

    @Option(names = {"-p", "--pending"}, description = "Only pending tasks")
    private boolean pending;

    @Option(names = {"-d", "--done"}, description = "Only completed tasks")
    private boolean done;

    @Option(names = {"-g", "--tag"}, description = "Only tasks with this tag")
    private String tag;

    @Option(names = {"-o", "--out"},
            description = "Output CSV file (default: tasks.csv)",
            defaultValue = "tasks.csv")
    private Path outFile;

    public ExportCommand(TaskRepository repo){
        this.repo = repo;
    }

    @Override
    public void run() {
        // 1) Fetch and filter tasks
        List<Task> tasks = repo.findAll();
        if (pending && !done) {
            tasks = tasks.stream().filter(t -> !t.isDone()).collect(Collectors.toList());
        } else if (done && !pending){
            tasks = tasks.stream().filter(Task::isDone).collect(Collectors.toList());
        }
        if (tag != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getTags().contains(tag))
                    .collect(Collectors.toList());
        }

        // 2) Write CSV
        try (FileWriter writer = new FileWriter(outFile.toFile())) {
            //header
            writer.append("ID,Description,Done,CreatedAt,Tags\n");
            for (Task t : tasks) {
                //escape quotes in description
                String desc = t.getDescription().replace("\"","\"\"");
                String tags = String.join(";", t.getTags());
                writer.append(t.getId() + ",")
                        .append("\"").append(desc).append("\",")
                        .append(t.isDone() + ",")
                        .append(t.getCreatedAt().toString()).append(",")
                        .append("\"").append(tags).append("\"\n");
            }
            System.out.println("Exported " + tasks.size() + " tasks to " + outFile);
        } catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
        }
    }
}
