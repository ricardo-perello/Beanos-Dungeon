package ch.epfl.cs107.play.game.icrogue.actor.items;
/*
 *  Author:  Ricardo Perelló Mas
 *  Date: 4-12-22
 */

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bow extends Item{
    private Sprite sprite;
    public Bow(Area area,Orientation orientation, DiscreteCoordinates coordinates){
        super(area,orientation,coordinates);
        setSprite();
    }

    public void setSprite(){
        sprite=new Sprite("zelda/bow.icon",.5f,.5f,this);
    }


    @Override
    public void draw(Canvas canvas) {
        if(!isCollected()){
            sprite.draw(canvas);
        }
    }

    public boolean takeCellSpace(){
        return true;
    }

    public boolean isViewInteractable(){
        return true;
    }

    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if(!isCollected()){
            ((ICRogueInteractionHandler)v).interactWith(this, isCellInteraction);
        }
    }
}
