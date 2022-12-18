package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow2;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class BossTurret extends Enemy{

    private Sprite sprite;
    private ImageGraphics greyHealthBar;
    private ImageGraphics redHealthBar;
    private String spriteName = "icrogue/static_npc";
    private boolean shootUp;
    private boolean shootDown;
    private boolean shootLeft;
    private boolean shootRight;
    public final static float COOLDOWN = 1.7f;
    private float counter = 0.f;
    private int hp = 5;

    public BossTurret(Area owner, Orientation orientation, DiscreteCoordinates coordinates, boolean sUp
            , boolean sDown, boolean sLeft, boolean sRight) {
        super(owner, orientation, coordinates);

        shootUp = sUp;
        shootDown = sDown;
        shootLeft = sLeft;
        shootRight = sRight;

        isAlive = true;

        sprite=new Sprite("icrogue/static_npc", 1.f,1.f,this,
                new RegionOfInterest(0,0,16,24), new Vector(-0.05f,0.25f));

        printGreyHealthBar();
        printRedHealthBar();
    }

    public void shootArrow(){
        if (shootUp){
            Arrow2 arrow = new Arrow2(getOwnerArea(), Orientation.UP,
                new DiscreteCoordinates(getCurrentMainCellCoordinates().x
                ,(getCurrentMainCellCoordinates().y)+1));

            arrow.enterArea(getOwnerArea(),new DiscreteCoordinates(getCurrentMainCellCoordinates().x
                    , (getCurrentMainCellCoordinates().y)+1));
        }
        if (shootDown){
            Arrow2 arrow = new Arrow2(getOwnerArea(), Orientation.DOWN,
                    new DiscreteCoordinates(getCurrentMainCellCoordinates().x
                    ,(getCurrentMainCellCoordinates().y)-1));


            arrow.enterArea(getOwnerArea(),new DiscreteCoordinates(getCurrentMainCellCoordinates().x
                    ,(getCurrentMainCellCoordinates().y)-1));
        }
        if (shootLeft){
            Arrow2 arrow = new Arrow2(getOwnerArea(), Orientation.LEFT,
                    new DiscreteCoordinates((getCurrentMainCellCoordinates().x-1)
                    ,(getCurrentMainCellCoordinates().y)));

            arrow.enterArea(getOwnerArea(),new DiscreteCoordinates((getCurrentMainCellCoordinates().x-1)
                    ,getCurrentMainCellCoordinates().y));
        }
        if (shootRight){
            Arrow2 arrow = new Arrow2(getOwnerArea(), Orientation.RIGHT,
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



    private ImageGraphics printRedHealthBar(){
        redHealthBar = new ImageGraphics("images/sprites/zelda/red.health.bar.png", ((.9f) * (getHp() /5)) ,
                .03f, new RegionOfInterest(0,0,(int)(100 * ((double)getHp()/5)),5),
                new Vector(((getPosition().x)+0.03f),((getPosition().y) + 0.07f)));
        return redHealthBar;
    }
    private ImageGraphics printGreyHealthBar(){
        greyHealthBar = new ImageGraphics("images/sprites/zelda/grey.health.bar.png", .9f,.03f,
                new RegionOfInterest(0,0,100,5),  new Vector(((getPosition().x)+0.03f)
                , ((getPosition().y) + 0.07f)));
        return greyHealthBar;
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
        if (getHp() > 0){
            printGreyHealthBar().draw(canvas);
            printRedHealthBar().draw(canvas);
        }

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
