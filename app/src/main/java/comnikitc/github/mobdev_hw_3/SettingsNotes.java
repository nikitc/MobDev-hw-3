package comnikitc.github.mobdev_hw_3;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

class SettingsNotes {
    private final String NAME_SORT = "name";
    private final String CREATE_SORT = "create";
    private final String EDIT_SORT = "edit";
    private final String VIEW_SORT = "view";
    private final String DATE_CREATE_FILTER = "dateCreate";
    private final String DATE_EDIT_FILTER = "dateEdit";
    private final String DATE_VIEW_FILTER = "dateView";
    private final String FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss";
    private final String FORMAT_SIMPLE = "dd.MM.yyyy";

    private String rule = "create";
    private String order = "asc";
    private String filter = "none";
    private String dateFull = "";

    SettingsNotes() {}

    private ArrayList<NoteModel> sortListNotes(ArrayList<NoteModel> listNotes) {
        Collections.sort(listNotes, new Comparator<NoteModel>() {
            @Override
            public int compare(NoteModel note1, NoteModel note2) {
                switch (rule) {
                    case NAME_SORT:
                        return note1.getName().compareTo(note2.getName());
                    case CREATE_SORT:
                        return note1.getDateCreate().compareTo(note2.getDateCreate());
                    case EDIT_SORT:
                        return note1.getDateEdit().compareTo(note2.getDateEdit());
                    case VIEW_SORT:
                        return note1.getDateView().compareTo(note2.getDateView());
                }
                return 0;
            }
        });
        if (Objects.equals(order, "desc")) {
            Collections.reverse(listNotes);
        }

        return listNotes;
    }

    private ArrayList<NoteModel> filterListNotes(ArrayList<NoteModel> listNotes) {
        if (filter.equals("none") || dateFull.equals("")) {
            return listNotes;
        }

        ArrayList<NoteModel> newListNotes = new ArrayList<NoteModel>();
        for (int i = 0; i < listNotes.size(); i++) {
            NoteModel currentNote = listNotes.get(i);
            switch (filter) {
                case DATE_CREATE_FILTER:
                    if (translateToDate(currentNote.getDateCreate()).equals(dateFull)) {
                        newListNotes.add(currentNote);
                    }
                    break;
                case DATE_EDIT_FILTER:
                    if (translateToDate(currentNote.getDateEdit()).equals(dateFull)) {
                        newListNotes.add(currentNote);
                    }
                    break;
                case DATE_VIEW_FILTER:
                    if (translateToDate(currentNote.getDateView()).equals(dateFull)) {
                        newListNotes.add(currentNote);
                    }
                    break;
            }
        }

        return newListNotes;
    }

    ArrayList<NoteModel> setSettingListNotes(ArrayList<NoteModel> listNotes) {
        return sortListNotes(
                filterListNotes(listNotes));
    }

    private String translateToDate(String dateISO) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_ISO);
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_SIMPLE);

        String dateAsString = "";
        try {
            Date dateFromString = simpleDateFormat.parse(dateISO);
            dateAsString = sdf.format(dateFromString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateAsString;
    }

    public String getDateFull() {
        return this.dateFull;
    }

    public void setDateFull(String date) {
        this.dateFull = date;
    }

    public String getRule() {
        return this.rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
    public String getFilter() {
        return this.filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}