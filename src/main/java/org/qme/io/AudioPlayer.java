package org.qme.io;

import org.qme.io.AudioPlayerState;
import org.qme.io.AudioFiles;
import org.qme.io.Logger;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * An audio player for background music
 * @author jakeroggenbuck
 * @since 0.3.0
 */
public class AudioPlayer {

    private static String filePath = AudioFiles.menu;

    // Store current position in audio
    private long currentPosition;
    private Clip clip;

    // The state of the player
    private AudioPlayerState audioPlayerState;

    private AudioInputStream audioInputStream;

    // Initialize streams and clip
    public AudioPlayer() {
        try {
            // Create AudioInputStream object
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

            // Create clip
            clip = AudioSystem.getClip();

            // Open audioInputStream to the clip
            clip.open(audioInputStream);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

        catch (Exception ex) {
            Logger.log("Error with playing sound.", Severity.ERROR);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        try {
            AudioPlayer audioPlayer = new AudioPlayer();
            audioPlayer.play();
        }

        catch (Exception ex) {
            Logger.log("Error with playing sound.", Severity.ERROR);
            ex.printStackTrace();
        }
    }

    // Method to play the audio
    public void play()
    {
        // Start the clip
        clip.start();
        audioPlayerState = AudioPlayerState.PLAY;
    }

    // Method to pause the audio
    public void pause() {
        if (audioPlayerState == AudioPlayerState.PAUSED) {
            Logger.log("Audio is already paused", Severity.DEBUG);
            return;
        }
        this.currentPosition = this.clip.getMicrosecondPosition();
        clip.stop();
        audioPlayerState = AudioPlayerState.PAUSED;
    }

    // Method to resume the audio
    public void resumeAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (audioPlayerState == AudioPlayerState.PLAY) {
            Logger.log("Audio is already being played", Severity.DEBUG);
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentPosition);
        this.play();
    }

    // Method to restart the audio
    public void restart() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        clip.stop();
        clip.close();
        resetAudioStream();
        currentPosition = 0L;
        clip.setMicrosecondPosition(0);
        this.play();
    }

    // Method to stop the audio
    public void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        currentPosition = 0L;
        clip.stop();
        clip.close();
    }

    // Method to jump over a specific part
    public void jump(long location) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (location > 0 && location < clip.getMicrosecondLength()) {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentPosition = location;
            clip.setMicrosecondPosition(location);
            this.play();
        }
    }

    // Method to reset audio stream
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
