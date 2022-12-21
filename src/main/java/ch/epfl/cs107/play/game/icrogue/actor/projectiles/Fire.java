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
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Beanos;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.BossTurret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Wither;
import ch.epfl.cs107.play.game.icrogue.actor.items.Bow;
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

    public Fire(Area area, Orientation orientation, DiscreteCoordinates coordinates, String spriteName, boolean isEnemy){
        super(area,orientation,coordinates,DEFAULT_DAMAGE,5, isEnemy);
        setSprite(spriteName,orientation);
        handler=new ICRogueFireInteractionHandler();
        enterArea(area,coordinates);
    }
    public Fire(Area area, Orientation orientation, DiscreteCoordinates coordinates, String spriteName, int damage, boolean isEnemy){
        super(area,orientation,coordinates,damage,5, isEnemy);
        setSprite(spriteName,orientation);
        handler=new ICRogueFireInteractionHandler();
        enterArea(area,coordinates);
    }

    public void setSprite(String name) {
        if (name.equals("zelda/flameskull")){
            sprite=new Sprite(name,0.75f,0.75f,this,
                    new RegionOfInterest(0,0,16,16), new Vector(0,0));
        }
        else{
            sprite=new Sprite("zelda/fire",0.75f,0.75f,this,
                    new RegionOfInterest(0,0,16,16), new Vector(0,0));
        }

    }
    public void setSprite(String name, Orientation orientation) {
        if (name.equals("zelda/flameskull")){
            if (orientation.equals(Orientation.DOWN)) {
                sprite = new Sprite(name, .9f, .9f, this,
                        new RegionOfInterest(0, 64, 32, 32), new Vector(.0f, 0.f));
            } else if (orientation.equals(Orientation.RIGHT)) {
                sprite = new Sprite(name, 0.9f, .9f, this,
                        new RegionOfInterest(0, 96, 32, 32), new Vector(0.0f, .2f));
            } else if (orientation.equals(Orientation.UP)) {
                sprite = new Sprite(name, 0.9f, 0.9f, this,
                        new RegionOfInterest(0, 0, 32, 32), new Vector(.0f, 0.0f));
            } else if (orientation.equals(Orientation.LEFT)) {
                sprite = new Sprite(name, 0.9f, .9f, this,
                        new RegionOfInterest(0, 32, 32, 32), new Vector(0.0f, .2f));
            }
        }
        else{
            sprite=new Sprite("zelda/fire",0.75f,0.75f,this,
                    new RegionOfInterest(0,0,16,16), new Vector(0,0));
        }

    }


    public void draw(Canvas canvas) {
        if(!isConsumed()){
            sprite.draw(canvas);
        }
    }


    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler,isCellInteraction);
    }


    /*this handles all the interactions for Fire and a handler was already created in the Fire constructor*/
    private class ICRogueFireInteractionHandler implements ICRogueInteractionHandler {


        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if(cell.getType().equals(ICRogueBehavior.ICRogueCellType.WALL)
                    ||cell.getType().equals(ICRogueBehavior.ICRogueCellType.HOLE)){
                consume();
            }
        }
        public void interactWith(Sword sword, boolean isCellInteraction) {
            if(wantsViewInteraction()) {
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

        public void interactWith(Turret turret, boolean isCellInteraction) {
            if(wantsViewInteraction()&&!isConsumed() && !getIsEnemy()) {
                consume();
                turret.decreaseHp(getDamage());
            }
            else if(wantsViewInteraction()&&!isConsumed() && getIsEnemy()) {
                consume();
            }
        }
        public void interactWith(BossTurret turret, boolean isCellInteraction) {
            if(wantsViewInteraction()&&!isConsumed() && !getIsEnemy()) {
                consume();
                turret.decreaseHp(getDamage());
            }
            else if(wantsViewInteraction()&&!isConsumed() && getIsEnemy()) {
                consume();
            }
        }
        public void interactWith(ICRoguePlayer player, boolean isCellInteraction) {
            if (wantsViewInteraction() && !(isConsumed()) && getIsEnemy()) {
                player.decreaseHp((float) getDamage());
                consume();
            }
        }
        public void interactWith(Wither wither, boolean isCellInteraction) {
            if (wantsViewInteraction() &&!(isConsumed()) && !getIsEnemy()) {
                consume();
                wither.decreaseHp(getDamage());
            }
        }
        public void interactWith(Beanos beanos, boolean isCellInteraction) {
            if (wantsViewInteraction() &&!(isConsumed()) && !getIsEnemy()) {
                consume();
                beanos.decreaseHp(getDamage());
            }
        }

    }

}