package ox040c;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;

import javax.sound.midi.*;

public class MidiParser {

    Sequence seq;
    //    wtl
    long numberOfMicroSecond;
    int PPQ;
    //    wtl
    ArrayList<MyMidiEvent> eventList = new ArrayList<MyMidiEvent>();

    /**
     * Constructor, with one arg to take filename
     * Open a midi file, and try to get sequence out of it.
     *
     * @param filename the file to be opened
     *
     */
    public MidiParser(String filename){
        try {
            File file = new File(filename);
            seq = MidiSystem.getSequence(file);
            eventList.clear();
        } catch (IOException e) {
            System.out.println("Failed to open file: " + filename);
        } catch (InvalidMidiDataException e) {
            System.out.println("Not a valid midi file: " + filename);
        }
    }

    /**
     * calling this method would decode out necessary information
     * move costly function out of constructor
     */
    public void init() {

        // TODO: what's division type?
        // float divisionType = seq.getDivisionType();
        // System.out.println(divisionType);
        // PPQ SMPTE

        // assume we got PPQ (most midi files do)
        PPQ = seq.getResolution();
        System.out.format("PPQ(pulse per quarter-note): %d\n", PPQ);

        // generate event list
        getEvent();
    }

    /**
     * private method for parsing file into an EventList
     *
     * for the sake of simplicity, all events will be put
     * into a single list, sorted by ticks
     */
    private void getEvent(){
        try{
            Track[] tracks = seq.getTracks();
            // all tracks
            for (Track track : tracks) {
                // all events of one track
                for (int j = 0; j < track.size(); j++) {
                    eventList.add(new MyMidiEvent(track.get(j)));
                }
            }

            Collections.sort(eventList, new MyMidiEvent());
            System.out.print(eventList.size());
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private void parseType(MidiMessage message) {
        //                	wtl
        byte b[]=message.getMessage();

        if(b[0] == -1)
        {
            if(b[1] == 81)
            {
                //                    		b[3] = 1;
                //                    		b[4] = 2;
                //                    		b[5] = 3;
                numberOfMicroSecond = b[3] * 256 * 256 + b[4] * 256 + b[5];
                System.out.println("numberOfMicroSecond is " + numberOfMicroSecond);
            }
        }
        //                    wtl
    }

    public ArrayList TimeEvent(double timestamp_in){
        long timestamp = 200;
        //long timestamp = (long)(timestamp_in * 1000000) / numberOfMicroSecond * PPQ;
        //System.out.println(timestamp);

        ArrayList<MyMidiEvent> poped = new ArrayList<MyMidiEvent>();
            int index = 0;
            /**
             * Get events whose ticks < timestamp and pop them out of the list
             */
            for (MyMidiEvent anEvent : eventList) {

                if (anEvent.getMyEvent().getTick() <= timestamp) {
                    poped.add(anEvent);
                }
                else {
                    index = eventList.indexOf(anEvent);
                    break;
                }
            }

            for (int i = 0; i < index; i++) {
                eventList.remove(i);
            }

        return poped;
    }

    private int getChannel(Byte b) {
        return b & 0x0f;
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
