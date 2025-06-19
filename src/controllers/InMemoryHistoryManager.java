package controllers;

import entities.Node;
import entities.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public void add(Task task) {
        Node<Task> oldHistoryTask = historyMap.get(task.getId());
        if (oldHistoryTask != null) {
            this.removeNode(oldHistoryTask);
        }
        this.linkLast(task);
        historyMap.put(task.getId(), tail);
    }

    @Override
    public void remove(int id) {
        Node<Task> oldHistoryTask = historyMap.remove(id);
        if (oldHistoryTask != null) {
            this.removeNode(oldHistoryTask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return this.getTasks();
    }

    private void linkLast(Task task) {
        final Node<Task> newTail = new Node<>(tail, task, null);
        if (tail == null) {
            head = newTail;
        } else {
            tail.setNext(newTail);
        }
        tail = newTail;
    }

    private List<Task> getTasks() {
        Node<Task> currentNode = head;
        List<Task> tasks = new ArrayList<>();
        while (currentNode != null) {
            tasks.add(currentNode.getData());
            currentNode = currentNode.getNext();
        }
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        final Node<Task> prev = node.getPrev();
        final Node<Task> next = node.getNext();
        if (prev == null) {
            head = next;
        } else {
            prev.setNext(next);
        }

        if (next == null) {
            tail = prev;
        } else {
            next.setPrev(prev);
        }
    }
}
