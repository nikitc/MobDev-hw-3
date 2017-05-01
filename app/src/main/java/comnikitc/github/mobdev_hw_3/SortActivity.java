package comnikitc.github.mobdev_hw_3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class SortActivity extends AppCompatActivity {
    private final String ORDER_ASC = "asc";
    private final String ORDER_DESC = "desc";
    private final String RULE_NAME = "name";
    private final String RULE_CREATE = "create";
    private final String RULE_EDIT = "edit";
    private final String RULE_VIEW = "view";


    public static String order = "create";
    public static String rule = "asc";
    private final String KEY_ORDER = "order";
    private final String KEY_RULE = "rule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        findViewById(R.id.ascRadio).setOnClickListener(radioButtonListener);
        findViewById(R.id.descRadio).setOnClickListener(radioButtonListener);
        findViewById(R.id.nameRadio).setOnClickListener(radioButtonListener);
        findViewById(R.id.dateCreateRadio).setOnClickListener(radioButtonListener);
        findViewById(R.id.dateEditRadio).setOnClickListener(radioButtonListener);
        findViewById(R.id.dateViewRadio).setOnClickListener(radioButtonListener);
        setCheckedRadio();
    }

    private void setCheckedRadio() {
        switch (rule) {
            case RULE_NAME:
                RadioButton buttonName = (RadioButton) findViewById(R.id.nameRadio);
                buttonName.setChecked(true);
                break;
            case RULE_CREATE:
                RadioButton buttonCreate = (RadioButton) findViewById(R.id.dateCreateRadio);
                buttonCreate.setChecked(true);
                break;
            case RULE_EDIT:
                RadioButton buttonEdit = (RadioButton) findViewById(R.id.dateEditRadio);
                buttonEdit.setChecked(true);
                break;
            case RULE_VIEW:
                RadioButton buttonView = (RadioButton) findViewById(R.id.dateViewRadio);
                buttonView.setChecked(true);
                break;
        }

        switch (order) {
            case ORDER_ASC:
                RadioButton buttonAsc = (RadioButton) findViewById(R.id.ascRadio);
                buttonAsc.setChecked(true);
                break;
            case ORDER_DESC:
                RadioButton buttonDesc = (RadioButton) findViewById(R.id.descRadio);
                buttonDesc.setChecked(true);
                break;
        }
    }

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
                answerIntent.putExtra(KEY_ORDER, order);
                answerIntent.putExtra(KEY_RULE, rule);
                setResult(RESULT_OK, answerIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener radioButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RadioButton rb = (RadioButton) view;
            switch (rb.getId()) {
                case R.id.ascRadio:
                    order = ORDER_ASC;
                    break;
                case R.id.descRadio:
                    order = ORDER_DESC;
                    break;
                case R.id.nameRadio:
                    rule = RULE_NAME;
                    break;
                case R.id.dateCreateRadio:
                    rule = RULE_CREATE;
                    break;
                case R.id.dateEditRadio:
                    rule = RULE_EDIT;
                    break;
                case R.id.dateViewRadio:
                    rule = RULE_VIEW;
                    break;
            }
        }
    };
}