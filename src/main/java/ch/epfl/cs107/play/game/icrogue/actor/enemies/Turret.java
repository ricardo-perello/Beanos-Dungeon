package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Turret extends Enemy{

    private Sprite sprite;
    private String spriteName = "icrogue/static_npc";
    private boolean shootUp;
    private boolean shootDown;
    private boolean shootLeft;
    private boolean shootRight;
    public final static float COOLDOWN = 2.f;
    private float counter = 0;

    public Turret(Area owner, Orientation orientation, DiscreteCoordinates coordinates, boolean sUp
            ,boolean sDown, boolean sLeft, boolean sRight) {
        super(owner, orientation, coordinates);

        shootUp = sUp;
        shootDown = sDown;
        shootLeft = sLeft;
        shootRight = sRight;

        sprite=new Sprite("icrogue/static_npc", 1.f,1.f,this,
                new RegionOfInterest(0,0,16,24), new Vector(0.f,0.f));

        shootArrow();

    }

    public void shootArrow(){
        if (shootUp){
            Arrow arrow = new Arrow(getOwnerArea(), Orientation.UP,getCurrentMainCellCoordinates());
            arrow.enterArea(getOwnerArea(),getCurrentMainCellCoordinates());
        }
        if (shootDown){
            Arrow arrow = new Arrow(getOwnerArea(), Orientation.DOWN,getCurrentMainCellCoordinates());
            arrow.enterArea(getOwnerArea(),getCurrentMainCellCoordinates());
        }
        if (shootLeft){
            Arrow arrow = new Arrow(getOwnerArea(), Orientation.LEFT,getCurrentMainCellCoordinates());
            arrow.enterArea(getOwnerArea(),getCurrentMainCellCoordinates());
        }
        if (shootRight){
            Arrow arrow = new Arrow(getOwnerArea(), Orientation.RIGHT,getCurrentMainCellCoordinates());
            arrow.enterArea(getOwnerArea(),getCurrentMainCellCoordinates());
        }
    }

    @Override
    public void update(float deltaTime){
        counter += deltaTime;
        if (counter >= COOLDOWN){
            shootArrow();
            counter = 0;
        }

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        //todo add interaction of getting hit by player
    }
}
