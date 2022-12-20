package ch.epfl.cs107.play.game.icrogue.area;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.NPC;
import ch.epfl.cs107.play.game.icrogue.actor.Portal;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2Behavior;
import ch.epfl.cs107.play.game.tutosSolution.area.Tuto2Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public class Shop extends Area {

    private Portal portals;
    private List<Connector> connectorList=new ArrayList<>();

    private ICRogueBehavior behavior;

    public String getTitle() {
        return "icrogue/Shop";
    }



    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(4,1);
    }

    protected void createArea() {
        // Shop

        registerActor(new Background(this)) ;
        registerActor(new NPC(this,new DiscreteCoordinates(6,5), "boy.1"));
        registerActor(new NPC(this,new DiscreteCoordinates(3, 5), "girl.1"));
        portals=new Portal(this,Orientation.DOWN,new DiscreteCoordinates(4,0),"shop");
        connectorList.add(new Connector(this,Orientation.DOWN,new DiscreteCoordinates(4,9)));
        connectorList.add(new Connector(this,Orientation.RIGHT,new DiscreteCoordinates(0,4)));
        connectorList.add(new Connector(this,Orientation.LEFT,new DiscreteCoordinates(9,4)));



        registerActor(portals);
        for(Connector connector:connectorList){
            registerActor(connector);
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