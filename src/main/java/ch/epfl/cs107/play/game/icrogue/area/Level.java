package ch.epfl.cs107.play.game.icrogue.area;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0BossRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0KeyRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;

public abstract class Level implements Logic {

    protected enum MapState{
        NULL,
        PLACED,
        EXPLORED,
        BOSS_ROOM,
        BOSS_KEYROOM,
        CREATED;

        @Override
        public String toString(){
            return Integer.toString(ordinal());
        }
    }

    private ICRogueRoom[][] map;
    private DiscreteCoordinates arrivalcoordinates;
    private DiscreteCoordinates bossPosition;
    private String roomName;
    private int[] roomDistribution;
    private int bossRoomKeyID;

    public int getBossRoomKeyID(){
        return bossRoomKeyID;
    }

    public boolean isNextToBossRoom(DiscreteCoordinates coordinates){
        return (map[coordinates.x][coordinates.y] instanceof Level0BossRoom);
    }

    public void setArrivalcoordinates(DiscreteCoordinates coordinates){
        arrivalcoordinates=coordinates;
    }

    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room){
        map[coords.x][coords.y]=room;
    }

    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination,
                                               ConnectorInRoom connector){
        int idx=connector.getIndex();
        map[coords.x][coords.y].SetConnectorAreaTitle(idx, destination);


    }
    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        setRoomConnectorDestination(coords,destination,connector);
        int idx=connector.getIndex();
        map[coords.x][coords.y].setConnectorState(idx,Connector.ConnectorState.CLOSED);


    }
    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){
        int idx=connector.getIndex();
        map[coords.x][coords.y].setConnectorState(idx,Connector.ConnectorState.LOCKED);
        map[coords.x][coords.y].setConnectorKeyID(idx,keyId);

    }
    protected void setRoomName(DiscreteCoordinates coordinates){
        roomName=map[coordinates.x][coordinates.y].getTitle();
    }

    public Level(boolean randomMap,DiscreteCoordinates startPosition,
                 int[] roomDistribution, int x, int y){
        bossRoomKeyID= RandomHelper.roomGenerator.nextInt(0,50);
        if(!randomMap){
            arrivalcoordinates=startPosition;
            map=new ICRogueRoom[x][y];
            bossPosition=new DiscreteCoordinates(0,0);
            generateFixedMap();
        }
        else{
            int i=0;
            for(int room:roomDistribution){
                i+=room;
            }
            map=new ICRogueRoom[i][i];
            this.roomDistribution=roomDistribution;
            generateRandomMap();
        }

    }

    public ICRoguePlayer addPlayer(DiscreteCoordinates startingroom){
        ICRoguePlayer player=new ICRoguePlayer(map[startingroom.x][startingroom.y],Orientation.UP,new DiscreteCoordinates(2,2));
        player.enterArea(map[startingroom.x][startingroom.y],new DiscreteCoordinates(2,2));
        map[startingroom.x][startingroom.y].visit();
        return player;
    }

    public void enterArea(DiscreteCoordinates transitionCoor, ICRoguePlayer player, String roomName){
        for(int i=0;i<map.length;++i){
            for(int k=0;k<map[i].length;++k){
                if(map[i][k]!=null){
                    if(roomName.equals(map[i][k].getTitle())){
                        player.enterArea(map[i][k],transitionCoor);
                        map[i][k].visit();
                    }
                }
            }
        }
    }

    public void addAreas(ICRogue game){
        for(int i=0;i<map.length;++i){
            for(int k=0;k<map[i].length;++k){
                if(map[i][k] != null){
                    game.addArea(map[i][k]);
                }

            }
        }
    }

    public String getRoomName(DiscreteCoordinates coordinates){
        return map[coordinates.x][coordinates.y].getTitle();
    }

    protected MapState[][] generateRandomRoomPlacement(){
        ArrayList<DiscreteCoordinates>placed=new ArrayList<>();
        ArrayList<DiscreteCoordinates>explored=new ArrayList<>();


        MapState[][]output=new MapState[map.length][map[0].length];
        int roomsToPlace=map.length;
        for(int i=0;i<map.length;++i){
            for(int k=0;k<map[i].length;++k){
                output[i][k]=MapState.NULL;
            }
        }
        output[(map.length+1)/2][(map[(map.length+1)/2].length+1)/2]=MapState.PLACED;
        placed.add(new DiscreteCoordinates ((map.length+1)/2,(map[(map.length+1)/2].length+1)/2));
        --roomsToPlace;

        while (roomsToPlace>0){
            for(int i=0;i<placed.size();++i){
                int xcor=placed.get(i).x;
                int ycor=placed.get(i).y;
                output[xcor][ycor]=MapState.EXPLORED;
                explored.add(new DiscreteCoordinates(xcor,ycor));

                int freeSlots=4;
                ArrayList<DiscreteCoordinates>roomCoorAvailable= new ArrayList<>();
                if(xcor+1>=output.length||!output[xcor+1][ycor].equals(MapState.NULL)){
                    --freeSlots;
                }
                else{
                    roomCoorAvailable.add(new DiscreteCoordinates(xcor+1,ycor));
                }
                if(xcor-1<0||!output[xcor-1][ycor].equals(MapState.NULL)){
                    --freeSlots;
                }
                else{
                    roomCoorAvailable.add(new DiscreteCoordinates(xcor-1,ycor));
                }
                if(ycor+1>=output[0].length||!output[xcor][ycor+1].equals(MapState.NULL)){
                    --freeSlots;
                }
                else{
                    roomCoorAvailable.add(new DiscreteCoordinates(xcor,ycor+1));
                }
                if(ycor-1<0||!output[xcor][ycor-1].equals(MapState.NULL)){
                    --freeSlots;
                }
                else{
                    roomCoorAvailable.add(new DiscreteCoordinates(xcor,ycor-1));
                }
                if(freeSlots>1&&roomsToPlace>1){
                    int rooms= RandomHelper.roomGenerator.nextInt(1,Math.min(freeSlots,roomsToPlace));

                    if(roomCoorAvailable.size()>0){
                        ArrayList<Integer>nOfCoor=new ArrayList<>();
                        for(int z=0;z<roomCoorAvailable.size();++z){
                            nOfCoor.add(z);
                        }
                        List<Integer> indexFromCoorArray=RandomHelper.chooseKInList(rooms,nOfCoor);
                            for (Integer integer : indexFromCoorArray) {
                                int x=roomCoorAvailable.get(integer).x;
                                int y=roomCoorAvailable.get(integer).y;
                                if(roomsToPlace>0){
                                    output[x][y] = MapState.PLACED;
                                    roomsToPlace--;
                                    placed.add(new DiscreteCoordinates(x,y));
                                }
                            }
                    }
                }
                else if(freeSlots==1||(roomsToPlace==1&&freeSlots!=0)){
                    int rooms= 1;

                    if(roomCoorAvailable.size()>0){
                        ArrayList<Integer>nOfCoor=new ArrayList<>();
                        for(int z=0;z<roomCoorAvailable.size();++z){
                            nOfCoor.add(z);
                        }
                        List<Integer> indexFromCoorArray=RandomHelper.chooseKInList(rooms,nOfCoor);
                            for (Integer integer : indexFromCoorArray) {
                                int x=roomCoorAvailable.get(integer).x;
                                int y=roomCoorAvailable.get(integer).y;
                                if(roomsToPlace>0){
                                    output[x][y] = MapState.PLACED;
                                    roomsToPlace--;
                                    placed.add(new DiscreteCoordinates(x,y));
                                }
                            }
                    }
                }


            }
        }

        int keyindx=RandomHelper.roomGenerator.nextInt(0,placed.size());
        DiscreteCoordinates keyroomCoords=placed.get(keyindx);
        placed.remove(keyindx);

        int bossindx=RandomHelper.roomGenerator.nextInt(0,placed.size());
        DiscreteCoordinates bossroomCoords=placed.get(bossindx);
        placed.remove(bossindx);


        output[keyroomCoords.x][keyroomCoords.y]=MapState.BOSS_KEYROOM;

        bossPosition=bossroomCoords;
        output[bossroomCoords.x][bossroomCoords.y]=MapState.BOSS_ROOM;

        return output;
    }

    public boolean isOn() {
        if(map[bossPosition.x][bossPosition.y]==null){
            return false;
        }
        else{
            return map[bossPosition.x][bossPosition.y].isOn();
        }

    }

    public boolean isOff() {
        if(map[bossPosition.x][bossPosition.y]==null){
            return false;
        }
        else{
            return map[bossPosition.x][bossPosition.y].isOff();
        }
    }

    public float getIntensity() {
        return 0;
    }

    public boolean isResolved(){
        return isOff()&&!isOn();
    }

    public abstract void generateFixedMap();

    public abstract void generateRandomMap();
}
