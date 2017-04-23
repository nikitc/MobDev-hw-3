package comnikitc.github.mobdev_hw_3.ColorPicker;


import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.widget.LinearLayout;


class GradientColorPicker {
    private static int countColors = 30;

    public GradientColorPicker() {

    }

    static void SetGradientBackGround(final LinearLayout colorPickerLayout) {
        ShapeDrawable.ShaderFactory factory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient lg = new LinearGradient(0, 0,
                        colorPickerLayout.getWidth(), colorPickerLayout.getHeight(),
                        GradientColorPicker.GetValueColors(),
                        GradientColorPicker.GetPositionColors(),
                        Shader.TileMode.REPEAT);
                return lg;
            }
        };

        PaintDrawable paint = new PaintDrawable();
        paint.setShape(new RectShape());
        paint.setShaderFactory(factory);
        colorPickerLayout.setBackground(paint);
    }

    static int[] GetValueColors() {
        int[] valuesColors = new int[countColors];
        for (int i = 0; i < countColors; i++) {
            float[] pixelHSV = new float[3];
            pixelHSV[0] = i * 12.25f;
            pixelHSV[1] = 1;
            pixelHSV[2] = 1;

            valuesColors[i] = Color.HSVToColor(pixelHSV);
        }

        return valuesColors;
    }

    protected static float[] GetPositionColors() {
        float[] positionColors = new float[countColors];
        for (int i = 0; i < countColors; i++) {
            positionColors[i] = i / 30f ;
        }

        return positionColors;
    }
}

