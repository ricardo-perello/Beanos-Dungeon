package ch.epfl.cs107.play.game.icrogue.actor.projectiles;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.items.Sword;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Projectile;

public class Fire extends Projectile{

    private Sprite sprite;
    private ICRogueFireInteractionHandler handler;

    public Fire(Area area, Orientation orientation, DiscreteCoordinates coordinates){
        super(area,orientation,coordinates,DEFAULT_DAMAGE,5);
        setSprite();
        handler=new ICRogueFireInteractionHandler();
    }

    public void setSprite() {
        sprite=new Sprite("zelda/fire",0.75f,0.75f,this,
                new RegionOfInterest(0,0,16,16), new Vector(0,0));
    }


    public void draw(Canvas canvas) {
        if(!isConsumed()){
            sprite.draw(canvas);
        }
    }

    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler,isCellInteraction);
    }


    /*this handles all of the interactions for Fire and a handler was already created in the Fire constructor*/
    private class ICRogueFireInteractionHandler implements ICRogueInteractionHandler {


        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if(cell.getType().equals(ICRogueBehavior.ICRogueCellType.WALL)||cell.getType().equals(ICRogueBehavior.ICRogueCellType.HOLE)){
                consume();
            }
        }
        public void interactWith(Sword sword, boolean isCellInteraction) {
            if(wantsViewInteraction()) {
                consume();
            }
        }
    }

}