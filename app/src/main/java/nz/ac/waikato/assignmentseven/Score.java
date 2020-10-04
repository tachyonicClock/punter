package nz.ac.waikato.assignmentseven;

import java.util.concurrent.atomic.AtomicInteger;

public class Score {
    /*
    A programmatic approach to representing a
    game score
     */
    private static final AtomicInteger count = new AtomicInteger(0);
    private final int id;

    private int currentScore;
    private String name = "Unknown User";

    public Score() {
        currentScore = 0;
        id = count.incrementAndGet();
    }
    public Score(int i) {
        /*
        This method is overloaded here based on
        the premise these objects are also used
        to represent persistently stored scores
         */
        currentScore = i;
        id = count.incrementAndGet();
    }

    public int GetId() { return id; }

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

    @Override
    public String toString(){
        return GetId() + "|" + GetName() + " : " + GetScore();
    }
}
