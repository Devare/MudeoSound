package mx.com.devare.mudeosound;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Switch;
import android.widget.VideoView;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class VideoMediaPlayer extends AppCompatActivity {
    VideoView videoView;
    int posicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obtenerOrientacioDispositivo();
        setContentView(R.layout.activity_video_media_player);

        inicializarComponentesUI();
        iniciarVideoMediaPlayer();
    }

    private void obtenerOrientacioDispositivo() {

        int orientation = this.getResources().getConfiguration().orientation;
        switch (orientation) {

            case ORIENTATION_PORTRAIT:
                pantallaSinBarraDeEstado();
                break;

            case ORIENTATION_LANDSCAPE:
                pantallaFullScreen();
                break;
        }
    }

    private void pantallaSinBarraDeEstado() {
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = getWindow();
        window.setFlags(flag, flag);
    }

    private void pantallaSinActionBar() {
        getSupportActionBar().hide();
    }

    private void pantallaFullScreen() {
        pantallaSinBarraDeEstado();
        pantallaSinActionBar();
    }


    private void inicializarComponentesUI() {
        videoView = (VideoView) findViewById(R.id.videoView);
    }

    private void iniciarVideoMediaPlayer() {

        // Prepara la URI del vídeo que será reproducido.
        Uri mUri = getUriToResource(this, R.raw.android_oreo);

        // Se crean los controles multimedia.
        MediaController mediaController = new MediaController(this);

        // Inicializa la VideoView.
        videoView.setMediaController(mediaController);
        try {
            // Asigna la URI del vídeo que será reproducido a la vista.
            videoView.setVideoURI(mUri);
            // Se asigna el foco a la VideoView.
            videoView.requestFocus();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        /*
         * Se asigna un listener que nos informa cuando el vídeo
         * está listo para ser reproducido.
         */
        videoView.setOnPreparedListener(videoViewListener);

    }

    public static final Uri getUriToResource(@NonNull Context context, @AnyRes int resId) throws Resources.NotFoundException {

        Resources res = context.getResources();

        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));

        return resUri;
    }

    private MediaPlayer.OnPreparedListener videoViewListener =
            new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
            /*
             * Se indica al reproductor multimedia que el vídeo
             * se reproducirá en un loop (on repeat).
             */
                    mediaPlayer.setLooping(false);

                    if (posicion == 0) {
                /*
                 * Si tenemos una posición en savedInstanceState,
                 * el vídeo debería comenzar desde aquí.
                 */
                        videoView.start();
                    } else {
                /*
                 * Si venimos de un Activity "resumed",
                 * la reproducción del vídeo será pausada.
                 */
                        videoView.pause();
                    }
                }
            };

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        /* Usamos onSaveInstanceState para guardar la posición de
           reproducción del vídeo en caso de un cambio de orientación. */
        savedInstanceState.putInt("Position",
                videoView.getCurrentPosition());
        videoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        /*
         * Usamos onRestoreInstanceState para reproducir el vídeo
         * desde la posición guardada.
         */
        posicion = savedInstanceState.getInt("Position");
        videoView.seekTo(posicion);
    }
}
