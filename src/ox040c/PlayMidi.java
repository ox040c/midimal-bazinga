package ox040c;

import java.io.File;  

import javax.sound.midi.MidiSystem;  
import javax.sound.midi.Sequence;  
import javax.sound.midi.Sequencer;  
  
public class PlayMidi  
{  
    private File sound;  
    private Sequence seq;  
    private Sequencer midi;  
      
    public void Play(String filename)  
    {  
         try   
         {  
             sound = new File(filename);  
             seq = MidiSystem.getSequence(sound);  
             midi= MidiSystem.getSequencer();  
             midi.open();  
             midi.setSequence(seq);  
               
             if(!midi.isRunning())  
                midi.start();  
               
         } catch (Exception ex) {  
         }  
    }  
      
    public void Stop()  
    {  
        if(midi.isRunning())  
            midi.stop();  
          
        if(midi.isOpen())  
            midi.close();  
    }  
      
    public long getTime(){
    	 return midi.getMicrosecondLength()/1000;
    }
    public static void main(String[] args)  
    {  
        PlayMidi PlayMidi = new PlayMidi(); 
        PlayMidi.Play("symphony_6_1_(c)cvikl.mid");  
        
        long time = PlayMidi.getTime();  
  
        try   
        {  
            Thread.sleep(time);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
          
        PlayMidi.Stop();  
    }  
}   