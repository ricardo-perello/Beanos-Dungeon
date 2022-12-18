package ch.epfl.cs107.play.game.icrogue.area;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.NPC;
import ch.epfl.cs107.play.game.icrogue.actor.Portal;
import ch.epfl.cs107.play.game.tutosSolution.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutosSolution.area.Tuto2Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class MainBase extends Tuto2Area {

    private ArrayList<Portal> portals=new ArrayList<>();

    public String getTitle() {
        return "zelda/Village";
    }


    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(5,15);
    }

    public void unlockPortal(int i){
        portals.get(i).setState(Portal.PortalState.OPEN);
    }

    protected void createArea() {
        // Base

        registerActor(new Background(this)) ;
        registerActor(new Foreground(this)) ;
        registerActor(new NPC(this,new DiscreteCoordinates(20, 10), "assistant.fixed"));
        registerActor(new NPC(this,new DiscreteCoordinates(10, 10), "boy.1"));
        registerActor(new NPC(this,new DiscreteCoordinates(6, 10), "girl.1"));
        registerActor(new NPC(this,new DiscreteCoordinates(20, 10), "joel.fixed"));
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

        for(Portal portal:portals){
            this.registerActor(portal);
        }
    }



}
