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
import ch.epfl.cs107.play.game.tutosSolution.area.Tuto2Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;


public class ICRogue extends AreaGame {
    public final static float CAMERA_SCALE_FACTOR = 11;

    private ICRoguePlayer player; /*Main character*/

    private SoundAcoustics soundtrack;



    private Level level;
    private Shop shop;
    private int lvl0clears;
    private int lvl1clears;
    private int lvl2clears;
    private MainBase base;
    private int lives;
    private int display;
    private DiscreteCoordinates previousCoorInBase;
    /**
     * Add all the areas
     */

    private void initGame(){
        base = new MainBase();
        soundtrack=new SoundAcoustics(ResourcePath.getSound("home"),1,false,false,true,false);
        addArea(base);
        soundtrack.shouldBeStarted();
        soundtrack.bip(getWindow());
        setCurrentArea(base.getTitle(),false);
        player=new ICRoguePlayer(base,Orientation.DOWN,base.getPlayerSpawnPosition());
        player.enterArea(base,base.getPlayerSpawnPosition());
        player.centerCamera();
        shop=new Shop();
        addArea(shop);

    }
    private void initLevel0(){


        level=new Level0(); /* creates first area*/
        level.addAreas(this); /*adds current room to the areas*/

        soundtrack=new SoundAcoustics(ResourcePath.getSound("dungeon"),1,true,false,true,false);
        soundtrack.shouldBeStarted();
        soundtrack.bip(getWindow());





        setCurrentArea(level.getRoomName(Level0.startingroom),false); /* makes it the current area */


        player=level.addPlayer(Level0.startingroom);/* creates main character and adds to starting room*/

    }

    private void initLevel1(){


        level=new Level1(); /* creates first area*/


        level.addAreas(this); /*adds current room to the areas*/
        soundtrack=new SoundAcoustics(ResourcePath.getSound("dungeon"),1,true,false,true,false);
        soundtrack.shouldBeStarted();
        soundtrack.bip(getWindow());


        setCurrentArea(level.getRoomName(Level1.startingroom),false); /* makes it the current area */


        player=level.addPlayer(Level1.startingroom);/* creates main character and adds to starting room*/

    }

    private void initLevel2(){


        level=new Level2(); /* creates first area*/


        level.addAreas(this); /*adds current room to the areas*/
        soundtrack=new SoundAcoustics(ResourcePath.getSound("dungeon"),1,true,false,true,false);
        soundtrack.shouldBeStarted();
        soundtrack.bip(getWindow());


        setCurrentArea(level.getRoomName(Level2.startingroom),false); /* makes it the current area */


        player=level.addPlayer(Level2.startingroom);/* creates main character and adds to starting room*/

    }

    private void initLevelBeanos(){


        level=new LevelBeanos(); /* creates first area*/


        level.addAreas(this); /*adds current room to the areas*/
        soundtrack=new SoundAcoustics(ResourcePath.getSound("boss"),1,true,false,true,false);
        soundtrack.shouldBeStarted();
        soundtrack.bip(getWindow());


        setCurrentArea(level.getRoomName(LevelBeanos.startingroom),false); /* makes it the current area */


        player=level.addPlayer(LevelBeanos.startingroom);/* creates main character and adds to starting room*/

    }


    @Override
    public boolean begin(Window window, FileSystem fileSystem) {

        if (super.begin(window, fileSystem)) {
            lives=3;
            display=0;
            /*starts level*/
            initGame();
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getWindow().getKeyboard() ;
        Button key=keyboard.get(Keyboard.R);
        /*resets current room for testing purposes*/
        if(key.isPressed()){
            initLevel0();
        }

        key=keyboard.get(Keyboard.P);
        if(key.isPressed()){
            ++lvl0clears;
        }
        key=keyboard.get(Keyboard.O);
        if(key.isPressed()){
            ++lvl1clears;
        }
        key=keyboard.get(Keyboard.I);
        if(key.isPressed()){
            ++lvl2clears;
        }
        switchRoom();
        switchArea();
        if(player.HasSoundFX()){
            player.playSound(getWindow());
        }
        if((player.getHp() <= 0)&&(display==0)){
            System.out.println("Dead");
            display=1;
        }

        if(level!=null&&level.isResolved()&&(display==0)){
            System.out.println("Win");

            display=1;
        }
        if(getCurrentArea() instanceof MainBase){
            unlockPortals();
        }
        super.update(deltaTime);

    }


    public void end() {
        /*
        add win screen
         */
    }

    public void unlockPortals(){
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

    public String getTitle() {
        return "Beanos' Dungeon";
    } /*returns the title of our game */

    protected void switchArea() {
        if(player.getisTransporting()&&getCurrentArea() instanceof MainBase){
            if(!player.getTransportArea().equals("shop")){
                SoundAcoustics fx=new SoundAcoustics(ResourcePath.getSound("portal"), (float) 0.8,false,false,false,true);
                fx.shouldBeStarted();
                fx.bip(getWindow());

            }
            else{
                SoundAcoustics fx=new SoundAcoustics(ResourcePath.getSound("door"),1,false,false,false,true);
                fx.shouldBeStarted();
                fx.bip(getWindow());
            }

            previousCoorInBase=player.getCurrentCells().get(0);
            player.leaveArea();
            player.transported();
            if(player.getTransportArea().equals("level0")){
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

                soundtrack=new SoundAcoustics(ResourcePath.getSound("shop"),1,true,false,true,false);
                soundtrack.shouldBeStarted();
                soundtrack.bip(getWindow());
                player.enterArea(shop,shop.getPlayerSpawnPosition());


            }
            display=0;

        }
        else if(player.getisTransporting()&&getCurrentArea() instanceof Shop){
            player.leaveArea();
            SoundAcoustics fx=new SoundAcoustics(ResourcePath.getSound("door"),1,false,false,false,true);
            fx.shouldBeStarted();
            fx.bip(getWindow());
            player.transported();
            setCurrentArea(base.getTitle(),false);
            soundtrack=new SoundAcoustics(ResourcePath.getSound("home"),1,true,false,true,false);
            soundtrack.shouldBeStarted();
            soundtrack.bip(getWindow());
            player.enterArea(base,previousCoorInBase);
            player.centerCamera();

        }
        else if(level!=null&&(player.getHp() <= 0)&&(display==0)){
            player.leaveArea();
            player.clearCarrying();
            setCurrentArea(base.getTitle(),false);
            soundtrack=new SoundAcoustics(ResourcePath.getSound("home"),1,true,false,true,false);
            soundtrack.shouldBeStarted();
            soundtrack.bip(getWindow());
            player.enterArea(base,previousCoorInBase);
            player.centerCamera();
            display=0;

        }
        else if(level!=null&&level.isResolved()&&(display==0)){
            if(level instanceof Level0){
                ++lvl0clears;
            }
            else if(level instanceof Level1){
                ++lvl1clears;
            }
            else if(level instanceof Level2){
                ++lvl2clears;
            }
            SoundAcoustics fx=new SoundAcoustics(ResourcePath.getSound("clear"),1,false,false,false,true);
            fx.shouldBeStarted();
            fx.bip(getWindow());
            player.leaveArea();
            player.clearCarrying();
            setCurrentArea(base.getTitle(),false);
            soundtrack=new SoundAcoustics(ResourcePath.getSound("home"),1,true,false,true,false);
            soundtrack.shouldBeStarted();
            soundtrack.bip(getWindow());
            player.enterArea(base,previousCoorInBase);
            player.centerCamera();
            display=0;

        }
    }

    protected void switchRoom() {
        if(player.getisTransitioning()){
            player.leaveArea();

            setCurrentArea(player.getTransitionArea(), false);

            level.enterArea(player.getCoordinatesTransition(),player,player.getTransitionArea());
            player.transitioned();


        }

    }
    private void playerSoundFX(){

    }

}

//TODO new signal rooms, enemies(one that moves and one that spawns other enemies), new levels, merchant room,
//TODO create TOTA and alejandro, dialogue, sounds, add portals for new levels and do the key spawn in  base,
//TODO progress bar for levels
