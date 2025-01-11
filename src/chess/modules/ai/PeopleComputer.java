package chess.modules.ai;

import chess.math.BlockPos2d;
import chess.modules.Chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static chess.modules.ChessMain.checkDirection;
import static chess.modules.ChessMain.cs;

public class PeopleComputer {
    public static int STEP=2;
    public static int TEAM=1;
    public volatile HashMap<Integer,HashMap<Integer, Chess>> map=new HashMap<>();
    public volatile ArrayList<Chess> chesses=new ArrayList<>();
    public volatile ArrayList<Chess> checkedAlive=new ArrayList<>();
    public volatile ArrayList<Chess> checkedDead=new ArrayList<>();
    public void think(){

        /*reset();
        Chess best=null;
        int bestScore=-1000;
        for(int x=-cs.sizeX;x<=cs.sizeX;x++){
            for(int y=-cs.sizeY;y<=cs.sizeY;y++){

                if(get(new BlockPos2d(x,y)).team!=-1) continue;

                Chess c=new Chess(1,new BlockPos2d(x,y));
                chesses.add(c);
                put(c.blockPos,c);
                int[] rs=updateEaten();
                int r=rs[0]-rs[1];






                if(r>bestScore){
                    bestScore=r;
                    best=c;
                }
                reset();
            }
        }*/
        Chess best=sim(1,STEP,cs.map,cs.chesses,null);
        if(best!=null) {
            cs.placeChess(best);
        }
        else{
            System.out.println("people computer broken \n");
        }
    }
    public Chess sim(int team,int left,HashMap<Integer,HashMap<Integer,Chess>> mapO,ArrayList<Chess> chessesO,Chess cAdd){
        if(left<=0){
            return null;
        }
        HashMap<Integer,HashMap<Integer,Chess>> newMapO= clone(mapO);
        ArrayList<Chess> newChessesO=new ArrayList<>(chessesO);
        if(cAdd!=null){
            put(newMapO,cAdd.blockPos,cAdd);
            newChessesO.add(cAdd);
        }
        reset(newMapO,newChessesO);
        Chess best=null;
        double bestScore=-1000;
        for(int x=-cs.sizeX;x<=cs.sizeX;x++){
            for(int y=-cs.sizeY;y<=cs.sizeY;y++){

                if(get(new BlockPos2d(x,y)).team!=-1) continue;

                Chess c=new Chess(team,new BlockPos2d(x,y));
                this.chesses.add(c);
                put(c.blockPos,c);
                int[] rs=updateEaten((team+1)%2);
                double r=rs[(team+1)%2]-rs[team]+airBlocked(c)*0.25;

                Chess c2= sim((team+1)%2,left-1,map,chesses,c);
                if(c2!=null){
                    r-=c2.moreInfo;
                }
                if(r>bestScore){
                    bestScore=r;
                    best=c;
                }
                reset(newMapO,newChessesO);
            }
        }
        if(best==null){
            return null;
        }
        best.moreInfo=bestScore;
        return best;
    }
    public void reset(){
        map=new HashMap<>();
        chesses=new ArrayList<>();
        for(Map.Entry<Integer,HashMap<Integer,Chess>> entry:cs.map.entrySet()) {
            HashMap<Integer, Chess> m = new HashMap<>(entry.getValue());
            map.put(entry.getKey(),m);
        }
        chesses.addAll(cs.chesses);
    }
    public void reset(Map<Integer,HashMap<Integer,Chess>> ma,ArrayList<Chess> c){
        map=new HashMap<>();
        chesses=new ArrayList<>();
        for(Map.Entry<Integer,HashMap<Integer,Chess>> entry:ma.entrySet()) {
            HashMap<Integer, Chess> m = new HashMap<>(entry.getValue());
            map.put(entry.getKey(),m);
        }
        chesses.addAll(c);
    }
    public HashMap<Integer,HashMap<Integer,Chess>> clone(HashMap<Integer,HashMap<Integer,Chess>> ma){
        HashMap<Integer,HashMap<Integer,Chess>> map=new HashMap<>();
        for(Map.Entry<Integer,HashMap<Integer,Chess>> entry:ma.entrySet()) {
            HashMap<Integer, Chess> m = new HashMap<>(entry.getValue());
            map.put(entry.getKey(),m);
        }
        return map;
    }
    public int[] updateEaten(int team){

        //long start=System.currentTimeMillis();
        int[] res=new int[4];
        checkedAlive=new ArrayList<>();
        checkedDead=new ArrayList<>();
        for(Chess chess:chesses){
            if(chess.team!=team)continue;
            if(checkedAlive.contains(chess)||checkedDead.contains(chess))continue;
            if(!haveAir(chess,1,new ArrayList<>())) {
                checkedDead.add(chess);
            }
        }
        for(Chess chess:checkedDead){
            chesses.remove(chess);
            put(chess.blockPos,new Chess(-1,chess.blockPos));
            res[1]++;
        }
        int t=(team+1)%2;
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
    public boolean haveAir(Chess chess, int team, List<Chess> checked){
        for(BlockPos2d off: checkDirection) {
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
    public int airBlocked(Chess chess){
        int i=0;
        for(BlockPos2d off:checkDirection){
            BlockPos2d p=chess.blockPos.add(off);
            Chess c=get(p);
            if(c.team==(chess.team+1)%2){
                i++;
            }
        }
        return i;
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
    public void put(HashMap<Integer, HashMap<Integer, Chess>> map, BlockPos2d pos,Chess c){
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
    public Chess get(HashMap<Integer, HashMap<Integer, Chess>> map,BlockPos2d pos){
        HashMap<Integer,Chess> m=map.get(pos.x);
        if(m==null) return null;
        return m.get(pos.y);
    }
}
