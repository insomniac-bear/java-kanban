package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest extends TaskTest {
    Epic e;
    Epic se;
    SubTask s1;
    SubTask s2;
    SubTask s3;
    SubTask s4;

    @BeforeEach
    void setUp() {
        e = new Epic("N", "D");
        e.setId(0);
        se = new Epic("N", "D", Status.NEW);
        se.setId(5);
        s1 = new SubTask("s1", "ds1", LocalDateTime.of(2025, 2, 1, 0, 0, 0), Duration.ofMinutes(10));
        s1.setEpicId(e.getId());
        s1.setId(1);
        s2 = new SubTask("s2", "ds2", LocalDateTime.of(2025, 3, 2, 0, 0, 0), Duration.ofMinutes(10));
        s2.setEpicId(e.getId());
        s2.setId(2);
        s3 = new SubTask("s3", "ds3", LocalDateTime.of(2025, 4, 3, 0, 0, 0), Duration.ofMinutes(10));
        s3.setEpicId(e.getId());
        s3.setId(3);
        s4 = new SubTask("s4", "ds4", LocalDateTime.of(2025, 5, 4, 0, 0, 0), Duration.ofMinutes(10));
        s4.setEpicId(se.getId());
        s4.setId(4);
        e.addSubTask(s1);
        e.addSubTask(s2);
        e.addSubTask(s3);
        se.addSubTask(s4);
    }

    @Test
    @DisplayName("Добавляет Сабтаску и устанавливает эпику дату начала и конца, продолжжительность, если у сабкаски " +
            "есть дата начала и продолжительность")
    void addSubtasksAndSetStartDateEndDateAndDuration() {
        LocalDateTime startDate = LocalDateTime.of(2025, 2, 1, 0, 0, 0);
        LocalDateTime startDateSe = LocalDateTime.of(2025, 5, 4, 0, 0, 0);
        assertEquals(e.getSubtaskList().size(), 3, "Не корректное количество подзадач: " + e.getSubtaskList().size());
        assertEquals(e.getDuration(), Duration.ofMinutes(30), "Не корректная продолжительность: " + e.getDuration().toString());
        assertEquals(e.getStartTime(), startDate, "Не корректное время начала: " + e.getStartTime().toString());
        assertEquals(e.getEndTime(), s3.getEndTime(), "Не корректное время окончания: " + e.getStartTime().toString());

        assertEquals(se.getSubtaskList().size(), 1, "Не корректное количество подзадач: " + se.getSubtaskList().size());
        assertEquals(se.getDuration(), Duration.ofMinutes(10),
                "Не корректная продолжительность: " + se.getDuration().toString());
        assertEquals(se.getStartTime(), startDateSe, "Не корректное время начала: " + se.getStartTime().toString());
        assertEquals(se.getEndTime(), s4.getEndTime(),
                "Не корректное время окончания: " + se.getStartTime().toString());
    }

    @Test
    @DisplayName("После очистки подзадач, жата начала, продолжительность и дата окончания равны null")
    void clearSubtaskList() {
        LocalDateTime startDate = LocalDateTime.of(2025, 2, 1, 0, 0, 0);
        assertEquals(e.getSubtaskList().size(), 3, "Не корректное количество подзадач: " + e.getSubtaskList().size());
        assertEquals(e.getDuration(), Duration.ofMinutes(30), "Не корректная продолжительность: " + e.getDuration().toString());
        assertEquals(e.getStartTime(), startDate, "Не корректное время начала: " + e.getStartTime().toString());

        SubTask s5 = new SubTask("s1", "ds1", LocalDateTime.of(2025, 1, 1, 0, 0, 0), Duration.ofMinutes(15));
        s5.setId(1);
        s5.setEpicId(e.getId());
        e.addSubTask(s5);
        SubTask s6 = new SubTask("s6", "ds6", null, Duration.ofMinutes(15));
        s6.setId(6);
        s6.setEpicId(e.getId());
        SubTask s7 = new SubTask("s7", "ds7", LocalDateTime.of(2025, 1, 1, 0, 0, 0), null);
        s7.setId(7);
        s7.setEpicId(e.getId());
        e.addSubTask(s6);
        e.addSubTask(s7);
        assertEquals(e.getSubtaskList().size(), 3, "Не корректное количество подзадач: " + e.getSubtaskList().size());
        assertEquals(e.getDuration(), Duration.ofMinutes(35),
                "Не корректная продолжительность: " + e.getDuration().toString());
        assertEquals(e.getStartTime(),  LocalDateTime.of(2025, 1, 1, 0, 0, 0), "Не корректное время начала: " + e.getStartTime().toString());

        e.clearSubtaskList();

        assertNull(e.getStartTime(), "Дата начала не равна null");
        assertNull(e.getDuration(), "Продолжительность не равна null");
        assertNull(e.getEndTime(), "Дата окончания не равна null");
    }

    @Test
    void removeSubTask() {
        LocalDateTime startDate = LocalDateTime.of(2025, 3, 2, 0, 0, 0);
        e.removeSubTask(s1);

        assertEquals(e.getSubtaskList().size(), 2, "Не корректное количество подзадач: " + e.getSubtaskList().size());
        assertEquals(e.getDuration(), Duration.ofMinutes(20),
                "Не корректная продолжительность: " + e.getDuration().toString());
        assertEquals(e.getStartTime(), startDate, "Не корректное время начала: " + e.getStartTime().toString());

        SubTask s6 = new SubTask("s6", "ds6", startDate, Duration.ofMinutes(15));
        e.removeSubTask(s6);
        assertEquals(e.getSubtaskList().size(), 2, "Не корректное количество подзадач: " + e.getSubtaskList().size());

        e.removeSubTask(s3);
        assertEquals(e.getEndTime(), s2.getEndTime(), "Не корректное время Окончания: " + e.getEndTime().toString());

        e.removeSubTask(s2);
        assertNull(e.getStartTime());
        assertNull(e.getEndTime());
        assertNull(e.getDuration());
    }
    @Test
    void testToString() {
        String eString =
                "0,EPIC,N,NEW,D,"
                        + LocalDateTime.of(2025, 2, 1, 0, 0, 0)
                        + "," + String.valueOf(Duration.ofMinutes(30).toMinutes());
        assertEquals(e.toString(), eString, "Return: " + e.toString());
    }
}