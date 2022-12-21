package ch.epfl.cs107.play.game.icrogue.actor;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Portal extends AreaEntity {
    public enum PortalState{
        OPEN,
        LOCKED,
        INVISIBLE,
        SHOP;

    }
    private String areaTitle;

    private String levelname;

    private DiscreteCoordinates arrivalcoordinates;
    private Orientation orientation;
    private int ID;
    private Sprite sprite;
    public static final int NO_KEY_ID=0; /*when no key is required to open the door, this will be its identifier*/
    private PortalState state;
    private int count;
    private ImageGraphics RedDots;
    private ImageGraphics GreenDots;

    public ImageGraphics printGreenDots(int count){
        GreenDots = new ImageGraphics("images/sprites/zelda/3.green.dots.png", ((1.5f) * ((float)count/3)) ,.5f,
                new RegionOfInterest(0,32,(int)(96 * ((double)count/3)),32), new Vector(this.getPosition().x+0.25f, this.getPosition().y-0.5f));
        this.count=count;
        return GreenDots;
    }
    private ImageGraphics printRedDots() {
        RedDots = new ImageGraphics("images/sprites/zelda/3.red.dots.png", ((1.5f)), .5f,
                new RegionOfInterest(0, 64, 96, 32), new Vector(this.getPosition().x+0.25f, this.getPosition().y-0.5f));
        return RedDots;
    }

    public Portal(Area area, Orientation orientation, DiscreteCoordinates position, String level){
        super(area,orientation,position);
        state= PortalState.OPEN;
        ID=0;
        this.orientation=orientation;
        levelname=level;
        setSprite();
    }
    public Portal(Area area, Orientation orientation, DiscreteCoordinates position, String level,PortalState state){
        super(area,orientation,position);
        this.state= state;
        ID=0;
        this.orientation=orientation;
        levelname=level;
        setSprite();
    }
    public void draw(Canvas canvas) {
        if(!state.equals(PortalState.SHOP)){
            sprite.draw(canvas);
            if(!levelname.equals("shop")&&!levelname.equals("level0")){
                printRedDots().draw(canvas);
                if(count>0){
                    printGreenDots(count).draw(canvas);
                }
            }

        }

    }


    public void setArrivalcoordinates(DiscreteCoordinates coordinates){
        arrivalcoordinates=coordinates;
    }

    public void setAreaTitle(String title){
        areaTitle=title;
    }

    public boolean compareState(PortalState state){

        return this.state.equals(state);
    }

    public String getLevel(){
        return levelname;
    }

    public DiscreteCoordinates getArrivalcoordinates(){
        return arrivalcoordinates;
    }

    public void setState(PortalState state){
        this.state=state;
        setSprite();
    }

    private void setSprite(){
        if(state.equals(PortalState.OPEN)||state.equals(PortalState.SHOP)){
            sprite=new Sprite("icrogue/door_2",
                    (orientation.ordinal()+1)%2+1,orientation.ordinal()%2+1,this);
        }
        else if(state.equals(PortalState.LOCKED)){
            sprite=new Sprite("icrogue/lockedDoor_2",
                    (orientation.ordinal()+1)%2+1,orientation.ordinal()%2+1,this);
        }
    }

    public void setID(int id){
        ID=id;
    }
    public int getID(){
        return ID;
    }

    public String getAreaTitle(){
        return areaTitle;
    }

    public List<DiscreteCoordinates> getCurrentCells() {
        DiscreteCoordinates coord= getCurrentMainCellCoordinates();
        return List.of(coord,coord.jump(new Vector((getOrientation().ordinal()+1)%2,
                getOrientation().ordinal()%2)));
    }

    public boolean takeCellSpace() {
            return !(state.equals(PortalState.SHOP));
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
