import controllers.FileBackedTaskManager;
import controllers.Managers;
import controllers.TaskManager;
import entities.Epic;
import entities.SubTask;
import entities.Task;
import utils.FileManager;
import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        Task testTask1 = new Task("T0", "TD0");
        Task testTask2 = new Task("T1", "TD1");
        Epic testEpic1 = new Epic("E1", "ED1");
        Epic testEpic2 = new Epic("E2", "ED2");
        taskManager.createTask(testTask1);
        taskManager.createTask(testTask2);
        taskManager.createEpic(testEpic1);
        taskManager.createEpic(testEpic2);

        SubTask testSubTask1 = new SubTask("ST1", "STD1");
        testSubTask1.setEpicId(testEpic1.getId());
        SubTask testSubTask2 = new SubTask("ST2", "STD2");
        testSubTask2.setEpicId(testEpic1.getId());
        SubTask testSubTask3 = new SubTask("ST3", "STD3");
        testSubTask3.setEpicId(testEpic1.getId());

        taskManager.createSubtask(testSubTask1);
        taskManager.createSubtask(testSubTask2);
        taskManager.createSubtask(testSubTask3);

        System.out.println("--- InMemoryTaskManager ---");
        System.out.println();
        printAllTasks(taskManager);

        taskManager.getTask(testTask1.getId());
        printHistory(taskManager);
        taskManager.getTask(testTask2.getId());
        taskManager.getEpic(testEpic1.getId());
        System.out.println();
        printHistory(taskManager);
        taskManager.getTask(testTask1.getId());
        taskManager.getSubTask(testSubTask1.getId());
        taskManager.getSubTask(testSubTask2.getId());
        System.out.println();
        printHistory(taskManager);
        taskManager.getSubTask(testSubTask1.getId());
        printHistory(taskManager);
        taskManager.removeEpic(testEpic1.getId());
        System.out.println();
        printHistory(taskManager);
        taskManager.removeTasks();
        System.out.println();
        printHistory(taskManager);

        File data = FileManager.getFile("data.csv");
        FileBackedTaskManager backedTaskManager = Managers.getDefaultFileBackedTaskManager(data);

        Task fbTask1 = new Task("fbT0", "fbTD0");
        Task fbTask2 = new Task("fbT1", "fbTD1");
        Epic fbEpic1 = new Epic("fbE1", "fbED1");
        Epic fbEpic2 = new Epic("fbE2", "fbED2");
        backedTaskManager.createTask(fbTask1);
        backedTaskManager.createTask(fbTask2);
        backedTaskManager.createEpic(fbEpic1);
        backedTaskManager.createEpic(fbEpic2);

        SubTask fbSubTask1 = new SubTask("ST1", "STD1");
        fbSubTask1.setEpicId(fbEpic1.getId());
        SubTask fbSubTask2 = new SubTask("ST2", "STD2");
        fbSubTask2.setEpicId(fbEpic1.getId());
        SubTask fbSubTask3 = new SubTask("ST3", "STD3");
        fbSubTask3.setEpicId(fbEpic2.getId());

        backedTaskManager.createSubtask(fbSubTask1);
        System.out.println(testSubTask1);
        backedTaskManager.createSubtask(fbSubTask2);
        backedTaskManager.createSubtask(fbSubTask3);

        System.out.println();
        System.out.println("--- FileBackedTaskManager ---");
        System.out.println();
        printAllTasks(backedTaskManager);
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
