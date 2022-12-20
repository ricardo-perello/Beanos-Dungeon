package ch.epfl.cs107.play.game.icrogue.actor.items;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Coin extends Item {
    private Sprite sprite;
    public Coin(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);
        setSprite();
    }

    public void setSprite() {
        sprite = new Sprite("zelda/coin", 0.6f, 0.6f, this,
                new RegionOfInterest(0,0,16,16), new Vector(0,0));
    }
//todo add animation for coin
    @Override
    public void draw(Canvas canvas) {
        if (!isCollected()) {
            sprite.draw(canvas);
        }
    }
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (!isCollected()) {
            ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
        }

    }
}
