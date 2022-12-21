package ch.epfl.cs107.play.game.icrogue.area.level1.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Bow;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level1BowRoom extends Level1ItemRoom {
    public Level1BowRoom(DiscreteCoordinates coordinates) {
        super(coordinates);

        addItem(new Bow(this, Orientation.DOWN,new DiscreteCoordinates(5,5)));
    }
}
