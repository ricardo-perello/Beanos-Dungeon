package ch.epfl.cs107.play.game.icrogue.handler;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.*;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Beanos;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.BossTurret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Wither;
import ch.epfl.cs107.play.game.icrogue.actor.items.*;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.*;
import ch.epfl.cs107.play.window.Keyboard;

public interface ICRogueInteractionHandler extends AreaInteractionVisitor {
    default void interactWith(Interactable other, boolean isCellInteraction){
    }
    default void interactWith(Portal portal, boolean isCellInteraction){
    }
    default void interactWith(Fire fire, boolean isCellInteraction) {
    }
    default void interactWith(Arrow arrow, boolean isCellInteraction) {}

    default void interactWith(Staff staff, boolean isCellInteraction) {
    }
    default void interactWith(Sword sword, boolean isCellInteraction){
    }

    default void interactWith(Bow bow, boolean isCellInteraction){
    }
    default void interactWith(Cherry cherry, boolean isCellInteraction) {}

    default void interactWith(Coin coin, boolean isCellInteraction) {}
    default void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
    }
    default void interactWith(ICRoguePlayer player, boolean isCellInteraction){
    }
    default void interactWith(Turret turret, boolean isCellInteraction){}
    default void interactWith(BossTurret turret, boolean isCellInteraction){}

    default void interactWith(Wither wither, boolean isCellInteraction){}
    default void interactWith(Beanos beanos, boolean isCellInteraction){}
    default void interactWith(Key key, boolean isCellInteraction){
    }
    default void interactWith(Connector connector, boolean isCellInteraction){
    }
    default void interactWith(NPC npc, boolean isCellInteraction){
    }
    default void interactWith(Tota tota, boolean isCellInteraction){
    }
    default void interactWith(Alejandro alejandro, boolean isCellInteraction){
    }
    default void interactWith(Lever lever, boolean isCellInteraction){
    }
}
