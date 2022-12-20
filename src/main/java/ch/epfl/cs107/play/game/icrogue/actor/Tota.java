package ch.epfl.cs107.play.game.icrogue.actor;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.area.MainBase;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.awt.*;
import java.util.List;

public class Tota extends NPC{



    /**
     * Default DemoActor constructor
     *
     * @param area
     * @param position   (Vector): initial position vector of the ghost
     * @param spriteName
     */
    public Tota(Area area, DiscreteCoordinates position, String spriteName, List<TextGraphics> dialogue) {
        super(area, position, spriteName, dialogue);
    }
    public void update(float deltaTime){
        super.update(deltaTime);
    }


    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);

    }

}
