package comnikitc.github.mobdev_hw_3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class NoteImageView extends ImageView {
    public NoteImageView(Context context) {
        super(context);
    }

    public NoteImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {

        super.setBackgroundDrawable(background);
    }
}
