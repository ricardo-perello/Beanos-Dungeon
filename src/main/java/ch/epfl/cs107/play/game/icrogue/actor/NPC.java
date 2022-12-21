package ch.epfl.cs107.play.game.icrogue.actor;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NPC extends ICRogueActor {
    private Sprite sprite;
    private List<TextGraphics> dialogue=new ArrayList<TextGraphics>();
    public static final int PRICE=5;

    private boolean isFirstInteraction=true;

    public void interacted(){
        isFirstInteraction=false;
    }

    public boolean isFirstInteraction(){
        return isFirstInteraction;
    }

    public NPC(Area area, DiscreteCoordinates position, String spriteName,List<TextGraphics> dialogue) {
        //super(position, new ImageGraphics(ResourcePath.getSprite(spriteName),  1.0f,1.0f, null, Vector.ZERO, 1.0f, -Float.MAX_VALUE));
        super(area, Orientation.DOWN,position);
        sprite = new Sprite(spriteName, .75f,1f,this,
                new RegionOfInterest(0,0,16,21), new Vector(.15f,-.15f));

        int i=0;
        for(TextGraphics d:dialogue){
            this.dialogue.add(d);
            ++i;
        }
    }
    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    public List<TextGraphics> getDialogue(List<TextGraphics> dialogue){//returns the current player dialogue so it can be displayed through player
        for(int i=0;i<this.dialogue.size();++i){
            dialogue.add(this.dialogue.get(i));
        }
        return dialogue;
    }

    protected void setDialogue(TextGraphics dialogue){
        this.dialogue.add(dialogue);
    }

    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler)v).interactWith(this, isCellInteraction);
    }

    public boolean takeCellSpace() {
        return true;
    }
    @Override
    public boolean isViewInteractable() {
        return true;
    }
}
