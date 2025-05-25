package controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    void testShouldReturnManagers() {
        InMemoryTaskManager memoryManager = Managers.getDefault();
        InMemoryHistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(memoryManager, "Менеджер задач не создан");
        assertNotNull(historyManager, "ММенеджер истории не создан");
    }
}