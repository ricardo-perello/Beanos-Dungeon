package ch.epfl.cs107.play.game.icrogue.area.Beanos;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.area.Beanos.rooms.BeanosBossRoom;
import ch.epfl.cs107.play.game.icrogue.area.Beanos.rooms.BeanosRoom;
import ch.epfl.cs107.play.game.icrogue.area.Beanos.rooms.BeanosStartingRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0KeyRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.area.level1.rooms.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

public class LevelBeanos extends Level {
    public static DiscreteCoordinates startingroom=new DiscreteCoordinates(1,0);
    private static final DiscreteCoordinates arrivalCoordinates=new DiscreteCoordinates(2,0);
    private static int[] roomArrangement;

    public LevelBeanos() {
        super(false,startingroom,createRoomArrangement(), 4, 4);
    }

    private static int[]createRoomArrangement(){
        roomArrangement= new int[]{1, 1};
        return roomArrangement;
    }


    public void generateFixedMap(){
        generateFinalMap();
    }

    @Override
    public void generateRandomMap() {

    }


    public void generateFinalMap(){
        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom(room11, new BeanosBossRoom(room11));
        setRoomConnector(room11, "icrogue/beanos10", BeanosRoom.BeanosConnectors.S);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1, 0);
        setRoom(room10, new BeanosStartingRoom(room10));
        setRoomConnector(room10, "icrogue/beanos11", BeanosRoom.BeanosConnectors.N);
    }
}
