import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import entities.Epic;
import entities.SubTask;
import entities.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
        Task testTask1 = new Task("Test task 0", "Description 0");
        Task testTask2 = new Task("Test task 1", "Description 1");
        Epic testEpic1 = new Epic("Test epic 1", "Description 1");
        Epic testEpic2 = new Epic("Test epic 2", "Description 2");
        taskManager.createTask(testTask1);
        taskManager.createTask(testTask2);
        taskManager.createTask(testEpic1);
        taskManager.createTask(testEpic2);

        SubTask testSubTask1 = new SubTask("Test subtask 1 for entities.Epic 1", "Description 1");
        testSubTask1.setEpicId(testEpic1.getId());
        SubTask testSubTask2 = new SubTask("Test subtask 2 for entities.Epic 1", "Description 2");
        testSubTask2.setEpicId(testEpic1.getId());
        SubTask testSubTask3 = new SubTask("Test subtask 3 for entities.Epic 1", "Description 3");
        testSubTask3.setEpicId(testEpic1.getId());

        taskManager.createTask(testSubTask1);
        taskManager.createTask(testSubTask2);
        taskManager.createTask(testSubTask3);

        printAllTasks(taskManager);

        taskManager.getTask(testTask1.getId());
        printHistory(taskManager);
        taskManager.getTask(testTask2.getId());
        taskManager.getEpic(testEpic1.getId());
        printHistory(taskManager);
        taskManager.getTask(testTask1.getId());
        taskManager.getSubTask(testSubTask1.getId());
        taskManager.getSubTask(testSubTask2.getId());
        printHistory(taskManager);
        taskManager.getSubTask(testSubTask1.getId());
        printHistory(taskManager);
        taskManager.removeEpic(testEpic1.getId());
        printHistory(taskManager);
        taskManager.removeTasks();
        printHistory(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubTasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTasks()) {
            System.out.println(subtask);
        }

        printHistory(manager);
    }

    private static void printHistory(TaskManager manager) {
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
