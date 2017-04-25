package comnikitc.github.mobdev_hw_3.ColorPicker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import comnikitc.github.mobdev_hw_3.CreateNoteActivity;
import comnikitc.github.mobdev_hw_3.R;


public class ColorActivity extends AppCompatActivity {

    private ColorPickerScroll scroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        scroll = (ColorPickerScroll) findViewById(R.id.colorPickerScroll);
        createColorPicker();
        createChooseColorView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.color_picker_menu, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        ColorImageView chooseColorView = (ColorImageView) findViewById(R.id.chooseColorImage);

        savedInstanceState.putFloatArray("chooseColor", chooseColorView.getHsvColor());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveColor:
                goToCreateNote();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        displayCurrentStatus(savedInstanceState.getFloatArray("chooseColor"));
    }

    private void createChooseColorView() {
        ColorImageView chooseColorView = (ColorImageView) findViewById(R.id.chooseColorImage);
        chooseColorView.setHsvColor(new float[] {0, 1.0f, 0});
    }

    private void goToCreateNote() {
        ColorImageView chooseColorView = (ColorImageView) findViewById(R.id.chooseColorImage);
        int color = Color.HSVToColor(chooseColorView.getHsvColor());

        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra("color", color);

        Toast toast = Toast.makeText(getApplicationContext(),
                R.string.saveChooseColor, Toast.LENGTH_SHORT);
        toast.show();

        setResult(RESULT_OK, intent);
        finish();
    }

    private void displayCurrentStatus(float[] hsvColor) {

        ColorImageView chooseColorView = (ColorImageView) findViewById(R.id.chooseColorImage);
        chooseColorView.setHsvColor(hsvColor);

        TextView rgbText = (TextView) findViewById(R.id.chooseValueRGB);
        TextView hsvText = (TextView) findViewById(R.id.chooseValueHSV);

        chooseColorView.setBackgroundColor(Color.HSVToColor(hsvColor));

        String newRGBColor = ColorButton.getStringRGBColor(hsvColor);
        String newHSVColor = ColorButton.getStringHSVColor(hsvColor);

        rgbText.setText(newRGBColor);
        hsvText.setText(newHSVColor);
    }


    private void displayOnPalitreStatus(ColorButton currentColorButton) {

        ImageView chooseColorView = (ImageView) findViewById(R.id.currentColorImage);

        TextView rgbText = (TextView) findViewById(R.id.currentValueRGB);
        TextView hsvText = (TextView) findViewById(R.id.currentValueHSV);


        chooseColorView.setBackgroundColor(
                Color.HSVToColor(currentColorButton.getCurrentColor()));

        String newRGBColor = ColorButton.getStringRGBColor(currentColorButton.getCurrentColor());
        String newHSVColor = ColorButton.getStringHSVColor(currentColorButton.getCurrentColor());

        rgbText.setText(newRGBColor);
        hsvText.setText(newHSVColor);
    }

    private boolean isDoubleClick(long lastClickTime) {
        final long doublePressInterval = 500;
        return SystemClock.elapsedRealtime() - lastClickTime < doublePressInterval;
    }

    private View.OnLongClickListener getOnLongClickListener() {
        View.OnLongClickListener onClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        R.string.startEdit, Toast.LENGTH_SHORT);
                toast.show();
                disableColorPickerScroll();
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
        };

        return onClickListener;
    }


    private View.OnTouchListener getOnTouchListener() {
        View.OnTouchListener onClickListener = new View.OnTouchListener() {
            float x = 0;
            float y = 0;


            @Override
            public boolean onTouch(View view, MotionEvent event) {
                ColorButton currentColorButton = (ColorButton) view;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        handleActionDown(currentColorButton);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (scroll.getCanMove()) {
                            break;
                        }
                        handleActionMove(currentColorButton, x, y, event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        if (!scroll.getCanMove()) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    R.string.finishEdit, Toast.LENGTH_SHORT);
                            toast.show();
                            scroll.setCanMove(true);
                        }
                        break;
                }
                return false;
            }

        };
        return onClickListener;
    }


    private int getSign(double number) {
        if (number > 0)
            return 1;
        else if (number < 0)
            return  -1;

        return 0;
    }


    private void handleActionMove(ColorButton currentColorButton, float oldX, float oldY,
                                    float x, float y) {
        float deltaX = x - oldX;
        float deltaY = oldY - y;
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (((currentColorButton.getCurrentColor()[0] == currentColorButton.getRightBorder()) &&
                    getSign(deltaX) > 0) ||
                    ((currentColorButton.getCurrentColor()[0] == currentColorButton.getLeftBorder() &&
                            getSign(deltaX) < 0))) {
                callVibrator();
            } else {
                currentColorButton.getCurrentColor()[0] += 0.25 * getSign(deltaX);
            }
        } else {
            if (((currentColorButton.getCurrentColor()[2] == currentColorButton.upBorderColor) &&
                    getSign(deltaY) > 0) ||
                    ((Math.max(currentColorButton.getCurrentColor()[2], 0) == currentColorButton.downBorderColor &&
                            getSign(deltaY) < 0))) {
                callVibrator();
            } else {
                currentColorButton.getCurrentColor()[2] += 0.025 * getSign(deltaY);
            }
        }

        currentColorButton.setBackgroundColor(
                Color.HSVToColor(currentColorButton.getCurrentColor()));
        displayOnPalitreStatus(currentColorButton);
    }

    private void handleActionDown(ColorButton currentColorButton) {

        if (isDoubleClick(currentColorButton.lastClickTime)) {
            currentColorButton.setBackgroundColor(
                    Color.HSVToColor(currentColorButton.getOriginalColor()));
            currentColorButton.discardColor();
            displayOnPalitreStatus(currentColorButton);
            currentColorButton.lastClickTime = SystemClock.elapsedRealtime();
            return;
        }

        currentColorButton.lastClickTime = SystemClock.elapsedRealtime();
        displayCurrentStatus(currentColorButton.getCurrentColor());
    }

    private void disableColorPickerScroll() {
        ColorPickerScroll scroll = (ColorPickerScroll) findViewById(R.id.colorPickerScroll);
        scroll.setCanMove(false);
    }

    private void callVibrator() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    private void createColorPicker() {
        float[] pixelHSV = new float[3];
        pixelHSV[0] = 12.25f;
        pixelHSV[1] = 1;
        pixelHSV[2] = 1;
        int countButtons = 16;
        final LinearLayout colorPickerLayout =
                (LinearLayout) this.findViewById(R.id.colorPickerLayout);
        final float step = 22.25f;

        for (int i = 0; i < countButtons; i++) {
            ColorButton button = new ColorButton(this, pixelHSV.clone());
            button.setOnLongClickListener(getOnLongClickListener());
            button.setOnTouchListener(getOnTouchListener());
            colorPickerLayout.addView(button);
            pixelHSV[0] += step;
        }
        GradientColorPicker.setGradientBackGround(colorPickerLayout);
    }

}