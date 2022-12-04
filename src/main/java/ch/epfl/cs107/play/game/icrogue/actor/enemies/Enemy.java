package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Enemy extends ICRogueActor {
    private boolean isAlive;
    private float hp;

    public Enemy(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
    }

    public boolean getIsAlive(){return isAlive;}

    public void die(){
        isAlive = false;
    }
}
