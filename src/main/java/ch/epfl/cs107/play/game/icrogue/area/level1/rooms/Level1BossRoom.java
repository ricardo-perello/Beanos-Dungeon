package ch.epfl.cs107.play.game.icrogue.area.level1.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.BossTurret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level1BossRoom extends Level1EnemyRoom {
    public Level1BossRoom(DiscreteCoordinates coordinates){
        super(coordinates);
        addEnemy(new BossTurret(this, Orientation.UP,new DiscreteCoordinates(5,5),
                true,true,true,true));
        addEnemy(new BossTurret(this, Orientation.UP,new DiscreteCoordinates(4,4),
                true,true,true,true));
    }
}
