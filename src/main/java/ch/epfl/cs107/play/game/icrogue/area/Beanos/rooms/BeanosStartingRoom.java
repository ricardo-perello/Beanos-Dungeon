package ch.epfl.cs107.play.game.icrogue.area.Beanos.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.items.Sword;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class BeanosStartingRoom extends BeanosRoom {

    public BeanosStartingRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
    }

    protected void createArea() {
        super.createArea();

        registerActor(new Sword(this, Orientation.DOWN, new DiscreteCoordinates(3, 5)));

        registerActor(new Staff(this, Orientation.DOWN, new DiscreteCoordinates(6, 5)));




    }

}
