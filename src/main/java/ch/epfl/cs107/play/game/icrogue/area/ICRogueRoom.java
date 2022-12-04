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
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area {
    private ICRogueBehavior behavior;
    private DiscreteCoordinates coordinates;
    private String behaviourName;
    private ArrayList<Connector>connectors=new ArrayList<>();
    public ICRogueRoom(List<DiscreteCoordinates> connectorsCoordinates,
                       List<Orientation> orientations,
                       String behaviourName, DiscreteCoordinates roomCoordinates){
        this.behaviourName=behaviourName;
        coordinates=roomCoordinates;
        for(int i=0;i<connectorsCoordinates.size();++i){
            connectors.add(new Connector(this,orientations.get(i),connectorsCoordinates.get(i)));
        }

    }

    public String getCoordinatesString(){
        return coordinates.x+""+coordinates.y;
    }


    public String getBehaviourName(){
        return behaviourName;
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

    @Override
    public final float getCameraScaleFactor() {
        return 11;
    }

    public abstract DiscreteCoordinates getPlayerSpawnPosition();

    /// Demo2Area implements Playable

    @Override
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
        Keyboard keyboard= getKeyboard();
        if(keyboard.get(Keyboard.O).isPressed()){
            for(Connector connector:connectors){
                connector.setState(Connector.ConnectorState.OPEN);
            }
        }
        if(keyboard.get(Keyboard.L).isPressed()){
            connectors.get(0).setState(Connector.ConnectorState.LOCKED);
            connectors.get(0).setID(1);
        }
        if(keyboard.get(Keyboard.T).isPressed()){
            for(Connector connector:connectors){
                if(connector.getState().equals(Connector.ConnectorState.OPEN)){
                    connector.setState(Connector.ConnectorState.CLOSED);
                }
                else if(connector.getState().equals(Connector.ConnectorState.CLOSED)){
                    connector.setState(Connector.ConnectorState.OPEN);
                }
                else if(connector.getState().equals(Connector.ConnectorState.LOCKED)){
                    connector.setState(Connector.ConnectorState.INVISIBLE);
                    connector.setID(Connector.NO_KEY_ID);
                }
                else if(connector.getState().equals(Connector.ConnectorState.INVISIBLE)){
                    connector.setState(Connector.ConnectorState.LOCKED);
                    connector.setID(1);
                }




            }
        }
        super.update(deltaTime);

    }

}
