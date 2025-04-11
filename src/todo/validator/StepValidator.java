package todo.validator;

import db.Database;
import db.Entity;
import db.Validator;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

public class StepValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Step)) {
            throw new IllegalArgumentException("This is not a valid type of entity");
        }

        Step step = (Step) entity;

        if (step.title == null)
            throw new InvalidEntityException("Title field cannot be empty");

        Task task = (Task) Database.get(step.getTaskRef());

        if (!(entity instanceof Task))
            throw new InvalidEntityException("Task does not exist for this Step");
    }
}
