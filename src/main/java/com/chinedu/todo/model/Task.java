package com.chinedu.todo.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Task {
    private int id;
    private String description;
    private boolean done;
    private LocalDateTime createdAt;
    private Set<String> tags = new HashSet<>();


    public Task() {}

    public Task(String description) {
        this.description = description;
        this.done = false;
        this.createdAt = LocalDateTime.now();
    }

    //full constructor (for internal use/tests)
    public Task(int id, String description, boolean done, LocalDateTime createdAt, Set<String> tags){
        this.id = id;
        this.description = description;
        this.done = done;
        this.createdAt = createdAt;
        this.tags = (tags != null) ? new HashSet<>(tags) : new HashSet<>();
    }

    //getters & setters
    public int getId() {return id;}
    public void setId(int id) { this.id = id;}

    public String getDescription() { return description;}
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {return done;}
    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = (tags != null) ? new HashSet<>(tags): new HashSet<>();
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;
        return id == task.id &&
                done == task.done &&
                Objects.equals(description, task.description) &&
                Objects.equals(createdAt, task.createdAt) &&
                Objects.equals(tags, task.tags);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, done, createdAt, tags);
    }

    @Override
    public String toString() {
        String base = "[" + id + "]" + (done ? "✔ " : "❏ ") + description;
        if (!tags.isEmpty()) {
            base += " (" + String.join(",", tags) + ")";
        }
        return base;
    }



}
