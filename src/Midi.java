import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.midi.*;

public class Midi {
    public static void main(String[] args) {

        MidiParser a=new MidiParser("./multi.mid");
        ArrayList<ArrayList<MidiEvent>> test = a.TimeEvent(200);

        for(int i=0;i<test.size();i++){
            System.out.println("Track "+i);
            ArrayList<MidiEvent> now = test.get(i);
            int len = now.size();
            for(int j=0;j<len;j++){
                System.out.println("event "+j+":");
                System.out.println("tick="+now.get(j).getTick());
                MidiMessage message=now.get(j).getMessage();
                byte b[]=message.getMessage();
                for (byte aB : b) {
                    String strHex = Integer.toHexString(aB & 0xFF);
                    System.out.print(strHex + " ");
                }
                System.out.println(Arrays.toString(message.getMessage()));
            }
        }
    }

}
