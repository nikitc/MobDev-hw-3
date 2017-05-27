package comnikitc.github.mobdev_hw_3;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.Calendar;

public class FilterFragment extends Fragment {
    private String date = "";
    private EditText dateText;
    private String filter = "none";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button chooseDateButton = (Button) getView().findViewById(R.id.chooseDateButton);
        dateText = (EditText) getView().findViewById(R.id.dateText);

        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDatePicker();
            }
        });
        getView().findViewById(R.id.noneFilter).setOnClickListener(radioButtonListener);
        getView().findViewById(R.id.dateCreateFilter).setOnClickListener(radioButtonListener);
        getView().findViewById(R.id.dateEditFilter).setOnClickListener(radioButtonListener);
        getView().findViewById(R.id.dateViewFilter).setOnClickListener(radioButtonListener);

        View v = inflater.inflate(R.layout.fragment_filter, null);

        return v;
    }


    private void callDatePicker() {
        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = getDate(year, monthOfYear + 1, dayOfMonth);
                        dateText.setText(date);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public String getDate(int year, int monthOfYear, int dayOfMonth) {
        String days = "";
        String months = "";

        days = dayOfMonth + "";
        if (dayOfMonth < 10) {
            days = "0" + days;
        }

        months = monthOfYear + "";
        if (monthOfYear < 10) {
            months = "0" + months;
        }

        return days + "." + months + "." + year;
    }

    View.OnClickListener radioButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RadioButton rb = (RadioButton) view;
            switch (rb.getId()) {
                case R.id.noneFilter:
                    filter = "none";
                    break;
                case R.id.dateCreateFilter:
                    filter = "dateCreate";
                    break;
                case R.id.dateEditFilter:
                    filter = "dateEdit";
                    break;
                case R.id.dateViewFilter:
                    filter = "dateView";
                    break;
            }
        }
    };
}
