package comnikitc.github.mobdev_hw_3.ColorPicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;


public class ColorPickerScroll extends HorizontalScrollView {

    private Boolean canMove = true;
    public Boolean getCanMove() {
        return canMove;
    }

    public void setCanMove(Boolean value) {
        canMove = value;
    }

    public ColorPickerScroll(Context context) {
        super(context);
    }

    public ColorPickerScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ColorPickerScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

