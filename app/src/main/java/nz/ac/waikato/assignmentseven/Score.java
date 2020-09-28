package nz.ac.waikato.assignmentseven;

public class Score {
    /*
    A programmatic approach to representing a
    game score
     */
    private int currentScore;
    private String name = "Unknown User";

    public Score() {
        currentScore = 0;
    }
    public Score(int i) {
        /*
        This method is overloaded here based on
        the premise these objects are also used
        to represent persistently stored scores
         */
        currentScore = i;
    }

    public int GetScore() {
        return currentScore;
    }
    public int ChangeScore(int amount){
        currentScore += amount;
        return GetScore();
    }

    public String GetName() {
        return name;
    }
    public String ChangeName(String newName) {
        name = newName;
        return GetName();
    }
}
