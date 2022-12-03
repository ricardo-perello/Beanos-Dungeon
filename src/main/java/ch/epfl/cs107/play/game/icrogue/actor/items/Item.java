package ch.epfl.cs107.play.game.icrogue.actor.items;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Item extends CollectableAreaEntity {

    public Item(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    public boolean takeCellSpace(){
        return false;
    }
    public abstract void setSprite();

    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if(!isCollected()){
            ((ICRogueInteractionHandler)v).interactWith(this, isCellInteraction);
        }

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    public abstract void draw(Canvas canvas);

    public boolean isCellInteractable(){
        return true;
    }
    public boolean isViewInteractable(){
        return false;
    }
}
