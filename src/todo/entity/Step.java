package todo.entity;

import db.Entity;

public class Step extends Entity {
    private static final int STEP_ENTITY_CODE = 17;
    public String title;
    private Status status;
    private int taskRef;

    public enum Status{
        NotStarted,
        Completed, getStatus;
    }

    public Step (String title, int taskRef){
        this.title = title;
        this.taskRef = taskRef;
        this.status = Status.NotStarted;
    }

    @Override
    public Step copy() {
        Step copyStep = new Step(this.title, this.taskRef);
        copyStep.id = id;
        copyStep.status = this.status;

        return copyStep;
    }

    @Override
    public int getEntityCode() {
        return STEP_ENTITY_CODE;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTaskRef() {
        return taskRef;
    }

    public void setTaskRef(int taskRef) {
        this.taskRef = taskRef;
    }
}

