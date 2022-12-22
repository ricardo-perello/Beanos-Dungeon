package ch.epfl.cs107.play.game.icrogue.actor;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.*;
import ch.epfl.cs107.play.game.icrogue.actor.items.*;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.MainBase;
import ch.epfl.cs107.play.game.icrogue.area.Shop;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.*;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.awt.*;
import java.awt.font.ImageGraphicAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.game.areagame.actor.Animation.createAnimations;
import static ch.epfl.cs107.play.game.areagame.io.ResourcePath.getSprite;

public class ICRoguePlayer extends ICRogueActor implements Interactor {
    public static int DEFAULT_PLAYER_HP = 6;
    /// Animation duration in frame number
    private final static int MOVE_DURATION = 6;
    public static int DEFAULT_MELEE_DAMAGE = 1;
    private static final int ANIMATION_DURATION = 2;
    private Sprite sprite;
    private ImageGraphics fullHearts;
    private ImageGraphics emptyHearts;
    private ImageGraphics CoinsDisplay;
    private TextGraphics CoinsNumber;
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
    private static int maxHp = DEFAULT_PLAYER_HP;
    private static int hp = maxHp;
    private boolean receivedDamage = false;
    private int meleeDamage;
    private float damageMultiplyer = 1;
    public final static float COOLDOWN = 1.f;
    private float counter = 1.f;
    private float dialogueCounter=2.1f;
    private static int CoinCounter = 5;
    private Sprite[] spritesDOWN = new Sprite[4], spritesLEFT = new Sprite[4], spritesUP = new Sprite[4], spritesRIGHT = new Sprite[4];
    private final Animation animationsDOWN = new Animation(ANIMATION_DURATION/2, spritesDOWN);
    private final Animation animationsLEFT = new Animation(ANIMATION_DURATION/2, spritesLEFT);
    private final Animation animationsUP = new Animation(ANIMATION_DURATION/2, spritesUP);
    private final Animation animationsRIGHT = new Animation(ANIMATION_DURATION/2, spritesRIGHT);
    private boolean hasSoundFX;
    private SoundAcoustics soundFX;
    private boolean dialogueStart;
    private List<TextGraphics> dialogue=new ArrayList<TextGraphics>();
    private boolean stopForDialogue;
    private boolean merchantInteraction;
    private boolean healthInteraction; //checks if interaction is with Tota
    private boolean damageInteraction; //checks if interaction is with Alejandro
    private boolean introductionInteraction;
    private boolean gameStartDialogue;
    private boolean gameEndDialogue;
    private int gameStartDialogueStep=1;
    private boolean isPoisoned;
    private float poisonCounter;

    /**
     * Resume method: constructor for player
     * @param owner area where player is being registered to, not null
     * @param orientation orientation of the player, not null
     * @param coordinates coordinates where the player will spanw, not null
     */
    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates){
        super(owner,orientation,coordinates);

            printEmptyHearts();
            printFullHearts();
            printCoinsDisplay();
            printCoinsNumber();

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

        //creates handler for player interactions
        handler= new ICRoguePlayerInteractionHandler();
        resetMotion();
    }

    /**
     * Resume method: initialised the dialogue displayed when game is started (the introduction)
     */
    public void startDialogue(){
        gameStartDialogue=true;
        stopForDialogue=true;
        gameStartDialogue();
        ++gameStartDialogueStep;
    }

    /**
     * Resume method: initialised the dialogue displayed when level beanos is cleared (the ending)
     */
    public void endDialogue(){

        gameEndDialogue=true;
        stopForDialogue=true;
        dialogue.clear();
        String key="Game_End1";
        String message = XMLTexts.getText(key);
        TextGraphics dialogue=new TextGraphics(message,0.46F, Color.BLACK,null, 0.0f, false, false, Vector.ZERO, TextAlign.Horizontal.LEFT, TextAlign.Vertical.BOTTOM, 1.0f, 0.0f);
        dialogue.setAnchor(new Vector(1.5f,2.3f));
        this.dialogue.add(dialogue);
        key="Game_End2";
        message = XMLTexts.getText(key);
        dialogue=new TextGraphics(message,0.46F, Color.BLACK,null, 0.0f, false, false, Vector.ZERO, TextAlign.Horizontal.LEFT, TextAlign.Vertical.BOTTOM, 1.0f, 0.0f);
        dialogue.setAnchor(new Vector(1.5f,1.9f));
        this.dialogue.add(dialogue);
        ((MainBase)getOwnerArea()).setFinalDialogueBox();
        setSoundFX("book",1);
        ((MainBase)getOwnerArea()).setDialogue(this,this.dialogue);

    }

    /**
     * Resume method: draws the player and its attributes based on certain conditions like the health and the instance of the current area
     * also draws the current dialogue
     * @param canvas where the sprite will be drawn, not null
     */
    public void draw(Canvas canvas) {

        //only draws hearts if player is in the middle of a level, not in the base or the shop
        if (!(getOwnerArea() instanceof MainBase)){
            printEmptyHearts().draw(canvas);
            printCoinsDisplay().draw(canvas);
            printCoinsNumber().draw(canvas);
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
        //prints the dialogue based on the current area (since that changes the position and size of the dialogue box
        if(dialogueStart){
            if(getOwnerArea() instanceof MainBase){
                //sets the player as the parent for the dialogue box present in Main base
                ((MainBase) getOwnerArea()).setParent(this);
                //sets the placement and size of the dialogue (text) based on the players position
                ((MainBase) getOwnerArea()).setDialogue(this,dialogue);
                //draws the dialogue box and the current player dialogue
                ((MainBase) getOwnerArea()).draw(canvas, dialogue);
            }
            else if(getOwnerArea() instanceof Shop){
                //draws the dialogue and the dialogue box for the shop
                ((Shop) getOwnerArea()).draw(canvas,dialogue);
            }
            else if(getOwnerArea() instanceof ICRogueRoom){
                //draws the dialogue and the dialogue box for ICRogueRoom
                ((ICRogueRoom) getOwnerArea()).draw(canvas,dialogue);
            }
        }
        if(gameStartDialogue){
            ((MainBase) getOwnerArea()).setParent(this);
            //sets the placement and size of the dialogue (text) based on the players position
            ((MainBase) getOwnerArea()).setDialogue(this,dialogue);
            //draws the dialogue box and the current player dialogue
            ((MainBase) getOwnerArea()).draw(canvas, dialogue);
        }
        if(gameEndDialogue){
            ((MainBase) getOwnerArea()).setParent(this);
            //sets the placement and size of the dialogue (text) based on the players position
            ((MainBase) getOwnerArea()).setDialogue(this,dialogue);
            //draws the dialogue box and the current player dialogue
            ((MainBase) getOwnerArea()).draw(canvas, dialogue);
        }
    }

    /**
     * Resume method: poisons player (used in interaction between player and poisonBall)
     */
    public void poison(){
        isPoisoned=true;
        poisonCounter=0;
    }

    /**
     * Resume method: returns the transition area of player, used to transfer between rooms during a level
     * @return String indicating the name of the room player should be transitioned to in a level
     */
    public String getTransitionArea(){
        return transitionArea;
    }

    /**
     * Resume method: returns the transport area of player, used to transfer between areas (MainBase, leve and shop) during the game (used in ICRogue)
     * @return String indicating the name of the area player should be transported to
     */
    public String getTransportArea(){
        return transportArea;
    }

    /**
     * Resume method: indicates whether player should transition from one room to the other in a level
     * @return boolean indicating whether player should change room or not in a level
     */
    public boolean getisTransitioning(){
        return isTransitioning;
    }

    /**
     * Resume method: works similarly to transitioning but for transporting between areas (Main Base, Shop and Level)
     * @return boolean indicating whether player should change areas or not
     */
    public boolean getisTransporting(){
        return isTransporting;
    }

    /**
     * Resume method: resets isTransporting to indicate the player already transported (should not transport again until he interacts with another portal
     */
    public void transported(){
        isTransporting=false;
    }

    /**
     * Resume method: resets player health
     */
    public void strengthen() {
        hp = maxHp;
    }

    /**
     * Resume method: returns the coordinates in which player should be added in new area
     * @return the coordination in whihc player will be added in new area
     */
    public DiscreteCoordinates getCoordinatesTransition(){
        return coordinatesTransition;
    }

    /**
     * Resume method: moves if dialogue is not occuring and player can enter the new cell
     * @param orientation current orientation of player, not null
     * @param b button that was being pressed when this method was called, not null
     * @param deltaTime time used for updating the animation, not null
     * @return boolean indicating whether player moved or not
     */
    private boolean moveIfPressed(Orientation orientation, Button b,float deltaTime) {
        //the stopForDialogue boolean indicates whether the player is in the middle of interacting through dialogue
        //the player should not move if it is in the middle of a dialogue
        if (b.isDown()&&!stopForDialogue) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);//change orientation
                move(MOVE_DURATION);//moves in that orientation
                setCurrentAnimation();//sets the animation to the walking animation
                currentAnimation.setSpeedFactor(4);
                currentAnimation.update(deltaTime);//updates the walking animation
            }
            return true;
        }
        return false;
    }

    private boolean isDoingAnimation=false;

    /**
     * Resume method: updates the player by checking its movement, dialogue and interactions (such as shooting fireballs and others)
     * @param deltaTime change in time used for updating the player, not null
     */
    public void update(float deltaTime) {
        poisonCounter+=deltaTime;
        boolean doStaffAnimation=false;
        counter += deltaTime;//increases counter for projectiles
        dialogueCounter+=deltaTime;//increases counter for dialogue
        setSpriteAnimation();
        Keyboard keyboard= getOwnerArea().getKeyboard();
        setCurrentAnimation();
        boolean movedLeft=moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT),deltaTime);
        boolean movedUp=moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP),deltaTime);
        boolean movedRight=moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT),deltaTime);
        boolean movedDown=moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN),deltaTime);

        if(!movedLeft&&!movedUp&&!movedDown&&!movedRight){
            currentAnimation.reset();//resets the animation if the player stops moving
        }

        //fireball shoots from a cell in front of player

        if(keyboard.get(Keyboard.X).isPressed()&& hasStaff && (counter >= COOLDOWN)){
            spriteName = "zelda/player.staff_water";
            isStaffAnimation = true;
            isDoingAnimation=false;
            doStaffAnimation=true;
            Fire fire = new Fire(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates(), "zelda/fire"
                    ,false);
            soundFX=new SoundAcoustics(ResourcePath.getSound("fire"),0.3F,false,false,false,false);
            hasSoundFX=true;
            fire.enterArea(getOwnerArea(),getCurrentMainCellCoordinates());
            counter = 0;

        }
        if(isPoisoned&&(poisonCounter>=1.0f)){
            decreaseHp(1);
            isPoisoned=false;
        }
        if(doStaffAnimation){
            if(!isDoingAnimation){
                setStaffAnimation();
                setCurrentStaffAnimation();
                currentAnimation.setSpeedFactor(1);
                isDoingAnimation=true;
            }
            currentAnimation.update(deltaTime);
            if(currentAnimation.isCompleted()){//&&counter<COOLDOWN){
                doStaffAnimation=false;
                setSpriteAnimation();
            }
        }


        if(keyboard.get(Keyboard.Z).isPressed()) {
            if (hasSword) {
                spriteName = "zelda/player.sword";

            } else {
                spriteName = "zelda/player";
                defaultSprite(getOrientation());
            }

        }

        if(keyboard.get(Keyboard.C).isPressed()&& hasBow && (counter >= COOLDOWN)){
            spriteName = "zelda/player.bow";
            Arrow arrow = new Arrow(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates(), false);
            arrow.enterArea(getOwnerArea(),getCurrentMainCellCoordinates());
            counter = 0;

        }
        if (hasSword){
            meleeDamage = Sword.DEFAULT_DAMAGE;
        }
        else{
            meleeDamage = DEFAULT_MELEE_DAMAGE;
        }
        //if the player is undergoing a dialogue and W is pressed the dialogue progresses (and ends if thats the case)
        if((dialogueStart&&stopForDialogue&&(keyboard.get(Keyboard.W).isPressed())||keyboard.get(Keyboard.X).isPressed())||dialogueCounter==1.f){
            dialogueStart=false;
            stopForDialogue=false;
            dialogueCounter=0;//resets the dialogue counter
            if(keyboard.get(Keyboard.X).isPressed()){
                healthInteraction=false;
                damageInteraction=false;
            }
            //this dialogue counter prevents the player from infinitelly restarting a dialogue since W is the same button used to start a dialogue
            if(merchantInteraction&&!introductionInteraction&&(keyboard.get(Keyboard.W).isPressed())){ //interaction with Tota and Alejandro class (the merchants)
                if(hp>=10&&healthInteraction){//if the health is maxed, health does not increase and warning message shows
                    String message = XMLTexts.getText("MaxH");
                    dialogue.clear();
                    dialogue.add(new TextGraphics(message,0.5F, Color.BLACK));
                    dialogue.get(0).setAnchor(new Vector(1.5f,2.3f));
                    dialogueStart=true;
                    setSoundFX("dialogNext",1);
                    hasSoundFX=true;
                    stopForDialogue=true;
                    merchantInteraction=false;
                    damageInteraction=false;
                    healthInteraction=false;
                }
                else if(DEFAULT_MELEE_DAMAGE>=3&&damageInteraction){//same as for max health but for damage
                    String message = XMLTexts.getText("MaxD");
                    dialogue.clear();
                    dialogue.add(new TextGraphics(message,0.5F, Color.BLACK));
                    dialogue.get(0).setAnchor(new Vector(1.5f,2.3f));
                    dialogueStart=true;
                    setSoundFX("dialogNext",1);
                    hasSoundFX=true;
                    stopForDialogue=true;
                    merchantInteraction=false;
                    damageInteraction=false;
                    healthInteraction=false;
                }
                else{
                    if (CoinCounter>=NPC.PRICE){
                        CoinCounter=CoinCounter-NPC.PRICE;
                        System.out.println("coins: "+CoinCounter);
                        if(healthInteraction){//if interaction with tota, health increases and conclusion message shows
                            setMaxHp(getMaxHp()+2);
                            setHp(getMaxHp());
                            String message = XMLTexts.getText("Health");
                            dialogue.clear();
                            dialogue.add(new TextGraphics(message,0.5F, Color.BLACK));
                            dialogue.get(0).setAnchor(new Vector(1.5f,2.3f));
                        }
                        else if(damageInteraction){//if interaction with alejandro, damage increases and conclusion message shows
                            increaseDamage();
                            String message = XMLTexts.getText("Damage");
                            dialogue.clear();
                            dialogue.add(new TextGraphics(message,0.5F, Color.BLACK));
                            dialogue.get(0).setAnchor(new Vector(1.5f,2.3f));
                        }

                        dialogueStart=true;
                        setSoundFX("dialogNext",1);
                        hasSoundFX=true;
                        stopForDialogue=true;
                        merchantInteraction=false;
                        damageInteraction=false;
                        healthInteraction=false;
                    }
                    else{//message displayed if not enough coins
                        String message = XMLTexts.getText("Not_enough");
                        dialogue.clear();
                        dialogue.add(new TextGraphics(message,0.5F, Color.BLACK));
                        dialogue.get(0).setAnchor(new Vector(1.5f,2.3f));
                        dialogueStart=true;
                        setSoundFX("dialogNext",1);
                        hasSoundFX=true;
                        stopForDialogue=true;
                        merchantInteraction=false;

                    }
                }
            }
            if(introductionInteraction){//it sets introduction interaction to false if it happened once

                introductionInteraction=false;
            }

        }
        //creates the dialogue for when the game starts
        if((gameStartDialogue&&stopForDialogue&&keyboard.get(Keyboard.W).isPressed())||dialogueCounter==1.f){
            gameStartDialogue=false;
            stopForDialogue=false;
            if(gameStartDialogueStep<9){
                gameStartDialogue();
                dialogueCounter=0;
                if(gameStartDialogueStep<=8){
                    ++gameStartDialogueStep;
                    gameStartDialogue=true;
                    stopForDialogue=true;
                }
            }

        }
        if((gameEndDialogue&&stopForDialogue&&keyboard.get(Keyboard.W).isPressed())||dialogueCounter==1.f){
            gameStartDialogue=false;
            stopForDialogue=false;
            dialogueCounter=0;

        }

        super.update(deltaTime);

    }

    /**
     * Resume method: sets the current part of dialogue of the gameStart dialogue (part that will be displayed for the introduction dialogue) and sets the sound of that animation
     */
    public void gameStartDialogue(){//sets up the game start dialogue
        dialogue.clear();
        for(int i=1;i<5;++i){
            String key="Game_Start"+gameStartDialogueStep+""+i;
            String message = XMLTexts.getText(key);
            TextGraphics dialogue=new TextGraphics(message,0.46F, Color.BLACK);
            dialogue.setAnchor(new Vector(1.5f,2.3f));
            this.dialogue.add(dialogue);
        }
        setSoundFX("book",1);
        ((MainBase)getOwnerArea()).setDialogue(this,dialogue);

    }
    /**
     * Resume method: sets walking animation
     */
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

    /**
     * Resume method: sets staff animation
     */
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


    /**
     * Resume method: sets new hp for the player
     * @param newHp new hp to be associated with the player, not null
     */
    public static void setHp(int newHp){
        hp = newHp;
    }
    /**
     * Resume method: sets new max value of hp for the player
     * @param newMaxHp new max value of hp to be associated with the player, not null
     */
    public static void setMaxHp(int newMaxHp){
        maxHp = newMaxHp;
    }
    /**
     * Resume method: sets new damage for player
     */
    public static void increaseDamage(){
        ++DEFAULT_MELEE_DAMAGE;
        ++Sword.DEFAULT_DAMAGE;
        ++Fire.DEFAULT_DAMAGE;


    }

    /**
     * Resume method: returns current hp associated with the player
     * @return players Hp
     */
    public int getHp(){return hp;}
    /**
     * Resume method: returns current maxHp associated with the player
     * @return players maxHp
     */
    public int getMaxHp(){return maxHp;}
    /**
     * Resume method: displays an image of 5 hearts but changes the roi depending on the current hp so that the hp is displayed in the hearts
     * if player has been hit by poison, hearts turn green
     * @return heart sprite to be printed
     */
    private ImageGraphics printFullHearts(){
        if(!isPoisoned){
            fullHearts = new ImageGraphics("images/sprites/zelda/5.full.red.hearts.png", ((2.5f) * ((float)getHp()/10)) ,.5f,
                    new RegionOfInterest(0,0,(int)(80 * ((double)getHp()/10)),16), new Vector(0.5f,.25f));
        }
        else{
            fullHearts = new ImageGraphics("images/sprites/zelda/5.full.green.hearts.png", ((2.5f) * ((float)getHp()/10)) ,.5f,
                    new RegionOfInterest(0,0,(int)(80 * ((double)getHp()/10)),16), new Vector(0.5f,.25f));
        }

        return fullHearts;
    }
    /**
     * Resume method: empty hearts display the maxhp possible
     * if player has been hit by poison, hearts turn green
     * @return heart sprite to be printed
     */
    private ImageGraphics printEmptyHearts(){
        if(!isPoisoned) {
            emptyHearts = new ImageGraphics("images/sprites/zelda/5.empty.red.hearts.png", ((2.5f) * ((float) getMaxHp() / 10)), .5f,
                    new RegionOfInterest(0, 0, (int) (80 * ((double) getMaxHp() / 10)), 16), new Vector(0.5f, .25f));
        }
        else{
            emptyHearts = new ImageGraphics("images/sprites/zelda/5.empty.green.hearts.png", ((2.5f) * ((float) getMaxHp() / 10)), .5f,
                    new RegionOfInterest(0, 0, (int) (80 * ((double) getMaxHp() / 10)), 16), new Vector(0.5f, .25f));
        }
        return emptyHearts;
    }

    /**
     * Resume method: displays a graphic where we will show how many coins we have
     * @return coins diplay sprite to be drawn
     */
    private ImageGraphics printCoinsDisplay(){
        CoinsDisplay = new ImageGraphics("images/sprites/zelda/coinsDisplay.png", 1.75f,0.875f,
                new RegionOfInterest(0,0,64,32), new Vector(0.5f,9.1f));
        return CoinsDisplay;
    }
    /**
     * Resume method: displays a number of coins on the graphic mentioned before
     * @return coin number to be drawn
     */
    private TextGraphics printCoinsNumber(){
        CoinsNumber = new TextGraphics( String.valueOf(CoinCounter), 0.5f, Color.black, Color.black,0.f,
                true, false, new Vector(CoinsDisplay.getAnchor().x+1, CoinsDisplay.getAnchor().y+0.33f));
        return CoinsNumber;
    }

    /**
     * Resume method: decreases the hp by a given number, sets up the damage sound effect for the player
     * @param delta amount that hp will be decreased by, not null
     */
    public void decreaseHp(float delta){
        hp -= delta;
        soundFX=new SoundAcoustics(ResourcePath.getSound("damage"),1,false,false,false,false);
        hasSoundFX=true;
        System.out.println(hp);
    }

    /**
     * Resume method: checks if hp can be increased
     * @return boolean indicating whether health can be increased or not
     */
    public boolean canIncreaseHp(){
        return hp < maxHp;
    }

    /**
     * Resume method: increases hp based on the boolean above and a number delta
     * @param delta amount that hp will be increased by, not null
     */
    public void increaseHp(float delta){
        if (canIncreaseHp()){
            hp += delta;
        }

    }
    /**
     * Resume method: turns all the carrying booleans to false (i.e. so you cant use fireball in the next level until you find staff), clears the carrying array
     */
    public void clearCarrying(){
        carrying.clear();
        hasStaff=false;
        hasBow=false;
        hasSword=false;
    }

    /**
     * Resume method: makes it so the handler handles all the interactions for player
     * @param v the interaction handler for the player, not null
     * @param isCellInteraction boolean determining if player wants interaction, not null
     */
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler)v).interactWith(this, isCellInteraction);
    }

    /**
     * Resume method: returns the cells the player is currently occupying
     * @return array of coordinates of cells being currently occupied by the player
     */
    public List<DiscreteCoordinates> getCurrentCells(){
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Resume method: returns the cell that the player sees, AKA the cell in front of the direction he is facing
     * @return array of coordinates of cells being currently faced by the player
     */
    public List<DiscreteCoordinates> getFieldOfViewCells(){
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    /**
     * Resume method: returns true indicating that player always wants to cell interact (interact with object if they occupy the same cell)
     * @return boolean indicating that player always wants to cell interact
     */
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * Resume method: interacts with intractable in its field of vision (aka in front of whatever direction he is facing) and W or Z is pressed
     * @return boolean indicating whether player interacted with interactable or not
     */
    public boolean wantsViewInteraction() {
        Keyboard keyboard= getOwnerArea().getKeyboard();
        /*this means that the player only interacts with an object in front of him if you press W*/
        return (keyboard.get(Keyboard.W).isPressed()) || (keyboard.get(Keyboard.Z).isPressed());
    }

    /**
     * Resume method: sets transitioning boolean to false indicating that player already transitioned and should not transition again
     */
    public void transitioned(){
        isTransitioning=false;
    }

    /**
     * Resume method: centers around player
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }



    /**
     * Resume method: sets the sprites for the walking animation
     */
    public void setSpriteAnimation(){
       Vector anchor = new Vector(0.15f, -0.15f);
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

    /**
     * Resume method: sets the sprites for the staff animation
     */
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

    /**
     * Resume method: sets the sprite of the player back to its default based on his orientation
     * @param orientation orientation of the player, not null
     */
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

    /**
     * Resume method: sets sound Fx for the player
     * @param name name (name of file) for the sound effect being set up
     * @param vol volume of the sound effect
     */
    private void setSoundFX(String name,float vol){ //sets the sound fx
        soundFX=new SoundAcoustics(ResourcePath.getSound(name),vol,false,false,false,false);
        hasSoundFX=true;
    }
    /**
     * Resume method: sets sound Fx for the player
     * @return  boolean indicating whether the player has a sound effect that should be played or not
     */
    public boolean HasSoundFX(){
        return hasSoundFX;
    }
    /**
     * Resume method: plays the sound on the current window
     * @param window where the sound should be played, not null
     */
    public void playSound(Window window){ //plays the sound effect stored in the player
        soundFX.shouldBeStarted();//starts the sound and plays it
        soundFX.bip(window);
        hasSoundFX=false;//sets hasSoundFX to false so the sound is not repeated forever
    }

    /**
     * Resume method: returns boolean indicating whether player received damage or not
     * @return boolean indicating whether player received damage or not
     */
    public boolean getReceivedDamage(){return receivedDamage;}

    /**
     * Resume method: sets receivedDamage boolean
     * @param rd boolean indicating whether player received damage or not
     */
    public void setReceivedDamage(boolean rd){receivedDamage = rd;}

    /**
     * Resume method: makes the interactable accept interaction from player
     * @param other entity being interacted with
     * @param isCellInteraction boolean indicating whether interaction will happen or not
     */
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler,isCellInteraction);
    }

    /**
     * Resume method: makes player transport
     */
    public void transport() {
        isTransporting=true;
    }

    /**
     * Resume method: sets the area that player will be transported to
     * @param base name of area that player should transport to
     */
    public void setTransportArea(String base) {
        transportArea=base;
    }

    private class ICRoguePlayerInteractionHandler implements ICRogueInteractionHandler{

        /**
         * Resume method: interaction with cherry, sets sound effect and increases hp
         * @param cherry cherry being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Cherry cherry, boolean isCellInteraction) {
            if(wantsCellInteraction()&&canIncreaseHp()){
                increaseHp(1);
                //sets the sound fx
                setSoundFX("health",1);
                cherry.collect();
            }
        }

        /**
         * Resume method: interaction with coin, sets sound effect and increases coin counter
         * @param coin coin being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Coin coin, boolean isCellInteraction) {
            if(wantsCellInteraction()){
                increaseCoinCounter(1);
                //sets the sound fx
                setSoundFX("coin",1);
                coin.collect();
            }
        }

        /**
         * Resume method: interaction with staff, sets sound effect and sets hasStaff to true
         * @param staff staff being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Staff staff, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())){
                staff.collect();
                //sets the sound fx
                setSoundFX("item",1);
                hasStaff = true;
                carrying.add(staff);
                spriteName = "zelda/player.staff_water";

            }
        }
        /**
         * Resume method: interaction with sword, sets sound effect and sets hasSword to true
         * @param sword sword being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Sword sword, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())){
                sword.collect();
                //sets the sound fx
                setSoundFX("item",1);
                hasSword = true;
                carrying.add(sword);
                spriteName = "zelda/player.sword";

            }
        }
        /**
         * Resume method: interaction with bow, sets sound effect and sets hasBow to true
         * @param bow bow being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Bow bow, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())){
                bow.collect();
                //sets the sound fx
                setSoundFX("item",1);
                hasBow = true;
                carrying.add(bow);
                spriteName = "zelda/player.bow";

            }
        }
        /**
         * Resume method: interaction with key, sets sound effect and adds key to carrying array
         * @param key key being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Key key, boolean isCellInteraction){
            if(isCellInteraction){
                key.collect();
                //sets the sound fx
                setSoundFX("item",1);
                carrying.add(key); /*adds key to carrying arraylist which represents the items the character is holding*/
            }
        }
        /**
         * Resume method: interaction with turret, sets sound effect and damages turret
         * @param turret turret being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Turret turret, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if (wantsViewInteraction() && keyboard.get(Keyboard.Z).isPressed()){
                //sets the sound fx
                if(hasSword){
                    setSoundFX("sword",1);
                }
                else{
                    setSoundFX("melee",1);
                }

                turret.decreaseHp(meleeDamage);
            }
        }
        /**
         * Resume method: same as for turret
         * @param turret turret being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(PoisonTurret turret, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if (wantsViewInteraction() && keyboard.get(Keyboard.Z).isPressed()){
                //sets the sound fx
                if(hasSword){
                    setSoundFX("sword",1);
                }
                else{
                    setSoundFX("melee",1);
                }
                turret.decreaseHp(meleeDamage);
            }
        }
        /**
         * Resume method: same as for turret
         * @param turret turret being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(BossTurret turret, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if (wantsViewInteraction() && keyboard.get(Keyboard.Z).isPressed()){
                //sets the sound fx
                if(hasSword){
                    setSoundFX("sword",1);
                }
                else{
                    setSoundFX("melee",1);
                }
                turret.decreaseHp(meleeDamage);
            }
        }
        /**
         * Resume method: same as for turret
         * @param wither wither being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Wither wither, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if (wantsViewInteraction() && keyboard.get(Keyboard.Z).isPressed()){
                //sets the sound fx
                if(hasSword){
                    setSoundFX("sword",1);
                }
                else{
                    setSoundFX("melee",1);
                }
                wither.decreaseHp(meleeDamage);
            }
        }
        /**
         * Resume method: same as for turret
         * @param wither PAWither being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(PAWither wither, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if (wantsViewInteraction() && keyboard.get(Keyboard.Z).isPressed()){
                //sets the sound fx
                setSoundFX("melee",1);
                wither.decreaseHp(meleeDamage);
            }
        }
        /**
         * Resume method: same as for turret
         * @param beanos Beanos being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Beanos beanos, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if (wantsViewInteraction() && keyboard.get(Keyboard.Z).isPressed()){
                //sets the sound fx
                if(hasSword){
                    setSoundFX("sword",1);
                }
                else{
                    setSoundFX("melee",1);
                }
                beanos.decreaseHp(meleeDamage);
            }
        }
        /**
         * Resume method: sets the new area that the player will be transition to, sets sound effect and sets isTransitioning to true
         * if the connector is locked and the player has the key, it unlocks the connector
         * @param connector connector being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Connector connector, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(wantsViewInteraction()&& (keyboard.get(Keyboard.W).isPressed())){
                boolean unlock=false;
                if(connector.compareState(Connector.ConnectorState.LOCKED)){
                    for(Item item:carrying){
                        if(item instanceof Key&&((Key) item).getIdentificator()==connector.getID()){
                            connector.setState(Connector.ConnectorState.OPEN);
                            unlock=true;
                            //sets the sound fx
                            setSoundFX("unlock",1);
                        }
                    }
                if(!unlock){
                    //sets the sound fx
                    setSoundFX("locked",0.3F);
                }
                }

                }
            if(isCellInteraction&&connector.compareState(Connector.ConnectorState.OPEN)){
                coordinatesTransition=connector.getArrivalcoordinates();
                transitionArea=connector.getAreaTitle();
                isTransitioning=true;

            }
        }

        /**
         * Resume method: same as for connector but now it relates to transition between area, also there is no unlocking interaction since the player does not directly unlock the portal
         * @param portal portal being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
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

        /**
         * Resume method: sets the dialogue in player based on the npc's dialogue, sets dialogueStart to true and initialises sound effect
         * @param npc npc being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(NPC npc, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(!dialogueStart&&wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())&&dialogueCounter>=1.f){
                dialogue.clear();
                //gets dialogue from the npc
                npc.getDialogue(dialogue);
                //starts the dialogue interaction
                dialogueStart=true;
                setSoundFX("dialogNext",1);
                stopForDialogue=true;
            }

        }
        /**
         * Resume method: same as npc but now with new booleans indicating that its a merchant and a health interaction
         * @param tota tota being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Tota tota, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(!dialogueStart&&wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())&&dialogueCounter>=1.f){
                dialogue.clear();
                if(tota.isFirstInteraction()){
                    introductionInteraction=true;
                    tota.interacted();
                    tota.startingDialogue(dialogue);
                }
                else{
                    tota.getDialogue(dialogue);
                    healthInteraction=true;
                }

                //starts the dialogue interaction
                dialogueStart=true;
                setSoundFX("dialogNext",1);
                stopForDialogue=true;
                merchantInteraction=true;//specifies that its a merchant interaction

            }

        }
        /**
         * Resume method: same as npc but now with new booleans indicating that its a merchant and a damage interaction
         * @param alejandro alejandro being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Alejandro alejandro, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(!dialogueStart&&wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())&&dialogueCounter>=1.f){
                dialogue.clear();
                if(alejandro.isFirstInteraction()){
                    introductionInteraction=true;
                    alejandro.interacted();
                    alejandro.startingDialogue(dialogue);
                }
                else {
                    alejandro.getDialogue(dialogue);
                    damageInteraction=true;
                }
                //starts the dialogue interaction
                dialogueStart=true;
                setSoundFX("dialogNext",1);
                stopForDialogue=true;
                merchantInteraction=true;//specifies that its a merchant interaction

            }

        }
        /**
         * Resume method: interacts with the lever which unlocks the room the lever is in
         * @param lever lever being interacted with
         * @param isCellInteraction boolean indicating whether player wants cell interaction or not
         */
        public void interactWith(Lever lever, boolean isCellInteraction){
            Keyboard keyboard= getOwnerArea().getKeyboard();
            if(wantsViewInteraction() && (keyboard.get(Keyboard.W).isPressed())){
                lever.interactWith();
                setSoundFX("lever",1);
            }

        }
    }
    /**
     * Resume method: increases number of coins player has
     * @param i number of coins to increase by
     */
    private void increaseCoinCounter(int i) {
        ++CoinCounter;
        System.out.println("coins: "+CoinCounter);
    }
}
