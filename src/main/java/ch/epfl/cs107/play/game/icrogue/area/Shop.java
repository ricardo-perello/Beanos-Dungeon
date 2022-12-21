package ch.epfl.cs107.play.game.icrogue.area;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.*;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Shop extends Area {

    private Portal portals;
    private List<Connector> connectorList=new ArrayList<>();

    private ICRogueBehavior behavior;

    private final ImageGraphics dialogueBox=new ImageGraphics("images/sprites/dialog.png",8,2,
            new RegionOfInterest(0,0,235,42), new Vector(1,.8f));

    public String getTitle() {
        return "icrogue/Shop";
    }



    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(4,1);
    }

    public void setUpDialogue(List<TextGraphics>dialogues,String name,int i){
        String message = XMLTexts.getText(name);
        TextGraphics dialogue=new TextGraphics(message,0.5F, Color.BLACK);
        if(i==1){
            dialogue.setAnchor(new Vector(1.5f,2.3f));
        }
        else if(i==2){
            dialogue.setAnchor(new Vector(1.5f,1.7f));
        }
        else if(i==3){
            dialogue.setAnchor(new Vector(1.5f,1.4f));
        }
        dialogues.add(dialogue);
    }

    protected void createArea() {
        // Shop

        registerActor(new Background(this)) ;
        List<TextGraphics>dialogues=new ArrayList<>();
        setUpDialogue(dialogues,"Tota_upgrade",1);
        setUpDialogue(dialogues,"price",2);
        setUpDialogue(dialogues,"Y/N",3);
        registerActor(new Tota(this,new DiscreteCoordinates(6,5), "policeman",dialogues));
        dialogues.clear();
        setUpDialogue(dialogues,"Alej_upgrade",1);
        setUpDialogue(dialogues,"price",2);
        setUpDialogue(dialogues,"Y/N",3);
        registerActor(new Alejandro(this,new DiscreteCoordinates(3, 5), "max",dialogues));
        portals=new Portal(this,Orientation.DOWN,new DiscreteCoordinates(4,0),"shop");
        connectorList.add(new Connector(this,Orientation.DOWN,new DiscreteCoordinates(4,9)));
        connectorList.add(new Connector(this,Orientation.RIGHT,new DiscreteCoordinates(0,4)));
        connectorList.add(new Connector(this,Orientation.LEFT,new DiscreteCoordinates(9,4)));



        registerActor(portals);
        for(Connector connector:connectorList){
            registerActor(connector);
        }
    }

    public void draw(Canvas canvas, List<TextGraphics> dialogue){
        dialogueBox.draw(canvas);
        for(TextGraphics d:dialogue){
            d.draw(canvas);
        }

    }


    @Override
    public float getCameraScaleFactor() {
        return 11;
    }
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new ICRogueBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }
}