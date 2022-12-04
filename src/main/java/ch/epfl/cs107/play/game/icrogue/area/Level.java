package ch.epfl.cs107.play.game.icrogue.area;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Level {
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
        ICRogueRoom room=map[coords.x][coords.y];


    }
    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        setRoomConnectorDestination(coords,destination,connector);


    }
    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){


    }
    protected void setRoomName(DiscreteCoordinates coordinates){
        roomName=map[coordinates.x][coordinates.y].getTitle();
    }
    public Level(DiscreteCoordinates coordinates, int x, int y){
        arrivalcoordinates=coordinates;
        map=new ICRogueRoom[x][y];
        bossPosition=new DiscreteCoordinates(0,0);
    }
    public abstract void generateFixedMap();
}
