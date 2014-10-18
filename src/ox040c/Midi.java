package ox040c;

import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.midi.*;

public class Midi {
    public static void main(String[] args) {

        MidiParser a=new MidiParser("./multi.mid");
        a.init();
        ArrayList<MyMidiEvent> test = a.TimeEvent(200);

        for(MyMidiEvent oneMyMidiEvent: test){
            // TODO: System.out.println("Track "+i);
                System.out.println("tick="+oneMyMidiEvent.getMyEvent().getTick());
                MidiMessage message=oneMyMidiEvent.getMyEvent().getMessage();
                byte b[]=message.getMessage();
                for (byte aB : b) {
                    String strHex = Integer.toHexString(aB & 0xFF);
                    System.out.print(strHex + " ");
                }
                //                wtl
                //                String strHex0 = Integer.toHexString(b[0] & 0xFF);
                if(b[0] == -1)
                {
                    //                	String strHex1 = Integer.toHexString(b[1] & 0xFF);
                    if(b[1] == 3)
                    {
                        int m;
                        for(m = 2;m < b.length; m++)
                        {
                            if(b[m] > 0)
                                break;
                        }
                        m++;
                        System.out.println("The name of the track is:");
                        String name = "";
                        for(;m < b.length; m++)
                        {
                            String strHext = Integer.toHexString(b[m] & 0xFF);
                            //                			System.out.print(strHext);
                            name = name + strHext;
                        }
                        System.out.println(name);
                    }
                    if(b[1] == 81)
                    {
                        System.out.println("The tempo of the track is:");
                        String strHex3 = Integer.toHexString(b[3] & 0xFF);
                        String strHex4 = Integer.toHexString(b[4] & 0xFF);
                        String strHex5 = Integer.toHexString(b[5] & 0xFF);
                        String s = strHex3 + strHex4 + strHex5;
                        System.out.println(s);
                    }
                    if(b[1] == 88)
                    {
                        System.out.println("The beat of the track is:");
                        String strHex3 = Integer.toHexString(b[3] & 0xFF);
                        String strHex4 = Integer.toHexString(b[4] & 0xFF);
                        String strHex5 = Integer.toHexString(b[5] & 0xFF);
                        String strHex6 = Integer.toHexString(b[6] & 0xFF);
                        String s = strHex3 + strHex4 + strHex5 + strHex6;
                        System.out.println(s);
                    }
                    if(b[1] == 89)
                    {
                        //                		System.out.println("The tempo of the track is:");
                        if((b[3] & 0x80) == 0)
                        {
                            System.out.println("Sharp" + (b[3] & 0x01));
                        }
                        else
                        {
                            System.out.println("Flat" + (b[3] & 0x01));
                        }
                        if(b[4] == 0)
                        {
                            System.out.println("Major");
                        }
                        else
                        {
                            System.out.println("Minor");
                        }
                    }
                }
                //                wtl
                System.out.println(Arrays.toString(message.getMessage()));
                System.out.println("*****************************");
            }
        }

}
