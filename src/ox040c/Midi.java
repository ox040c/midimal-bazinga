package ox040c;

import java.util.ArrayList;

import javax.sound.midi.*;

public class Midi {
    public static void main(String[] args) {

        MidiParser a = new MidiParser("./multi.mid");
        a.init();

        ArrayList<MyMidiEvent> test = a.TimeEvent(1);
        for (MyMidiEvent oneMyMidiEvent : test) {
            // TODO: System.out.println("Track "+i);
            MidiMessage message = oneMyMidiEvent.getMyEvent().getMessage();
            byte b[] = message.getMessage();
            for (byte aB : b) {
                String strHex = Integer.toHexString(aB & 0xFF);
                System.out.print(" " + strHex);
            }
            System.out.print("  tick: " + oneMyMidiEvent.getMyEvent().getTick());
            System.out.print("\n");
        }

        System.out.println("--");
    } //void main
} // class midi