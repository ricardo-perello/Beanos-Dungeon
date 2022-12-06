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

public class Level0StaffRoom extends Level0ItemRoom{
    public Level0StaffRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
        /*
        addItem((Item) new Staff(this, Orientation.DOWN,new DiscreteCoordinates(5,5)));*/
    }
}
