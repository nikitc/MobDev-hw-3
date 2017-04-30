package comnikitc.github.mobdev_hw_3;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

class SettingsNotes {

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
                    case "name":
                        return note1.getName().compareTo(note2.getName());
                    case "create":
                        return note1.getDateCreate().compareTo(note2.getDateCreate());
                    case "edit":
                        return note1.getDateEdit().compareTo(note2.getDateEdit());
                    case "view":
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
                case "dateCreate":
                    if (translateToDate(currentNote.getDateCreate()).equals(dateFull)) {
                        newListNotes.add(currentNote);
                    }
                    break;
                case "dateEdit":
                    if (translateToDate(currentNote.getDateEdit()).equals(dateFull)) {
                        newListNotes.add(currentNote);
                    }
                    break;
                case "dateView":
                    if (translateToDate(currentNote.getDateView()).equals(dateFull)) {
                        newListNotes.add(currentNote);
                    }
                    break;
            }
        }

        return newListNotes;
    }

    ArrayList<NoteModel> setSettingListNotes(ArrayList<NoteModel> listNotes) {
        return filterListNotes(
                sortListNotes(listNotes));
    }

    private String translateToDate(String dateISO) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

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