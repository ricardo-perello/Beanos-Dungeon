package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Beanos;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.BossTurret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Wither;
import ch.epfl.cs107.play.game.icrogue.actor.items.Bow;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.items.Sword;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class poisonBalls extends Projectile {

    private Sprite sprite;
    private poisonBalls.ICRogueArrowInteractionHandler handler;



    @Override
    public void setSprite(String name) {}

    public poisonBalls(Area area, Orientation orientation, DiscreteCoordinates coordinates, boolean isEnemy) {
        super(area, orientation, coordinates, DEFAULT_DAMAGE, 5, isEnemy);
        setSprite(orientation);
        handler = new poisonBalls.ICRogueArrowInteractionHandler();
    }
    public poisonBalls(Area area, Orientation orientation, DiscreteCoordinates coordinates,boolean isEnemy, int damage) {
        super(area, orientation, coordinates, damage, 5, isEnemy);
        setSprite(orientation);
        handler = new poisonBalls.ICRogueArrowInteractionHandler();
    }


    public void setSprite(Orientation orientation) {

            sprite = new Sprite("Inball", .7f, 0.7f, this);
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
            if (wantsViewInteraction() && !(isConsumed()) && getIsEnemy()) {
                player.decreaseHp((float) getDamage());
                player.poison();
                consume();
            }
        }
        public void interactWith(Turret turret, boolean isCellInteraction) {
            if (wantsViewInteraction() && !getIsEnemy()) {
                consume();
                turret.decreaseHp(getDamage());
            }
        }
        public void interactWith(BossTurret turret, boolean isCellInteraction) {
            if (wantsViewInteraction() && !getIsEnemy()) {
                consume();
                turret.decreaseHp(getDamage());
            }
        }
        public void interactWith(Wither wither, boolean isCellInteraction) {
            if (wantsViewInteraction() && !getIsEnemy()) {
                consume();
                wither.decreaseHp(getDamage());
            }
            if (wantsViewInteraction() && getIsEnemy()) {
                consume();
            }
        }
        public void interactWith(Beanos beanos, boolean isCellInteraction) {
            if (wantsViewInteraction() && !getIsEnemy()) {
                consume();
                beanos.decreaseHp(getDamage());
            }
            if (wantsViewInteraction() && getIsEnemy()) {
                consume();
            }
        }


    }

}
