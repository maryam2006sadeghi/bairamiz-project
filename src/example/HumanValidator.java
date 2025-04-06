package example;

import db.Entity;
import db.Validator;
import db.exception.InvalidEntityException;

public class HumanValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Human)) {
            throw new IllegalArgumentException("This is not a valid type of entity");
        }

        Human human = (Human) entity;
        if (human.age < 0)
            throw new InvalidEntityException("Age must be a positive integer");

        if (human.name == null)
            throw new InvalidEntityException("Name field cannot be empty");
    }
}

