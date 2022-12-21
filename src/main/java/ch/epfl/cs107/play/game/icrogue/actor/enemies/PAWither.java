package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Random;

public class PAWither extends Enemy{

    private Sprite sprite;
    private ImageGraphics greyHealthBar;
    private ImageGraphics redHealthBar;
    private String spriteName = "icrogue/yellow_static_npc";
    private boolean shootUp;
    private boolean shootDown;
    private boolean shootLeft;
    private boolean shootRight;
    public final static float COOLDOWN = 2.f;
    private float FireCounter = 2.f;
    private float MoveCounter = 1.f;
    private int hp = 10;

    public PAWither(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);

        isAlive = true;
        sprite= setSprite();

        printGreyHealthBar();
        printRedHealthBar();

    }

    public Sprite setSprite() {
        Sprite witherSprite;
        witherSprite = new Sprite(spriteName,1.5f,1.5f, this);
        return witherSprite;
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        if (getHp() > 0){
            printGreyHealthBar().draw(canvas);
            printRedHealthBar().draw(canvas);
        }
    }
    @Override
    public void update(float deltaTime){
        Random rand = new Random();
        FireCounter += deltaTime;
        MoveCounter += deltaTime;
        if (FireCounter >= COOLDOWN){
            shootArrow();
            FireCounter = 0;
        }
        if(MoveCounter >= COOLDOWN){
            Integer [] arrays = {0,1,2,3};
            Integer array = arrays[rand.nextInt(arrays.length)];
            if(!isDisplacementOccurs()){
                Orientation orientationMovement = Orientation.fromInt(array);
                orientate(orientationMovement);
                move(5);
                MoveCounter = 0;
            }
        }
        if (hp <= 0){
            die();
        }
        super.update(deltaTime);
    }

    private void shootArrow() {
        for(Orientation i : Orientation.values()){
            DiscreteCoordinates spawnPosition;
            if(i.equals(Orientation.UP)){
                spawnPosition = new DiscreteCoordinates(getCurrentMainCellCoordinates().x , getCurrentMainCellCoordinates().y+1);
            }
            else if(i.equals(Orientation.DOWN)){
                spawnPosition = new DiscreteCoordinates(getCurrentMainCellCoordinates().x , getCurrentMainCellCoordinates().y-1);
            }
            else if(i.equals(Orientation.RIGHT)){
                spawnPosition = new DiscreteCoordinates(getCurrentMainCellCoordinates().x+1 , getCurrentMainCellCoordinates().y);
            }
            else{
                spawnPosition = new DiscreteCoordinates(getCurrentMainCellCoordinates().x-1 , getCurrentMainCellCoordinates().y);
            }
            Arrow arrow = new Arrow(getOwnerArea(),i, spawnPosition,  true, 1);
            arrow.enterArea(getOwnerArea(),spawnPosition);

        }
    }
    public void decreaseHp(int delta){
        if (hp - delta > 0) {
            hp -= delta;
        }
        else{
            hp = 0;
        }
        System.out.println("PA wither: "+hp);
    }

    public void increaseHp(float delta){
        hp += delta;
    }


    private ImageGraphics printRedHealthBar(){
        redHealthBar = new ImageGraphics("images/sprites/zelda/red.health.bar.png", ((.9f) * ((float)getHp()/10)) ,
                .03f, new RegionOfInterest(0,0,(int)(100 * ((double)getHp()/10)),5), new Vector(((getPosition().x)+0.25f),((getPosition().y) -0.1f)));
        return redHealthBar;
    }
    private ImageGraphics printGreyHealthBar(){
        greyHealthBar = new ImageGraphics("images/sprites/zelda/grey.health.bar.png", .9f,.03f,
                new RegionOfInterest(0,0,100,5),  new Vector(((getPosition().x)+0.25f),((getPosition().y) - 0.1f)));
        return greyHealthBar;
    }
    public float getHp(){
        return hp;
    }

    public boolean takeCellSpace(){
        return true;
    }

    public boolean isViewInteractable(){
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (getIsAlive()) {
            ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
        }
    }
}
