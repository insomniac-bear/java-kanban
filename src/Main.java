import controllers.TaskManager;
import entities.Epic;
import entities.SubTask;
import entities.Task;
import utils.Status;
import utils.TaskType;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task testTask1 = new Task("Test task 0", "Description 0");
        Task testTask2 = new Task("Test task 1", "Description 1");
        Epic testEpic1 = new Epic("Test epic 1", "Description 1");
        Epic testEpic2 = new Epic("Test epic 2", "Description 2");
        taskManager.createTask(testTask1);
        taskManager.createTask(testTask2);
        taskManager.createTask(testEpic1);
        taskManager.createTask(testEpic2);

        SubTask testSubTask1 = new SubTask("Test subtask 1 for entities.Epic 1", "Description 1", testEpic1.getId());
        SubTask testSubTask2 = new SubTask("Test subtask 2 for entities.Epic 1", "Description 2", testEpic1.getId());
        SubTask testSubTask3 = new SubTask("Test subtask 3 for entities.Epic 2", "Description 3", testEpic2.getId());
        taskManager.createTask(testSubTask1);
        taskManager.createTask(testSubTask2);
        taskManager.createTask(testSubTask3);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());
        testTask1.setStatus(Status.IN_PROGRESS);
        System.out.println(taskManager.getTasks());
        testSubTask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(testSubTask1);
        testSubTask3.setStatus(Status.DONE);
        taskManager.updateTask(testSubTask3);
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTask(testTask1.getId()));
        System.out.println(taskManager.getSubTask(testSubTask1.getId()));
        System.out.println(taskManager.getEpic(testEpic1.getId()));
        System.out.println(taskManager.getEpicSubTasks(testEpic1.getId()));

        taskManager.removeTasks();
        System.out.println(taskManager.getTasks());

        taskManager.removeSubtask(testSubTask1.getId());
        taskManager.removeSubtask(testSubTask2.getId());
        System.out.println(taskManager.getSubTasks());
        System.out.println(taskManager.getEpics());
    }
}
