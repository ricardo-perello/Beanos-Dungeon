package ch.epfl.cs107.play.game.icrogue.actor;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Connector extends AreaEntity {
    public enum ConnectorState{
        OPEN,
        CLOSED,
        LOCKED,
        INVISIBLE,

    }
    private String areaTitle;
    private DiscreteCoordinates arrivalcoordinates;
    private Orientation orientation;
    private int ID;
    private Sprite sprite;
    public static final int NO_KEY_ID=0; /*when no key is required to open the door, this will be its identifier*/
    private ConnectorState state;

    public Connector(Area area, Orientation orientation, DiscreteCoordinates position){
        super(area,orientation,position);
        areaTitle=area.getTitle();
        state=ConnectorState.INVISIBLE;
        ID=0;
        this.orientation=orientation;
        setSprite();
    }
    public void draw(Canvas canvas) {
        if(!getState().equals(ConnectorState.OPEN)){
            sprite.draw(canvas);
        }
    }

    public ConnectorState getState(){
        return state;
    }

    public void setState(ConnectorState state){
        this.state=state;
        setSprite();

    }
    private void setSprite(){
        if(state.equals(ConnectorState.INVISIBLE)){
            sprite=new Sprite("icrogue/invisibleDoor_"+orientation.ordinal(),
                    (orientation.ordinal()+1)%2+1,orientation.ordinal()%2+1,this);
        }
        else if(state.equals(ConnectorState.CLOSED)){
            sprite=new Sprite("icrogue/door_"+orientation.ordinal(),
                    (orientation.ordinal()+1)%2+1,orientation.ordinal()%2+1,this);
        }
        else if(state.equals(ConnectorState.LOCKED)){
            sprite=new Sprite("icrogue/lockedDoor_"+orientation.ordinal(),
                    (orientation.ordinal()+1)%2+1,orientation.ordinal()%2+1,this);
        }
    }

    public void setID(int id){
        ID=id;
    }

    public List<DiscreteCoordinates> getCurrentCells() {
        DiscreteCoordinates coord= getCurrentMainCellCoordinates();
        return List.of(coord,coord.jump(new Vector((getOrientation().ordinal()+1)%2,
                getOrientation().ordinal()%2)));
    }

    public boolean takeCellSpace() {
        if(state.equals(ConnectorState.OPEN)){
            return false;
        }
        else{
            return true;
        }

    }

    public boolean isCellInteractable() {
        return true;
    }

    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler)v).interactWith(this, isCellInteraction);
    }
}

