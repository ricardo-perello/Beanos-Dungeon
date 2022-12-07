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
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.*;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Melee;
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
    public final static float DEFAULT_PLAYER_HP = 10;
    /// Animation duration in frame number
    private final static int MOVE_DURATION = 8;
    public final static float DEFAULT_MELEE_DAMAGE = 1;
    private Sprite sprite;
    private boolean hasStaff;
    private boolean hasSword;
    private boolean hasBow;
    private ICRoguePlayerInteractionHandler handler;
    private String spriteName = "zelda/player";
    private ArrayList<Item> carrying = new ArrayList<>();
    private float hp = DEFAULT_PLAYER_HP;
    private boolean receivedDamage = false;
    private float meleeDamage;


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
                    defaultSprite(orientation);
                }
                else if (spriteName.equals("zelda/player.staff_water")){
                    staffSprite(orientation);
                }
                else if (spriteName.equals("zelda/player.sword")){
                    swordSprite(orientation);
                }
                else if (spriteName.equals("zelda/player.bow")){
                    bowSprite(orientation);
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
            spriteName = "zelda/player.staff_water";
            staffSprite(getOrientation());
            Fire fire = new Fire(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates());
            fire.enterArea(getOwnerArea(),getCurrentMainCellCoordinates());

        }
        //todo fix melee damage method
        if(keyboard.get(Keyboard.Z).isPressed()) {
            if (hasSword) {
                spriteName = "zelda/player.sword";
                swordSprite(getOrientation());
            } else {
                spriteName = "zelda/player";
                defaultSprite(getOrientation());
            }

        }

        if(keyboard.get(Keyboard.C).isPressed()&& hasBow){
            spriteName = "zelda/player.bow";
            bowSprite(getOrientation());
            Arrow arrow = new Arrow(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates());
            arrow.enterArea(getOwnerArea(),getCurrentMainCellCoordinates());

        }
        if (hasSword){
            meleeDamage = Sword.DEFAULT_DAMAGE;
        }
        else{
            meleeDamage = DEFAULT_MELEE_DAMAGE;
        }
        super.update(deltaTime);

    }

    public void setHp(float hp){
        this.hp = hp;
    }

    public float getHp(){
        return hp;
    }

    public void decreaseHp(float delta){
        hp -= delta;
        System.out.println(hp);
    }

    public boolean canIncreaseHp(){
        return hp < DEFAULT_PLAYER_HP;
    }

    public void increaseHp(float delta){
        if (hp < 10){
            hp += delta;
        }

    }

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
        /*this means that the player only interacts with an object in front of him if you press W*/
        return (keyboard.get(Keyboard.W).isPressed()) || (keyboard.get(Keyboard.Z).isPressed());
    }

    public void defaultSprite(Orientation orientation){
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

    public void staffSprite(Orientation orientation){
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

    public void bowSprite(Orientation orientation){
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
                    new RegionOfInterest(34,96,19,32), new Vector(-.1f,-.15f));
        }
    }

    public void swordSprite(Orientation orientation){
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


/*
    public void shootFlameSprite(Orientation orientation){

        //todo change area region of interest for animation of shooting fireball
        if(orientation.equals(Orientation.DOWN)){
            sprite=new Sprite(spriteName, .75f,1.5f,this,
                    new RegionOfInterest(60,0,16,32), new Vector(.15f,-.15f));
        }
        else if(orientation.equals(Orientation.RIGHT)){
            sprite=new Sprite(spriteName, 0.95f,1.5f,this,
                    new RegionOfInterest(62,64,19,32), new Vector(.15f,-.15f));
        }
        else if(orientation.equals(Orientation.UP)){
            sprite=new Sprite(spriteName, .75f,1.5f,this,
                    new RegionOfInterest(60,32,16,32), new Vector(.15f,-.15f));
        }
        else if(orientation.equals(Orientation.LEFT)) {
            sprite = new Sprite(spriteName, 0.95f, 1.5f, this,
                    new RegionOfInterest(64, 96, 19, 32), new Vector(.15f, -.15f));
        }


    }

 */

    public boolean getReceivedDamage(){return receivedDamage;}

    public void setReceivedDamage(boolean rd){receivedDamage = rd;}

    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler,isCellInteraction);
    }

    private class ICRoguePlayerInteractionHandler implements ICRogueInteractionHandler{

        public void interactWith(Cherry cherry, boolean isCellInteraction) {
            if(wantsCellInteraction()&&canIncreaseHp()){
                increaseHp(1);
                cherry.collect();
            }
        }
        public void interactWith(Staff staff, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())){
                staff.collect();
                hasStaff = true;
                carrying.add(staff);
                spriteName = "zelda/player.staff_water";
                staffSprite(getOrientation());
            }
        }
        public void interactWith(Sword sword, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())){
                sword.collect();
                hasSword = true;
                carrying.add(sword);
                spriteName = "zelda/player.sword";
                swordSprite(getOrientation());
            }
        }
        public void interactWith(Bow bow, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())){
                bow.collect();
                hasBow = true;
                carrying.add(bow);
                spriteName = "zelda/player.bow";
                staffSprite(getOrientation());
            }
        }

        public void interactWith(Key key, boolean isCellInteraction){
            if(wantsCellInteraction()){
                key.collect();
                carrying.add(key); /*adds key to carrying arraylist which represents the items the character is holding*/
            }
        }

        public void interactWith(Turret turret, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if (wantsViewInteraction() && keyboard.get(Keyboard.Z).isPressed()){
                turret.decreaseHp(meleeDamage);
            }
        }
    }


}
