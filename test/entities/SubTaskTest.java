package entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest extends TaskTest {
    @Test
    @DisplayName("Subtask создается из строки")
    void setSubtaskFromString() {
        SubTask st = new SubTask("ST", "D", Status.NEW, LocalDateTime.of(2025, 1, 1, 0, 0, 0).toString(), "15");
        assertNotNull(st);
    }

    @Test
    @DisplayName("Subtask корректно устанавливает epicId")
    void getEpicId() {
        SubTask st = new SubTask("ST", "D", LocalDateTime.of(2025, 1, 1, 0, 0, 0), Duration.ofMinutes(15));
        st.setEpicId(1);
        assertEquals(1, st.getEpicId());
    }

    @Test
    @DisplayName("Subtask корректно приводится к строке формата csv")
    void testToString() {
        SubTask st = new SubTask("ST", "D", LocalDateTime.of(2025, 1, 1, 0, 0, 0), Duration.ofMinutes(15));
        st.setEpicId(1);
        assertNotNull(st.toString(), "0,SUBTASK,T,NEW,D," + LocalDateTime.of(2025, 1, 1, 0, 0, 0) + ",15,1");
    }
}