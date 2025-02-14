package ch.epfl.cs107.play.game.icrogue.area.level1.rooms;
/*
 *  Author:  Mateus Vital Nabholz and Ricardo Perelló Mas
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level1TurretRoom extends Level1EnemyRoom {
    public Level1TurretRoom(DiscreteCoordinates coordinates){
        super(coordinates);
        addEnemy(new Turret(this, Orientation.UP,new DiscreteCoordinates(1,8),
                false,true,false,true));
        addEnemy(new Turret(this, Orientation.UP,new DiscreteCoordinates(8,1),
                true,false,true,false));
    }
}
