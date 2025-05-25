import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import entities.Epic;
import entities.SubTask;
import entities.Task;
import utils.Status;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
        Task testTask1 = new Task("Test task 0", "Description 0");
        Task testTask2 = new Task("Test task 1", "Description 1");
        Epic testEpic1 = new Epic("Test epic 1", "Description 1");
        Epic testEpic2 = new Epic("Test epic 2", "Description 2");
        SubTask testSubTask1 = new SubTask("Test subtask 1 for entities.Epic 1", "Description 1");
        SubTask testSubTask2 = new SubTask("Test subtask 2 for entities.Epic 1", "Description 2");
        SubTask testSubTask3 = new SubTask("Test subtask 3 for entities.Epic 2", "Description 3");

        taskManager.createTask(testTask1);
        taskManager.createTask(testTask2);
        taskManager.createTask(testEpic1);
        taskManager.createTask(testEpic2);
        taskManager.createTask(testSubTask1, testEpic1.getId());
        taskManager.createTask(testSubTask2, testEpic1.getId());
        taskManager.createTask(testSubTask3, testEpic2.getId());

        printAllTasks(taskManager);
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

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
