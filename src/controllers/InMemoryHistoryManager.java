package controllers;

import entities.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_SIZE = 10;
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == HISTORY_SIZE) {
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
