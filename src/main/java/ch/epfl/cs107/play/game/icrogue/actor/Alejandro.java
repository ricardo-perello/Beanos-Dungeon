package ch.epfl.cs107.play.game.icrogue.actor;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

import java.awt.*;
import java.util.List;

public class Alejandro extends NPC{


    /**
     * Default DemoActor constructor
     *
     * @param area
     * @param position   (Vector): initial position vector of the ghost
     * @param spriteName
     */
    public Alejandro(Area area, DiscreteCoordinates position, String spriteName, List<TextGraphics> dialogue) {
        super(area, position, spriteName, dialogue);
    }
    public void update(float deltaTime){
        super.update(deltaTime);
    }


    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);

    }

    public void startingDialogue(List<TextGraphics> dialogue){
        String message = XMLTexts.getText("Alej_Introduction1");
        dialogue.add(new TextGraphics(message,0.5F, Color.BLACK));
        dialogue.get(0).setAnchor(new Vector(1.5f,2.3f));
        message = XMLTexts.getText("Alej_Introduction2");
        dialogue.add(new TextGraphics(message,0.5F, Color.BLACK));
        dialogue.get(1).setAnchor(new Vector(1.5f,1.85f));
        message = XMLTexts.getText("Alej_Introduction3");
        dialogue.add(new TextGraphics(message,0.5F, Color.BLACK));
        dialogue.get(2).setAnchor(new Vector(1.5f,1.4f));
    }

}
