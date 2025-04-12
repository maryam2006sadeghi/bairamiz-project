import db.Database;
import db.Entity;
import todo.entity.Step;
import todo.entity.Task;
import todo.service.StepService;
import todo.service.TaskService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static todo.entity.Task.Status.*;

public class Main {
    private static final List<Task> tasks = new ArrayList<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        JFrame frame = new JFrame("To-Do List");
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(3, 3, 10, 10));

        JButton addTaskButton = new JButton("Add Task");
        JButton addStepButton = new JButton("Add Step");
        JButton deleteButton = new JButton("Delete");
        JButton updateTaskButton = new JButton("Update Task");
        JButton updateStepButton = new JButton("Update Step");
        JButton getTaskByIdButton = new JButton("Get Task-by-id");
        JButton getAllTasksButton = new JButton("Get All-tasks");
        JButton getIncompleteTasksButton = new JButton("Get Incomplete-tasks");
        JButton exitButton = new JButton("Exit");

        mainPanel.add(addTaskButton);
        mainPanel.add(addStepButton);
        mainPanel.add(deleteButton);
        mainPanel.add(updateTaskButton);
        mainPanel.add(updateStepButton);
        mainPanel.add(getTaskByIdButton);
        mainPanel.add(getAllTasksButton);
        mainPanel.add(getIncompleteTasksButton);
        mainPanel.add(exitButton);

        frame.add(mainPanel);

        addTaskButton.addActionListener(e -> addTask(frame));
        addStepButton.addActionListener(e -> addStep(frame));
        deleteButton.addActionListener(e -> delete(frame));
        updateTaskButton.addActionListener(e -> updateTask(frame));
        updateStepButton.addActionListener(e -> updateStep(frame));
        getTaskByIdButton.addActionListener(e -> getTaskById(frame));
        getAllTasksButton.addActionListener(e -> getAllTasks(frame));
        getIncompleteTasksButton.addActionListener(e -> getIncompleteTasks(frame));
        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private static void addTask(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel titleLabel = new JLabel("Title: ");
        JLabel descriptionLabel = new JLabel("Description: ");
        JLabel dueDateLabel = new JLabel("Due Date: ");

        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dueDateField = new JTextField();

        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(descriptionLabel);
        panel.add(descriptionField);
        panel.add(dueDateLabel);
        panel.add(dueDateField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Task",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText().trim();
                String description = descriptionField.getText().trim();
                String dueDateStr = dueDateField.getText().trim();

                if (title.isEmpty()) {
                    throw new IllegalArgumentException("Cannot save task.\nError : Task title cannot be empty");
                }

                LocalDate dueDate = LocalDate.parse(dueDateStr, DATE_FORMATTER);
                Date sqlDate = java.sql.Date.valueOf(dueDate);

                JOptionPane.showMessageDialog(frame, "Task saved successfully.\nID: " + TaskService.addTask(title, description, sqlDate), "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(frame, "Cannot save task.\nError: Invalid date format. Please use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(frame, "Cannot save task.\n", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void addStep(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        JLabel taskRefLabel = new JLabel("Task ID: ");
        JLabel titleLabel = new JLabel("Title: ");

        JTextField taskRefField = new JTextField();
        JTextField titleField = new JTextField();

        panel.add(taskRefLabel);
        panel.add(taskRefField);
        panel.add(titleLabel);
        panel.add(titleField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Step",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        int taskRef = Integer.parseInt(taskRefField.getText().trim());
        String title = titleField.getText().trim();

        if (result == JOptionPane.OK_OPTION) {
            try {
                if (title.isEmpty()) {
                    throw new IllegalArgumentException("Cannot save step\nTitle cannot be empty");
                }

                JOptionPane.showMessageDialog(frame, "Step saved successfully.\nID: " + StepService.saveStep(title, taskRef), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Cannot save step\nInvalid Task ID. Please enter a number", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Cannot save step\nError : Cannot find task with ID=" + taskRef , "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void delete(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();

        panel.add(idLabel);
        panel.add(idField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Delete",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        int id = Integer.parseInt(idField.getText().trim());
        Entity entity = Database.get(id);
        if (result == JOptionPane.OK_OPTION) {
            try {
                if (entity instanceof Task) {
                    TaskService.deleteTask(id);
                } else if (entity instanceof Step) {
                    StepService.deleteStep(id);
                } else {
                    throw new IllegalArgumentException("Entity not found");
                }

                JOptionPane.showMessageDialog(frame, "Entity with ID=" + id + " successfully deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid ID. Please enter a number", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Cannot delete entity with ID=.\n"+ id +"Error: Something happend","Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void updateTask(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel idLabel = new JLabel("ID:");
        JLabel fieldLabel = new JLabel("Field (title/description/dueDate/status):");
        JLabel newValueLabel = new JLabel("New Value:");

        JTextField idField = new JTextField();
        JTextField fieldField = new JTextField();
        JTextField newValueField = new JTextField();

        panel.add(idLabel);
        panel.add(idField);
        panel.add(fieldLabel);
        panel.add(fieldField);
        panel.add(newValueLabel);
        panel.add(newValueField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Update Task",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String field = fieldField.getText().trim().toLowerCase();
                String newValue = newValueField.getText().trim();

                switch (field) {
                    case "title":
                        TaskService.updateTaskTitle(id, newValue);
                        break;
                    case "description":
                        TaskService.updateTaskDescription(id, newValue);
                        break;
                    case "duedate":
                        LocalDate dueDate = LocalDate.parse(newValue, DATE_FORMATTER);
                        TaskService.updateTaskDueDate(id, java.sql.Date.valueOf(dueDate));
                        break;
                    case "status":
                        TaskService.updateTaskStatus(id, newValue);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid field");
                }

                JOptionPane.showMessageDialog(frame, "Successfully updated the task.\n" +
                        "Field: " + field +
                        "\nNew Value: " + newValue +
                        "\nModification Date: " + new Date(), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Cannot update task.\nError: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void updateStep(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel idLabel = new JLabel("ID:");
        JLabel fieldLabel = new JLabel("Field (title/status):");
        JLabel newValueLabel = new JLabel("New Value:");

        JTextField idField = new JTextField();
        JTextField fieldField = new JTextField();
        JTextField newValueField = new JTextField();

        panel.add(idLabel);
        panel.add(idField);
        panel.add(fieldLabel);
        panel.add(fieldField);
        panel.add(newValueLabel);
        panel.add(newValueField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Update Step",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        int id = Integer.parseInt(idField.getText().trim());
        String field = fieldField.getText().trim().toLowerCase();
        String newValue = newValueField.getText().trim();

        if (result == JOptionPane.OK_OPTION) {
            try {
                switch (field) {
                    case "title":
                        StepService.updateStepTitle(id, newValue);
                        break;
                    case "status":
                        StepService.setAsCompleted(id, newValue);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid field");
                }

                JOptionPane.showMessageDialog(frame, "Successfully updated the step.\n" +
                        "Field: " + field +
                        "\nNew Value: " + newValue +
                        "\nModification Date: " + new Date() , "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,"Cannot update step with ID=." + id +"\n" +
                        "Error: Cannot find entity with ID=" + id  , "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void getTaskById(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

        JLabel idLabel = new JLabel("Task ID:");
        JTextField idField = new JTextField();

        panel.add(idLabel);
        panel.add(idField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Get Task by ID", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                Task task = (Task) Database.get(id);

                if (task == null) {
                    throw new IllegalArgumentException("Task not found");
                }

                StringBuilder message = new StringBuilder();
                message.append("ID: ").append(task.id).append("\n")
                        .append("Title: ").append(task.title).append("\n")
                        .append("Due Date: ").append(task.getDueDate()).append("\n")
                        .append("Status: ").append(task.getStatus()).append("\n\nSteps:\n");

                List<Step> steps = getStepsForTask(id);
                if (steps.isEmpty()) {
                    message.append("No steps found");
                } else {
                    steps.forEach(step -> message.append("- ").append(step.title).append(" (").append(step.getStatus()).append(")\n"));
                }

                JOptionPane.showMessageDialog(frame, message.toString(), "Task Details", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Cannot find task.\nError: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void getAllTasks(JFrame frame) {
        try {
            tasks.clear();
            for (Entity entity : Database.getAll(16)) {
                if (entity instanceof Task) {
                    tasks.add((Task) entity);
                }
            }

            tasks.sort(Comparator.comparing(Task::getDueDate));

            if (tasks.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No tasks found", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder allTasks = new StringBuilder("All Tasks:\n\n");
            for (Task task : tasks) {
                allTasks.append("ID: ").append(task.id).append("\n").append("Title: ").append(task.title).append("\n").append("Due Date: ").append(task.getDueDate()).append("\n").append("Status: ").append(task.getStatus()).append("\n");

                List<Step> steps = getStepsForTask(task.id);
                if (!steps.isEmpty()) {
                    allTasks.append("Steps:\n");
                    for (Step step : steps) {
                        allTasks.append("- ").append(step.title).append(" (").append(step.getStatus()).append(")\n");
                    }
                }
                allTasks.append("\n");
            }

            JTextArea textArea = new JTextArea(allTasks.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(frame, scrollPane, "All Tasks", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error loading tasks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void getIncompleteTasks(JFrame frame) {
        try {
            tasks.clear();
            for (Entity entity : Database.getAll(16)) {
                if (entity instanceof Task &&
                        (((Task) entity).getStatus() == InProgress ||
                                ((Task) entity).getStatus() == NotStarted)) {
                    tasks.add((Task) entity);
                }
            }

            tasks.sort(Comparator.comparing(Task::getDueDate));

            if (tasks.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No incomplete tasks found", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder incompleteTasks = new StringBuilder("Incomplete Tasks:\n\n");
            for (Task task : tasks) {
                incompleteTasks.append("ID: ").append(task.id).append("\n").append("Title: ").append(task.title).append("\n").append("Due Date: ").append(task.getDueDate()).append("\n").append("Status: ").append(task.getStatus()).append("\n");

                List<Step> incompleteSteps = new ArrayList<>();
                for (Step step : getStepsForTask(task.id)) {
                    if (step.getStatus() == Step.Status.NotStarted) {
                        incompleteSteps.add(step);
                    }
                }

                if (!incompleteSteps.isEmpty()) {
                    incompleteTasks.append("Incomplete Steps:\n");
                    for (Step step : incompleteSteps) {
                        incompleteTasks.append("- ").append(step.title).append("\n");
                    }
                }
                incompleteTasks.append("\n");
            }

            JTextArea textArea = new JTextArea(incompleteTasks.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(frame, scrollPane, "Incomplete Tasks", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error loading incomplete tasks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static List<Step> getStepsForTask(int taskId) {
        List<Step> steps = new ArrayList<>();
        for (Entity entity : Database.getAll(17)) {
            if (entity instanceof Step && ((Step) entity).getTaskRef() == taskId) {
                steps.add((Step) entity);
            }
        }
        return steps;
    }
}