package nz.ac.waikato.assignmentseven;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import nz.ac.waikato.assignmentseven.scoring.Score;
import nz.ac.waikato.assignmentseven.scoring.TinyDB;

public class ScoreHandler extends Activity{
    private static ScoreHandler instance = null;

    /*
    To be called during app execution to load our data in
    from our persistent storage and initialize the class

    Persistent storage format is {Int id: [Int score, String username]}

    @param ctx The android context for usage with SharedPreferences
     */
    public static void SetupScoreHandler(Context ctx){
        instance = new ScoreHandler(ctx);
    }
    public static ScoreHandler GetInstance() {
        // Used to make this a singleton class
        return instance;
    }

    private Score currentGame;
    private Score previousGame;
    private TinyDB tinydb;
    private ArrayList<Score> topScores;

    public Score GetCurrentScore() {
        return currentGame;
    }

    public Score EndCurrentGame() {
        return EndCurrentGame(0);
    }

    public Score getLastGame(){
        if (previousGame == null) return new Score();
        return previousGame;
    }

    /*
        Its game over, so trigger a cleanup. Figure out the score in relation to
        top scores and whether or not we have a new top 5 score

        @param finalScoreChange The final addition to the users score, if any.
    */
    public Score EndCurrentGame(int finalScoreChange) {

        currentGame.ChangeScore(finalScoreChange);
        tinydb.clear();

        for (Score score : GetTopScores()){
            StoreScore(score);
        }
        Score lastGame = GetCurrentScore();
        currentGame = new Score();
        topScores.add(currentGame);
        previousGame = lastGame;
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
