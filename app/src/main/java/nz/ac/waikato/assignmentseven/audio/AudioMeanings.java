package nz.ac.waikato.assignmentseven.audio;


import nz.ac.waikato.assignmentseven.R;

/*
 * Following on from the ColourMeanings file, this file
 * aims to help out with the playing of different
 * audio files by providing a simple way to work
 * with the relevant id's that the AudioHandler wants.
 */
public class AudioMeanings {
    public final static int TARGET_COLLISION = R.raw.target;
    public final static int RECT_COLLISION = R.raw.rect;
    public final static int SLOWDOWN_COLLISION = R.raw.rect;
    public final static int HOMESCREEN_THEME = R.raw.bg_home_theme;
    public final static int TRANSITION = R.raw.transitions;

    public static void loadAll(SoundManager soundManager){
        soundManager.load(TARGET_COLLISION);
        soundManager.load(RECT_COLLISION);
        soundManager.load(SLOWDOWN_COLLISION);
        soundManager.load(HOMESCREEN_THEME);
        soundManager.load(TRANSITION);
    }
}
