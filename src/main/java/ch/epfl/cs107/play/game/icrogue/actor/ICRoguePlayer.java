package ch.epfl.cs107.play.game.icrogue.actor;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.*;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Positionable;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.game.areagame.actor.Animation.createAnimations;
import static ch.epfl.cs107.play.game.areagame.io.ResourcePath.getSprite;

public class ICRoguePlayer extends ICRogueActor implements Interactor {
    public static int DEFAULT_PLAYER_HP = 10;
    /// Animation duration in frame number
    private final static int MOVE_DURATION = 8;
    public final static int DEFAULT_MELEE_DAMAGE = 1;
    private static final int ANIMATION_DURATION = 8;
    private Sprite sprite;
    private ImageGraphics fullHearts;
    private ImageGraphics emptyHearts;
    private TextGraphics message;
    private Animation currentAnimation;
    private Animation staffAnimationsDOWN,
            staffAnimationsLEFT,
            staffAnimationsUP,
            staffAnimationsRIGHT;
    private boolean isStaffAnimation;

    private boolean hasStaff;
    private boolean hasSword;
    private boolean hasBow;
    private boolean isTransitioning;
    private boolean isTransporting;
    private String transitionArea;
    private String transportArea;
    private DiscreteCoordinates coordinatesTransition;
    private ICRoguePlayerInteractionHandler handler;
    private String spriteName = "zelda/player";
    private ArrayList<Item> carrying = new ArrayList<>();
    private int hp = DEFAULT_PLAYER_HP;
    private boolean receivedDamage = false;
    private int meleeDamage;


    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates){
        super(owner,orientation,coordinates);

        printEmptyHearts();
        printFullHearts();
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


        setStaffAnimation();


        handler= new ICRoguePlayerInteractionHandler();
        resetMotion();
    }


    public void draw(Canvas canvas) {
        printEmptyHearts().draw(canvas);
        if (getHp()>0){
            printFullHearts().draw(canvas);
        }
        if (isStaffAnimation) {

            currentAnimation.draw(canvas);
            if (currentAnimation.isCompleted()){
                isStaffAnimation = false;
                currentAnimation.reset();
            }
        }
        else{
            sprite.draw(canvas);
        }
    }

    public String getTransitionArea(){
        return transitionArea;
    }

    public String getTransportArea(){
        return transportArea;
    }

    public boolean getisTransitioning(){
        return isTransitioning;
    }

    public boolean getisTransporting(){
        return isTransporting;
    }

    public void transported(){
        isTransporting=false;
    }

    public void strengthen() {
        hp = DEFAULT_PLAYER_HP;
    }

    public DiscreteCoordinates getCoordinatesTransition(){
        return coordinatesTransition;
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
        setCurrentAnimation();
        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        //fireball shoots from a cell in front of player

        if(keyboard.get(Keyboard.X).isPressed()&& hasStaff){
            spriteName = "zelda/player.staff_water";
            isStaffAnimation = true;

            Fire fire = new Fire(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates());
            fire.enterArea(getOwnerArea(),getCurrentMainCellCoordinates());

        }

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

    private void setCurrentAnimation() {
        if (spriteName.equals("zelda/player.staff_water")){
            if (getOrientation().equals(Orientation.DOWN)){
                currentAnimation = staffAnimationsDOWN;
            }
            else if(getOrientation().equals(Orientation.LEFT)){
                currentAnimation = staffAnimationsLEFT;
            }
            else if(getOrientation().equals(Orientation.UP)){
                currentAnimation = staffAnimationsUP;
            }
            else if(getOrientation().equals(Orientation.RIGHT)){
                currentAnimation = staffAnimationsRIGHT;
            }
        }

    }


    public void setHp(int hp){
        this.hp = hp;
    }

    public int getHp(){
        return hp;
    }

    private ImageGraphics printFullHearts(){
        fullHearts = new ImageGraphics("images/sprites/zelda/5.full.red.hearts.png", ((2.5f) * ((float)getHp()/10)) ,.5f,
                new RegionOfInterest(0,0,(int)(80 * ((double)getHp()/10)),16), new Vector(0.5f,.25f));
        return fullHearts;
    }
    private ImageGraphics printEmptyHearts(){
        emptyHearts = new ImageGraphics("images/sprites/zelda/5.empty.red.hearts.png", 2.5f,.5f,
                new RegionOfInterest(0,0,80,16), new Vector(0.5f,.25f));
        return emptyHearts;
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

    public void clearCarrying(){
        carrying.clear();
        hasStaff=false;
        hasBow=false;
        hasSword=false;
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

    public void transitioned(){
        isTransitioning=false;
    }

    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    //ANIMATIONS
    //todo add music
    //todo add animations for walking
    //todo add animations for sword
    //todo fix staff animation for right
    public void setStaffAnimation() {
        String name = "zelda/player.staff_water";
        Sprite[] spritesDOWN = {
                (new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(10 , 0, 16, 32)
                        , new Vector(0.25f ,-0.15f))),
                (new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(40,  0, 16, 32)
                        , new Vector(0.2f, -0.15f))),
                (new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(72,  0, 16, 32)
                        , new Vector(0.12f, -0.15f))),
                (new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(100, 0, 16, 32)
                        , new Vector(-0.0f, -0.15f)))};

        Sprite[] spritesLEFT = {
                (new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(0 , 96, 25, 32)
                        , new Vector(0.1f ,-0.15f))),
                (new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(32, 96, 25, 32)
                        , new Vector(0.05f, -0.15f))),
                (new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(64, 96, 25, 32)
                        , new Vector(.05f, -0.15f))),
                (new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(90, 96, 24, 32)
                        , new Vector(-.25f, -0.15f)))};

        Sprite[] spritesUP = {
                (new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(10, 32, 16, 32)
                        , new Vector(0.25f ,-0.15f))),
                (new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(40, 32, 16, 32)
                        , new Vector(0.2f, -0.15f))),
                (new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(72, 32, 16, 32)
                        , new Vector(0.12f, -0.15f))),
                (new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(100,32, 16, 32)
                        , new Vector(-0.0f, -0.15f)))};

        Sprite[] spritesRIGHT = {
                (new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(10, 64, 25, 32)
                        , new Vector(0.15f ,-0.15f))),
                (new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(40,64, 25, 32)
                        , new Vector(0.1f, -0.15f))),
                (new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(72,64, 25, 32)
                        , new Vector(0.1f, -0.15f))),
                (new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(96,64, 24, 32)
                        , new Vector(-0.15f, -0.15f)))};


        // on présume:   private final static int ANIMATION_DURATION = 8;
        staffAnimationsDOWN = new Animation(ANIMATION_DURATION *2 , spritesDOWN);
        staffAnimationsLEFT = new Animation(ANIMATION_DURATION *2 , spritesLEFT);
        staffAnimationsUP = new Animation(ANIMATION_DURATION *2, spritesUP);
        staffAnimationsRIGHT = new Animation(ANIMATION_DURATION*2, spritesRIGHT);
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
                    new RegionOfInterest(42,64,19,32), new Vector(.2f,-.15f));
        }
        else if(orientation.equals(Orientation.UP)){
            sprite=new Sprite(spriteName, .75f,1.5f,this,
                    new RegionOfInterest(40,32,16,32), new Vector(.15f,-.15f));
        }
        else if(orientation.equals(Orientation.LEFT)){
            sprite=new Sprite(spriteName, 0.95f,1.5f,this,
                    new RegionOfInterest(35,96,19,32), new Vector(.2f,-.15f));
        }
    }

    public void bowSprite(Orientation orientation){
        if(orientation.equals(Orientation.DOWN)){
            sprite=new Sprite(spriteName, .75f,1.5f,this,
                    new RegionOfInterest(40,0,16,32), new Vector(.15f,-.15f));
        }
        else if(orientation.equals(Orientation.RIGHT)){
            sprite=new Sprite(spriteName, 0.95f,1.5f,this,
                    new RegionOfInterest(42,64,19,32), new Vector(.25f,-.15f));
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
            if(isCellInteraction){
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
        public void interactWith(Connector connector, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(wantsViewInteraction()&& (keyboard.get(Keyboard.W).isPressed())){
                boolean unlock=false;
                if(connector.compareState(Connector.ConnectorState.LOCKED)){
                    for(Item item:carrying){
                        if(item instanceof Key&&((Key) item).getIdentificator()==connector.getID()){
                                connector.setState(Connector.ConnectorState.OPEN);
                            }
                        }
                    }
                }
            if(isCellInteraction&&connector.compareState(Connector.ConnectorState.OPEN)){
                coordinatesTransition=connector.getArrivalcoordinates();
                transitionArea=connector.getAreaTitle();
                isTransitioning=true;

            }
        }

        public void interactWith(Portal portal, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())&&
                    portal.compareState(Portal.PortalState.OPEN)){
                transportArea=portal.getLevel();
                isTransporting=true;
            }
            else{
                isTransporting=false;
            }
        }
    }
}
