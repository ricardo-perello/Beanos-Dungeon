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
        for(int i=0;i<connectorsCoordinates.size();++i){
            connectors.add(new Connector(this,orientations.get(i),connectorsCoordinates.get(i)));
        }
        this.behaviourName=behaviourName;
        coordinates=roomCoordinates;
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

}
