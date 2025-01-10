package chess.modules;

import chess.math.BlockPos2d;
import chess.math.Box;
import chess.math.Vec2d;
import chess.math.util.Util;

import java.awt.*;
import java.util.List;

public class Chess {
    public static List<Color> colors=List.of(Color.BLACK,Color.WHITE,new Color(0,0,0,100),new Color(255,255,255,100));
    public int team;
    public BlockPos2d blockPos;
    public Vec2d pos;
    public Box boundingBox;
    public Chess(int team,BlockPos2d blockPos){
        this.team=team;
        this.blockPos=blockPos;
        this.pos=blockPos.toCenterPos();
        this.boundingBox=new Box(pos,0.45,0.45);
    }
    public Chess(int team,Vec2d pos){
        this.team=team;
        this.blockPos=BlockPos2d.ofFloor(pos.copy());
        this.pos=blockPos.toCenterPos();
        this.boundingBox=new Box(this.pos,0.45,0.45);
    }
    public void render(Graphics g){
        if(team<0) return;
        g.setColor(Color.GRAY);
        Util.render(g,boundingBox.switchToJFrame());
        g.setColor(colors.get(team));
        Util.render(g,boundingBox.expand(-0.05,-0.05).switchToJFrame());
    }
}
