package ch.epfl.cs107.play.game.icrogue.actor.projectiles;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends ICRogueActor implements Consumable, Interactor {
    private int frames;
    private int damage;
    private boolean isConsumed;
    private boolean isEnemy;
    public static final int DEFAULT_MOVE_DURATION=10;
    public static final int DEFAULT_DAMAGE=1;

    public Projectile(Area area, Orientation orientation, DiscreteCoordinates coordinates,
                      int damagepts, int nframes, boolean isEnemy){
        super(area,orientation,coordinates);
        damage=damagepts;
        frames=nframes;
        isConsumed=false;
        this.isEnemy = isEnemy;
    }

    public abstract void setSprite(String name);

    public Projectile(Area area, Orientation orientation, DiscreteCoordinates coordinates){
        super(area,orientation,coordinates);
        damage=DEFAULT_DAMAGE;
        frames=DEFAULT_MOVE_DURATION;
        isConsumed=false;
    }

    public void consume(){
        isConsumed=true;
        getOwnerArea().unregisterActor(this);
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    public void update(float deltaTime) {
        move(frames);
        super.update(deltaTime);
    }

    public int getDamage(){return damage;}

    public boolean getIsEnemy(){return isEnemy;}

    public List<DiscreteCoordinates> getCurrentCells(){
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /*returns the cell that the player sees, AKA the cell in front of the direction he is facing*/
    public List<DiscreteCoordinates> getFieldOfViewCells(){
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    /*only interacts if its not consumed*/
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if(!isConsumed()){
            ((ICRogueInteractionHandler)v).interactWith(this, isCellInteraction);
        }

    }

    public boolean wantsCellInteraction() {
        return true;
    }

    public boolean wantsViewInteraction() {
        return true;
    }




}

