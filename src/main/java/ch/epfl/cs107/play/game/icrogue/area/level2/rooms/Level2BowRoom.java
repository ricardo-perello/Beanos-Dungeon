package ch.epfl.cs107.play.game.icrogue.area.level2.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Bow;
import ch.epfl.cs107.play.game.icrogue.area.level1.rooms.Level1ItemRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level2BowRoom extends Level2ItemRoom {
    public Level2BowRoom(DiscreteCoordinates coordinates) {
        super(coordinates);

        addItem(new Bow(this, Orientation.DOWN,new DiscreteCoordinates(5,5)));
    }
}
