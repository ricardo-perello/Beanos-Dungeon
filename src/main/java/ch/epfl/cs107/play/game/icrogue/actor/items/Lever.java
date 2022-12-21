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
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Lever extends Item{

    private Sprite sprite;
    private boolean wasInteractedWith;

    public Lever(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        setSprite();
        wasInteractedWith=false;
    }

    @Override
    public void setSprite() {
            sprite = new Sprite("lever", .7f, .7f, this, new RegionOfInterest(210,150,250,200));
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    public void interactWith(){
        sprite=new Sprite("lever", .7f, .7f, this, new RegionOfInterest(210,400,250,200));
        wasInteractedWith=true;
    }
    @Override
    public boolean takeCellSpace(){
        return true;
    }
    @Override
    public boolean isViewInteractable(){
        return true;
    }
    public boolean wasInteracted(){
        return wasInteractedWith;
    }
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler)v).interactWith(this, isCellInteraction);
    }
}
