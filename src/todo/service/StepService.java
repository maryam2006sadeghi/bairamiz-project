package todo.service;

import db.Database;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;

public class StepService {
    public static void saveStep(String title, int taskRef) throws InvalidEntityException {
        Step step = new Step(title,taskRef);
        Database.add(step);
    }

    public static void addStep(String title, int taskRef) throws InvalidEntityException {
        Step step = new Step(title, taskRef);
        step.setStatus(Step.Status.NotStarted);
        Database.add(step);
    }
<<<<<<< Updated upstream

=======
    
>>>>>>> Stashed changes
    public static void updateStepTitle(int stepId, String newTitle) throws InvalidEntityException {
        Step step = (Step) Database.get(stepId);
        step.title = newTitle;
        Database.update(step);
    }

    public static void setAsCompleted(int id) throws InvalidEntityException {
        Step step = (Step) Database.get(id);
        step.setStatus(Step.Status.Completed);
        Database.update(step);
    }

    public static void deleteStep(int stepId) throws EntityNotFoundException {
        Database.delete(stepId);
    }
}
