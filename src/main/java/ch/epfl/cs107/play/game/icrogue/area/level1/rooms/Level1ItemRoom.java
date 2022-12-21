package ch.epfl.cs107.play.game.icrogue.area.level1.rooms;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public abstract class Level1ItemRoom extends Level1Room {
    private List<Item>items;

    public Level1ItemRoom(DiscreteCoordinates coordinates) {
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
        if(this instanceof Level1CherryRoom){
            return super.isOn();
        }
        else{
            return super.isOn()||(items.size()>0);
        }
    }

    public boolean isOff() {
        if(this instanceof Level1CherryRoom){
            return super.isOff();
        }
        else{
            return super.isOff()&&(items.size()==0);
        }
    }
}
