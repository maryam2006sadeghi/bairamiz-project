package todo.service;

import db.Database;
import db.Entity;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

import java.util.ArrayList;
import java.util.Date;

import static db.Database.getAll;

public class TaskService {
    public static void addTask(String title, String description, Date dueDate) throws InvalidEntityException {
        Task task = new Task(title, description, dueDate);
        task.setStatus(Task.Status.NotStarted);
        Database.add(task);
    }

    public static void setAsInProgress(int taskId) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(Task.Status.InProgress);
        Database.update(task);
    }

    public static void setAsCompleted(int taskId) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(Task.Status.Completed);
        Database.update(task);
    }

    public static void updateTaskTitle(int taskId, String newTitle) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.title = newTitle;
        Database.update(task);
    }

    public static void deleteTask(int taskId) {
        Database.delete(taskId);
        int counter = 0 ;
        ArrayList<Entity> copyEntities = new ArrayList<>(getAll(17));
        for (Entity entity : copyEntities){
            if (entity instanceof Step && ((Step) entity).getTaskRef() == taskId){
                counter ++;
            }
        }

        while (counter > 0){
            for (Entity entity : copyEntities){
                if (entity instanceof Step && ((Step) entity).getTaskRef() == taskId){
                    copyEntities.remove(entity);
                    break;
                }
            }
            counter --;
        }
        Database.updateEntityList(copyEntities);
    }
}
