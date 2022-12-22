package ch.epfl.cs107.play.game.icrogue;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.window.Window;
public class ICRogueBehavior extends AreaBehavior{
    public enum ICRogueCellType{
        NULL(0, false),
        GROUND(-16777216, true),
        WALL(-14112955, false),
        HOLE(-65536, true);

        final int type;
        final boolean isWalkable;
        /**
         * Default ICRogueCellType Constructor
         * @param type (int), not null
         * @param isWalkable (boolean): isWalkable
         */
        ICRogueCellType(int type, boolean isWalkable){
            this.type = type;
            this.isWalkable = isWalkable;
        }
        /**
         * toType
         * @param type (int)
         */
        public static ICRogueCellType toType(int type){
            for(ICRogueCellType ict : ICRogueCellType.values()){
                if(ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            System.out.println(type);
            return NULL;
        }
    }

    /**
     *
     * @param window (Window), not null
     * @param name (String): Name of the Behavior, not null
     *
     */
    public ICRogueBehavior(Window window, String name){
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                ICRogueCellType color = ICRogueCellType.toType(getRGB(height-1-y, x));
                setCell(x,y, new ICRogueCell(x,y,color));
            }
        }
    }

    /**
     * Cell adapted to the Tuto2 game
     */
    public class ICRogueCell extends Cell {
        /// Type of the cell following the enum
        private final ICRogueCellType type;

        /**
         *
         * @param x (int): x coordinate of the cell
         * @param y (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */
        public  ICRogueCell(int x, int y, ICRogueCellType type){
            super(x, y);
            this.type = type;
        }

        public ICRogueCellType getType(){
            return type;
        }
        /**
         *
         * @param entity (Interactable), not null
         * @return (boolean): canLeave
         */
        protected boolean canLeave(Interactable entity) {
            return true;
        }
        /**
         *
         * @param entity (Interactable), not null
         * @return (boolean): canEnter
         */
        protected boolean canEnter(Interactable entity) {
            for(Interactable elements: entities){
                if(elements.takeCellSpace()){
                    return false;
                }
                else {
                    return true;
                }
            }

            return type.isWalkable;
        }
        /**
         *
         * @param v (AreaInteractionVisitor), not null
         * @param isCellInteraction (boolean): canLeave
         */
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
                ((ICRogueInteractionHandler)v).interactWith(this, isCellInteraction);

        }

        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }


    }
}
