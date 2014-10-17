package ox040c;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

import javax.sound.midi.*;


public class MidiParser {

    Sequence seq;
    //    wtl
    long numberOfMicroSecond;
    int resolution;
    //    wtl
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
            //            wtl
            float divisionType = seq.getDivisionType();
            System.out.println(divisionType);//PPQ SMPTE
            resolution = seq.getResolution();
            System.out.println("resolution is " + resolution);
            //            wtl
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
                {
                    tempList.add(track.get(j));
                    //                	wtl
                    MidiMessage message=track.get(j).getMessage();
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
                eventList.add(tempList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList TimeEvent(double timestamp_in){
        //    	wtl
        long timestamp = (long)(timestamp_in * 1000000) / numberOfMicroSecond * resolution;
        System.out.println(timestamp);
        //    	wtl
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
