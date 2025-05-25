package entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import utils.Status;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    static private Task task;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        task = new Task("Test", "Test description");
    }

    @Test
    void shouldHaveInProgressStatus() {
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    void shouldHaveCompletedStatus() {
        task.setStatus(Status.DONE);
        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    void shouldHaveId() {
        task.setId(1);
        assertEquals(1, task.getId());
    }
}