package ox040c;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;

import javax.sound.midi.*;

public class MidiParser {

    public static void main(String[] args) {

        MidiParser inst = new MidiParser(args[0]);
        inst.init();

        ArrayList<MidiNote> test;

        for (int i = 0; i < 120; i++) {

            test = inst.queryBySecond(1.0 / 60.0 * i);

            for (MidiNote oneMidiNote : test) {
                System.out.format("ch:%2d freq:%3d st:%3d tick:%6d\n",
                        oneMidiNote.getChannel(), oneMidiNote.getFreq(),
                        oneMidiNote.getStress(), oneMidiNote.getTick());
            }

            System.out.println("--");

        }
    }

    Sequence seq;

    long microSecondPerQuarterNote;
    int PPQ;

    ArrayList<MyMidiEvent> noteEventList = new ArrayList<MyMidiEvent>();
    ArrayList<MyMidiEvent> timeEventList = new ArrayList<MyMidiEvent>();
    ArrayList<MyMidiEvent> metaEventList = new ArrayList<MyMidiEvent>();

    /**
     * Constructor, with one arg to take filename
     * Open a midi file, and try to get sequence out of it.
     *
     * @param filename the file to be opened
     */
    public MidiParser(String filename) {
        try {
            File file = new File(filename);
            seq = MidiSystem.getSequence(file);
            noteEventList.clear();
            metaEventList.clear();
        } catch (IOException e) {
            System.out.println("Failed to open file: " + filename);
        } catch (InvalidMidiDataException e) {
            System.out.println("Not a valid midi file: " + filename);
        }
    }

    /**
     * private method for parsing file into an EventList
     * calling this method would decode out necessary information
     * move costly function out of constructor
     * <p/>
     * for the sake of simplicity, all events will be put
     * into a single list, sorted by ticks
     */
    public void init() {

        try {

            // TODO: what's division type?
            // float divisionType = seq.getDivisionType();
            // System.out.println(divisionType);
            // PPQ SMPTE

            // assume we got PPQ (most midi files do)
            PPQ = seq.getResolution();
            System.out.format("PPQ(pulse per quarter-note): %d\n", PPQ);

            // generate event list

            Track[] tracks = seq.getTracks();
            // all tracks
            for (Track track : tracks) {
                // all events of one track
                for (int j = 0; j < track.size(); j++) {
                    //noteEventList.add(new MyMidiEvent(track.get(j)));
                    eventFilter(track.get(j));
                }
            }

            Collections.sort(noteEventList, new MyMidiEvent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void eventFilter(MidiEvent event) {

        byte b[] = event.getMessage().getMessage();

        switch (b[0] >> 4 & 0x0f) {
            case 0x9:
            case 0x8:
                noteEventList.add(new MyMidiEvent(event));
                break;
            case 0xf:
                if (b[0] == (byte) 0xff) {
                    switch (b[1]) {
                        case (byte) 0x51:
                            timeEventList.add(new MyMidiEvent(event));
                            microSecondPerQuarterNote = b[3] * 256 * 256 + b[4] * 256 + b[5];
                            System.out.println("tick: " + event.getTick() + " msg: " +
                                    "microSecondPerQuarterNote is " + microSecondPerQuarterNote);
                            break;
                        case (byte) 0x58:
                            System.out.println("The beat of the track is:");
                            String strHex3 = Integer.toHexString(b[3] & 0xFF);
                            String strHex4 = Integer.toHexString(b[4] & 0xFF);
                            String strHex5 = Integer.toHexString(b[5] & 0xFF);
                            String strHex6 = Integer.toHexString(b[6] & 0xFF);
                            String s = strHex3 + strHex4 + strHex5 + strHex6;
                            System.out.println(s);
                            break;
                        case (byte) 0x59:
                            String scales = new String("CGDAEBFCGDAEBFC");
                            String mSig = Character.toString(scales.charAt(7 + b[3]));
                            String mSh = b[3] >= 0 ? "Sharp" : "Flat";
                            mSh = b[3] == 0 ? "" : mSh;
                            String mKey = b[4] == 1 ? "Minor" : "Major";
                            System.out.println(mSig + " " + mSh + " " + mKey);
                            break;
                        default:
                            break;
                    }
                }
                break;
            default:
                break;
        }

    }

    /**
     * convert time(second) to ticks
     * currently this function takes the first "set tempo"
     * event as the conversion parameter, and later "set tempo"
     * events would be ignored
     * TODO: more accurate timing
     *
     * @param timestamp_in
     * @return
     */
    private long secondToTick(double timestamp_in) {
        return (long) (timestamp_in * 1000000 / microSecondPerQuarterNote * PPQ);
    }

    public ArrayList queryBySecond(double timestamp_in) {
        long timestamp = secondToTick(timestamp_in);
        //System.out.println(timestamp);

        ArrayList<MidiNote> poped = new ArrayList<MidiNote>();
        poped.clear();
        int index = 0;
        /**
         * Get events whose ticks < timestamp and pop them out of the list
         */
        for (MyMidiEvent anEvent : noteEventList) {

            if (anEvent.getMyEvent().getTick() <= timestamp) {
                poped.add(new MidiNote(anEvent));
            } else {
                index = noteEventList.indexOf(anEvent);
                break;
            }
        }

        for (int i = 0; i < index; i++) {
            noteEventList.remove(0);
        }

        return poped;
    }
}

class MyMidiEvent implements Comparator<MyMidiEvent>{

    /**
     * Constructs a new <code>MidiEvent</code>.
     *
     * @param event the MIDI event contained in the class
     */
    public MyMidiEvent(MidiEvent event) {
        myEvent = event;

        byte b[] = event.getMessage().getMessage();

    }
    public MyMidiEvent() {
        myEvent = null;
    }

    public MidiEvent getMyEvent() {
        return  myEvent;
    }

    private MidiEvent myEvent;



    @Override
    public int compare(MyMidiEvent o1, MyMidiEvent o2) {
        long diff = o1.myEvent.getTick() - o2.myEvent.getTick();
        if (diff < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        if (diff > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) diff;

        // most "accepted" way to convert boolean to int
        //return 5 - b.toString().length();
    }
}

