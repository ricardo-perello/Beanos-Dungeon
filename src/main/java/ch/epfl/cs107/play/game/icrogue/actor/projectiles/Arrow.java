package ch.epfl.cs107.play.game.icrogue.actor.projectiles;
/*
 *  Author:  Ricardo Perelló Mas
 *  Date: 4-12-22
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

public class Arrow extends Projectile {

    private Sprite sprite;
    private ICRogueArrowInteractionHandler handler;

    public Arrow(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates, DEFAULT_DAMAGE, 5);
        setSprite(orientation);
        handler = new ICRogueArrowInteractionHandler();
    }

    public void setSprite() {
    }

    public void setSprite(Orientation orientation) {
        if (orientation.equals(Orientation.DOWN)) {
            sprite = new Sprite("zelda/arrow", .75f, 1.5f, this,
                    new RegionOfInterest(0, 0, 16, 32), new Vector(.15f, -.15f));
        } else if (orientation.equals(Orientation.RIGHT)) {
            sprite = new Sprite("zelda/arrow", 1.5f, 0.5f, this,
                    new RegionOfInterest(64, 16, 32, 8), new Vector(.15f, -.15f));
        } else if (orientation.equals(Orientation.UP)) {
            sprite = new Sprite("zelda/arrow", 0.5f, 1.5f, this,
                    new RegionOfInterest(8, 0, 16, 32), new Vector(.15f, -.15f));
        } else if (orientation.equals(Orientation.LEFT)) {
            sprite = new Sprite("zelda/arrow", 1.5f, 0.5f, this,
                    new RegionOfInterest(96, 16, 32, 8), new Vector(.15f, -.15f));
        }
    }


    public void draw(Canvas canvas) {
        if (!isConsumed()) {
            sprite.draw(canvas);
        }
    }

    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }


    /*this handles all the interactions for Arrow and a handler was already created in the Arrow constructor*/
    private class ICRogueArrowInteractionHandler implements ICRogueInteractionHandler {


        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if (cell.getType().equals(ICRogueBehavior.ICRogueCellType.WALL)
                    || cell.getType().equals(ICRogueBehavior.ICRogueCellType.HOLE)) {
                consume();
            }
        }

        public void interactWith(Sword sword, boolean isCellInteraction) {
            if (wantsViewInteraction()) {
                consume();
            }
        }
    }

}