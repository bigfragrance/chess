package chess.math;

import chess.math.util.Util;
import chess.modules.Screen;

public class Vec2d {
    public double x;
    public double y;
    public Vec2d(double x, double y){
        this.x=x;
        this.y=y;
    }
    public Vec2d add(double x,double y){
        return new Vec2d(this.x+x,this.y+y);
    }
    public Vec2d add(Vec2d v){
        return add(v.x,v.y);
    }
    public double length(){
        return Math.sqrt(x*x+y*y);
    }
    public Vec2d subtract(double x, double y) {
        return this.add(-x, -y);
    }
    public Vec2d multiply(double a){
        return new Vec2d(x*a,y*a);
    }
    public void multiply1(double a){
        set(x*a,y*a);
    }

    public Vec2d subtract(Vec2d pos) {
        return subtract(pos.x,pos.y);
    }
    public void set(double x,double y){
        this.x=x;
        this.y=y;
    }
    public void offset(double x,double y){
        this.x+=x;
        this.y+=y;
    }
    public Vec2d copy(){
        return new Vec2d(x,y);
    }

    public void offset(Vec2d velocity) {
        offset(velocity.x, velocity.y);
    }

    public void set(Vec2d vec) {
        set(vec.x,vec.y);
    }
    public Vec2d switchToJFrame(){
        return new Vec2d(Util.switchXToJFrame(x),Util.switchYToJFrame(y));
    }
    public Vec2d switchToGame(){
        return new Vec2d(Util.switchXToGame(x),Util.switchYToGame(y));
    }
    public Vec2d switchToGame1(){
        return new Vec2d(x- (double) Screen.INSTANCE.windowWidth /2,-y+ (double) Screen.INSTANCE.windowHeight /2);
    }
    public Vec2d limit(double l){
        double e=l/length();
        e=Math.min(e,1);
        return this.multiply(e);
    }
    public double distanceTo(Vec2d pos){
        return this.subtract(pos).length();
    }
}
