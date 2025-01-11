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
    public static int STEP=4;
    public static int TEAM=1;
    public volatile HashMap<Integer,HashMap<Integer, Chess>> map=new HashMap<>();
    public volatile ArrayList<Chess> chesses=new ArrayList<>();
    public volatile ArrayList<Chess> checkedAlive=new ArrayList<>();
    public volatile ArrayList<Chess> checkedDead=new ArrayList<>();
    public void think(){

        reset();
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
        }
        if(best!=null) {
            cs.chesses.add(best);
            cs.put(best.blockPos, best);
            cs.nowTeam = (cs.nowTeam + 1) % 2;
        }
        else{
            System.out.println("people computer broken \n");
        }
    }
    public void reset(){
        map=new HashMap<>();
        chesses=new ArrayList<>();
        map.putAll(cs.map);
        chesses.addAll(cs.chesses);
    }
    public int[] updateEaten(){

        //long start=System.currentTimeMillis();
        int[] res=new int[2];
        checkedAlive=new ArrayList<>();
        checkedDead=new ArrayList<>();
        for(Chess chess:chesses){
            if(chess.team!=1)continue;
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
        int t=0;
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
}
