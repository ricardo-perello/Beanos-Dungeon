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
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class Cherry extends Item{
    private Sprite sprite;
    public Cherry(Area area,Orientation orientation, DiscreteCoordinates coordinates){
        super(area,orientation,coordinates);
        setSprite();
    }

    public void setSprite(){
        sprite=new Sprite("icrogue/cherry",0.6f,0.6f,this);
    }

    @Override
    public void draw(Canvas canvas) {
        if(!isCollected()){
            sprite.draw(canvas);
        }
    }
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if(!isCollected()){
            ((ICRogueInteractionHandler)v).interactWith(this, isCellInteraction);
        }

    }
}
