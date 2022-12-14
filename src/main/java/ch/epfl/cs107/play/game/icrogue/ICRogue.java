package ch.epfl.cs107.play.game.icrogue;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.MainBase;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.tutosSolution.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutosSolution.area.Tuto2Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;


public class ICRogue extends AreaGame {
    public final static float CAMERA_SCALE_FACTOR = 11;

    private ICRoguePlayer player; /*Main character*/

    private Level level;

    private MainBase base;
    private int lives;
    private int display;
    private int areaIndex;
    private DiscreteCoordinates previousCoorInBase;
    /**
     * Add all the areas
     */

    private void initGame(){
        base = new MainBase();
        addArea(base);
        setCurrentArea(base.getTitle(),false);
        player=new ICRoguePlayer(base,Orientation.DOWN,base.getPlayerSpawnPosition());
        player.enterArea(base,base.getPlayerSpawnPosition());
        player.centerCamera();

    }
    private void initLevel(){


        level=new Level0(); /* creates first area*/


        level.addAreas(this); /*adds current room to the areas*/


        setCurrentArea(level.getRoomName(Level0.startingroom),false); /* makes it the current area */


        player=level.addPlayer(Level0.startingroom);/* creates main character and adds to starting room*/

    }

    public void PlayerDies(){
        initLevel();
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
        if(key.isDown()){
            initLevel();
        }

        if((player.getHp() <= 0)&&lives>=0){
            switchArea();
        }

        if((lives<0)&&(display==0)){
            end();
            System.out.println("Game Over");
            display=1;
        }
        switchRoom();
        switchArea();
        if(level!=null&&level.isResolved()&&(display==0)){
            end();
            System.out.println("Win");


            display=1;
        }
        super.update(deltaTime);

    }


    public void end() {
        /*
        add win screen
         */
    }


    public String getTitle() {
        return "Beanos' Dungeon";
    } /*returns the title of our game */

    protected void switchArea() {
        if(player.getisTransporting()&&getCurrentArea() instanceof MainBase){
            previousCoorInBase=player.getCurrentCells().get(0);
            player.leaveArea();
            player.transported();
            initLevel();

        }
        else if(level!=null&&level.isResolved()&&(display==0)){
            player.leaveArea();
            player.clearCarrying();
            setCurrentArea(base.getTitle(),false);
            player.enterArea(base,previousCoorInBase);
            player.centerCamera();

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

}
