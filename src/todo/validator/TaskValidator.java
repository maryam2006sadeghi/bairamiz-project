package todo.validator;

import db.Entity;
import db.Validator;
import db.exception.InvalidEntityException;
import todo.entity.Task;

public class TaskValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Task)) {
            throw new IllegalArgumentException("This is not a valid type of entity");
        }

        Task task = (Task) entity;

        if(task.title == null)
            throw new InvalidEntityException("Title field cannot be empty");
    }
}
