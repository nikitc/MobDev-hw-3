package comnikitc.github.mobdev_hw_3;


public class NoteModel {
    private String name;
    private String text;
    private int color;
    private int id;

    public NoteModel(int id, String name, String text, int color) {
        this.name = name;
        this.text = text;
        this.color = color;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
