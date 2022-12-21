package ch.epfl.cs107.play.game.icrogue.area.level1.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.area.level2.rooms.Level2ItemRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level1CherryRoom extends Level1ItemRoom {

    public Level1CherryRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
        Cherry cherry= new Cherry(this, Orientation.DOWN,new DiscreteCoordinates(5,5));
        addItem(cherry);


    }
}
