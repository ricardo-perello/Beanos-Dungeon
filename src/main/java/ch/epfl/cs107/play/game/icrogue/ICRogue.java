package ch.epfl.cs107.play.game.icrogue;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icrogue.actor.Portal;
import ch.epfl.cs107.play.game.icrogue.area.Beanos.LevelBeanos;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.MainBase;
import ch.epfl.cs107.play.game.icrogue.area.Shop;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.area.level1.Level1;
import ch.epfl.cs107.play.game.icrogue.area.level1.rooms.Level1Room;
import ch.epfl.cs107.play.game.icrogue.area.level2.Level2;
import ch.epfl.cs107.play.game.icrogue.area.level2.rooms.Level2Room;
import ch.epfl.cs107.play.game.tutosSolution.actor.GhostPlayer;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import com.sun.tools.javac.Main;


public class ICRogue extends AreaGame {
    public final static float CAMERA_SCALE_FACTOR = 11;

    private ICRoguePlayer player; /*Main character*/

    private SoundAcoustics soundtrack;



    private Level level;
    private Shop shop;
    private static int lvl0clears;
    private static int lvl1clears;
    private static int lvl2clears;
    private MainBase base;
    private int lives;
    private DiscreteCoordinates previousCoorInBase;

    /**
     * Add all the areas and initialises the soundtrack and player
     */
    private void initGame(){
        base = new MainBase();
        addArea(base);
        setSoundtrack("home",1);
        setCurrentArea(base.getTitle(),false);
        player=new ICRoguePlayer(base,Orientation.DOWN,base.getPlayerSpawnPosition());
        player.enterArea(base,base.getPlayerSpawnPosition());
        player.centerCamera();
        shop=new Shop();
        addArea(shop);
        player.startDialogue();
    }
    /**
     * Resume method: creates a level zero and transportas player to it to start the level
     */
    private void initLevel0(){


        level=new Level0(); /* creates first area*/
        level.addAreas(this); /*adds current room to the areas*/

        setSoundtrack("dungeon",1);

        setCurrentArea(level.getRoomName(Level0.startingroom),false); /* makes it the current area */


        player=level.addPlayer(Level0.startingroom);/* creates main character and adds to starting room*/

    }

    /**
     * Resume method: creates a level 1 and transportas player to it to start the level
     */
    private void initLevel1(){


        level=new Level1(); /* creates first area*/


        level.addAreas(this); /*adds current room to the areas*/
        setSoundtrack("dungeon",1);


        setCurrentArea(level.getRoomName(Level1.startingroom),false); /* makes it the current area */


        player=level.addPlayer(Level1.startingroom);/* creates main character and adds to starting room*/

    }

    /**
     * Resume method: creates a level 2 and transportas player to it to start the level
     */
    private void initLevel2(){


        level=new Level2(); /* creates first area*/


        level.addAreas(this); /*adds current room to the areas*/
        soundtrack=new SoundAcoustics(ResourcePath.getSound("dungeon"),1,true,false,true,false);
        soundtrack.shouldBeStarted();
        soundtrack.bip(getWindow());


        setCurrentArea(level.getRoomName(Level2.startingroom),false); /* makes it the current area */


        player=level.addPlayer(Level2.startingroom);/* creates main character and adds to starting room*/

    }

    /**
     * Resume method: creates a level beanos and transportas player to it to start the level
     */
    private void initLevelBeanos(){


        level=new LevelBeanos(); /* creates first area*/


        level.addAreas(this); /*adds current room to the areas*/
        setSoundtrack("boss",1);


        setCurrentArea(level.getRoomName(LevelBeanos.startingroom),false); /* makes it the current area */


        player=level.addPlayer(LevelBeanos.startingroom);/* creates main character and adds to starting room*/

    }


    @Override
    public boolean begin(Window window, FileSystem fileSystem) {

        if (super.begin(window, fileSystem)) {
            lives=3;
            /*starts level*/
            initGame();
            return true;
        }
        return false;
    }

    /**
     * Resume method: updates icrogue
     * @param deltaTime time by which method is updated
     */
    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getWindow().getKeyboard() ;
        Button key=keyboard.get(Keyboard.P); //for testing purposes
        if(key.isPressed()){
            ++lvl0clears;
            base.printGreenDots(lvl0clears,1);
        }
        key=keyboard.get(Keyboard.O); //for testing purposes
        if(key.isPressed()){
            ++lvl1clears;
            base.printGreenDots(lvl1clears,2);
        }
        key=keyboard.get(Keyboard.I); //for testing purposes
        if(key.isPressed()){
            ++lvl2clears;
            base.printGreenDots(lvl2clears,3);
        }
        switchRoom();//used to switch rooms in level
        switchArea();//used to switch main areas (between Main Base, Shop and ICRogueRoom)
        if(player.HasSoundFX()){
            player.playSound(getWindow()); //plays the player soundFx if it exists
        }
        if(((player.getHp() <= 0)||(level!=null&&level.isResolved()))&&(!(getCurrentArea()instanceof MainBase)&&!(getCurrentArea()instanceof Shop))){
            //sends player back to base if dead or if level cleared and the current area is a level
            player.setTransportArea("base");
            player.transport();
        }
        if(getCurrentArea() instanceof MainBase){ //checks to see if portals can be unlocked and does so
            unlockPortals();
        }

        super.update(deltaTime);

    }


    public void end() {
        /*
       todo add win screen
         */
    }

    /**
     * Resume method: unlocks portals if the previous level was cleared 3 times
     */
    public void unlockPortals(){//check if the previous level has been cleared enough times and then unlocks the portal to the new level
        MainBase base=(MainBase)getCurrentArea();
        if(lvl0clears==3){
            base.unlockPortal(1);
        }
        if(lvl1clears==3){
            base.unlockPortal(2);
        }
        if(lvl2clears==3){
            base.unlockPortal(3);
        }

    }

    /**
     * Resume method: returns title of game
     */
    public String getTitle() {
        return "Beanos' Dungeon";
    } /*returns the title of our game */

    /**
     * Resume method: sets sound effect and plays it
     * @param name name of file of sound effect to be played
     * @param vol volume of sound effect to be played
     * @param stop determines whether it shoud stop others on start or not
     */
    public void setSoundFx(String name, float vol, boolean stop){//sets momentary sound fx
        SoundAcoustics fx=new SoundAcoustics(ResourcePath.getSound(name), vol,false,false,false,stop);
        fx.shouldBeStarted();
        fx.bip(getWindow());
    }
    /**
     * Resume method: sets soundtrack and plays it
     * @param name name of file of soundtrack to be played
     * @param vol volume of sound effect to be played
     */
    private void setSoundtrack(String name,float vol){ //sets the soundtrack (music so it differs from the momentary soundFX)
        soundtrack=new SoundAcoustics(ResourcePath.getSound(name),vol,false,false,true,false);
        soundtrack.shouldBeStarted();
        soundtrack.bip(getWindow());
    }

    /**
     * Resume method: method to alternate between MainBase, Shop and Level
     */
    protected void switchArea() {
        if(player.getisTransporting()&&getCurrentArea() instanceof MainBase){
            previousCoorInBase=player.getCurrentCells().get(0);
            if(!player.getTransportArea().equals("shop")){//if the portal leads to the shop it sounds like a door else it sounds like a portal
                setSoundFx("portal",(float) 0.8,true);
            }
            else{
                setSoundFx("door",1,true);
            }

            player.leaveArea();
            player.transported();
            if(player.getTransportArea().equals("level0")){//starts level corresponding to the name of the portal being interacted with
                initLevel0();
            }
            else if(player.getTransportArea().equals("level1")){
                initLevel1();
            }
            else if(player.getTransportArea().equals("level2")){
                initLevel2();
            }
            else if(player.getTransportArea().equals("beanos")){
                initLevelBeanos();
            }
            else if(player.getTransportArea().equals("shop")){
                setCurrentArea(shop.getTitle(), false); /* makes it the current area */
                setSoundtrack("shop",1);
                player.enterArea(shop,shop.getPlayerSpawnPosition());


            }

        }
        else if(player.getisTransporting()&&getCurrentArea() instanceof Shop){//does the switching of area when the current area is the shop
            player.leaveArea();
            setSoundFx("door",1,true);
            player.transported();
            setCurrentArea(base.getTitle(),false);
            setSoundtrack("home",1);
            player.enterArea(base,previousCoorInBase);
            player.centerCamera();

        }
        else if(level!=null&&((player.getHp() <= 0)||level.isResolved())&&player.getisTransporting()){
            //if player dies or level is cleared it sets the main Base as the current area again
            player.transported();
            player.leaveArea();
            player.clearCarrying();
            if((player.getHp() <= 0)){
                setSoundFx("beanos",0.6F,true);
                setSoundtrack("home",1);
                player.strengthen();
            }
            else if(level.isResolved()){
                if(level instanceof Level0){
                    ++lvl0clears;
                    base.printGreenDots(lvl0clears,1);

                }
                else if(level instanceof Level1){
                    ++lvl1clears;
                    base.printGreenDots(lvl1clears,2);

                }
                else if(level instanceof Level2){
                    ++lvl2clears;
                    base.printGreenDots(lvl2clears,3);

                }
                setSoundFx("clear",1,true);
                setSoundtrack("home",1);
                player.strengthen();
            }
            setCurrentArea(base.getTitle(),false);
            player.enterArea(base,previousCoorInBase);
            player.centerCamera();

        }
    }

    /**
     * Resume method: method to alternate between rooms in a level
     */
    protected void switchRoom() {
        if(player.getisTransitioning()){
            player.leaveArea();

            setCurrentArea(player.getTransitionArea(), false);

            level.enterArea(player.getCoordinatesTransition(),player,player.getTransitionArea());
            player.transitioned();


        }

    }

}




//todo add damage display
//todo change sprites for connector



