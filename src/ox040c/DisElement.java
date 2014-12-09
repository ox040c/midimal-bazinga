package ox040c;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import ox040c.Bazingaler;

public class DisElement {
	private int Frame;
	private int x, y; // the address of the initialized point
	private double rate;
	private String FileName;

	public enum Element {
		bat0, bat1, bat2, bat3, bat4, w0, w1, w2, w3, w4, w5, w6, n0, n1, n2, n3, n4, n5, n6, n7;
	}

	public Element Element = null;

	class batPoint {
		public int x, y;

		batPoint(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	batPoint[] batPoint = { new batPoint(70, 600), new batPoint(330, 600),
			new batPoint(520, 520), new batPoint(670, 560),
			new batPoint(860, 590) };

	private Element GetElement(int Freq, int Channel)
	{
//		System.out.println(Freq + " " + Channel);
		Element result = null;
		if (Channel % 3 == 1) {
			switch (Freq %5) {
			case 0:
				result = Element.bat0;
				break;
			case 1:
				result = Element.bat1;
				break;
			case 2:
				result = Element.bat2;
				break;
			case 3:
				result = Element.bat3;
				break;
			case 4:
				result = Element.bat4;
				break;
			default:
				break;
			}
		} else if (Channel % 3 == 2) {
			switch (Freq % 7) {
			case 0:
				result = Element.w0;
				break;
			case 1:
				result = Element.w1;
				break;
			case 2:
				result = Element.w2;
				break;
			case 3:
				result = Element.w3;
				break;
			case 4:
				result = Element.w4;
				break;
			case 5:
				result = Element.w5;
				break;
			case 6:
				result = Element.w6;
				break;
			default:
				break;
			}
		} else if (Channel % 3 == 0) {
			switch (Freq % 8) {
			case 0:
				result = Element.n0;
				break;
			case 1:
				result = Element.n1;
				break;
			case 2:
				result = Element.n2;
				break;
			case 3:
				result = Element.n3;
				break;
			case 4:
				result = Element.n4;
				break;
			case 5:
				result = Element.n5;
				break;
			case 6:
				result = Element.n6;
				break;
			case 7:
				result = Element.n7;
				break;
			default:
				break;
			}
		}
		return result;
	}
	DisElement(int Freq, int Channel, int Stress) {
		this.Frame = 0;
		this.rate = (double) Stress / 128.0;
		this.x = batPoint[Freq / 26].x;
		this.y = batPoint[Freq / 26].y;
		Element = GetElement(Freq, Channel);
		FileName = Element.toString();
		
	}

	// 渲染当前帧的图片
	public void Draw() {
		Bazingaler.genList("bg", 0, 0, 1000, 1000);
		Bazingaler.genList("w0", 0, 0, 1000, 1000);
		Bazingaler.genList("w1", 0, 0, 1000, 1000);
		Bazingaler.genList("w2", 0, 0, 1000, 1000);
		Bazingaler.genList("w3", 0, 0, 1000, 1000);
		Bazingaler.genList("w4", 0, 0, 1000, 1000);
		Bazingaler.genList("w5", 0, 0, 1000, 1000);
		Bazingaler.genList("w6", 0, 0, 1000, 1000);
		Bazingaler.genList("n0", 0, 0, 1000, 1000);
		Bazingaler.genList("n1", 0, 0, 1000, 1000);
		Bazingaler.genList("n2", 0, 0, 1000, 1000);
		Bazingaler.genList("n3", 0, 0, 1000, 1000);
		Bazingaler.genList("n4", 0, 0, 1000, 1000);
		Bazingaler.genList("n5", 0, 0, 1000, 1000);
		Bazingaler.genList("n6", 0, 0, 1000, 1000);
		Bazingaler.genList("n7", 0, 0, 1000, 1000);
		Bazingaler.genList("bat0", 0, 0, 300, 150);
		Bazingaler.genList("bat1", 0, 0, 125, 100);
		Bazingaler.genList("bat2", 0, 0, 180, 140);
		Bazingaler.genList("bat3", 0, 0, 80, 60);
		Bazingaler.genList("bat4", 0, 0, 100, 100);

		switch (Element) {
		case w0:
		case w1:
		case w2:
		case w3:
		case w4:
		case w5:
		case w6:
		case n0:
		case n1:
		case n2:
		case n3:
		case n4:
		case n5:
		case n6:
		case n7:
			if (Frame < (rate * 60) && Frame > 3) {
				glPushMatrix();
				Bazingaler.callList(FileName);
				glPopMatrix();
			}
			break;
		case bat0:
			glPushMatrix();
			switch (Frame / 10) {
			case 0:
				glTranslated(x + (Frame / 10) * 100, y + (Frame / 10) * 100,
						0.0f);
				Bazingaler.callList("bat1");
				break;
			case 1:
				glTranslated(x + (Frame / 10) * 100, y + (Frame / 10) * 100,
						0.0f);
				Bazingaler.callList("bat0");
				break;
			case 2:
				glTranslated(x + (Frame / 10) * 100 + 50, y + (Frame / 10)
						* 100 + 50, 0.0f);
				Bazingaler.callList("bat1");
				break;
			}
			glPopMatrix();
			break;
		case bat1:
			glPushMatrix();
			switch (Frame / 10) {
			case 0:
				glTranslated(x - (Frame / 10) * 100, y + (Frame / 10) * 100,
						0.0f);
				Bazingaler.callList("bat2");
				break;
			case 1:
				glTranslated(x - (Frame / 10) * 100, y + (Frame / 10) * 100,
						0.0f);
				Bazingaler.callList("bat4");
				break;
			case 2:
				glTranslated(x - (Frame / 10) * 100, y + (Frame / 10) * 100,
						0.0f);
				Bazingaler.callList("bat2");
				break;
			case 3:
				glTranslated(x - (Frame / 10) * 100, y + (Frame / 10) * 100,
						0.0f);
				Bazingaler.callList("bat2");
				break;
			case 4:
				glTranslated(x - (Frame / 10) * 100, y + (Frame / 10) * 100,
						0.0f);
				Bazingaler.callList("bat4");
				break;
			}
			glPopMatrix();
			break;
		case bat2:
			glPushMatrix();
			switch (Frame / 10) {
			case 0:
			case 1:
				glTranslated(x + (Frame / 10) * 100, y + (Frame / 10) * 100,
						0.0f);
				Bazingaler.callList("bat3");
				break;
			case 2:
				glTranslated(x + (Frame / 10) * 100, y + (Frame / 10) * 100
						- 50, 0.0f);
				Bazingaler.callList("bat2");
				break;
			case 3:
			case 4:
			case 5:
				glTranslated(x + 200 - (Frame / 10 - 2) * 100, y + (Frame / 10)
						* 100 - 50, 0.0f);
				Bazingaler.callList("bat4");
				break;
			default:
				break;
			}
			glPopMatrix();
			break;
		case bat3:
			glPushMatrix();
			switch (Frame / 10) {
			case 0:
			case 1:
				glTranslated(x - (Frame / 10) * 100, y + (Frame / 10) * 100,
						0.0f);
				Bazingaler.callList("bat4");
				break;
			case 2:
				glTranslated(x - (Frame / 10) * 100, y + (Frame / 10) * 100
						- 50, 0.0f);
				Bazingaler.callList("bat1");
				break;
			case 3:
			case 4:
			case 5:
				glTranslated(x - 200 + (Frame / 10 - 2) * 100, y + (Frame / 10)
						* 100 - 50, 0.0f);
				Bazingaler.callList("bat0");
				break;
			default:
				break;
			}
			glPopMatrix();
			break;
		case bat4:
			glPushMatrix();
			switch (Frame / 10) {
			case 0:
			case 1:
				glTranslated(x - (Frame / 10) * 200, y + (Frame / 10) * 60,
						0.0f);
				Bazingaler.callList("bat4");
				break;
			case 2:
				glTranslated(x - (Frame / 10) * 200, y + (Frame / 10) * 60,
						0.0f);
				Bazingaler.callList("bat2");
				break;
			case 3:
			case 4:
			case 5:
				glTranslated(x - (Frame / 10) * 200, y + (Frame / 10) * 60,
						0.0f);
				glScaled(2.0f, 2.0f, 0);
				Bazingaler.callList("bat4");
				break;
			default:
				break;
			}
			glPopMatrix();
			break;
		default:
			break;
		}
	}

	// 帧数加一
	public boolean addFrame() {
		boolean end = true;
		if (Frame < 60)
			Frame++;
		else {
			Frame = 0;
			end = false;
		}
		return end;
	}

	// 是否被创建
	public boolean Exist(int Freq, int Channel) {
		boolean result;
		Element nowElement = GetElement(Freq, Channel);
		if (nowElement == Element){
			result = true;
		}else
		{
			result = false;
		}
		
		return result;
	}

	public void initGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1000, 0, 1000, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		Bazingaler.init();
	}

	public static void main(String[] argv) {
		DisElement DisElement = new DisElement(80, 1, 120);
		try {
			Display.setDisplayMode(new DisplayMode(600, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		DisElement.initGL();
		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glColor3f(1.0f, 0.5f, 0.5f);
			Bazingaler.genList("bg", 0, 0, 1000, 1000);
			glPushMatrix();
			Bazingaler.callList("bg");
			glPopMatrix();

			DisElement.Draw();

			DisElement.addFrame();
			Display.update();
			Display.sync(60); // cap fps to 60fps
		}

		Display.destroy();
	}
}
