package ch.epfl.cs107.play.game.icrogue.area.level2.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level2HeartRoom extends Level2ItemRoom {

    public Level2HeartRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
        Cherry cherry= new Cherry(this, Orientation.DOWN,new DiscreteCoordinates(4,5));
        addItem(cherry);
        Cherry cherry1= new Cherry(this, Orientation.DOWN,new DiscreteCoordinates(6,5));
        addItem(cherry1);

    }
}
