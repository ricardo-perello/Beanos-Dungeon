package ch.epfl.cs107.play.game.icrogue.handler;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.items.*;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public interface ICRogueInteractionHandler extends AreaInteractionVisitor {
    default void interactWith(Interactable other, boolean isCellInteraction){
    }
    default void interactWith(Fire fire, boolean isCellInteraction) {
    }
    default void interactWith(Arrow arrow, boolean isCellInteraction) {
    }
    default void interactWith(Staff staff, boolean isCellInteraction) {
    }
    default void interactWith(Sword sword, boolean isCellInteraction){
    }
    default void interactWith(Bow bow, boolean isCellInteraction){
    }
    default void interactWith(Cherry cherry, boolean isCellInteraction) {
    }
    default void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
    }
    default void interactWith(ICRoguePlayer player, boolean isCellInteraction){

    }
    default void interactWith(Key key, boolean isCellInteraction){

    }

}
