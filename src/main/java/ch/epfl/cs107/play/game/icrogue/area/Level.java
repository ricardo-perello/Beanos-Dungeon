package ch.epfl.cs107.play.game.icrogue.area;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public abstract class Level implements Logic {
    private ICRogueRoom[][] map;
    private DiscreteCoordinates arrivalcoordinates;
    private DiscreteCoordinates bossPosition;
    private String roomName;

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
        map[coords.x][coords.y].setConnectorState(idx,Connector.ConnectorState.OPEN);


    }
    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){
        int idx=connector.getIndex();
        map[coords.x][coords.y].setConnectorState(idx,Connector.ConnectorState.LOCKED);
        map[coords.x][coords.y].setConnectorKeyID(idx,keyId);

    }
    protected void setRoomName(DiscreteCoordinates coordinates){
        roomName=map[coordinates.x][coordinates.y].getTitle();
    }
    public Level(DiscreteCoordinates coordinates, int x, int y){
        arrivalcoordinates=coordinates;
        map=new ICRogueRoom[x][y];
        bossPosition=new DiscreteCoordinates(0,0);
        generateFixedMap();
    }

    public Level(DiscreteCoordinates coordinates, int x, int y, DiscreteCoordinates bossCoor){
        arrivalcoordinates=coordinates;
        map=new ICRogueRoom[x][y];
        bossPosition=bossCoor;
        generateFixedMap();
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
}
