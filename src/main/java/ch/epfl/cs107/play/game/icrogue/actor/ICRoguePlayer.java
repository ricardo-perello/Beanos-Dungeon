package ch.epfl.cs107.play.game.icrogue.actor;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ICRoguePlayer extends ICRogueActor implements Interactor {
    private Sprite sprite;
    private boolean hasStaff;
    private ICRoguePlayerInteractionHandler handler;
    private String spriteName = "zelda/player";
    private ArrayList<Item> carrying = new ArrayList<>();
    /// Animation duration in frame number
    private final static int MOVE_DURATION = 8;

    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates){
        super(owner,orientation,coordinates);
        hasStaff =false;

        //setting sprites based on orientation
        if(orientation.equals(Orientation.DOWN)){
            sprite=new Sprite("zelda/player", .75f,1.5f,this,
                    new RegionOfInterest(0,0,16,32), new Vector(.15f,-.15f));
        }
        else if(orientation.equals(Orientation.RIGHT)){
            sprite=new Sprite("zelda/player", .75f,1.5f,this,
                    new RegionOfInterest(0,32,16,32), new Vector(.15f,-.15f));
        }
        else if(orientation.equals(Orientation.UP)){
            sprite=new Sprite("zelda/player", .75f,1.5f,this,
                    new RegionOfInterest(0,64,16,32), new Vector(.15f,-.15f));
        }
        else if(orientation.equals(Orientation.LEFT)){
            sprite=new Sprite("zelda/player", .75f,1.5f,this,
                    new RegionOfInterest(0,96,16,32), new Vector(.15f,-.15f));
        }
        handler= new ICRoguePlayerInteractionHandler();
        resetMotion();
    }


    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    private void moveIfPressed(Orientation orientation, Button b){

        if(b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);

                if (spriteName.equals("zelda/player")){
                    if(orientation.equals(Orientation.DOWN)){
                        sprite=new Sprite(spriteName, .75f,1.5f,this,
                                new RegionOfInterest(0,0,16,32), new Vector(.15f,-.15f));
                    }
                    else if(orientation.equals(Orientation.RIGHT)){
                        sprite=new Sprite(spriteName, .75f,1.5f,this,
                                new RegionOfInterest(0,32,16,32), new Vector(.15f,-.15f));
                    }
                    else if(orientation.equals(Orientation.UP)){
                        sprite=new Sprite(spriteName, .75f,1.5f,this,
                                new RegionOfInterest(0,64,16,32), new Vector(.15f,-.15f));
                    }
                    else if(orientation.equals(Orientation.LEFT)){
                        sprite=new Sprite(spriteName, .75f,1.5f,this,
                                new RegionOfInterest(0,96,16,32), new Vector(.15f,-.15f));
                    }
                }
                else if (spriteName.equals("zelda/player.staff_water")){
                    if(orientation.equals(Orientation.DOWN)){
                        sprite=new Sprite(spriteName, .75f,1.5f,this,
                                new RegionOfInterest(40,0,16,32), new Vector(.15f,-.15f));
                    }
                    else if(orientation.equals(Orientation.RIGHT)){
                        sprite=new Sprite(spriteName, 0.95f,1.5f,this,
                                new RegionOfInterest(42,64,19,32), new Vector(.15f,-.15f));
                    }
                    else if(orientation.equals(Orientation.UP)){
                        sprite=new Sprite(spriteName, .75f,1.5f,this,
                                new RegionOfInterest(40,32,16,32), new Vector(.15f,-.15f));
                    }
                    else if(orientation.equals(Orientation.LEFT)){
                        sprite=new Sprite(spriteName, 0.95f,1.5f,this,
                                new RegionOfInterest(34,96,19,32), new Vector(.15f,-.15f));
                    }
                }

                move(MOVE_DURATION);
            }
        }
    }


    public void update(float deltaTime) {
        Keyboard keyboard= getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        //fireball shoots from a cell in front of player

        if(keyboard.get(Keyboard.X).isPressed()&& hasStaff){
            Fire fire = new Fire(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates());
            fire.enterArea(getOwnerArea(),getCurrentMainCellCoordinates());
        }

        super.update(deltaTime);

    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler)v).interactWith(this, isCellInteraction);
    }

    /*returns the cell that the player occupies, im not sure about this since this method already exists
    * in ICRogueActor, so we might want to see if this is right or not*/
    public List<DiscreteCoordinates> getCurrentCells(){
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /*returns the cell that the player sees, AKA the cell in front of the direction he is facing*/
    public List<DiscreteCoordinates> getFieldOfViewCells(){
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    /*interracts with interactable if its in contact with it*/
    public boolean wantsCellInteraction() {
        return true;
    }

    /*interacts with intractable in its field of vision (aka in front of whatever direction he is facing)*/
    public boolean wantsViewInteraction() {
        Keyboard keyboard= getOwnerArea().getKeyboard();
        if(keyboard.get(Keyboard.W).isPressed()){  /*this means that the player only interacts with an object in front of him if you press W*/
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler,isCellInteraction);
    }

    private class ICRoguePlayerInteractionHandler implements ICRogueInteractionHandler{

        public void interactWith(Cherry cherry, boolean isCellInteraction) {
            if(wantsCellInteraction()){
                cherry.collect();
            }
        }
        public void interactWith(Staff staff, boolean isCellInteraction){
            if(wantsViewInteraction()){
                staff.collect();
                hasStaff = true;
                carrying.add(staff);
                spriteName = "zelda/player.staff_water";

                if(getOrientation().equals(Orientation.DOWN)){
                    sprite=new Sprite(spriteName, .75f,1.5f,sprite,
                            new RegionOfInterest(40,0,16,32), new Vector(.15f,-.15f));
                }
                else if(getOrientation().equals(Orientation.RIGHT)){
                    sprite=new Sprite(spriteName, 0.95f,1.5f,sprite,
                            new RegionOfInterest(42,64,19,32), new Vector(.15f,-.15f));
                }
                else if(getOrientation().equals(Orientation.UP)){
                    sprite=new Sprite(spriteName, .75f,1.5f,sprite,
                            new RegionOfInterest(40,32,16,32), new Vector(.15f,-.15f));
                }
                else if(getOrientation().equals(Orientation.LEFT)){
                    sprite=new Sprite(spriteName, 0.95f,1.5f,sprite,
                            new RegionOfInterest(34,96,19,32), new Vector(.15f,-.15f));
                }

            }
        }
    }


}
