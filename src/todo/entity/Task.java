package todo.entity;

import db.Entity;
import db.Trackable;

import java.util.Date;

public class Task extends Entity implements Trackable {
    private static final int TASK_ENTITY_CODE = 16;
    public String title;
    public String description;
    private Date dueDate;
    private Date creationDate;
    private Date lastModificationDate;
    private Status status;

    public enum Status{
        NotStarted,
        InProgress,
        Completed, getStatus;
    }

    public Task(String title, String description, Date dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = Status.NotStarted;
        this.creationDate = new Date();
    }

    @Override
    public Task copy() {
        Task copyTask = new Task(this.title, this.description, this.dueDate);
        copyTask.id = id;
        copyTask.status = this.status;

        if (this.creationDate != null) {
            copyTask.creationDate = new Date(this.creationDate.getTime());
        }

        if (this.lastModificationDate != null) {
            copyTask.lastModificationDate = new Date(this.lastModificationDate.getTime());
        }

        if (this.dueDate != null) {
            copyTask.dueDate = new Date(this.dueDate.getTime());
        }

        return copyTask;
    }

    @Override
    public int getEntityCode() {
        return TASK_ENTITY_CODE;
    }

    @Override
    public void setCreationDate(Date date) {
        this.creationDate = date;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setLastModificationDate(Date date) {
        this.lastModificationDate = lastModificationDate;
    }

    @Override
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
