package ch.epfl.cs107.play.game.icrogue.area;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.NPC;
import ch.epfl.cs107.play.game.icrogue.actor.Portal;
import ch.epfl.cs107.play.game.tutosSolution.area.Tuto2Area;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;
import java.util.ArrayList;

public class MainBase extends Tuto2Area {

    private ArrayList<Portal> portals=new ArrayList<>();

    public String getTitle() {
        return "zelda/Village";
    }
    private final ImageGraphics dialogueBox=new ImageGraphics("images/sprites/dialog.png",8,2,
            new RegionOfInterest(0,0,235,42), new Vector(1,-2));



    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(5,15);
    }

    public void unlockPortal(int i){
        portals.get(i).setState(Portal.PortalState.OPEN);
    }

    public void draw(Canvas canvas, TextGraphics dialogue){
        dialogueBox.draw(canvas);
        dialogue.draw(canvas);
    }

    public void setParent(ICRoguePlayer player){
        dialogueBox.setParent(player);
    }


    protected void createArea() {
        // Base


        registerActor(new Background(this)) ;
        registerActor(new Foreground(this)) ;
        String message = XMLTexts.getText("NPC1");
        TextGraphics dialogue=new TextGraphics(message,0.3F, Color.BLACK);
        registerActor(new NPC(this,new DiscreteCoordinates(20, 10), "assistant.fixed",dialogue));
        message = XMLTexts.getText("NPC2");
        dialogue=new TextGraphics(message,0.3F, Color.BLACK);
        registerActor(new NPC(this,new DiscreteCoordinates(10, 10), "boy.1",dialogue));
        message = XMLTexts.getText("NPC3");
        dialogue=new TextGraphics(message,0.3F, Color.BLACK);
        registerActor(new NPC(this,new DiscreteCoordinates(6, 10), "girl.1",dialogue));
        message = XMLTexts.getText("NPC4");
        dialogue=new TextGraphics(message,0.3F, Color.BLACK);
        registerActor(new NPC(this,new DiscreteCoordinates(20, 10), "joel.fixed",dialogue));
        Portal lvl1=new Portal(this,Orientation.UP,new DiscreteCoordinates(19,5),"level1");
        lvl1.setState(Portal.PortalState.LOCKED);
        Portal lvl2=new Portal(this,Orientation.UP,new DiscreteCoordinates(17,7),"level2");
        lvl2.setState(Portal.PortalState.LOCKED);
        Portal beanos=new Portal(this,Orientation.UP,new DiscreteCoordinates(29,18),"beanos");
        beanos.setState(Portal.PortalState.LOCKED);
        portals.add(new Portal(this,Orientation.UP,new DiscreteCoordinates(15,5),"level0"));
        portals.add(lvl1);
        portals.add(lvl2);
        portals.add(beanos);
        portals.add(new Portal(this,Orientation.UP,new DiscreteCoordinates(10,6),"shop", Portal.PortalState.SHOP));

        for(Portal portal:portals){
            this.registerActor(portal);
        }
    }



}
