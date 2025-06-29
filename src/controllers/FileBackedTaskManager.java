package controllers;

import Exceptions.ManagerSaveException;
import entities.Epic;
import entities.SubTask;
import entities.Task;
import utils.Status;
import utils.TaskType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    File file;
    static final String FILE_HEADER = "id,type,name,status,description,epic";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        this.save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        this.save();
    }

    @Override
    public void removeSubTasks() {
        super.removeSubTasks();
        this.save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        this.save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        this.save();
    }

    @Override
    public void createSubtask(SubTask subtask) {
        super.createSubtask(subtask);
        this.save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        this.save();
    }

    @Override
    public void updateTask(SubTask subtask) {
        super.updateTask(subtask);
        this.save();
    }

    @Override
    public void updateTask(Epic epic) {
        super.updateTask(epic);
        this.save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        this.save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        this.save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        this.save();
    }

    private void save() {
        List<String> allTasks = new ArrayList<>();
        allTasks.add(FILE_HEADER);
        for (Task task : this.getTasks()) {
            allTasks.add(task.toString());
        }

        for (Epic epic : this.getEpics()) {
            allTasks.add(epic.toString());
        }

        for (SubTask subtask : this.getSubTasks()) {
            allTasks.add(subtask.toString());
        }

        try (Writer out = new FileWriter(this.file, StandardCharsets.UTF_8, false)) {
            for (String line: allTasks) {
                out.write(line + "\n");
            }
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Ошибка сохранения в файл: " + e.getMessage());
            } catch (ManagerSaveException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.startsWith(FILE_HEADER)) {
                    continue;
                }
                Task task = createTaskFromString(line);
                if (task instanceof Epic) {
                    manager.createEpic((Epic)task);
                } else if (task instanceof SubTask) {
                    manager.createSubtask((SubTask) task);
                } else {
                    manager.createTask(task);
                }
            }
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Ошибка чтения из файла: " + e.getMessage());
            } catch (ManagerSaveException ex) {
                throw new RuntimeException(ex);
            }
        }
        return manager;
    }

    private static Task createTaskFromString(String taskString) {
        String[] taskParts = taskString.split(",");
        int id = Integer.parseInt(taskParts[0]);
        TaskType type = TaskType.valueOf(taskParts[1]);
        String name = taskParts[2];
        Status status = Status.valueOf(taskParts[3]);
        String description = taskParts[4];
        switch (type) {
            case TaskType.TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            case TaskType.EPIC:
                Epic epic = new Epic(name, description, status);
                epic.setId(id);
                return epic;
            case TaskType.SUBTASK:
                SubTask subtask = new SubTask(name, description, status);
                subtask.setId(id);
                subtask.setEpicId(Integer.parseInt(taskParts[5]));
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи" + type);
        }
    }
}
