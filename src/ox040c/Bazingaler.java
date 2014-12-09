package ox040c;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class Bazingaler {

    private static final String RES_PATH = "images/";
    private static HashMap<String, Texture> clipTextureMap;
    private static HashMap<String, Integer> clipListMap;
    private static boolean alreadyInit = false;

    public static void init() {

        clipListMap = new HashMap<String, Integer>();

        // enable alpha blender mode
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // enable texture
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        String clipNames[] = {
                "bat0","bat1","bat2","bat3","bat4",
                "w0","w1", "w2","w3","w4","w5","w6",
                "n0","n1","n2","n3","n4","n5","n6","n7",
                "bg"
        };

        Texture texture;
        clipTextureMap = new HashMap<String, Texture>();
        for (String name : clipNames) {

            try {
                texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(RES_PATH + name + ".png"));
                clipTextureMap.put(name, texture);

//                System.out.println("Texture loaded: "+name);
//                System.out.println(">> Image width: "+texture.getImageWidth());
//                System.out.println(">> Image height: "+texture.getImageHeight());
//                System.out.println(">> Texture width: "+texture.getTextureWidth());
//                System.out.println(">> Texture height: "+texture.getTextureHeight());
//                System.out.println(">> Texture ID: "+texture.getTextureID());

            } catch (IOException e) {
                System.out.println("Not found: " + name);
                e.printStackTrace();
            }

        }

        alreadyInit = true;

    }
    
    

    public static void genList(String nameOfClip, float x1, float y1, float x2, float y2) {

        int displayList = glGenLists(1);
        glNewList(displayList, GL_COMPILE);

        Color.white.bind();
        clipTextureMap.get(nameOfClip).bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x1, y1);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(x1, y2);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(x2, y2);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(x2, y1);
        GL11.glTexCoord2f(0, 1);

        GL11.glEnd();

        glEndList();

        clipListMap.put(nameOfClip, displayList);

    }

    public static void callList(String nameOfClip) {

        if (alreadyInit && clipListMap.containsKey(nameOfClip)) {
            glCallList(clipListMap.get(nameOfClip));
        }
        else {
            System.out.println("No list:" + nameOfClip);
        }

    }



}