package nz.ac.waikato.assignmentseven;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import nz.ac.waikato.assignmentseven.storage.TinyDB;

public class ScoreHandler extends Activity{
    private static ScoreHandler instance = new ScoreHandler();
    public static ScoreHandler GetInstance() {
        // Used to make this a singleton class
        return instance;
    }

    private Score currentGame;
    TreeMap<Integer, Score> sortedTopScores;
    private TinyDB tinydb;
    private boolean isInitialised = false;

    public Score GetCurrentScore() throws Exception {
        IsInitialised();
        return GetInstance().currentGame;
    }

    public void LoadClass(Context ctx) {
        /*
        To be called during app execution to load our data in
        from our persistent storage and initialize the class

        Persistent storage format is {Int id: [Int score, String username]}
         */
        System.out.println("Loading class");

        GetInstance().sortedTopScores = new TreeMap<Integer, Score>(Collections.reverseOrder());
        GetInstance().tinydb =  new TinyDB(ctx);


        BuildTree();
        GetInstance().currentGame = new Score();
        GetInstance().isInitialised = true;

    public Score EndCurrentGame() {
        return EndCurrentGame(0);
    }

    public Score EndCurrentGame(int finalScoreChange) {
        /*
        Its game over, so trigger a cleanup. Figure out the score in relation to
        top scores and whether or not we have a new top 5 score
         */
        IsInitialised();

        return GetInstance().currentGame;
    }

    /*
        Populate our TreeMap based off the current persistent data

        Items are saved {Int id: [Int score, String username]}
    */
    public void BuildTree() {
        Map<String, ?> allEntries = tinydb.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            ArrayList<String> arr = tinydb.getListString(entry.getKey());
            int score = Integer.parseInt(arr.get(0));
            String username = (String) arr.get(1);
            Score s = new Score(score);
            s.ChangeName(username);

            topScores.add(s);
        }
    }

    public ArrayList<Score> GetTopScores() {
        Collections.sort(topScores);
        ArrayList<Score> topFive = new ArrayList<Score>();
        int counter = 0;
        for (Score score : topScores){
            if (counter == 5) break;
            counter++;
            topFive.add(score);
        }
        return topFive;
    }


    private void StoreScore(Score score) {
        ArrayList<String> item = new ArrayList<String>();
        item.add(Integer.toString(score.GetScore()));
        item.add(score.GetName());
        tinydb.putListString(Integer.toString(score.GetId()), item);
    }

    private ScoreHandler(Context ctx){
        topScores = new ArrayList<Score>();
        tinydb =  new TinyDB(ctx);
        BuildTree();
        currentGame = new Score();
        topScores.add(currentGame);
    }

}
