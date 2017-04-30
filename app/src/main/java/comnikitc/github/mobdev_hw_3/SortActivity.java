package comnikitc.github.mobdev_hw_3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class SortActivity extends AppCompatActivity {
    private String order = "create";
    private String rule = "asc";
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
                    order = "asc";
                    break;
                case R.id.descRadio:
                    order = "desc";
                    break;
                case R.id.nameRadio:
                    rule = "name";
                    break;
                case R.id.dateCreateRadio:
                    rule = "create";
                    break;
                case R.id.dateEditRadio:
                    rule = "edit";
                    break;
                case R.id.dateViewRadio:
                    rule = "view";
                    break;
            }
        }
    };
}