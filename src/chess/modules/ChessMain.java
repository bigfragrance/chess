package chess.modules;

import chess.math.BlockPos2d;
import chess.modules.ai.PeopleComputer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChessMain implements Runnable{
    public static volatile ChessMain cs;
    public volatile ArrayList<Chess> chesses=new ArrayList<>();
    public volatile Chess tempChess=null;
    public volatile ArrayList<Chess> checkedAlive=new ArrayList<>();
    public volatile ArrayList<Chess> checkedDead=new ArrayList<>();
    public volatile HashMap<Integer,HashMap<Integer,Chess>> map=new HashMap<>();
    public volatile AtomicInteger nowTeam=new AtomicInteger(0);
    public int sizeX=4;
    public int sizeY=4;
    public static volatile PeopleComputer pc=new PeopleComputer();
    public static Thread thread;
    public ChessMain(){
        cs=this;
    }

    @Override
    public void run() {
        for(int x=-sizeX-1;x<=sizeX+1;x++){
            for(int y=-sizeY-1;y<=sizeY+1;y++){
                put(new BlockPos2d(x,y),new Chess(2,new BlockPos2d(x,y)));
            }
        }
        for(int x=-sizeX;x<=sizeX;x++){
            for(int y=-sizeY;y<=sizeY;y++){
                put(new BlockPos2d(x,y),new Chess(-1,new BlockPos2d(x,y)));
            }
        }
        while (true) {
            long start=System.currentTimeMillis();
            try {
                update();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                long s= -(System.currentTimeMillis() - start) +1000 / 60;
                if(s>0)Thread.sleep(s); // 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void update(){
        tempChess=new Chess(nowTeam.get()+2,Screen.mousePos.switchToGame());
        for(Chess chess:chesses){
            if(chess.blockPos.equals(tempChess.blockPos)){
                tempChess=null;
                break;
            }
        }
        if(tempChess==null||tempChess.blockPos.x<-sizeX||tempChess.blockPos.x>sizeX||tempChess.blockPos.y<-sizeY||tempChess.blockPos.y>sizeY){
            tempChess=null;
        }
        updateEaten();
        if(nowTeam.get()==1){
            runPc();
        }
        //updateEaten();
        //System.out.printf(String.valueOf(Screen.mousePos.switchToGame().x)+"\n");
    }
    public void runPc(){
        if(thread==null||!thread.isAlive()) {
            thread = new Thread(()->{
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pc.think();
            });
            thread.start();
        }
    }
    public static List<BlockPos2d> checkDirection=List.of(new BlockPos2d(0,1),new BlockPos2d(0,-1),new BlockPos2d(1,0),new BlockPos2d(-1,0));
    public int[] updateEaten(){

        //long start=System.currentTimeMillis();
        int[] res=new int[2];
        checkedAlive=new ArrayList<>();
        checkedDead=new ArrayList<>();
        for(Chess chess:chesses){
            if(chess.team!=nowTeam.get())continue;
            if(checkedAlive.contains(chess)||checkedDead.contains(chess))continue;
            if(!haveAir(chess,nowTeam.get(),new ArrayList<>())) {
                checkedDead.add(chess);
            }
        }
        for(Chess chess:checkedDead){
            chesses.remove(chess);
            put(chess.blockPos,new Chess(-1,chess.blockPos));
            res[nowTeam.get()]++;
        }
        int t=(nowTeam.get()+1)%2;
        checkedAlive=new ArrayList<>();
        checkedDead=new ArrayList<>();
        for(Chess chess:chesses){
            if(chess.team!=t)continue;
            if(checkedAlive.contains(chess)||checkedDead.contains(chess))continue;
            if(!haveAir(chess,t,new ArrayList<>())) {
                checkedDead.add(chess);
            }
        }
        for(Chess chess:checkedDead){
            chesses.remove(chess);
            put(chess.blockPos,new Chess(-1,chess.blockPos));
            res[t]++;
        }
        return res;
        //System.out.println("updateEaten: "+(System.currentTimeMillis() - start));
    }
    public boolean haveAir(Chess chess,int team,List<Chess> checked){
        for(BlockPos2d off:checkDirection) {
            BlockPos2d p = chess.blockPos.add(off);
            boolean sc=false;
            for(Chess s:checked){
                if(s.blockPos.equals(p)){
                    sc=true;
                    break;
                }
            }
            if(sc) continue;
            Chess c=get(p);
            int t=c.team;
            if(t==-1){
                checkedAlive.add(chess);
                return true;
            }
            if(t==team){
                if(!checked.contains(chess)) checked.add(chess);
                if(haveAir(c,team,new ArrayList<>(checked))){
                    checkedAlive.add(chess);
                    return true;
                }
            }
        }
        //checkedDead.add(chess);
        //checkedDead.addAll(checked);
        return false;
    }
    public void put(BlockPos2d pos,Chess c){
        HashMap<Integer,Chess> m=map.get(pos.x);
        if(m==null) {
            m=new HashMap<>();
            m.put(pos.y,c);
            map.put(pos.x,m);
        }
        else{
            m.put(pos.y,c);
        }
    }
    public Chess get(BlockPos2d pos){
        HashMap<Integer,Chess> m=map.get(pos.x);
        if(m==null) return null;
        return m.get(pos.y);
    }
    public void placeChess(Chess chess){
        chesses.add(chess);
        put(chess.blockPos,chess);
        nowTeam.set((chess.team + 1) % 2) ;
    }
}
