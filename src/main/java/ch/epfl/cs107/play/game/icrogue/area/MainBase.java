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
    private ImageGraphics RedDots;
    private ImageGraphics GreenDots;


    public String getTitle() {
        return "zelda/Village";
    }
    private ImageGraphics dialogueBox=new ImageGraphics("images/sprites/dialog.png",8,2,
            new RegionOfInterest(0,0,235,42), new Vector(1,-2));

    /**
     * Resume method: sets dialoguebox for final dialogue (new depth value)
     */
    public void setFinalDialogueBox(){
        dialogueBox=new ImageGraphics("images/sprites/dialog.png",8,2,
                new RegionOfInterest(0,0,235,42), new Vector(1,-2),1.0f,0.000001f);
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(5,15);
    }

    public void unlockPortal(int i){
        portals.get(i).setState(Portal.PortalState.OPEN);
    }

    /**
     * Resume method: prints dots under portals to indicate progress
     */
    public void printGreenDots(int count, int portal){
        GreenDots = portals.get(portal).printGreenDots(count);
    }
    /**
     * Resume method: prints dots under portals to indicate progress
     */
    public ImageGraphics printRedDots(int portal) {
        RedDots = new ImageGraphics("images/sprites/zelda/3.red.dots.png", ((2.5f)), .5f,
                new RegionOfInterest(0, 64, 96, 32), portals.get(portal).getPosition());
        return RedDots;
    }

    public void draw(Canvas canvas, List<TextGraphics> dialogue){
        dialogueBox.draw(canvas);

        for(TextGraphics d:dialogue){
            d.draw(canvas);
        }

    }

    /**
     * Resume method: makes the parent of the dialogue box the player
     */
    public void setParent(ICRoguePlayer player){
        dialogueBox.setParent(player);
        dialogueBox.setAnchor(new Vector(-3f,-3f));
    }

    public void setDialogue(ICRoguePlayer player, List<TextGraphics> dialogue){
        for(int i=0;i<dialogue.size();++i){
            dialogue.get(i).setParent(player);
            double t=i;
            float k= (float) (-1.4f-(t/2.2));
            dialogue.get(i).setAnchor(new Vector(-2.8f,k));
        }

    }

    //sets up the dialogue for each character
    public void setUpDialogue(List<TextGraphics>dialogues,String name,float size){
        String message = XMLTexts.getText(name);
        TextGraphics dialogue=new TextGraphics(message,size, Color.BLACK);
        dialogue.setAnchor(new Vector(1.5f,2.3f));
        dialogues.add(dialogue);
    }

    protected void createArea() {
        // Base
        registerActor(new Background(this)) ;
        registerActor(new Foreground(this)) ;
        List<TextGraphics>dialogues=new ArrayList<>();
        setUpDialogue(dialogues,"NPC11",0.5F);
        setUpDialogue(dialogues,"NPC12",0.5F);
        registerActor(new NPC(this,new DiscreteCoordinates(20, 10), "assistant.fixed",dialogues));
        dialogues.clear();
        setUpDialogue(dialogues,"NPC21",0.5F);
        setUpDialogue(dialogues,"NPC22",0.5F);
        registerActor(new NPC(this,new DiscreteCoordinates(6, 5), "boy.1",dialogues));
        dialogues.clear();
        setUpDialogue(dialogues,"NPC31",0.5F);
        setUpDialogue(dialogues,"NPC32",0.5F);
        registerActor(new NPC(this,new DiscreteCoordinates(6, 10), "girl.1",dialogues));
        dialogues.clear();
        setUpDialogue(dialogues,"NPC41",0.47F);
        setUpDialogue(dialogues,"NPC42",0.47F);
        setUpDialogue(dialogues,"NPC43",0.47F);
        setUpDialogue(dialogues,"NPC44",0.47F);
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
