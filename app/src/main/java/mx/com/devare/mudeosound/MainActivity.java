package mx.com.devare.mudeosound;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton ibtn_reproductor_musica,
            ibtn_reproductor_video,
            ibtn_capturar_foto,
            ibtn_media_player;

    Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarComponetesUI();
        inicializarSetOnClickListener();

    }


    private void inicializarComponetesUI() {
        ibtn_reproductor_musica=(ImageButton) findViewById(R.id.ibtn_reproductor_musica);
        ibtn_reproductor_video=(ImageButton) findViewById(R.id.ibtn_reproductor_video);
        ibtn_capturar_foto=(ImageButton) findViewById(R.id.ibtn_capturar_foto);
        ibtn_media_player=(ImageButton) findViewById(R.id.ibtn_media_player);

    }
    private void inicializarSetOnClickListener() {
        ibtn_reproductor_musica.setOnClickListener(this);
        ibtn_reproductor_video.setOnClickListener(this);
        ibtn_capturar_foto.setOnClickListener(this);
        ibtn_media_player.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.ibtn_reproductor_musica:
                mIntent=new Intent(this,AudioMediaPlayer.class);
                break;

            case R.id.ibtn_reproductor_video:
                mIntent=new Intent(this,VideoMediaPlayer.class);
                break;

            case R.id.ibtn_capturar_foto:
                mIntent=new Intent(this,CamaraPlayer.class);
                break;

            case R.id.ibtn_media_player:
                mIntent=new Intent(this,MudeoSoundPlayer.class);
                break;
        }
        startActivity(mIntent);
    }
}
