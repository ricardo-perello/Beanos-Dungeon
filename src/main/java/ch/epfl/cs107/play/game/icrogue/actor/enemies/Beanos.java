package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Random;

public class Beanos extends Enemy{

    private Sprite sprite;
    private ImageGraphics fullHearts;
    private ImageGraphics emptyHearts;
    private String spriteName = "beanos";
    private boolean shootUp;
    private boolean shootDown;
    private boolean shootLeft;
    private boolean shootRight;
    public final static float COOLDOWN = 2.f;
    private float FireCounter = 2.f;
    private float MoveCounter = 1.f;
    private int hp = 10;

    public Beanos(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);

        isAlive = true;
        sprite= setSprite();

        printEmptyHearts();
        printFullHearts();

    }

    public Sprite setSprite() {
        Sprite witherSprite;
        witherSprite = new Sprite(spriteName,2.f,2.f, this
                    ,new RegionOfInterest(200,50,400, 300), new Vector(0,0));

        return witherSprite;
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        if (getHp() > 0){
            printEmptyHearts().draw(canvas);
            printFullHearts().draw(canvas);
        }
    }
    @Override
    public void update(float deltaTime){
        Random rand = new Random();
        FireCounter += deltaTime;
        MoveCounter += deltaTime;
        if (FireCounter >= COOLDOWN){
            shootFire();
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

    private void shootFire() {
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
            Fire fire = new Fire(getOwnerArea(),i, spawnPosition, "zelda/flameskull",true);

        }
    }
    public void decreaseHp(int delta){
        if (hp - delta > 0) {
            hp -= delta;
        }
        else{
            hp = 0;
        }
        System.out.println("beanos: "+hp);
    }

    public void increaseHp(float delta){
        hp += delta;
    }


    private ImageGraphics printFullHearts(){
        fullHearts = new ImageGraphics("images/sprites/zelda/5.full.purple.hearts.png", ((2.5f) * ((float)getHp()/10)) ,.5f,
                new RegionOfInterest(0,0,(int)(80 * ((double)getHp()/10)),16), new Vector(7.f,.25f));
        return fullHearts;
    }
    private ImageGraphics printEmptyHearts() {
        emptyHearts = new ImageGraphics("images/sprites/zelda/5.empty.purple.hearts.png", (2.5f), .5f,
                new RegionOfInterest(0, 0, 80 , 16), new Vector(7.f, .25f));
        return emptyHearts;
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
