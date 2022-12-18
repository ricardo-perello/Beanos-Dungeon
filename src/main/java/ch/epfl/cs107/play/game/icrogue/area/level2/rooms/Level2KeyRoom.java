package ch.epfl.cs107.play.game.icrogue.area.level2.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level2KeyRoom extends Level2ItemRoom {

    public Level2KeyRoom(DiscreteCoordinates coordinates, int keyID) {
        super(coordinates);
        Key key= new Key(this, Orientation.DOWN,new DiscreteCoordinates(5,5),keyID);
        addItem(key);


    }
}
