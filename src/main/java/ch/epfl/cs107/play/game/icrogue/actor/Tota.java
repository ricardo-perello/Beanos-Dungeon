package ch.epfl.cs107.play.game.icrogue.actor;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.awt.*;

public class Tota extends NPC{
    private boolean dialogueStart;
    private Dialog dialogue;

    /**
     * Default DemoActor constructor
     *
     * @param area
     * @param position   (Vector): initial position vector of the ghost
     * @param spriteName
     */
    public Tota(Area area, DiscreteCoordinates position, String spriteName) {
        super(area, position, spriteName);
    }
    public void update(float deltaTime){
        if(dialogueStart){

        }
        super.update(deltaTime);
    }
    public void startDialogue(){
        dialogueStart=true;
    }
}
