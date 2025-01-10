package chess.math.util;

import chess.math.Box;
import chess.math.Vec2d;
import chess.modules.ChessMain;
import chess.modules.Screen;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Util {
    public static Random random=new Random(114514);
    public static double sin(double d){
        return Math.sin(Math.toRadians(d));
    }
    public static double cos(double d){
        return Math.cos(Math.toRadians(d));
    }

    public static int round(double d){
        return (int)Math.round(d);
    }

    public static void render( Graphics g,double mx, double my, double xs, double ys){
        g.fillOval(round(mx),round(my),round(xs),round(ys));
    }
    public static void render(Graphics g, Box b){
        render(g,b.minX,b.minY,b.xSize(),b.ySize());
    }
    public static void render(Graphics g, Box b,double i){
        render(g,b.minX+i,b.minY+i,b.xSize()-2*i,b.ySize()-2*i);
    }
    public static void renderCube( Graphics g,double mx, double my, double xs, double ys){
        g.fillRect(round(mx),round(my),round(xs),round(ys));
    }
    public static void renderCube(Graphics g, Box b){
        renderCube(g,b.minX,b.minY,b.xSize(),b.ySize());
    }
    public static void renderCube(Graphics g, Box b,double i){
        renderCube(g,b.minX+i,b.minY+i,b.xSize()-2*i,b.ySize()-2*i);
    }
    public static double switchXToJFrame(double x){
        return x* Screen.INSTANCE.zoom - Screen.INSTANCE.camX+ (double) Screen.INSTANCE.windowWidth /2;
    }
    public static double switchYToJFrame(double y){
        return -(y*Screen.INSTANCE.zoom-Screen.INSTANCE.camY)+ (double) Screen.INSTANCE.windowHeight /2;
    }
    public static double switchXToGame(double x){
        return (x- (double) Screen.INSTANCE.windowWidth /2+Screen.INSTANCE.camX)/Screen.INSTANCE.zoom ;
    }
    public static double switchYToGame(double y){
        return (-y+ (double) Screen.INSTANCE.windowHeight /2+Screen.INSTANCE.camX)/Screen.INSTANCE.zoom ;
    }
    public static double random(double min,double max){
        return min+random.nextDouble()*(max-min);
    }
    public static Vec2d getVec2dFromString(String str){
        try {
            List<String> strings = splitStringBySpace(str);
            double d1 = Double.parseDouble(strings.get(0));
            double d2 = Double.parseDouble(strings.get(1));
            return new Vec2d(d1,d2);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static String getStringFromVec2d(Vec2d vec){
        StringBuilder sb=new StringBuilder();
        sb.append(vec.x+"-");
        sb.append(vec.y);
        return sb.toString();
    }
    public static List<String> splitStringBySpace(String input) {
        if (input == null || input.isEmpty()) {
            return Arrays.asList();
        }
        return Arrays.asList(input.split("\\s+"));
    }
}
