package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.tutosSolution.actor.SimpleGhost;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.Vector;

public class Level0Room extends ICRogueRoom {

    public Level0Room(DiscreteCoordinates coordinates){
        super("icrogue/Level0Room",coordinates);
    }

    public String getTitle() {
        return "icrogue/level0"+getCoordinatesStrin();
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(0,0);
    }

    protected void createArea() {
        // Base
        registerActor(new Background(this, getBehaviourName())) ;
        registerActor(new Cherry(this, Orientation.DOWN, new DiscreteCoordinates(6,3)));
        registerActor(new Staff(this, Orientation.DOWN, new DiscreteCoordinates(4,3)));
    }

}
