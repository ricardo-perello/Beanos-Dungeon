package ch.epfl.cs107.play.game.icrogue;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.tutosSolution.actor.GhostPlayer;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;


public class ICRogue extends AreaGame {
    public final static float CAMERA_SCALE_FACTOR = 11;

    private ICRoguePlayer player; /*Main character*/

    private Level0Room currentRoom;


    private int areaIndex;
    /**
     * Add all the areas
     */
    private void initLevel(){
        currentRoom=new Level0Room(new DiscreteCoordinates(0,0)); /* creates first area*/

        addArea(currentRoom); /*adds current room to the areas*/

        setCurrentArea(currentRoom.getTitle(),true); /* makes it the current area */

        player=new ICRoguePlayer(currentRoom, Orientation.UP,new DiscreteCoordinates(2,2));/* creates main character*/

        player.enterArea(currentRoom,new DiscreteCoordinates(2,2)); /*makes mc enter the main room*/


    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {

        if (super.begin(window, fileSystem)) {
            /*starts level*/
            initLevel();
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
        super.update(deltaTime);

    }

    @Override
    public void end() {
    }

    @Override
    public String getTitle() {
        return "Beanos' Dungeon";
    } /*returns the title of our game */

    protected void switchArea() {

    }

}
