CONCEPTION

Part 1: new Classes

The main bulk of the new classes added are related to the levels and the general progression of the game. That is because, in order to make the game more interesting, we decided to create a main base of operations (class MainBase) which is where the player spawns and where he gets transported back to every time he clears a level. We also added another class called Shop which, in terms of behaviour, works analogously to an ICRogueRoom but it is fixed and it spawns two NPCs that, when interacted with can increase your health and damage (up to a given maximum value). 

The main base class works similarly to the Tuto2 area (only the village), except in its behaviour we made it so that the player cannot enter a cell that is being occupied by another entity that takes up a cell space (like NPCs and portals).

The class NPC has also been added which, when interacted with, displays a message (in the form of dialogue) which is given as a parameter in its constructor. The classes Tota and Alejandro (the merchants) then are made to extend the NPC class except they have an introduction message and when interacted with they increase certain attributes of the player (Tota increases health and Alejandro damage) depending on the number of coins the player possesses.

The class Coin works similarly to a key in terms of interaction except, when interacted with, they also increase the number of coins a player possesses (int attribute in player).

The dialogues are all managed through player. When player interacts with an npc with dialogue, its dialogue attribute (of type TextGraphic) gets modified and then displayed with different updates depending on who the player interacted with and the dialogue box which changes its coordinates based on where the player is (for main base it is related to the player and in shop it is in a fixed position)

The initial dialogue (that contains instructions and a small backstory for the game) is created when the game is initialised and it has a fixed progression (which is updated in a fixed way and displayed once when the game is initialised) works the same way as normal dialogue.

To know which type of dialogue is occurring, different booleans were created in ICRoguePlayer that indicate which type of dialogue should occur.

The class Portal works in a similar way as a connector but it manages the transition between areas (the levels, main base and the shop). This is done by associating a string (which indicates the destination of that portal) to each portal when they are created.

For the level aspects, we decided to create 4 different types of levels.

The first one (Level0) works the same as the one shown in the proposed architecture except we have a different boss which functions as a turret but moves around and has more health. We also modified the turrets so there are two types. One with blue sprite which is the one provided in the given architecture and one with a red sprite that deals more damage. We also added a bow together with the Staff so the player can test both in the level 0 

The second one (Level1) works the same as Level0 except there are more rooms, the of the rooms is different (snow themed) and there are two new rooms. A bow room which works the same as a staff room but with a bow instead of a staff and a Cherry room which contains a cherry (item that can be consumed if health is not full and if it is consumed, it increases the health). Unlike other item rooms, this one is cleared even if the cherry is not picked up (since player health can be full). The boss is also different now as it is an upgraded version of the previous boss (more health and shoots flames instead of arrows).

The third one (Level2) works the same as level1 except with 4main differences. The first one is that there is now a new enemy in a new enemy room which shoots a new projectile that poisons the player on impact (explained later on in part 2). The second one is that the level is bigger (more rooms). The third one is that Cherry room now spawns two cherries instead of one. The final one is that there is a new Sword rooms that works the same as Staff room and Bow room but with a sword.

The final level (level beanos) is much simpler and spawns a fixed map (instead of random like the previous ones) which consists of a spawn room with a sword and a staff and a boss room which spawns our final and most difficult boss (class Beanos) which works like the level1 and 2 boss but shoots arrows more often and moves more often too.

Each level also has their own background sprite which is themed (level0 is the base dungeon, level1 is snow themed, level2 is poisoned themed and the final level is red). For every level, when they are completed (boss room is beaten), the player gets sent back to the base and the number of completions of that level (for level0, level1 and level2) is marked in ICRogue. For level 0,1 and 2, if they are completed 3 times, the next level is unlocked (level0 unlocks level1 and so on). The progression of clearances is indicated by a sprite that is displayed below each portal that leads to a level (except for level0 portal since it is always unlocked)

We also created a new projectile (class poisonBalls) which works the same as arrow but also poisons the player

The final addition has to do with sound effects and sound tracks. 

The soundtrack (of type SoundAcoustics) is an attribute in ICRogue which changes based on the current area. That means MainBase, Level and Shop each have their own unique soundtracks which were composed by Mateus Vital Nabholz (who is one of the two people behind this project). In addition to that, the final level (Beanos level) has its own unique soundtrack (boss music) which differs from the soundtrack of the regular levels. The soundtracks do not stop others on start (in order to make it so if a sound effect was occurring before the music starts, like a transition sound, they are not interrupted by the new soundtrack), therefore, in order to change the soundtracks and prevent them from overlapping, we added transition sound effects. The sound effects, unlike the soundtracks, do not loop and they stop others on start. We made it so in between every soundtrack change we have a transition sound effect that plays (a portal sound or a door sound which plays and stops the current soundtrack before the new one is started).

On top of that we also created normal sound effects that work the same as the transition one (they are all simply sound effects with no important differences between them). The only difference between them and transition sound effects is that they do not stop others on start (simply plays on top of the current soundtrack and does not stop it).


Part 2: modification of existing Classes

The first big modification done to the existing classes is in the projectile class. That is because, in order to determine whether an arrow was shot by an enemy (can hurt the player) or shot by the player (can hurt the enemy), a boolean was created and initialised in its constructor (which now has an extra parameter of type boolean as parameter).

The second modification is related to the sprite of the player. That is because we created a walking animation so instead of constantly setting the sprites, we just have an animation based on the players orientation which occurs when the player moves and is reset otherwise.

Also in player we made it so he can get poisoned (by interacting with poison ball) and in which case its display of hearts (which is usually red and displayed based on the player’s current health) turns to green and the player receives one extra damage a given time after it is hit (after which the player is no longer poisoned and its heart display goes back to normal).

The final alteration we did to existing classes is in the Area class. We made it so if an enemy is in the area and it dies, it spawns a coin in the cell it occupied before. The player can choose to collect those coins or not throughout the level (has no impact on the room and overall level completion)
