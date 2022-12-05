package ch.epfl.cs107.play.game.icrogue.area.level0;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0KeyRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0StaffRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0 extends Level {
    public Level0(DiscreteCoordinates coordinates, int x, int y) {
        super(coordinates, x, y);
    }

    public void generateFixedMap(){

    }
    public void generateMap1(){
        Level0KeyRoom keyRoom=new Level0KeyRoom(new DiscreteCoordinates(0,0),1);
        Level0Room room=new Level0Room(new DiscreteCoordinates(1,0));
        setRoom(new DiscreteCoordinates(0,0),keyRoom);
        setRoom(new DiscreteCoordinates(1,0),room);
        setRoomConnector(new DiscreteCoordinates(0,0),room.getTitle(), Level0Room.Level0Connectors.E);
        setRoomConnector(new DiscreteCoordinates(1,0),keyRoom.getTitle(), Level0Room.Level0Connectors.W);
        lockRoomConnector(new DiscreteCoordinates(0,0), Level0Room.Level0Connectors.E,1);
        lockRoomConnector(new DiscreteCoordinates(1,0), Level0Room.Level0Connectors.W,1);
    }
    public void generateMap2(){
        Level0KeyRoom keyRoom=new Level0KeyRoom(new DiscreteCoordinates(3,0),2);
        Level0Room room=new Level0Room(new DiscreteCoordinates(0,0));
        Level0Room room2=new Level0Room(new DiscreteCoordinates(1,0));
        Level0Room room3=new Level0Room(new DiscreteCoordinates(1,1));
        Level0StaffRoom staffRoom= new Level0StaffRoom(new DiscreteCoordinates(2,0));
        setRoom(new DiscreteCoordinates(3,0),keyRoom);
        setRoom(new DiscreteCoordinates(0,0),room);
        setRoom(new DiscreteCoordinates(1,0),room2);
        setRoom(new DiscreteCoordinates(1,1),room3);
        setRoom(new DiscreteCoordinates(2,0),staffRoom);

        /*
        setRoomConnector(new DiscreteCoordinates(0,0),room.getTitle(), Level0Room.Level0Connectors.E);
        setRoomConnector(new DiscreteCoordinates(1,0),keyRoom.getTitle(), Level0Room.Level0Connectors.W);
        */

        lockRoomConnector(new DiscreteCoordinates(3,0), Level0Room.Level0Connectors.W,2);
    }
}
