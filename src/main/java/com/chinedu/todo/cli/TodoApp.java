package com.chinedu.todo.cli;

import com.chinedu.todo.repository.FileTaskRepository;
import com.chinedu.todo.repository.InMemoryTaskRepository;
import com.chinedu.todo.repository.TaskRepository;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TodoApp {
    private static final Logger log = LoggerFactory.getLogger(TodoApp.class);
    public static void main(String[] args){
        log.info("Starting TodoApp with args: {}", (Object[]) args);
        Path dataFile = Paths.get(System.getProperty("user.home"), "todo-cli", "tasks.js");
        dataFile.toFile().getParentFile().mkdirs();
        TaskRepository repo = new FileTaskRepository(dataFile);
        //TaskRepository repo = new InMemoryTaskRepository();
        CommandLine cmd = new CommandLine(new RootCommand(repo));
        //register subcommands
        cmd.addSubcommand("add", new AddCommand(repo));
        cmd.addSubcommand("list", new ListCommand(repo));
        cmd.addSubcommand("done", new DoneCommand(repo));
        cmd.addSubcommand("delete", new DeleteCommand(repo));
        cmd.addSubcommand("export", new ExportCommand(repo));
        try{
            int exitCode = cmd.execute(args);
            System.exit(exitCode);
        } catch (Exception e){
            log.error("Command failed", e);
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
//        int exitCode = cmd.execute(args);
//        System.exit(exitCode);

    }
}
