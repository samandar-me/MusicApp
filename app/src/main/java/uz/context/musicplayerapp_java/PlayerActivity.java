package uz.context.musicplayerapp_java;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    ImageView btnNext, btnPrev, btnFf, btnFr;
    TextView txtName, txtStart, txtStop;
    SeekBar seekMusic;
    BarVisualizer visualizer;
    String sName;
    Button btnPlay;
    static MediaPlayer mediaPlayer;
    int position;
    ImageView imageView;
    ArrayList<File> mySongs;
    Thread updateSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btnPlay = findViewById(R.id.play_btn);
        btnNext = findViewById(R.id.btn_next);
        btnPrev = findViewById(R.id.btn_prev);
        btnFf = findViewById(R.id.btn_ff);
        btnFr = findViewById(R.id.btn_fr);
        txtName = findViewById(R.id.txtsn);
        txtStart = findViewById(R.id.txt_start);
        txtStop = findViewById(R.id.txt_stop);
        seekMusic = findViewById(R.id.seek_bar);
        visualizer = findViewById(R.id.visualizer);
        imageView = findViewById(R.id.image_view);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        position = bundle.getInt("pos", 0);
        txtName.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        sName = mySongs.get(position).getName();
        txtName.setText(sName);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        updateSeekbar = new Thread() {
            @Override
            public void run() {
                int totalDur = mediaPlayer.getDuration();
                int currentPos = 0;

                while (currentPos < totalDur) {
                    try {
                        sleep(500);
                        currentPos = mediaPlayer.getCurrentPosition();
                        seekMusic.setProgress(currentPos);
                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        seekMusic.setMax(mediaPlayer.getDuration());
        updateSeekbar.start();
        seekMusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        seekMusic.getThumb().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        seekMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        ///////////
        String endTime = createTime(mediaPlayer.getDuration());
        txtStop.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                txtStart.setText(currentTime);
                handler.postDelayed(this,delay);
            }
        }, delay);

        btnPlay.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                btnPlay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                mediaPlayer.pause();
            } else {
                btnPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                mediaPlayer.start();
            }
        });
        btnNext.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % mySongs.size());
            Uri u = Uri.parse(mySongs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
            sName = mySongs.get(position).toString();
            txtName.setText(sName);
            mediaPlayer.start();
            btnPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            startAnimation(imageView);
            int audioId = mediaPlayer.getAudioSessionId();
            if (audioId != -1) {
                visualizer.setAudioSessionId(audioId);
            }
        });
        btnPrev.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);
            Uri u = Uri.parse(mySongs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
            sName = mySongs.get(position).toString();
            txtName.setText(sName);
            mediaPlayer.start();
            btnPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            startAnimation(imageView);
            int audioId = mediaPlayer.getAudioSessionId();
            if (audioId != -1) {
                visualizer.setAudioSessionId(audioId);
            }
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            btnNext.performClick();
        });

        int audioId = mediaPlayer.getAudioSessionId();
        if (audioId != -1) {
            visualizer.setAudioSessionId(audioId);
        }

        btnFf.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
            }
        });
        btnFr.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
            }
        });
    }

    public void startAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String createTime(int duration) {
        String time = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        time += min + ":";

        if (sec < 10) {
            time += "0";
        }
        time += sec;

        return time;
    }
}