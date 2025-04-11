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
    public static int addTask(String title, String description, Date dueDate) throws InvalidEntityException {
        Task task = new Task(title, description, dueDate);
        task.setStatus(Task.Status.NotStarted);
        Database.add(task);
        return task.id;
    }

    public static void setAsInProgress(int taskId) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(Task.Status.InProgress);
        task.setLastModificationDate(new Date());
        Database.update(task);
    }

    public static void setAsCompleted(int taskId) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(Task.Status.Completed);
        task.setLastModificationDate(new Date());
        Database.update(task);
    }

    public static void updateTaskTitle(int taskId, String newTitle) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.title = newTitle;
        task.setLastModificationDate(new Date());
        Database.update(task);
    }

    public static void updateTaskDescription(int taskId, String newDescription) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.description = newDescription;
        task.setLastModificationDate(new Date());
        Database.update(task);
    }

    public static void updateTaskDueDate(int taskId, Date newDueDate) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setDueDate(newDueDate);
        task.setLastModificationDate(new Date());
        Database.update(task);
    }

    public static void updateTaskStatus(int taskId, String newStatus) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        Task.Status taskStatus = Task.Status.valueOf(newStatus);
        task.setStatus(taskStatus);
        task.setLastModificationDate(new Date());
        Database.update(task);

        if (newStatus.equals("Completed")) {
            ArrayList<Entity> copyEntities = new ArrayList<>(getAll(17));
            Step.Status stepStatus = Step.Status.valueOf(newStatus);
            for (Entity entity : copyEntities) {
                if (entity instanceof Step && ((Step) entity).getTaskRef() == taskId) {
                    ((Step) entity).setStatus(stepStatus);
                }
            }
            Database.updateEntityList(copyEntities);
        }
    }

    public static void deleteTask(int taskId) {
        Database.delete(taskId);
        ArrayList<Entity> copyEntities = new ArrayList<>(getAll(17));
        int counter = entityCounter(copyEntities, taskId);

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

    public static int entityCounter(ArrayList<Entity> arrayList, int taskId){
        int counter = 0 ;
        for (Entity entity : arrayList){
            if (entity instanceof Step && ((Step) entity).getTaskRef() == taskId){
                counter ++;
            }
        }
        return counter;
    }
}
