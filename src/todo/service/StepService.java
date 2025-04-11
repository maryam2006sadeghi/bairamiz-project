package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

public class StepService {
    public static int saveStep(String title, int taskRef) throws InvalidEntityException {
        Step step = new Step(title,taskRef);
        step.setStatus(Step.Status.NotStarted);
        Database.add(step);
        return step.id;
    }

    public static void updateStepTitle(int stepId, String newTitle) throws InvalidEntityException {
        Step step = (Step) Database.get(stepId);
        step.title = newTitle;
        Database.update(step);
    }

    public static void setAsCompleted(int id, String newValue) throws InvalidEntityException {
        Step step = (Step) Database.get(id);
        Task task = (Task) Database.get(((Step) Database.get(id)).getTaskRef());
        int counter = 0;
        int equalStatus = 0;

        for (Entity entity : Database.getAll(17)){
            if (entity instanceof Step && ((Step) entity).id == id){
                    if(((Step) entity).getStatus().equals(((Step) entity).getStatus()))
                       equalStatus ++;
                counter ++;
            }
        }

        task.setStatus(Task.Status.InProgress);
        step.setStatus(Step.Status.Completed);

        if (counter == equalStatus)
            task.setStatus(Task.Status.Completed);
        Database.update(step);
        Database.update(task);
    }

    public static void deleteStep(int stepId) throws EntityNotFoundException {
        Database.delete(stepId);
    }
}
