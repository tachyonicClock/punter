package nz.ac.waikato.assignmentseven;

import android.content.Context;

import nz.ac.waikato.assignmentseven.audio.AudioMeanings;
import nz.ac.waikato.assignmentseven.audio.ContextSingleton;
import nz.ac.waikato.assignmentseven.audio.MusicManager;
import nz.ac.waikato.assignmentseven.audio.SoundManager;

public class AudioHandler {
    /*
    An 'event' driven sound entry point based loosely on
    having a simple interaction environment through which
    to play a sound rather then methods per item
     */
    public static void PlaySound(int event){
        // Validate here?
        PlayAudioFile(event);
    }

    private static void PlayAudioFile(int filename){
        MusicManager.getInstance().play(ContextSingleton.getInstance(), filename);
    }
}
