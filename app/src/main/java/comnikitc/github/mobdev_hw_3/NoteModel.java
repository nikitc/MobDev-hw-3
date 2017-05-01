package comnikitc.github.mobdev_hw_3;


class NoteModel {
    private String name;
    private String text;
    private int color;
    private int id;
    private String dateCreate;
    private String dateEdit;
    private String dateView;

    NoteModel(int id, String name, String text, int color,
              String dateCreate, String dateEdit, String dateView) {
        this.name = name;
        this.text = text;
        this.color = color;
        this.id = id;
        this.dateCreate = dateCreate;
        this.dateEdit = dateEdit;
        this.dateView = dateView;
    }

    public String getDateView() {
        return dateView;
    }

    public void setDateView(String dateView) {
        this.dateView = dateView;
    }

    public String getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(String dateEdit) {
        this.dateEdit = dateEdit;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
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
