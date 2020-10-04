package nz.ac.waikato.assignmentseven;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import nz.ac.waikato.assignmentseven.scoring.Score;
import nz.ac.waikato.assignmentseven.scoring.TinyDB;

public class ScoreHandler extends Activity{
    private static ScoreHandler instance = new ScoreHandler();
    public static ScoreHandler GetInstance() {
        // Used to make this a singleton class
        return instance;
    }

    private Score currentGame;
    private ArrayList<Score> topScores;
    private TinyDB tinydb;
    private boolean isInitialised = false;

    public Score GetCurrentScore() throws Exception {
        IsInitialised();
        return GetInstance().currentGame;
    }

    /*
    To be called during app execution to load our data in
    from our persistent storage and initialize the class

    Persistent storage format is {Int id: [Int score, String username]}

    @param ctx The android context for usage with SharedPreferences
     */
    public void LoadClass(Context ctx) {
        GetInstance().topScores = new ArrayList<Score>();
        GetInstance().tinydb =  new TinyDB(ctx);


        BuildTree();
        GetInstance().currentGame = new Score();
        GetInstance().topScores.add(GetInstance().currentGame);
        GetInstance().isInitialised = true;

    public Score EndCurrentGame() {
        return EndCurrentGame(0);
    }

    /*
        Its game over, so trigger a cleanup. Figure out the score in relation to
        top scores and whether or not we have a new top 5 score

        @param finalScoreChange The final addition to the users score, if any.
    */
    public Score EndCurrentGame(int finalScoreChange) throws Exception {
        IsInitialised();

        GetInstance().currentGame.ChangeScore(finalScoreChange);
        GetInstance().tinydb.clear();

        for (Score score : GetInstance().GetTopScores()){
            StoreScore(score);
        }
        Score lastGame = GetCurrentScore();
        GetInstance().currentGame = new Score();
        GetInstance().topScores.add(GetInstance().currentGame);
        return lastGame;
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
