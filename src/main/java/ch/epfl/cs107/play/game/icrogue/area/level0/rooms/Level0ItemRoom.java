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
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.game.icrogue.actor.items.Sword;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public abstract class Level0ItemRoom extends Level0Room {
    private List<Item>items;

    public Level0ItemRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
        items=new ArrayList<>();
    }
    public void addItem(ch.epfl.cs107.play.game.icrogue.actor.items.Item item){
        items.add(item);
    }

    protected void createArea() {
        super.createArea();
        // Base
        for(Item item:items){
            registerActor(item);
        }
    }
}
