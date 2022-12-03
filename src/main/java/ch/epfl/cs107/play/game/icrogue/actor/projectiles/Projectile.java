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

public abstract class Projectile extends ICRogueActor {
    private int frames;
    private int damage;
    private boolean isConsumed;
    public final int DEFAULT_MOVE_DURATION=10;
    public final int DEFAULT_DAMAGE=1;

    public Projectile(Area area, Orientation orientation, DiscreteCoordinates coordinates,
                      int damagepts, int nframes){
        super(area,orientation,coordinates);
        damage=damagepts;
        frames=nframes;
    }

    public Projectile(Area area, Orientation orientation, DiscreteCoordinates coordinates){
        super(area,orientation,coordinates);
        damage=DEFAULT_DAMAGE;
        frames=DEFAULT_MOVE_DURATION;
    }



    public void update(float deltaTime) {

        move(frames);
        super.update(deltaTime);
    }
}
