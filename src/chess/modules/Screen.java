package chess.modules;

import chess.math.Box;
import chess.math.Vec2d;
import chess.math.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static chess.math.util.Util.round;
import static chess.modules.ChessMain.cs;

public class Screen extends JPanel implements Runnable{
    public static Screen INSTANCE;
    public static JFrame frame;
    public volatile double camX=0;
    public volatile double camY=0;
    public volatile double zoom=40;
    public int windowWidth=1000;
    public int windowHeight=1000;
    public static int mouseX = 50;
    public static int mouseY = 50;
    public static volatile Vec2d mousePos=new Vec2d(50,50);
    public Screen(){
        INSTANCE=this;
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = round(/*Util.switchXToGame*/(e.getX()));
                mouseY = round(/*Util.switchYToGame*/(e.getY()));
                mousePos.set(mouseX,mouseY);
            }

        });
        /*addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("Key Typed: " + e.getKeyChar());
                if(e.getKeyChar()=='o'){
                    zoom+=10;
                }
                if(e.getKeyChar()=='p'){
                    zoom-=10;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Key Typed: " + e.getKeyChar());
                if(e.getKeyChar()=='o'){
                    zoom+=10;
                }
                if(e.getKeyChar()=='p'){
                    zoom-=10;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });*/
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e){

            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(cs.tempChess!=null) {
                    Chess c=new Chess(cs.nowTeam.get(), cs.tempChess.blockPos);
                    cs.placeChess(c);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        addKeyListener(new MyAdapter());
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double old=zoom;
                zoom+=e.getPreciseWheelRotation()*zoom/5;
                if(zoom<=0){
                    zoom=old;
                }
            }
        });
    }
    public class MyAdapter extends KeyAdapter{
        @Override
        public void keyTyped(KeyEvent e) {
            System.out.println("Key Typed: " + e.getKeyChar());
            super.keyTyped(e);
            if(e.getKeyChar()=='o'){
                zoom+=10;
            }
            if(e.getKeyChar()=='p'){
                zoom-=10;
            }
        }
    }
    public void run(){
        while (true) {
            long start=System.currentTimeMillis();
            repaint();
            try {
                long s= -(System.currentTimeMillis() - start) +1000 / 60;
                if(s>0)Thread.sleep(s); // 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        paintWeb(g);
        for(Chess c: cs.chesses){
            c.render(g);
        }
        if(cs.tempChess!=null){
            cs.tempChess.render(g);
        }

    }
    public void paintWeb(Graphics g){
        g.setColor(new Color(0,0,0,50));
        double dx=cs.sizeX+0.05;
        double dy=cs.sizeY+0.05;
        for(int x=-cs.sizeX;x<=cs.sizeX;x++){
            Box box=new Box(new Vec2d(x+0.5,0.5),0.05,dy);
            Util.renderCube(g,box.switchToJFrame());
        }
        for(int y=-cs.sizeY;y<=cs.sizeY;y++){
            Box box=new Box(new Vec2d(0.5,y+0.5),dx,0.05);
            Util.renderCube(g,box.switchToJFrame());
        }
    }
}
