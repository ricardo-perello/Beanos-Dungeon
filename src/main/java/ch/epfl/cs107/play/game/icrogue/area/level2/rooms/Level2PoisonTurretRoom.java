package ch.epfl.cs107.play.game.icrogue.area.level2.rooms;
/*
 *  Author:  Mateus Vital Nabholz and Ricardo Perelló Mas
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.PoisonTurret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level2PoisonTurretRoom extends Level2EnemyRoom {
    public Level2PoisonTurretRoom(DiscreteCoordinates coordinates){
        super(coordinates);
        addEnemy(new PoisonTurret(this, Orientation.UP,new DiscreteCoordinates(1,8),
                false,true,false,true));
        addEnemy(new PoisonTurret(this, Orientation.UP,new DiscreteCoordinates(8,1),
                true,false,true,false));
    }
}
