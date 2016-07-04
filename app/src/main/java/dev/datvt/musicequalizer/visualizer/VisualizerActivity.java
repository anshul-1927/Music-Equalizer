package dev.datvt.musicequalizer.visualizer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

import dev.datvt.musicequalizer.R;

/**
 * Created by datvt on 7/2/2016.
 */
public class VisualizerActivity extends Activity implements View.OnClickListener {

    VisualizerView visualizerView;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizer);

        AudioManager manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if (manager.isMusicActive()) {
            visualizerView = (VisualizerView) findViewById(R.id.my_visualizer);
            visualizerView.setOnClickListener(this);
            visualizerView.link();
            visualizerView.flash();

            addBarGraphRenderers();
            addLineRenderer();
            addCircleBarRenderer();
            addCircleRenderer();
        }
    }

    @Override
    protected void onPause() {
        visualizerView.release();
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        if (view == visualizerView) {
            if (position < 6) {
                visualizerView.clearRenderers();
                position++;
                changeEffect(position);
            } else {
                position = 0;
                changeEffect(position);
            }
        }
    }

    private void changeEffect(int pos) {
        switch (pos) {
            case 0:
                visualizerView.clearRenderers();
                addBarGraphRenderers();
                addCircleRenderer();
                addCircleBarRenderer();
                addLineRenderer();
                break;
            case 1:
                visualizerView.clearRenderers();
                addBarGraphRenderers();
                break;
            case 2:
                visualizerView.clearRenderers();
                addCircleRenderer();
                break;
            case 3:
                visualizerView.clearRenderers();
                addCircleBarRenderer();
                break;
            case 4:
                visualizerView.clearRenderers();
                addLineRenderer();
                break;
            case 5:
                visualizerView.clearRenderers();
                addBarGraphOnRenderers();
                break;
            case 6:
                visualizerView.clearRenderers();
                addBarGraphBelowRenderers();
                break;
        }
    }

    public void addBarGraphRenderers() {
        Paint paint = new Paint();
        paint.setStrokeWidth(50f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 255, 0, 0));
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(13, paint, false, 30);
        visualizerView.addRenderer(barGraphRendererBottom);

        Paint paint2 = new Paint();
        paint2.setStrokeWidth(20f);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.argb(255, 0, 255, 0));
        BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(6, paint2, true, 30);
        visualizerView.addRenderer(barGraphRendererTop);
    }

    public void addBarGraphOnRenderers() {
        Paint paint2 = new Paint();
        paint2.setStrokeWidth(50f);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.argb(255, 0, 255, 0));
        BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(13, paint2, true, 50);
        visualizerView.addRenderer(barGraphRendererTop);
    }

    public void addGraphRenderers() {
        Paint paint2 = new Paint();
        paint2.setStrokeWidth(1f);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.argb(255, 0, 0, 0));
        BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(1, paint2, true, 50);
        visualizerView.addRenderer(barGraphRendererTop);
    }

    public void addBarGraphBelowRenderers() {
        Paint paint = new Paint();
        paint.setStrokeWidth(50f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 255, 0, 0));
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(13, paint, false, 50);
        visualizerView.addRenderer(barGraphRendererBottom);
    }

    public void addCircleBarRenderer() {
        Paint paint = new Paint();
        paint.setStrokeWidth(8f);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        paint.setColor(Color.argb(255, 222, 92, 143));
        CircleBarRenderer circleBarRenderer = new CircleBarRenderer(paint, 32, true);
        visualizerView.addRenderer(circleBarRenderer);
    }

    public void addCircleRenderer() {
        Paint paint = new Paint();
        paint.setStrokeWidth(3f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 222, 92, 143));
        CircleRenderer circleRenderer = new CircleRenderer(paint, true);
        visualizerView.addRenderer(circleRenderer);
    }

    public void addLineRenderer() {
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(1f);
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.argb(88, 0, 128, 255));

        Paint lineFlashPaint = new Paint();
        lineFlashPaint.setStrokeWidth(5f);
        lineFlashPaint.setAntiAlias(true);
        lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
        LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint, true);
        visualizerView.addRenderer(lineRenderer);
    }
}
