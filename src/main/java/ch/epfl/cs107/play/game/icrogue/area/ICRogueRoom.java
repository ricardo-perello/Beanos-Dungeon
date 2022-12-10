package ch.epfl.cs107.play.game.icrogue.area;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area implements Logic {
    private ICRogueBehavior behavior;
    private DiscreteCoordinates coordinates;
    private String behaviourName;
    private boolean wasVisited;
    private ArrayList<Connector>connectors=new ArrayList<>();
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

    public String getCoordinatesString(){
        return coordinates.x+""+coordinates.y;
    }

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
