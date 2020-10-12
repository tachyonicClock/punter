package nz.ac.waikato.assignmentseven.audio;

import android.app.Application;

public class ContextSingleton extends Application {
    private static ContextSingleton instance;

    public ContextSingleton() {
        instance = this;
    }

    public static ContextSingleton getInstance() {
        return instance;
    }
}
