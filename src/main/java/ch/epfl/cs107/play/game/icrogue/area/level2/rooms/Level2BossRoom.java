package ch.epfl.cs107.play.game.icrogue.area.level2.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.BossTurret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Wither;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level2BossRoom extends Level2EnemyRoom {
    public Level2BossRoom(DiscreteCoordinates coordinates){
        super(coordinates);
        addEnemy(new BossTurret(this, Orientation.UP,new DiscreteCoordinates(1,8),
                false,true,false,true));
        addEnemy(new BossTurret(this, Orientation.UP,new DiscreteCoordinates(8,1),
                true,false,true,false));
        addEnemy(new Wither(this, Orientation.DOWN,new DiscreteCoordinates(3,4)));
    }
}
