package ch.epfl.cs107.play.game.icrogue.actor.projectiles;
/*
 *  Author:  Ricardo Perelló Mas
 *  Date: 4-12-22
 */

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Bow;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.items.Sword;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Melee extends Projectile {

    private Sprite sprite;
    private ICRogueMeleeInteractionHandler handler;

    public Melee(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates, (int) Sword.DEFAULT_DAMAGE, 5);

        handler = new ICRogueMeleeInteractionHandler();
    }

    public void setSprite() {
        //no sprite needed
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }



    public void draw(Canvas canvas) {
        if (!isConsumed()) {
            //sprite.draw(canvas);
        }
    }

    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }


    /*this handles all the interactions for Meleee and a handler was already created in the Melee constructor*/
    private class ICRogueMeleeInteractionHandler implements ICRogueInteractionHandler {


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

        public void interactWith(Staff staff, boolean isCellInteraction) {
            if(wantsViewInteraction()) {
                consume();
            }
        }
        public void interactWith(Bow bow, boolean isCellInteraction) {
            if(wantsViewInteraction()) {
                consume();
            }
        }

        public void interactWith(ICRoguePlayer player, boolean isCellInteraction) {
            if (wantsViewInteraction() && !(isConsumed())) {
                player.decreaseHp((float) DEFAULT_DAMAGE);
                consume();
            }
        }
        public void interactWith(Turret turret, boolean isCellInteraction) {
            if (wantsViewInteraction()) {
                turret.decreaseHp((float) Sword.DEFAULT_DAMAGE);
                consume();
            }
        }

    }

}