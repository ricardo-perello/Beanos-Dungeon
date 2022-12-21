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
import ch.epfl.cs107.play.game.icrogue.area.mainBase.MainBaseArea;
import ch.epfl.cs107.play.game.tutosSolution.area.Tuto2Area;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainBase extends MainBaseArea {

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

    public void draw(Canvas canvas, List<TextGraphics> dialogue){
        dialogueBox.draw(canvas);
        for(TextGraphics d:dialogue){
            d.draw(canvas);
        }

    }

    public void setParent(ICRoguePlayer player){
        dialogueBox.setParent(player);
        dialogueBox.setAnchor(new Vector(-3f,-3f));
    }

    public void setDialogue(ICRoguePlayer player, List<TextGraphics> dialogue){
        for(int i=0;i<dialogue.size();++i){
            dialogue.get(i).setParent(player);
            double t=i;
            float k= (float) (-1.4f-(t/2));
            dialogue.get(i).setAnchor(new Vector(-2.8f,k));
        }

    }


    protected void createArea() {
        // Base


        registerActor(new Background(this)) ;
        registerActor(new Foreground(this)) ;
        String message = XMLTexts.getText("NPC11");
        TextGraphics dialogue=new TextGraphics(message,0.5F, Color.BLACK);
        List<TextGraphics>dialogues=new ArrayList<>();
        dialogues.add(dialogue);
        message = XMLTexts.getText("NPC12");
        dialogue=new TextGraphics(message,0.5F, Color.BLACK);
        dialogues.add(dialogue);
        registerActor(new NPC(this,new DiscreteCoordinates(20, 10), "assistant.fixed",dialogues));
        message = XMLTexts.getText("NPC21");
        dialogue=new TextGraphics(message,0.5F, Color.BLACK);
        dialogues.clear();
        dialogues.add(dialogue);
        message = XMLTexts.getText("NPC22");
        dialogue=new TextGraphics(message,0.5F, Color.BLACK);
        dialogues.add(dialogue);
        registerActor(new NPC(this,new DiscreteCoordinates(6, 5), "boy.1",dialogues));
        message = XMLTexts.getText("NPC31");
        dialogue=new TextGraphics(message,0.5F, Color.BLACK);
        dialogues.clear();
        dialogues.add(dialogue);
        message = XMLTexts.getText("NPC32");
        dialogue=new TextGraphics(message,0.5F, Color.BLACK);
        dialogues.add(dialogue);
        registerActor(new NPC(this,new DiscreteCoordinates(6, 10), "girl.1",dialogues));
        message = XMLTexts.getText("NPC41");
        dialogue=new TextGraphics(message,0.47F, Color.BLACK);
        dialogues.clear();
        dialogues.add(dialogue);
        message = XMLTexts.getText("NPC42");
        dialogue=new TextGraphics(message,0.47F, Color.BLACK);
        dialogues.add(dialogue);
        message = XMLTexts.getText("NPC43");
        dialogue=new TextGraphics(message,0.47F, Color.BLACK);
        dialogues.add(dialogue);
        message = XMLTexts.getText("NPC44");
        dialogue=new TextGraphics(message,0.47F, Color.BLACK);
        dialogues.add(dialogue);
        registerActor(new NPC(this,new DiscreteCoordinates(14, 10), "joel.fixed",dialogues));
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
