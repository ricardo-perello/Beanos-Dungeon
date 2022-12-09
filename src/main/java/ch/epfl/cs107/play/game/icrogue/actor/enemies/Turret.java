package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
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
    private float counter = 2.f;
    private int hp = 5;

    public Turret(Area owner, Orientation orientation, DiscreteCoordinates coordinates, boolean sUp
            ,boolean sDown, boolean sLeft, boolean sRight) {
        super(owner, orientation, coordinates);

        shootUp = sUp;
        shootDown = sDown;
        shootLeft = sLeft;
        shootRight = sRight;

        isAlive = true;

        sprite=new Sprite("icrogue/static_npc", 1.f,1.f,this,
                new RegionOfInterest(0,0,16,24), new Vector(0.f,0.f));

    }

    public void shootArrow(){
        if (shootUp){
            Arrow arrow = new Arrow(getOwnerArea(), Orientation.UP,
                new DiscreteCoordinates(getCurrentMainCellCoordinates().x
                ,(getCurrentMainCellCoordinates().y)+1));

            arrow.enterArea(getOwnerArea(),new DiscreteCoordinates(getCurrentMainCellCoordinates().x
                    , (getCurrentMainCellCoordinates().y)+1));
        }
        if (shootDown){
            Arrow arrow = new Arrow(getOwnerArea(), Orientation.DOWN,
                    new DiscreteCoordinates(getCurrentMainCellCoordinates().x
                    ,(getCurrentMainCellCoordinates().y)-1));


            arrow.enterArea(getOwnerArea(),new DiscreteCoordinates(getCurrentMainCellCoordinates().x
                    ,(getCurrentMainCellCoordinates().y)-1));
        }
        if (shootLeft){
            Arrow arrow = new Arrow(getOwnerArea(), Orientation.LEFT,
                    new DiscreteCoordinates((getCurrentMainCellCoordinates().x-1)
                    ,(getCurrentMainCellCoordinates().y)));

            arrow.enterArea(getOwnerArea(),new DiscreteCoordinates((getCurrentMainCellCoordinates().x-1)
                    ,getCurrentMainCellCoordinates().y));
        }
        if (shootRight){
            Arrow arrow = new Arrow(getOwnerArea(), Orientation.RIGHT,
                    new DiscreteCoordinates((getCurrentMainCellCoordinates().x+1)
                    ,(getCurrentMainCellCoordinates().y)));

            arrow.enterArea(getOwnerArea(),new DiscreteCoordinates((getCurrentMainCellCoordinates().x+1)
                    ,(getCurrentMainCellCoordinates().y)));
        }
    }

    @Override
    public void update(float deltaTime){
        counter += deltaTime;
        if (counter >= COOLDOWN){
            shootArrow();
            counter = 0;
        }
        if (hp <= 0){
            die();
        }

        super.update(deltaTime);
    }

    public void decreaseHp(int delta){
        if (hp - delta > 0) {
            hp -= delta;
        }
        else{
            hp = 0;
        }
        System.out.println("turret: "+hp);
    }

    public void increaseHp(float delta){
        hp += delta;
    }

    public float getHp(){
        return hp;
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }


    public boolean takeCellSpace(){
        return true;
    }

    public boolean isViewInteractable(){
        return true;
    }

    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (getIsAlive()) {
            ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
        }
    }

}
