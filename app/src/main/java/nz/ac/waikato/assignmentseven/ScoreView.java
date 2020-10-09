package nz.ac.waikato.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import nz.ac.waikato.assignmentseven.scoring.Score;

public class ScoreView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        ScoreHandler sh = ScoreHandler.GetInstance();
        ArrayList<Score> topScores = new ArrayList<Score>();
        try {
            topScores = sh.GetTopScores();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> stringTopScores = new ArrayList<String>();
        for (Score item : topScores) {
            String username = item.GetName();
            String score = Integer.toString(item.GetScore());
            stringTopScores.add(username + " | " + score);
        }

        ListView listView = (ListView)findViewById(R.id.scoreList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.listTextitem, stringTopScores);
        listView.setAdapter(arrayAdapter);
    }

    public void gotoHome(View view){
        Intent i = new Intent(this, MainActivity.class);
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
