package controllers;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import exceptions.ManagerGetException;
import exceptions.ManagerSaveException;
import utils.Status;
import utils.TaskType;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String FILE_HEADER = "id,type,name,status,description,startTime,duration,epic";
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.startsWith(FILE_HEADER)) {
                    continue;
                }

                String[] taskParts = line.split(",");
                int id = Integer.parseInt(taskParts[0]);
                TaskType type = TaskType.valueOf(taskParts[1]);
                String name = taskParts[2];
                Status status = Status.valueOf(taskParts[3]);
                String description = taskParts[4];
                String start = taskParts[5] != null ? taskParts[5] : "";
                String duration = taskParts[6] != null ? taskParts[6] : "";

                switch (type) {
                    case TaskType.TASK:
                        Task task = new Task(name, description, status, start, duration);
                        task.setId(id);
                        manager.createTask(task);
                        break;
                    case TaskType.EPIC:
                        Epic epic = new Epic(name, description, status);
                        epic.setId(id);
                        manager.createEpic(epic);
                        break;
                    case TaskType.SUBTASK:
                        SubTask subtask = new SubTask(name, description, status, start, duration);
                        subtask.setId(id);
                        int epicId = taskParts.length > 6 ? Integer.parseInt(taskParts[7]) : null;
                        subtask.setEpicId(epicId);
                        manager.createSubtask(subtask);
                        break;
                    default:
                        throw new ManagerSaveException("Ошибка чтения из файла. Неизвестный тип задачи");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения из файла: " + e.getMessage());
        }
        return manager;
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
    public void updateTask(Task task) throws ManagerGetException {
        super.updateTask(task);
        this.save();
    }

    @Override
    public void updateTask(SubTask subtask) {
        super.updateTask(subtask);
        this.save();
    }

    @Override
    public void updateTask(Epic epic) throws ManagerGetException {
        super.updateTask(epic);
        this.save();
    }

    @Override
    public void removeTask(int id) throws ManagerGetException {
        super.removeTask(id);
        this.save();
    }

    @Override
    public void removeEpic(int id) throws ManagerGetException {
        super.removeEpic(id);
        this.save();
    }

    @Override
    public void removeSubtask(int id) throws ManagerGetException {
        super.removeSubtask(id);
        this.save();
    }

    private void save() {
        try (Writer out = new FileWriter(this.file, StandardCharsets.UTF_8, false)) {
            out.write(FILE_HEADER + System.lineSeparator());
            for (Task task : this.getTasks()) {
                out.write(task.toString() + System.lineSeparator());
            }

            for (Epic epic : this.getEpics()) {
                out.write(epic.toString() + System.lineSeparator());
            }

            for (SubTask subtask : this.getSubTasks()) {
                out.write(subtask.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл: " + e.getMessage());
        }
    }
}
