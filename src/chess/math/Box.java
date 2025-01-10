package chess.math;

import chess.math.util.Util;

public class Box {
    public double minX;
    public double maxX;
    public double minY;
    public double maxY;
    public Box(double max, double mix, double may, double miy){
        this.minX=Math.min(mix,max);
        this.maxX=Math.max(mix,max);
        this.minY=Math.min(miy,may);
        this.maxY=Math.max(miy,may);
    }
    public Box(Vec2d center, double dx, double dy){
        this(center.x-dx, center.x+dx, center.y -dy,center.y+dy);
    }
    public boolean intersects(Box box) {
        return this.intersects(box.minX, box.minY, box.maxX, box.maxY);
    }
    public boolean intersectsCircle(Box box){
        double d=box.getCenter().distanceTo(this.getCenter());
        return d*2<=Math.max(box.xSize(),box.ySize())+Math.max(this.xSize(),this.ySize());
    }
    public Box expand(double x,double y){
        return new Box(maxX+x,minX-x,maxY+y,minY-y);
    }
    /**
     * Checks if this box intersects the box of the given coordinates.
     */
    public boolean intersects(double minX, double minY, double maxX, double maxY) {
        return this.minX < maxX && this.maxX > minX && this.minY < maxY && this.maxY > minY;
    }

    /**
     * Checks if this box intersects the box of the given positions as
     * corners.
     */
    public boolean intersects(Vec2d pos1, Vec2d pos2) {
        return this.intersects(Math.min(pos1.x, pos2.x), Math.min(pos1.y, pos2.y), Math.max(pos1.x, pos2.x), Math.max(pos1.y, pos2.y));
    }

    /**
     * Checks if the given position is in this box.
     */
    public boolean contains(Vec2d pos) {
        return this.contains(pos.x, pos.y);
    }

    /**
     * Checks if the given position is in this box.
     */
    public boolean contains(double x, double y) {
        return x >= this.minX && x < this.maxX && y >= this.minY && y < this.maxY;
    }
    public void offset1(double x,double y){
        this.minX+=x;
        this.maxX+=x;
        this.minY+=y;
        this.maxY+=y;
    }
    public void offset1(Vec2d vec){
        offset1(vec.x,vec.y);
    }
    public double xSize(){
        return this.maxX-this.minX;
    }
    public double ySize(){
        return this.maxY-this.minY;
    }
    public Box switchToJFrame(){
        return new Box(Util.switchXToJFrame(minX), Util.switchXToJFrame(maxX),Util.switchYToJFrame(minY),Util.switchYToJFrame(maxY));
    }
    public Box switchToGame(){
        return new Box(Util.switchXToGame(minX),Util.switchXToGame(maxX),Util.switchYToGame(minY),Util.switchYToGame(maxY));
    }
    public Vec2d getCenter(){
        return new Vec2d((minX+maxX)/2,(minY+maxY)/2);
    }
}
