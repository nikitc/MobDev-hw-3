package comnikitc.github.mobdev_hw_3;


class NoteModel {
    private String name;
    private String text;
    private int color;
    private int id;
    private String dateCreate;
    private String dateEdit;
    private String dateView;
    private String imageUrl;
    private int serverNoteId;

    NoteModel(int id, String name, String text, int color, String imageUrl,
              String dateCreate, String dateEdit, String dateView, int serverNoteId) {
        this.name = name;
        this.text = text;
        this.color = color;
        this.imageUrl = imageUrl;
        this.id = id;
        this.dateCreate = dateCreate;
        this.dateEdit = dateEdit;
        this.dateView = dateView;
        this.serverNoteId = serverNoteId;
    }

    NoteModel(String name, String text, int color, String imageUrl,
              String dateCreate, String dateEdit, String dateView) {
        this.name = name;
        this.text = text;
        this.color = color;
        this.imageUrl = imageUrl;
        this.dateCreate = dateCreate;
        this.dateEdit = dateEdit;
        this.dateView = dateView;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public int getServerNoteId() {
        return serverNoteId;
    }

    public void setServerNoteId(int serverNoteId) {
        this.serverNoteId = serverNoteId;
    }
}
