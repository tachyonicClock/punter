package nz.ac.waikato.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import nz.ac.waikato.assignmentseven.audio.AudioMeanings;
import nz.ac.waikato.assignmentseven.audio.SoundManager;

import nz.ac.waikato.assignmentseven.audio.AudioMeanings;
import nz.ac.waikato.assignmentseven.audio.SoundManager;

public class GameActivity extends AppCompatActivity {
    private GameView game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    public void restartGame(View view){
        game.restartGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Enter full screen mode
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        // Setup game
        game = (GameView)findViewById(R.id.game_view);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
