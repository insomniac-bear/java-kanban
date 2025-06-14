package entities;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return this.epicId;
    }
}
