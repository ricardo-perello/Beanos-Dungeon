package ch.epfl.cs107.play.game.icrogue.actor;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public abstract class ICRogueActor extends MovableAreaEntity {
    public ICRogueActor(Area owner, Orientation orientation, DiscreteCoordinates coordinates){
        super(owner,orientation,coordinates);
    }
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    @Override
    public boolean takeCellSpace() {
        return false;
    } /*the base actor does not occupy space*/

    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    public void enterArea(Area area, DiscreteCoordinates position){
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());//todo fix when you beat level 0 there is a null pointer exception because position is null when you switch area.
        resetMotion();
    }
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override /*by default, it cannot be interacted with from far away*/
    public boolean isViewInteractable() {
        return false;
    }
}
