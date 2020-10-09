package nz.ac.waikato.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ScoreHandler.SetupScoreHandler(this);
    }

    public void startGame(View view){
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    public void gotoScores(View view){
        Intent i = new Intent(this, ScoreView.class);
        startActivity(i);
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
    }
}