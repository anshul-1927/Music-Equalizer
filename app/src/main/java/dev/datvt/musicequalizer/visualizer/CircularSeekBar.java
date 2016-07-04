package dev.datvt.musicequalizer.visualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by datvt on 7/3/2016.
 */
public class CircularSeekBar extends SeekBar {

    public CircularSeekBar(Context paramContext) {
        super(paramContext);
    }

    public CircularSeekBar(Context paramContext,
                           AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public CircularSeekBar(Context paramContext,
                           AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public void draw(Canvas paramCanvas) {
        int i = getMeasuredWidth();
        int j = getMeasuredHeight();
        paramCanvas.save();
        paramCanvas.rotate(135.0F, i / 2, j / 2);
        super.draw(paramCanvas);
        paramCanvas.restore();
    }
}
