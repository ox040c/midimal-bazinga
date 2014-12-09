package ox040c;

public class MidiNote {

    public MidiNote(MyMidiEvent myMidiEvent) {
        byte b[] = myMidiEvent.getMyEvent().getMessage().getMessage();
        channel = b[0] & 0x0f;
        freq = b[1] & 0xff;
        stress = b[2] & 0xff;

        tick = myMidiEvent.getMyEvent().getTick();
    }

    private int channel;
    private int freq;
    private int stress;
    private long tick;

    public int getChannel() {
        return channel;
    }

    public int getFreq() {
        return freq;
    }

    public int getStress() {
        return stress;
    }

    public long getTick() {
        return tick;
    }

}