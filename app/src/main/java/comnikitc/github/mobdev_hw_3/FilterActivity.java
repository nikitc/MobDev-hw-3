package comnikitc.github.mobdev_hw_3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FilterActivity extends AppCompatActivity {

    private String date = "";
    private EditText dateText;
    private String filter = "none";
    private final String keyFilter = "filter";
    private final String keyDate = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Button chooseDateButton = (Button) findViewById(R.id.chooseDateButton);
        dateText = (EditText) findViewById(R.id.dateText);

        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDatePicker();
            }
        });
        findViewById(R.id.noneFilter).setOnClickListener(radioButtonListener);
        findViewById(R.id.dateCreateFilter).setOnClickListener(radioButtonListener);
        findViewById(R.id.dateEditFilter).setOnClickListener(radioButtonListener);
        findViewById(R.id.dateViewFilter).setOnClickListener(radioButtonListener);
    }

    private void callDatePicker() {
        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = GetDate(year, monthOfYear + 1, dayOfMonth);
                        dateText.setText(date);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public String GetDate(int year, int monthOfYear, int dayOfMonth) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.saveSettingsSort:
                Intent answerIntent = new Intent();
                answerIntent.putExtra(keyFilter, filter);
                answerIntent.putExtra(keyDate, date);
                setResult(RESULT_OK, answerIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
