package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.swing.Item;

public class Level0KeyRoom extends Level0ItemRoom{

    public Level0KeyRoom(DiscreteCoordinates coordinates, int keyID) {
        super(coordinates);
        Key key= new Key(this, Orientation.DOWN,new DiscreteCoordinates(5,5),keyID);
        addItem(key);


    }
}
