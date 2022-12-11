package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Bow;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.items.Sword;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.tutosSolution.actor.SimpleGhost;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class Level0Room extends ICRogueRoom {
    public enum Level0Connectors implements ConnectorInRoom{
        W(new DiscreteCoordinates(0,4),
                new DiscreteCoordinates(8,5),Orientation.LEFT),
        S(new DiscreteCoordinates(4,0),
                new DiscreteCoordinates(5,8),Orientation.DOWN),
        E(new DiscreteCoordinates(9,4),
                new DiscreteCoordinates(1,5),Orientation.RIGHT),
        N(new DiscreteCoordinates(4,9),
                new DiscreteCoordinates(5,1),Orientation.UP);

        private DiscreteCoordinates position;
        private DiscreteCoordinates destination;
        private Orientation orientation;

        Level0Connectors(DiscreteCoordinates coordinates, DiscreteCoordinates coordinates1, Orientation orientation) {
            position=coordinates;
            destination=coordinates1;
            this.orientation=orientation;
        }

        public int getIndex() {
            return ordinal();
        }


        public DiscreteCoordinates getDestination() {
            return destination;
        }
        public static List<Orientation>getAllConnectorsOrientation(){
            List<Orientation>output=new ArrayList<>();
            output.add(Orientation.RIGHT);
            output.add(Orientation.UP);
            output.add(Orientation.LEFT);
            output.add(Orientation.DOWN);
            return output;

        }
        public static List<DiscreteCoordinates>getAllConnectorsPosition(){
            List<DiscreteCoordinates>output=new ArrayList<>();
            output.add(new DiscreteCoordinates(0,4));
            output.add(new DiscreteCoordinates(4,0));
            output.add(new DiscreteCoordinates(9,4));
            output.add(new DiscreteCoordinates(4,9));
            return output;
        }
        public static List<DiscreteCoordinates>getAllConnectorsDestination(){
            List<DiscreteCoordinates>output=new ArrayList<>();
            output.add(new DiscreteCoordinates(8,5));
            output.add(new DiscreteCoordinates(5,8));
            output.add(new DiscreteCoordinates(1,5));
            output.add(new DiscreteCoordinates(5,1));
            return output;
        }
    }

    public Level0Room(DiscreteCoordinates coordinates){
        super(Level0Connectors.getAllConnectorsPosition(), Level0Connectors.getAllConnectorsOrientation(),Level0Connectors.getAllConnectorsDestination(),
                "icrogue/Level0Room", coordinates);
    }

    public String getTitle() {
        return "icrogue/level0"+getCoordinatesString();
    }

    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(2,0);
    }

    protected void createArea() {
        super.createArea();
        // Base
        registerActor(new Background(this, getBehaviourName())) ;

        /*
        registerActor(new Cherry(this, Orientation.DOWN, new DiscreteCoordinates(6,3)));
        registerActor(new Sword(this, Orientation.DOWN, new DiscreteCoordinates(7, 3)));
        registerActor(new Bow(this, Orientation.DOWN, new DiscreteCoordinates(7, 7)));
        registerActor(new Turret(this, Orientation.DOWN, new DiscreteCoordinates(1, 1)
                ,true, false, false, true));

         */


    }

}
