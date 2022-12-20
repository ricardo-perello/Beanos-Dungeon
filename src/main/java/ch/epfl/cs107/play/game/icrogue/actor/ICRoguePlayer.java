package ch.epfl.cs107.play.game.icrogue.actor;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.BossTurret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.*;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.MainBase;
import ch.epfl.cs107.play.game.icrogue.area.Shop;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Positionable;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.game.areagame.actor.Animation.createAnimations;
import static ch.epfl.cs107.play.game.areagame.io.ResourcePath.getSprite;

public class ICRoguePlayer extends ICRogueActor implements Interactor {
    public static int DEFAULT_PLAYER_HP = 10;
    /// Animation duration in frame number
    private final static int MOVE_DURATION = 6;
    public final static int DEFAULT_MELEE_DAMAGE = 1;
    private static final int ANIMATION_DURATION = 2;
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
    public final static float COOLDOWN = 1.f;
    private float counter = 1.f;
    private Sprite[] spritesDOWN = new Sprite[4], spritesLEFT = new Sprite[4], spritesUP = new Sprite[4], spritesRIGHT = new Sprite[4];
    private final Animation animationsDOWN = new Animation(ANIMATION_DURATION/2, spritesDOWN);
    private final Animation animationsLEFT = new Animation(ANIMATION_DURATION/2, spritesLEFT);
    private final Animation animationsUP = new Animation(ANIMATION_DURATION/2, spritesUP);
    private final Animation animationsRIGHT = new Animation(ANIMATION_DURATION/2, spritesRIGHT);
    private boolean hasSoundFX;
    private SoundAcoustics soundFX;
    private boolean dialogueStart;
    private TextGraphics dialogue;
    private boolean stopForDialogue;

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
        setSpriteAnimation();


        handler= new ICRoguePlayerInteractionHandler();
        resetMotion();
    }


    public void draw(Canvas canvas) {

        if (!(getOwnerArea() instanceof MainBase)){
            printEmptyHearts().draw(canvas);
            if(hp>0){
                printFullHearts().draw(canvas);
            }

        }
        if(hp>0) {
            currentAnimation.draw(canvas);
        }

        if (isStaffAnimation) {

            currentAnimation.draw(canvas);
            if (currentAnimation.isCompleted()){
                isStaffAnimation = false;
                currentAnimation.reset();
            }
        }
        if(dialogueStart){
            if(getOwnerArea() instanceof MainBase){
                ((MainBase) getOwnerArea()).setParent(this);
                ((MainBase) getOwnerArea()).draw(canvas,dialogue);
            }
            else if(getOwnerArea() instanceof Shop){
                ((Shop) getOwnerArea()).draw(canvas,dialogue);
            }
            else if(getOwnerArea() instanceof ICRogueRoom){
                ((ICRogueRoom) getOwnerArea()).draw(canvas,dialogue);
            }


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

    private boolean moveIfPressed(Orientation orientation, Button b,float deltaTime) {
        if (b.isDown()&&!stopForDialogue) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);//change orientation
                move(MOVE_DURATION);//moves in that orientation
                setCurrentAnimation();
                currentAnimation.setSpeedFactor(4);
                currentAnimation.update(deltaTime);
            }
            return true;
        }
        return false;
    }


    public void update(float deltaTime) {
        boolean doStaffAnimation=false;
        counter += deltaTime;
        setSpriteAnimation();
        Keyboard keyboard= getOwnerArea().getKeyboard();
        setCurrentAnimation();
        boolean movedLeft=moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT),deltaTime);
        boolean movedUp=moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP),deltaTime);
        boolean movedRight=moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT),deltaTime);
        boolean movedDown=moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN),deltaTime);

        if(!movedLeft&&!movedUp&&!movedDown&&!movedRight){
            currentAnimation.reset();
        }

        //fireball shoots from a cell in front of player

        if(keyboard.get(Keyboard.X).isPressed()&& hasStaff && (counter >= COOLDOWN)){
            spriteName = "zelda/player.staff_water";
            isStaffAnimation = true;
            doStaffAnimation=true;
            soundFX=new SoundAcoustics(ResourcePath.getSound("fire"),0.3F,false,false,false,false);
            hasSoundFX=true;
            Fire fire = new Fire(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates());
            fire.enterArea(getOwnerArea(),getCurrentMainCellCoordinates());
            counter = 0;

        }
        if(doStaffAnimation){
            setStaffAnimation();
            setCurrentStaffAnimation();
            currentAnimation.setSpeedFactor(1);
            currentAnimation.update(deltaTime);
            if(currentAnimation.isCompleted()){
                doStaffAnimation=false;
                setSpriteAnimation();
            }
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
        if(dialogueStart&&stopForDialogue&&keyboard.get(Keyboard.W).isPressed()){
            dialogueStart=false;
            stopForDialogue=false;
        }

        super.update(deltaTime);

    }
    public void setCurrentAnimation(){
        if(getOrientation().equals(Orientation.DOWN)){
            currentAnimation = animationsDOWN;
        } else if (getOrientation().equals(Orientation.UP)) {
            currentAnimation = animationsUP;
        }else if (getOrientation().equals(Orientation.LEFT)) {
            currentAnimation = animationsLEFT;
        }else{
            currentAnimation = animationsRIGHT;
        }
    }

    private void setCurrentStaffAnimation() {
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
        soundFX=new SoundAcoustics(ResourcePath.getSound("damage"),1,false,false,false,false);
        hasSoundFX=true;
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
        return (!stopForDialogue&&keyboard.get(Keyboard.W).isPressed()) || (keyboard.get(Keyboard.Z).isPressed());
    }

    public void transitioned(){
        isTransitioning=false;
    }

    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }


    private Vector anchor = new Vector(0.15f, -0.15f);
    public void setSpriteAnimation(){
            for(int i = 0; i<4; i++){

                spritesDOWN[i] = new Sprite("zelda/player", 0.75f, 1.5f,
                        this, new RegionOfInterest(i*16, 0, 16, 32), anchor);
                spritesLEFT[i] = new Sprite("zelda/player", 0.75f, 1.5f,
                        this, new RegionOfInterest(i*16, 96, 16, 32), anchor);
                spritesUP[i] = new Sprite("zelda/player", 0.75f, 1.5f,
                        this, new RegionOfInterest(i*16, 64, 16, 32), anchor);
                spritesRIGHT[i] = new Sprite("zelda/player", 0.75f, 1.5f,
                        this, new RegionOfInterest(i*16, 32, 16, 32), anchor);
        }
    }

    //ANIMATIONS
    //todo fix animations for staff
    //todo add animations for sword
    public void setStaffAnimation() {
        String name = "zelda/player.staff_water";

        spritesDOWN[0] = new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(10 , 0, 16, 32)
                , new Vector(0.25f ,-0.15f));
        spritesDOWN[1] =new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(40,  0, 16, 32)
                , new Vector(0.2f, -0.15f));
        spritesDOWN[2] =new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(72,  0, 16, 32)
                , new Vector(0.12f, -0.15f));
        spritesDOWN[3]=new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(100, 0, 16, 32)
                , new Vector(-0.0f, -0.15f));

        spritesLEFT[0] = new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(0 , 96, 25, 32)
                , new Vector(0.1f ,-0.15f));
        spritesLEFT[1] =new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(32, 96, 25, 32)
                , new Vector(0.05f, -0.15f));
        spritesLEFT[2] =new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(64, 96, 25, 32)
                , new Vector(.05f, -0.15f));
        spritesLEFT[3]=new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(90, 96, 24, 32)
                , new Vector(-.25f, -0.15f));

        spritesUP[0] = new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(10, 32, 16, 32)
                , new Vector(0.25f ,-0.15f));
        spritesUP[1] =new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(40, 32, 16, 32)
                , new Vector(0.2f, -0.15f));
        spritesUP[2] =new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(72, 32, 16, 32)
                , new Vector(0.12f, -0.15f));
        spritesUP[3]=new Sprite(name, 0.75f, 1.5f, this, new RegionOfInterest(100,32, 16, 32)
                , new Vector(-0.0f, -0.15f));

        spritesRIGHT[0] = new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(10, 64, 25, 32)
                , new Vector(0.15f ,-0.15f));
        spritesRIGHT[1] =new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(40,64, 25, 32)
                , new Vector(0.1f, -0.15f));
        spritesRIGHT[2] =new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(72,64, 25, 32)
                , new Vector(0.1f, -0.15f));
        spritesRIGHT[3]=new Sprite(name, 1.25f, 1.5f, this, new RegionOfInterest(96,64, 24, 32)
                , new Vector(-0.15f, -0.15f));


        // on présume:   private final static int ANIMATION_DURATION = 8;
        staffAnimationsDOWN = new Animation(ANIMATION_DURATION *8 , spritesDOWN);
        staffAnimationsLEFT = new Animation(ANIMATION_DURATION *8 , spritesLEFT);
        staffAnimationsUP = new Animation(ANIMATION_DURATION *8, spritesUP);
        staffAnimationsRIGHT = new Animation(ANIMATION_DURATION*8, spritesRIGHT);
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

    public boolean HasSoundFX(){
        return hasSoundFX;
    }

    public void playSound(Window window){
        soundFX.shouldBeStarted();
        soundFX.bip(window);
        hasSoundFX=false;
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
                soundFX=new SoundAcoustics(ResourcePath.getSound("health"),1,false,false,false,false);
                hasSoundFX=true;
                cherry.collect();
            }
        }
        public void interactWith(Staff staff, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())){
                staff.collect();
                soundFX=new SoundAcoustics(ResourcePath.getSound("item"),1,false,false,false,false);
                hasSoundFX=true;
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
                soundFX=new SoundAcoustics(ResourcePath.getSound("item"),1,false,false,false,false);
                hasSoundFX=true;
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
                soundFX=new SoundAcoustics(ResourcePath.getSound("item"),1,false,false,false,false);
                hasSoundFX=true;
                hasBow = true;
                carrying.add(bow);
                spriteName = "zelda/player.bow";
                staffSprite(getOrientation());
            }
        }
        public void interactWith(Key key, boolean isCellInteraction){
            if(isCellInteraction){
                key.collect();
                soundFX=new SoundAcoustics(ResourcePath.getSound("item"),1,false,false,false,false);
                hasSoundFX=true;
                carrying.add(key); /*adds key to carrying arraylist which represents the items the character is holding*/
            }
        }
        public void interactWith(Turret turret, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if (wantsViewInteraction() && keyboard.get(Keyboard.Z).isPressed()){
                soundFX=new SoundAcoustics(ResourcePath.getSound("melee"),1,false,false,false,false);
                hasSoundFX=true;
                turret.decreaseHp(meleeDamage);
            }
        }
        public void interactWith(BossTurret turret, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if (wantsViewInteraction() && keyboard.get(Keyboard.Z).isPressed()){
                soundFX=new SoundAcoustics(ResourcePath.getSound("melee"),1,false,false,false,false);
                hasSoundFX=true;
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
                            unlock=true;
                            soundFX=new SoundAcoustics(ResourcePath.getSound("unlock"),1,false,false,false,false);
                            hasSoundFX=true;
                        }
                    }
                if(!unlock){
                    soundFX=new SoundAcoustics(ResourcePath.getSound("locked"), 0.3F,false,false,false,false);
                    hasSoundFX=true;
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
                    (portal.compareState(Portal.PortalState.OPEN)||portal.compareState(Portal.PortalState.SHOP))){
                transportArea=portal.getLevel();
                isTransporting=true;
            }
            else{
                isTransporting=false;
            }
        }

        public void interactWith(NPC npc, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(!dialogueStart&&wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())){
                dialogue=npc.getDialogue();
                dialogueStart=true;
                stopForDialogue=true;
            }

        }
        public void interactWith(Tota tota, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(!dialogueStart&&wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())){
                dialogue=tota.getDialogue();
                dialogueStart=true;
                stopForDialogue=true;
            }

        }
    }
}
