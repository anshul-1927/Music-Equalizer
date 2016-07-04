package dev.datvt.musicequalizer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBar {

    private int mLastProgress = 0;
    private OnSeekBarChangeListener mOnChangeListener;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected synchronized void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);
        super.onDraw(c);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    public void updateThumb(){
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case 0: //MotionEvent.ACTION_DOWN:
                if (mOnChangeListener != null) {
                    mOnChangeListener.onStartTrackingTouch(this);
                }
                setPressed(true);
                setSelected(true);
                break;
            case 2: //MotionEvent.ACTION_MOVE:
                super.onTouchEvent(event);
                int progress = getMax() - (int) (getMax() * event.getY() / getHeight());

                // Ensure progress stays within boundaries
                if (progress < 0) {
                    progress = 0;
                }
                if (progress > getMax()) {
                    progress = getMax();
                }
                setProgress(progress); // Draw progress
                if (progress != mLastProgress) {
                    // Only enact listener if the progress has actually changed
                    mLastProgress = progress;
                    if (mOnChangeListener != null) {
                        mOnChangeListener.onProgressChanged(this, progress, true);
                    }
                }

                onSizeChanged(getWidth(), getHeight(), 0, 0);
                setPressed(true);
                setSelected(true);
                break;
            case 1: //MotionEvent.ACTION_UP:
                if (mOnChangeListener != null) {
                    mOnChangeListener.onStopTrackingTouch(this);
                }
                setPressed(false);
                setSelected(false);
                break;
            case 3: //MotionEvent.ACTION_CANCEL:
                super.onTouchEvent(event);
                setPressed(false);
                setSelected(false);
                break;
        }
        return true;
    }

    private void changeProcess(MotionEvent motionEvent) {
        float f;
        int height = getHeight();
        int paddingBottom = (height - getPaddingBottom()) - getPaddingTop();
        int y = (int) motionEvent.getY();
        if (y > height - getPaddingBottom()) {
            f = 0.0f;
        } else if (y < getPaddingTop()) {
            f = 1.0f;
        } else {
            f = ((float) ((height - getPaddingBottom()) - y)) / ((float) paddingBottom);
        }
        setProgress((int) (f * ((float) getMax())));
    }

}
