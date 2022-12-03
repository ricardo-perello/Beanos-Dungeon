package ch.epfl.cs107.play.game.icrogue.actor.projectiles;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Projectile;

public class Fire extends Projectile{

    private Sprite sprite;

    public Fire(Area area, Orientation orientation, DiscreteCoordinates coordinates){
        super(area,orientation,coordinates,DEFAULT_DAMAGE,5);
    }

    @Override
    public void setSprite() {
        sprite=new Sprite("zelda/fire",1f,1f,this,
                new RegionOfInterest(0,0,16,16), new Vector(0,0));
    }
}
