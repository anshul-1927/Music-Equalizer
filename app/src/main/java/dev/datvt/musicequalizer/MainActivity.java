package dev.datvt.musicequalizer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.AudioManager;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dev.datvt.musicequalizer.list_song.DataHelper;
import dev.datvt.musicequalizer.list_song.ListMusicActivity;
import dev.datvt.musicequalizer.list_song.Music;
import dev.datvt.musicequalizer.visualizer.BarGraphRenderer;
import dev.datvt.musicequalizer.visualizer.CircleBarRenderer;
import dev.datvt.musicequalizer.visualizer.CircleRenderer;
import dev.datvt.musicequalizer.visualizer.LineRenderer;
import dev.datvt.musicequalizer.visualizer.VisualizerActivity;
import dev.datvt.musicequalizer.visualizer.VisualizerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {

    private final int REQUEST_CODE_SONG_LIST = 100;

    private TextView tvSongName, tvSongDuration, tvTimeRun;
    private TextView tv1, tv2, tv3, tv4, tv5;
    private ImageView btnListMusic;
    private ImageView btnPlay, btnRepeat, btnPrev, btnNext, btnShuffle, btnSpectrum;
    private ImageView btnBass, btnVisualizer, btnVolumn, btnOn;
    private ImageView progressBass, progressVisualizer, progressVolumn;
    private SeekBar seekBar;
    private Spinner spinner;
    private VisualizerView mVisualizerView;
    private DataHelper dataHelper;
    private ArrayList<Music> musicArrayList;
    private Handler mHandler = new Handler();
    private AudioManager manager;
    private Equalizer mEqualizer;
    private VerticalSeekBar seekBar_one, seekBar_two, seekBar_three, seekBar_four, seekBar_five;
    private ArrayList<String> equalizerPresetNames;
    private ListSpinnerAdapter spinnerAdapter;

    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;

    private boolean checkPlay = false;
    private boolean checkRepeat = false;
    private boolean checkShufle = false;
    private boolean checkOn = false;
    private int pos = 0;
    private double mCurrAngle = 0;
    private double mPrevAngle = 0;
    private double startTime = 0;
    private short lowerEqualizerBandLevel = 0;
    private short upperEqualizerBandLevel = 0;
    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            musicService.setList(musicArrayList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    // Cập nhật thanh progress chạy nhạc
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if (musicService != null && musicService.getMediaPlayer() != null) {
                startTime = musicService.getMediaPlayer().getCurrentPosition();
                long minus = TimeUnit.MILLISECONDS.toMinutes((long) startTime);
                long second = TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime));
                if (minus < 10) {
                    if (second < 10) {
                        tvTimeRun.setText("0" + minus + ":" + "0" + second);
                    } else {
                        tvTimeRun.setText("0" + minus + ":" + second);
                    }
                } else {
                    if (second < 10) {
                        tvTimeRun.setText(minus + ":" + "0" + second);
                    } else {
                        tvTimeRun.setText(minus + ":" + second);
                    }
                }
                seekBar.setProgress((int) startTime);
                mHandler.postDelayed(this, 1000);
                if (startTime == seekBar.getMax()) {
                    btnPlay.setImageResource(R.drawable.btn_play);
                    musicService.nextSong();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_music);

        getForWidgets();
        addEvents();
    }

    // Lấy id cho các widget
    private void getForWidgets() {
        btnListMusic = (ImageView) findViewById(R.id.btnListMusic);
        btnSpectrum = (ImageView) findViewById(R.id.btnSpectrum);
        btnPlay = (ImageView) findViewById(R.id.btnPlay);
        btnPrev = (ImageView) findViewById(R.id.btnPrev);
        btnNext = (ImageView) findViewById(R.id.btnNext);
        btnShuffle = (ImageView) findViewById(R.id.btnShufle);
        btnRepeat = (ImageView) findViewById(R.id.btnRepeat);
        ////////////////////////////////////////////
        btnBass = (ImageView) findViewById(R.id.btnBass);
        btnVisualizer = (ImageView) findViewById(R.id.btnVisualizer);
        btnVolumn = (ImageView) findViewById(R.id.btnVolumn);
        btnOn = (ImageView) findViewById(R.id.btnOn);
        progressBass = (ImageView) findViewById(R.id.iv_cb_progress_bg);
        progressVisualizer = (ImageView) findViewById(R.id.iv_cb_progress_bg_1);
        progressVolumn = (ImageView) findViewById(R.id.iv_cb_v_progress_bg);
        ///////////////////////////////////
        tvSongName = (TextView) findViewById(R.id.tvSongName);
        tvSongDuration = (TextView) findViewById(R.id.tvSongDuration);
        tvTimeRun = (TextView) findViewById(R.id.tvTimeRun);
        tvSongName.setSelected(true);
        tvSongName.setText("");
        tvSongDuration.setText("00.00");
        ///////////////////////////////////////////////////
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        ///////////////////////////////////////////////////////
        mVisualizerView = (VisualizerView) findViewById(R.id.visualizer_view);
        ///////////////////////////////////////////////////////////
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        ///////////////////////////////////////////////////////
        seekBar_one = (VerticalSeekBar) findViewById(R.id.seekBar_one);
        seekBar_three = (VerticalSeekBar) findViewById(R.id.seekBar_three);
        seekBar_two = (VerticalSeekBar) findViewById(R.id.seekBar_two);
        seekBar_four = (VerticalSeekBar) findViewById(R.id.seekBar_four);
        seekBar_five = (VerticalSeekBar) findViewById(R.id.seekBar_five);
        spinner = (Spinner) findViewById(R.id.spinner_equalizer);
        ///////////////////////////////////////////////
        mEqualizer = new Equalizer(0, 0);
        mEqualizer.setEnabled(true);
        initEqualizer();
        ////////////////////////////////////////
        musicArrayList = new ArrayList<>();
        dataHelper = new DataHelper();
        musicArrayList = dataHelper.getDataMusic(this);
    }

    // Bắt các sự kiện cho widgets
    private void addEvents() {
        btnListMusic.setOnClickListener(this);
        btnSpectrum.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
        btnShuffle.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnOn.setOnClickListener(this);
        btnBass.setOnTouchListener(this);
        btnVisualizer.setOnTouchListener(this);
        btnVolumn.setOnTouchListener(this);

        seekBar.setClickable(false);
        seekBar_one.setOnSeekBarChangeListener(this);
        seekBar_two.setOnSeekBarChangeListener(this);
        seekBar_three.setOnSeekBarChangeListener(this);
        seekBar_four.setOnSeekBarChangeListener(this);
        seekBar_five.setOnSeekBarChangeListener(this);

        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if (manager.isMusicActive()) {
            mVisualizerView.link();
            addBarGraphRenderers();
            addLineRenderer();
            addCircleBarRenderer();
            addCircleRenderer();

        }
    }

    @Override
    protected void onDestroy() {
        cleanUp();
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    private void initPlayerMusic(int index) {
        musicService.setSong(index);
        musicService.playSong();

        // Thiết lập giá trị cho progress nhạc
        seekBar.setProgress((int) startTime);
        seekBar.setMax(musicService.getMediaPlayer().getDuration());
        mHandler.postDelayed(UpdateSongTime, 1000);
        tvSongName.setText(musicArrayList.get(pos).getName() + " - " + musicArrayList.get(pos).getAuthor());
        tvSongDuration.setText(musicArrayList.get(pos).getTime());

        // Khởi tạo visualizer
        mVisualizerView.link(musicService.getMediaPlayer());
        addBarGraphRenderers();
        addCircleBarRenderer();
        addCircleRenderer();
        addLineRenderer();

        // Khởi tạo Equalizer
//        mEqualizer = new Equalizer(0, musicService.getMediaPlayer().getAudioSessionId());
//        mEqualizer.setEnabled(true);
//        initEqualizer();
    }

    // Giải phóng khi thoát app
    private void cleanUp() {
        if (musicService != null) {
            mVisualizerView.release();
            mEqualizer.release();
            musicService.stopSong();
        }
    }

    // Khởi tạo equalizer
    private void initEqualizer() {
        equalizerPresetNames = new ArrayList<String>();

        lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];
        upperEqualizerBandLevel = mEqualizer.getBandLevelRange()[1];

        seekBar_one.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);
        seekBar_three.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);
        seekBar_two.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);
        seekBar_four.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);
        seekBar_five.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);

        seekBar_one.setProgress(mEqualizer.getBandLevel((short) 0));
        seekBar_three.setProgress(mEqualizer.getBandLevel((short) 2));
        seekBar_two.setProgress(mEqualizer.getBandLevel((short) 1));
        seekBar_four.setProgress(mEqualizer.getBandLevel((short) 3));
        seekBar_five.setProgress(mEqualizer.getBandLevel((short) 4));

        seekBar_one.updateThumb();
        seekBar_two.updateThumb();
        seekBar_four.updateThumb();
        seekBar_three.updateThumb();
        seekBar_five.updateThumb();

        tv1.setText((seekBar_one.getProgress() - 1500) + "");
        tv2.setText((seekBar_two.getProgress() - 1500) + "");
        tv3.setText((seekBar_three.getProgress() - 1500) + "");
        tv4.setText((seekBar_four.getProgress() - 1500) + "");
        tv5.setText((seekBar_five.getProgress() - 1500) + "");

        equalizerPresetNames.clear();

        for (short i = 0; i < mEqualizer.getNumberOfPresets(); i++) {
            equalizerPresetNames.add(mEqualizer.getPresetName(i));
        }

        spinnerAdapter = new ListSpinnerAdapter(this, android.R.layout.simple_spinner_item, equalizerPresetNames);
        spinner.setAdapter(spinnerAdapter);
    }

    // Vẽ đồ thị
    public void addBarGraphRenderers() {
        Paint paint = new Paint();
        paint.setStrokeWidth(50f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 255, 0, 0));
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(13, paint, false, 10);
        mVisualizerView.addRenderer(barGraphRendererBottom);

        Paint paint2 = new Paint();
        paint2.setStrokeWidth(20f);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.argb(255, 0, 255, 0));
        BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(6, paint2, true, 10);
        mVisualizerView.addRenderer(barGraphRendererTop);
    }

    // Vẽ đồ thị theo vòng tròn trong visualizer
    public void addCircleBarRenderer() {
        Paint paint = new Paint();
        paint.setStrokeWidth(8f);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        paint.setColor(Color.argb(255, 222, 92, 143));
        CircleBarRenderer circleBarRenderer = new CircleBarRenderer(paint, 32, true);
        mVisualizerView.addRenderer(circleBarRenderer);
    }

    // Vẽ vòng tròn trong Visualizer
    public void addCircleRenderer() {
        Paint paint = new Paint();
        paint.setStrokeWidth(3f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 222, 92, 143));
        CircleRenderer circleRenderer = new CircleRenderer(paint, true);
        mVisualizerView.addRenderer(circleRenderer);
    }

    // Vẽ đường kẻ trong visualizer
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
        mVisualizerView.addRenderer(lineRenderer);
    }

    // Xóa Visualizer
    public void clearRenderer() {
        if (mVisualizerView != null) {
            mVisualizerView.clearRenderers();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SONG_LIST && data != null) {
            if (resultCode == 101) {
                musicService.stopSong();
                pos = data.getIntExtra("position", 0);
                tvSongName.setText(musicArrayList.get(pos).getName() + " - " + musicArrayList.get(pos).getAuthor());
                tvSongDuration.setText(musicArrayList.get(pos).getTime());
                initPlayerMusic(pos);
                btnPlay.setImageResource(R.drawable.btn_pause);
                checkPlay = true;

            }
        }
    }

    // Hiển thị danh sách nhạc
    private void displayListMusic() {
        Intent intentSongList = new Intent(MainActivity.this, ListMusicActivity.class);
        startActivityForResult(intentSongList, REQUEST_CODE_SONG_LIST);
    }

    // Hiển thị spectrum
    private void displaySpectrum() {
        Intent intentVisualizer = new Intent(MainActivity.this, VisualizerActivity.class);
        intentVisualizer.putExtra("Song", musicArrayList.get(pos).getPathSong());
        startActivity(intentVisualizer);
    }

    // Chơi nhạc
    private void playMusic() {
        if (musicArrayList.size() > 0 && musicBound) {
            if (!checkPlay) {
                initPlayerMusic(pos);
                btnPlay.setImageResource(R.drawable.btn_pause);
                checkPlay = true;
            } else {
                musicService.stopSong();
                clearRenderer();
                btnPlay.setImageResource(R.drawable.btn_play);
                checkPlay = false;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Danh sách bài hát trống", Toast.LENGTH_SHORT).show();
        }
    }

    // Chọn nhạc sau
    private void nextSong() {
        if (musicArrayList.size() > 0 && musicBound) {
            if (pos < musicArrayList.size() - 1) {
                musicService.stopSong();
                clearRenderer();
                pos += 1;
                initPlayerMusic(pos);
                btnPlay.setImageResource(R.drawable.btn_pause);
                tvSongName.setText(musicArrayList.get(pos).getName() + " - " + musicArrayList.get(pos).getAuthor());
                tvSongDuration.setText(musicArrayList.get(pos).getTime());
            } else {
                Toast.makeText(getApplicationContext(), "Đây là bài hát cuối cùng", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Danh sách bài hát trống", Toast.LENGTH_SHORT).show();
        }
    }

    // Chọn nhạc trước
    private void previousSong() {
        if (musicArrayList.size() > 0 && musicBound) {
            if (pos > 0) {
                musicService.stopSong();
                clearRenderer();
                pos -= 1;
                initPlayerMusic(pos);
                btnPlay.setImageResource(R.drawable.btn_play);
                tvSongName.setText(musicArrayList.get(pos).getName() + " - " + musicArrayList.get(pos).getAuthor());
                tvSongDuration.setText(musicArrayList.get(pos).getTime());

            } else {
                Toast.makeText(getApplicationContext(), "Đây là bài hát đầu tiên", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Danh sách bài hát trống", Toast.LENGTH_SHORT).show();
        }
    }

    // Lặp nhạc
    private void repeatSong() {
        if (musicBound) {
            if (!checkRepeat) {
                musicService.loopSong(1, pos);
                btnRepeat.setImageResource(R.drawable.ic_player_v4_repeat_all);
                checkRepeat = true;
            } else {
                checkRepeat = false;
                musicService.loopSong(0, pos);
                btnRepeat.setImageResource(R.drawable.ic_player_v4_repeat_off);
            }
        }
    }

    // Trộn nhạc
    private void shuffleSong() {
        if (musicBound) {
            if (!checkShufle) {
                btnShuffle.setImageResource(R.drawable.ic_player_v4_shuffle_on);
                checkShufle = true;
            } else {
                btnShuffle.setImageResource(R.drawable.ic_player_v4_shuffle_off);
                checkShufle = false;
            }
        }
    }

    // Bật tắt điều chỉnh equalizer
    private void onOffEqualizer() {
        if (!checkOn) {
            btnOn.setImageResource(R.drawable.chb_on);
            checkOn = true;
        } else {
            btnOn.setImageResource(R.drawable.chb_off);
            checkOn = false;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnListMusic:
                displayListMusic();
                break;
            case R.id.btnSpectrum:
                displaySpectrum();
                break;
            case R.id.btnPlay:
                playMusic();
                break;
            case R.id.btnNext:
                nextSong();
                break;
            case R.id.btnPrev:
                previousSong();
                break;
            case R.id.btnRepeat:
                repeatSong();
                break;
            case R.id.btnShufle:
                shuffleSong();
                break;
            case R.id.btnOn:
                onOffEqualizer();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == btnBass) {
            processRotateBass(btnBass, event, progressBass);
        }

        if (v == btnVisualizer) {
            processRotateBass(btnVisualizer, event, progressVisualizer);
        }

        if (v == btnVolumn) {
            processRotateBass(btnVolumn, event, progressVolumn);
        }
        return true;

    }

    // Animation khi xoay nút
    private void animate(double fromDegrees, double toDegrees, long durationMillis, View view) {
        final RotateAnimation rotate = new RotateAnimation((float) fromDegrees, (float) toDegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillEnabled(true);
        rotate.setFillAfter(true);
        view.startAnimation(rotate);
    }

    // Xử lý việc xoay nút khi chạm vào
    private void processRotateBass(View view, MotionEvent event, View view_progress) {
        final float xc = view.getWidth() / 2;
        final float yc = view.getHeight() / 2;

        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                view.clearAnimation();
                mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mPrevAngle = mCurrAngle;
                mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));

                if (mCurrAngle > 0 || mCurrAngle < -90) {
                    animate(mPrevAngle, mCurrAngle, 0, view);
                }

                if (view_progress == progressVisualizer) {
                    if (mCurrAngle > 10 && mCurrAngle <= 40) {
                        progressVisualizer.setImageResource(R.drawable.cb_progress_bg_1);
                    } else if (mCurrAngle > 40 && mCurrAngle <= 70) {
                        progressVisualizer.setImageResource(R.drawable.cb_progress_bg_2);
                    } else if (mCurrAngle > 70 && mCurrAngle <= 100) {
                        progressVisualizer.setImageResource(R.drawable.cb_progress_bg_3);
                    } else if (mCurrAngle > 100 && mCurrAngle <= 130) {
                        progressVisualizer.setImageResource(R.drawable.cb_progress_bg_4);
                    } else if (mCurrAngle > 130 && mCurrAngle <= 160) {
                        progressVisualizer.setImageResource(R.drawable.cb_progress_bg_5);
                    } else if (mCurrAngle > 160 && mCurrAngle <= 180) {
                        progressVisualizer.setImageResource(R.drawable.cb_progress_bg_6);
                    } else if (mCurrAngle > -180 && mCurrAngle <= -150) {
                        progressVisualizer.setImageResource(R.drawable.cb_progress_bg_7);
                    } else if (mCurrAngle > -150 && mCurrAngle <= -110) {
                        progressVisualizer.setImageResource(R.drawable.cb_progress_bg_8);
                    } else if (mCurrAngle > -110 && mCurrAngle <= -40) {
                        progressVisualizer.setImageResource(R.drawable.cb_progress_bg_9);
                    } else {
                        progressVisualizer.setImageResource(R.drawable.cb_progress_bg_0);
                    }
                }

                if (view_progress == progressBass) {
                    if (mCurrAngle > 10 && mCurrAngle <= 40) {
                        progressBass.setImageResource(R.drawable.cb_progress_bg_1);
                    } else if (mCurrAngle > 40 && mCurrAngle <= 70) {
                        progressBass.setImageResource(R.drawable.cb_progress_bg_2);
                    } else if (mCurrAngle > 70 && mCurrAngle <= 100) {
                        progressBass.setImageResource(R.drawable.cb_progress_bg_3);
                    } else if (mCurrAngle > 100 && mCurrAngle <= 130) {
                        progressBass.setImageResource(R.drawable.cb_progress_bg_4);
                    } else if (mCurrAngle > 130 && mCurrAngle <= 160) {
                        progressBass.setImageResource(R.drawable.cb_progress_bg_5);
                    } else if (mCurrAngle > 160 && mCurrAngle <= 180) {
                        progressBass.setImageResource(R.drawable.cb_progress_bg_6);
                    } else if (mCurrAngle > -180 && mCurrAngle <= -150) {
                        progressBass.setImageResource(R.drawable.cb_progress_bg_7);
                    } else if (mCurrAngle > -150 && mCurrAngle <= -110) {
                        progressBass.setImageResource(R.drawable.cb_progress_bg_8);
                    } else if (mCurrAngle > -110 && mCurrAngle <= -40) {
                        progressBass.setImageResource(R.drawable.cb_progress_bg_9);
                    } else {
                        progressBass.setImageResource(R.drawable.cb_progress_bg_0);
                    }
                }

                if (view_progress == progressVolumn) {
                    if (mCurrAngle > 10 && mCurrAngle <= 40) {
                        progressVolumn.setImageResource(R.drawable.cb_progress_bg_1);
                    } else if (mCurrAngle > 40 && mCurrAngle <= 70) {
                        progressVolumn.setImageResource(R.drawable.cb_progress_bg_2);
                    } else if (mCurrAngle > 70 && mCurrAngle <= 100) {
                        progressVolumn.setImageResource(R.drawable.cb_progress_bg_3);
                    } else if (mCurrAngle > 100 && mCurrAngle <= 130) {
                        progressVolumn.setImageResource(R.drawable.cb_progress_bg_4);
                    } else if (mCurrAngle > 130 && mCurrAngle <= 160) {
                        progressVolumn.setImageResource(R.drawable.cb_progress_bg_5);
                    } else if (mCurrAngle > 160 && mCurrAngle <= 180) {
                        progressVolumn.setImageResource(R.drawable.cb_progress_bg_6);
                    } else if (mCurrAngle > -180 && mCurrAngle <= -150) {
                        progressVolumn.setImageResource(R.drawable.cb_progress_bg_7);
                    } else if (mCurrAngle > -150 && mCurrAngle <= -110) {
                        progressVolumn.setImageResource(R.drawable.cb_progress_bg_8);
                    } else if (mCurrAngle > -110 && mCurrAngle <= -40) {
                        progressVolumn.setImageResource(R.drawable.cb_progress_bg_9);
                    } else {
                        progressVolumn.setImageResource(R.drawable.cb_progress_bg_0);
                    }
                }

                break;
            }
            case MotionEvent.ACTION_UP: {
                mPrevAngle = mCurrAngle = 0;
                break;
            }
        }
    }

    // Bắt sự kiện thay đổi thanh seekbar điều chỉnh Equalizer
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if (seekBar == seekBar_one) {
            mEqualizer.setBandLevel((short) 0,
                    (short) (progress + lowerEqualizerBandLevel));
            tv1.setText((seekBar_one.getProgress() - 1500) + "");
        }

        if (seekBar == seekBar_two) {
            mEqualizer.setBandLevel((short) 1,
                    (short) (progress + lowerEqualizerBandLevel));
            tv2.setText((seekBar_two.getProgress() - 1500) + "");
        }

        if (seekBar == seekBar_three) {
            mEqualizer.setBandLevel((short) 2,
                    (short) (progress + lowerEqualizerBandLevel));
            tv3.setText((seekBar_three.getProgress() - 1500) + "");
        }

        if (seekBar == seekBar_four) {
            mEqualizer.setBandLevel((short) 3,
                    (short) (progress + lowerEqualizerBandLevel));
            tv4.setText((seekBar_four.getProgress() - 1500) + "");
        }

        if (seekBar == seekBar_five) {
            mEqualizer.setBandLevel((short) 4,
                    (short) (progress + lowerEqualizerBandLevel));
            tv5.setText((seekBar_five.getProgress() - 1500) + "");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    // Bắt sự kiện thay đổi khi điều chỉnh spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mEqualizer.usePreset((short) i);

        seekBar_one.setProgress(mEqualizer.getBandLevel((short) 0) - lowerEqualizerBandLevel);
        seekBar_two.setProgress(mEqualizer.getBandLevel((short) 1) - lowerEqualizerBandLevel);
        seekBar_three.setProgress(mEqualizer.getBandLevel((short) 2) - lowerEqualizerBandLevel);
        seekBar_four.setProgress(mEqualizer.getBandLevel((short) 3) - lowerEqualizerBandLevel);
        seekBar_five.setProgress(mEqualizer.getBandLevel((short) 4) - lowerEqualizerBandLevel);

        seekBar_one.updateThumb();
        seekBar_two.updateThumb();
        seekBar_four.updateThumb();
        seekBar_three.updateThumb();
        seekBar_five.updateThumb();

        Log.d("EQUALIZER: ", seekBar_one.getProgress() + " - " + seekBar_two.getProgress() + " - " +
                seekBar_three.getProgress() + " - " + seekBar_four.getProgress() + " - " + seekBar_five.getProgress());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
