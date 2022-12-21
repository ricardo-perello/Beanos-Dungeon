package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.actor.items.Coin;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Enemy extends ICRogueActor {
    protected boolean isAlive;
    protected float hp;
    public static DiscreteCoordinates lastCoordinates;
    public static boolean wantsAddCoin = false;

    public Enemy(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
    }
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    public boolean getIsAlive(){return isAlive;}

    public void die(){
        isAlive = false;
        lastCoordinates = getCurrentMainCellCoordinates();
        leaveArea();
        wantsAddCoin = true;
    }
}
