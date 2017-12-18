package mx.com.devare.mudeosound;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import static android.media.MediaDrm.PROPERTY_DESCRIPTION;
import static android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_SUBTITLE;

public class AudioMediaPlayer extends AppCompatActivity implements View.OnClickListener {

    TextView tv_nombre_cancion, tv_tiempo_inicio, tv_tiempo_total;
    ImageButton ibtn_play, ibtn_pause, ibtn_stop;
    SeekBar sbar_progreso;

    MediaPlayer mediaPlayer = null;
    Handler mHandler = new Handler();

    public static final int TIME_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_media_player);
        onCreateMediaPlayer();
        inicializarComponentesUI();
        inicializarSetOnClickListener();
        inicializarDatosMediaPlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sbar_progreso.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
                onCreateMediaPlayer();
            }
        });
    }

    private void onCreateMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.collapse);
    }

    private void inicializarComponentesUI() {
        tv_nombre_cancion = (TextView) findViewById(R.id.tv_nombre_cancion);
        tv_tiempo_inicio = (TextView) findViewById(R.id.tv_tiempo_inicio);
        tv_tiempo_total = (TextView) findViewById(R.id.tv_tiempo_total);
        ibtn_play = (ImageButton) findViewById(R.id.ibtn_play);
        ibtn_pause = (ImageButton) findViewById(R.id.ibtn_pause);
        ibtn_stop = (ImageButton) findViewById(R.id.ibtn_stop);
        sbar_progreso = (SeekBar) findViewById(R.id.sbar_progreso);
    }

    private void inicializarSetOnClickListener() {
        ibtn_play.setOnClickListener(this);
        ibtn_pause.setOnClickListener(this);
        ibtn_stop.setOnClickListener(this);
    }

    private void inicializarDatosMediaPlayer() {
        tv_tiempo_total.setText(getHRM(mediaPlayer.getDuration()));
        sbar_progreso.setMax(mediaPlayer.getDuration());
        sbar_progreso.setProgress(mediaPlayer.getCurrentPosition());
        mHandler.postDelayed(updateMusica, TIME_DELAY);
    }

    Runnable updateMusica = new Runnable() {
        @Override
        public void run() {
            sbar_progreso.setProgress(mediaPlayer.getCurrentPosition());
            tv_tiempo_inicio.setText(getHRM(mediaPlayer.getCurrentPosition()));
            mHandler.postDelayed(updateMusica, TIME_DELAY);
        }
    };

    /***********************************************************
     * Método que convierte milisegundos a Hora:Minuto:Segundo *
     *********************************************************/
    private String getHRM(int milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        return ((hours < 10) ? "0" + hours : hours) + ":" +
                ((minutes < 10) ? "0" + minutes : minutes) + ":" +
                ((seconds < 10) ? "0" + seconds : seconds);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ibtn_play:
                iniciarReproducion();
                break;

            case R.id.ibtn_pause:
                pausarReproduccion();
                break;

            case R.id.ibtn_stop:
                pararReproduccion();
                break;
        }
    }

    private void iniciarReproducion() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void pausarReproduccion() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void pararReproduccion() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            onCreateMediaPlayer();
        }
    }

    /******************************************************
     * Cuando la actividad ya no es visible por el usuario,
     * se detiene el reproductor
     ****************************************************/
    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            onCreateMediaPlayer();
        }
    }

}
