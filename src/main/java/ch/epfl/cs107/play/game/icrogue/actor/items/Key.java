package ch.epfl.cs107.play.game.icrogue.actor.items;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class Key extends Item {
    private Sprite sprite;
    private int identificator;
    public Key(Area area, Orientation orientation, DiscreteCoordinates coordinates, int id) {
        super(area, orientation, coordinates);
        setSprite();
        identificator=id;
    }

    public void setSprite() {
        sprite = new Sprite("zelda/key", 0.6f, 0.6f, this);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isCollected()) {
            sprite.draw(canvas);
        }
    }
    public int getIdentificator(){
        return identificator;
    }
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (!isCollected()) {
            ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
        }

    }
}
