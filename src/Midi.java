import java.io.File;
import java.io.ObjectInputStream.GetField;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.sound.midi.spi.MidiFileReader;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;


public class Midi {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        /*	try{
            File f1=new File("./1.MID");

            Sequence seq;
            seq = MidiSystem.getSequence(f1);
            Track[] gui=seq.getTracks();
            Track track=gui[0];
            for(int i=0;i<10;i++){
            MidiEvent event=track.get(i);
            MidiMessage message=event.getMessage();
        //byte[] b={'a','A'};
        //byte[] b = message.getMessage();
        //System.out.println(message.getMessage());
        byte b[]=message.getMessage();
        System.out.println(event.getTick());
        for(int j=0;j<b.length;j++){	
        String strHex=Integer.toHexString(b[j]&0xFF);
        System.out.println(strHex);
        }
        System.out.println(Arrays.toString(message.getMessage()));
        //System.out.println(Arrays.toString(b));


            }
            }catch(Exception e){
            e.printStackTrace();
            System.out.print("fail");
            }*/
        //接口
        MidiParse a=new MidiParse("./multi.mid");
        a.Parser();
        ArrayList<MidiEvent[]> test=a.TimeEvent(8000, 10000);
        //这里是打印测试test.size()
        for(int i=0;i<test.size();i++){
            System.out.println("Track "+i);
            MidiEvent[] now=test.get(i);
            int len=now.length;
            for(int j=0;j<len;j++){
                System.out.println("event "+j+":");
                System.out.println("tick="+now[j].getTick());
                MidiMessage message=now[j].getMessage();
                byte b[]=message.getMessage();
                for(int k=0;k<b.length;k++){	
                    String strHex=Integer.toHexString(b[k]&0xFF);
                    System.out.print(strHex+" ");					
                }
                System.out.println(Arrays.toString(message.getMessage()));

            }
        }


    }

}
