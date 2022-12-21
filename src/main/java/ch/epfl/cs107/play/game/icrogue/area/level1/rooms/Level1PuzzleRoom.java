package ch.epfl.cs107.play.game.icrogue.area.level1.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Lever;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level1PuzzleRoom extends Level1Room{

    private Lever lever;

    public Level1PuzzleRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
        lever=new Lever(this, Orientation.RIGHT,new DiscreteCoordinates(5,4));
    }
    protected void createArea() {
        super.createArea();
        // Base
        registerActor(new Background(this, getBehaviourName())) ;
        registerActor(lever);

        /*
        registerActor(new Cherry(this, Orientation.DOWN, new DiscreteCoordinates(6,3)));
        registerActor(new Sword(this, Orientation.DOWN, new DiscreteCoordinates(7, 3)));
        registerActor(new Bow(this, Orientation.DOWN, new DiscreteCoordinates(7, 7)));
        registerActor(new Turret(this, Orientation.DOWN, new DiscreteCoordinates(1, 1)
                ,true, false, false, true));

         */


    }
    public boolean isOn() {
        return super.isOn()||!lever.wasInteracted();
    }

    public boolean isOff() {
        return super.isOff()&&lever.wasInteracted();
    }
}
