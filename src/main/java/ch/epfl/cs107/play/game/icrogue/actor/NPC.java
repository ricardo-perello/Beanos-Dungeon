package ch.epfl.cs107.play.game.icrogue.actor;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class NPC extends ICRogueActor {
    private Sprite sprite;
    /**
     * Default DemoActor constructor
     * @param position (Vector): initial position vector of the ghost
     * @param text (String): initial text moving with the ghost
     */
    public NPC(Area area, DiscreteCoordinates position, String spriteName) {
        //super(position, new ImageGraphics(ResourcePath.getSprite(spriteName),  1.0f,1.0f, null, Vector.ZERO, 1.0f, -Float.MAX_VALUE));
        super(area, Orientation.DOWN,position);
        sprite = new Sprite(spriteName, .75f,1f,this,
                new RegionOfInterest(0,0,16,21), new Vector(.15f,-.15f));;
    }
    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler)v).interactWith(this, isCellInteraction);
    }

    public boolean takeCellSpace() {
        return true;
    }
}
