package dev.datvt.musicequalizer.visualizer;

// Data class to explicitly indicate that these bytes are raw audio data
public class AudioData {
    public byte[] bytes;

    public AudioData(byte[] bytes) {
        this.bytes = bytes;
    }
}
