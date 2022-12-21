package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;
/*
 *  Author:  Mateus Vital Nabholz and Ricardo Perelló Mas
 *  Date:
 */

import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Level0EnemyRoom extends Level0Room{
    private List<Enemy> enemies;


    public Level0EnemyRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
        enemies=new ArrayList<>();
    }
    protected void addEnemy(Enemy enemy){
        enemies.add(enemy);
    }

    protected void createArea() {
        super.createArea();
        // Base
        for(Enemy enemy:enemies){
            registerActor(enemy);
        }
    }

    public void update(float deltaTime) {
        enemies.removeIf(enemy -> !enemy.getIsAlive());
        super.update(deltaTime);
    }

    public boolean isOn() {
        return super.isOn()||(enemies.size()>0);
    }

    public boolean isOff() {
        return super.isOff()&&(enemies.size()==0);
    }
}
