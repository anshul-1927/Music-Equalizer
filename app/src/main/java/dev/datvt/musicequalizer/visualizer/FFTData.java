package dev.datvt.musicequalizer.visualizer;

// Data class to explicitly indicate that these bytes are the FFT of audio data
public class FFTData {
    public byte[] bytes;

    public FFTData(byte[] bytes) {
        this.bytes = bytes;
    }
}
