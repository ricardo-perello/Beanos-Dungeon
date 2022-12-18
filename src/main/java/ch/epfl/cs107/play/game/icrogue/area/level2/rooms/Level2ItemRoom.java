package ch.epfl.cs107.play.game.icrogue.area.level2.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public abstract class Level2ItemRoom extends Level2Room {
    private List<Item>items;

    public Level2ItemRoom(DiscreteCoordinates coordinates) {
        super(coordinates);
        items=new ArrayList<>();
    }
    public void addItem(Item item){
        items.add(item);
    }

    protected void createArea() {
        super.createArea();
        // Base
        for(Item item:items){
            registerActor(item);
        }
    }

    public void update(float deltaTime) {
        items.removeIf(CollectableAreaEntity::isCollected);
        super.update(deltaTime);
    }

    public boolean isOn() {
        return super.isOn()||(items.size()>0);
    }

    public boolean isOff() {
        return super.isOff()&&(items.size()==0);
    }
}
