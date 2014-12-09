package ox040c;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import ox040c.DisElement;
import ox040c.Bazingaler;
import ox040c.PlayMidi;

public class TimerExample {
	/* the size of screen */
	int max_x = 600, max_y = 600;

	/** time at last frame */
	long lastFrame;
	/** time at the music started*/
	long musicStart;
	/** frames per second */
	int fps;

	/**the list to save the element to draw*/
	ArrayList<DisElement> DisElement = new ArrayList<DisElement>();
	
	/** last fps time */
	long lastFPS;
	/**the class have function to get the infomation of inst*/
	static MidiParser inst;

	public void start(String argv) {
		try {
			Display.setDisplayMode(new DisplayMode(max_x, max_y));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		initGL(); // init OpenGL
		getDelta(); // call once before loop to initialise lastFrame
		musicStart = getTime();// record the time music start
		lastFPS = getTime(); // call before loop to initialise fps timer

		
		PlayMidi PlayMidi = new PlayMidi(); 
        PlayMidi.Play(argv);//"symphony_6_1_(c)cvikl.mid");  
        
        long time = PlayMidi.getTime();  
		
		while (!Display.isCloseRequested()) {
			int delta = getDelta();

			update(delta);
			renderGL();

			Display.update();
			Display.sync(60); // cap fps to 60fps
			if(getTime()-musicStart > time) break;
		}
		PlayMidi.Stop();
		Display.destroy();
	}

	public void update(int delta) {
		// add the new element to show
		ArrayList<MidiNote> test;
		test = inst.queryBySecond((double) (getTime() - musicStart) / 1000.0);

		for (MidiNote oneMidiNote : test) {
			int Freq = oneMidiNote.getFreq();
			int Channel = oneMidiNote.getChannel();
			int Stress = oneMidiNote.getStress();
			if (oneMidiNote.getStress() > 0) {
				DisElement it = new DisElement(Freq,Channel, Stress);
				if (Channel == 5) System.out.println(getTime() - musicStart+" "+Freq);
				boolean flag=true;
				for (DisElement kk:DisElement){
					if(kk.Exist(Freq, Channel))
						flag = false;
				}
				if (flag){
					DisElement.add(it);	
				}
			}
		}
		System.out.println("------");
		updateFPS(); // update FPS Counter
	}

	/**
	 * Calculate how many milliseconds have passed since last frame.
	 * 
	 * @return milliseconds passed since last frame
	 */
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	public void initGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1000, 0, 1000, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		Bazingaler.init();
	}

	public void renderGL() {
		// Clear The Screen And The Depth Buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		// R,G,B,A Set The Color To Blue One Time Only

		//Put the background to the scream
		Bazingaler.genList("bg", 0, 0, 1000, 1000);
		glPushMatrix();
		Bazingaler.callList("bg");
		glPopMatrix();

		//Draw the element has saved in list
		Iterator<DisElement> iter = DisElement.iterator(); 
		while(iter.hasNext()){
			DisElement test = iter.next();
			if (test.addFrame()) {
				test.Draw();
			} else {
				iter.remove();
			}
		}
	}

	public static void main(String[] argv) {
		inst = new MidiParser(argv[0]);
		inst.init();
        
		TimerExample timerExample = new TimerExample();
		timerExample.start(argv[0]);

	}
}