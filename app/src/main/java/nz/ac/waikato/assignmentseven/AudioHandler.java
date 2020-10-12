package nz.ac.waikato.assignmentseven;

import android.content.Context;
import android.util.Log;

import nz.ac.waikato.assignmentseven.audio.AudioMeanings;
import nz.ac.waikato.assignmentseven.audio.ContextSingleton;
import nz.ac.waikato.assignmentseven.audio.MusicManager;
import nz.ac.waikato.assignmentseven.audio.SoundManager;

public class AudioHandler {

    int maxSimultaneousStreams = 3;
    private SoundManager soundManager;

    private static AudioHandler audioHandler = new AudioHandler();

    public void load(){
        if(soundManager != null) return;
        soundManager = new SoundManager(ContextSingleton.getInstance(), maxSimultaneousStreams);
        soundManager.start();
        AudioMeanings.loadAll(soundManager);
    }

    public void unload(){
        if (soundManager == null) return;
        soundManager.cancel();
        soundManager = null;
    }

    /*
    An 'event' driven sound entry point based loosely on
    having a simple interaction environment through which
    to play a sound rather then methods per item
     */
    public static void PlaySound(int event){
        MusicManager.getInstance().play(ContextSingleton.getInstance(), event);
    }

    public void playSoundSimultaneously(int sound){
        if(soundManager == null) load();
        soundManager.play(sound);
    }

    public static AudioHandler getInstance(){
        return audioHandler;
    }

}
