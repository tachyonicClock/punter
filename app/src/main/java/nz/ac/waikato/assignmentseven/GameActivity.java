package nz.ac.waikato.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import nz.ac.waikato.assignmentseven.scoring.Score;

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
        ((GameView)findViewById(R.id.game_view)).restartGame();

        if (ScoreHandler.GetInstance().GetCurrentScore().GetScore() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Name");
            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = input.getText().toString();
                    ScoreHandler.GetInstance().GetCurrentScore().ChangeName(name);
                    ScoreHandler.GetInstance().EndCurrentGame();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ScoreHandler.GetInstance().EndCurrentGame();
                }
            });

            builder.show();
        }
        else { ScoreHandler.GetInstance().EndCurrentGame(); }
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
