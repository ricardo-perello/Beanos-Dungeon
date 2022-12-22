package ch.epfl.cs107.play.game.icrogue.area;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area implements Logic {
    private ICRogueBehavior behavior;
    private DiscreteCoordinates coordinates;
    private String behaviourName;
    private boolean wasVisited;
    private final ImageGraphics dialogueBox=new ImageGraphics("images/sprites/dialog.png",8,2,
            new RegionOfInterest(0,0,235,42), new Vector(1,.8f));
    private ArrayList<Connector>connectors=new ArrayList<>();
    /**

     *@param   connectorsCoordinates (List<DiscreteCoordinates>),
     *@param   orientations (List<Orientation>);
     *@param destinationCoordinates (List<DiscreteCoordinates>),
     *@param behaviourName  (String),
     *@param roomCoordinates (DiscreteCoordinates)
     */
    public ICRogueRoom(List<DiscreteCoordinates> connectorsCoordinates,
                       List<Orientation> orientations,List<DiscreteCoordinates>destinationCoordinates,
                       String behaviourName, DiscreteCoordinates roomCoordinates){
        this.behaviourName=behaviourName;
        coordinates=roomCoordinates;
        for(int i=0;i<connectorsCoordinates.size();++i){
            Connector connector=new Connector(this,orientations.get(i),connectorsCoordinates.get(i));
            connector.setArrivalcoordinates(destinationCoordinates.get(i));
            connectors.add(connector);
        }
        wasVisited=false;

    }
    /**

     *@param  canvas (Canvas),
     *@param  dialogue (List<TextGraphics>),
     */
    public void draw(Canvas canvas, List<TextGraphics> dialogue){
        dialogueBox.draw(canvas);
        for(TextGraphics d:dialogue){
            d.draw(canvas);
        }
    }

    /**
     *@return  getCoordinatesString (String),
     *
     */
    public String getCoordinatesString(){
        return coordinates.x+""+coordinates.y;
    }
    /**
     *@return  getCoordinates (DiscreteCoordinates),
     *
     */
    public DiscreteCoordinates getCoordinates(){return coordinates;}




    public void SetConnectorAreaTitle (int index, String title){
        connectors.get(index).setAreaTitle(title);
    }
    public void SetConnectorArrivalCoordinates (int index, DiscreteCoordinates coordinates){
        connectors.get(index).setArrivalcoordinates(coordinates);
    }

    public void setConnectorState(int idx, Connector.ConnectorState state){
        connectors.get(idx).setState(state);
    }

    public void setConnectorKeyID(int idx, int ID){
        connectors.get(idx).setID(ID);
    }

    public void setConnectors(int i,Connector connector){
        connectors.set(i,connector);
    }

    public String getBehaviourName(){
        return behaviourName;
    }

    public void visit(){
        wasVisited=true;
    }

    /**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */
    protected void createArea(){
        for(Connector connector:connectors){
            registerActor(connector);
        }
    }

    /// EnigmeArea extends Area

    public final float getCameraScaleFactor() {
        return 11;
    }

    public abstract DiscreteCoordinates getPlayerSpawnPosition();

    /// Demo2Area implements Playable

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new ICRogueBehavior(window, getBehaviourName());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

    public void update(float deltaTime) {
        if(isOff()&&!isOn()){
            for(Connector connector:connectors){
                if(connector.compareState(Connector.ConnectorState.CLOSED)){
                    connector.setState(Connector.ConnectorState.OPEN);
                }
            }
        }

        super.update(deltaTime);

    }

    public boolean isOn() {
        return !wasVisited;
    }

    public boolean isOff() {
        return wasVisited;
    }

    public float getIntensity() {
        return 0;
    }

}
