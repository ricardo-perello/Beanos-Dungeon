package ch.epfl.cs107.play.game.icrogue.actor.items;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Sword extends Item{
    private Sprite sprite;
    public static int DEFAULT_DAMAGE = 2;
    public Sword(Area area,Orientation orientation, DiscreteCoordinates coordinates){
        super(area,orientation,coordinates);
        setSprite();
    }

    public void setSprite(){
        sprite=new Sprite("zelda/sword.icon",.5f,.5f,this,new RegionOfInterest(0,0,16,16),new Vector(0.25f,0.25f));
    }

    public float getMeleeDamage(){
        return DEFAULT_DAMAGE;
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
