import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;


public class MidiParse {
    String filename;
    Sequence seq;
    Track[] tracks;
    ArrayList<MidiEvent[]> eventary=new ArrayList<MidiEvent[]>();
    //	Vector events=new Vector();
    public  MidiParse(String fl){
        filename=fl;
    };
    //open file ,get sequence
    void openfile(){
        try{
            File file=new File(filename); 		
            seq = MidiSystem.getSequence(file);
        }catch(Exception e){
            System.out.println("file open fails");
        }
    };
    //get a list of all events
    void getevent(){
        try{		
            tracks=seq.getTracks();
            for(int i=0;i<tracks.length;i++){
                ArrayList<MidiEvent> tmplist=new ArrayList<MidiEvent>();
                for(int j=0;j<tracks[i].size();j++)//all events of one track
                    tmplist.add(tracks[i].get(j));
                MidiEvent[] tmpary=new MidiEvent[tmplist.size()];
                tmplist.toArray(tmpary);
                eventary.add(tmpary);

            }

        }catch (Exception s){
            s.printStackTrace();
        }
    };

    public void Parser(){
        openfile();
        getevent();
    };

    public ArrayList TimeEvent(long start,long end){
        ArrayList<MidiEvent[]> need=new ArrayList<MidiEvent[]>();
        for(int i=0;i<eventary.size();i++){
            int len=eventary.get(i).length;
            ArrayList<MidiEvent> onetrack=new ArrayList<MidiEvent>();
            //MidiEvent[] onetrack=new MidiEvent[]();			
            for(int j=0;j<len;j++){
                MidiEvent tmp=eventary.get(i)[j];
                long time=tmp.getTick();
                //fetch time in [start,end)
                if(time<start)continue;
                if(time>=end)break;
                onetrack.add(tmp);
            }
            MidiEvent[] track1=new MidiEvent[onetrack.size()];
            onetrack.toArray(track1);
            need.add(track1);
        }
        return need;
    };
}
