import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.*;


public class MidiParser {

    Sequence seq;
    ArrayList<ArrayList<MidiEvent>> eventList = new ArrayList<ArrayList<MidiEvent>>();

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

            getEvent();

        } catch (IOException e) {
            System.out.println("Failed to open file: " + filename);
        } catch (InvalidMidiDataException e) {
            System.out.println("Not a valid midi file: " + filename);
        }
    }

    /**
     * private method for parsing file into an EventList
     */
    private void getEvent(){
        try{
            Track[] tracks = seq.getTracks();
            for (Track track : tracks) {
                ArrayList<MidiEvent> tempList = new ArrayList<MidiEvent>();
                for (int j = 0; j < track.size(); j++)//all events of one track
                    tempList.add(track.get(j));
                eventList.add(tempList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList TimeEvent(long timestamp){
        ArrayList<ArrayList<MidiEvent>> poped = new ArrayList<ArrayList<MidiEvent>>();
        for (ArrayList<MidiEvent> aList : eventList) {
            int index = 0;
            ArrayList<MidiEvent> oneTrack = new ArrayList<MidiEvent>();
            /**
             * Get events whose ticks < timestamp and pop them out of the list
             */
            for (MidiEvent tmp : aList) {
                long time = tmp.getTick();

                if (time <= timestamp) {
                    oneTrack.add(tmp);
                }
                else {
                    index = aList.indexOf(tmp);
                    break;
                }
            }
            for (int i = 0; i < index; i++) {
                aList.remove(i);
            }
            poped.add(oneTrack);
        }
        return poped;
    }
}
