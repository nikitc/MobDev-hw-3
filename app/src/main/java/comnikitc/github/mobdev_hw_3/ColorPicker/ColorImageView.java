package comnikitc.github.mobdev_hw_3.ColorPicker;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ColorImageView extends ImageView {
    private float[] hsvColor;

    public float[] getHsvColor() {
        return hsvColor;
    }

    public void setHsvColor(float[] value) {
        hsvColor = value;
    }
    public ColorImageView(Context context, float[] hsv) {
        super(context);
        this.hsvColor = hsv;
    }

    public ColorImageView(Context context) {
        super(context);
    }

    public ColorImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
