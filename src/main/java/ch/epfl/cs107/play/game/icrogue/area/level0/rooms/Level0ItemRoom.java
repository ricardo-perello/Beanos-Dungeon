package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.swing.Item;

import java.util.ArrayList;
import java.util.List;

public abstract class Level0ItemRoom extends Level0Room {
    private List<Item>items;

    public Level0ItemRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
        items=new ArrayList<>();
    }
    public void addItem(Item item){
        items.add(item);
    }
}
