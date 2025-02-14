package ch.epfl.cs107.play.game.icrogue.area;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0BossRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0KeyRoom;
import ch.epfl.cs107.play.game.icrogue.area.level1.rooms.Level1BossRoom;
import ch.epfl.cs107.play.game.icrogue.area.level2.rooms.Level2BossRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;

public abstract class Level implements Logic {

    protected enum MapState{
        NULL,
        PLACED,
        EXPLORED,
        BOSS_ROOM,
        BOSS_KEYROOM,
        SPAWN_ROOM,
        CREATED;

        @Override
        public String toString(){
            return Integer.toString(ordinal());
        }
    }

    private ICRogueRoom[][] map;
    private DiscreteCoordinates arrivalcoordinates;
    private DiscreteCoordinates bossPosition;
    private String roomName;
    private int[] roomDistribution;
    private int bossRoomKeyID;


    /**
     * Resume methods: returns the ID of the bossRoom key
     * @return  getBossRoomKeyID (int), BossRoomKeyID
     *
     */
    public int getBossRoomKeyID(){
        return bossRoomKeyID;
    }
    /**
     *Resume methods: checks if its next to a boss room
     * @param coordinates (DiscreteCoordinates),
     * @return isNextToBossRoom (boolean),
     */
    public boolean isNextToBossRoom(DiscreteCoordinates coordinates){
        return (map[coordinates.x][coordinates.y] instanceof Level0BossRoom)||(map[coordinates.x][coordinates.y] instanceof Level1BossRoom)||(map[coordinates.x][coordinates.y] instanceof Level2BossRoom);
    }
    /**
     *Resume methods: returns the arrival coordinates
     * @param coordinates (DiscreteCoordinates),
     */
    public void setArrivalcoordinates(DiscreteCoordinates coordinates){
        arrivalcoordinates=coordinates;
    }
    /**
     *Resume methods: sets a room in the map table
     * @param coords (DiscreteCoordinates)
     * @param room (ICRogueRoom),
     *
     *
     */
    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room){
        map[coords.x][coords.y]=room;
    }
    /**
     *Resume methods: sets the connector of a given room
     * @param coords (int),
     * @param destination (int),
     *@param connector (ConnectorInRoom),
     *
     */
    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination,
                                               ConnectorInRoom connector){
        int idx=connector.getIndex();
        map[coords.x][coords.y].SetConnectorAreaTitle(idx, destination);

    }
    /**
     *Resume methods: sets connector of a given room and closes it
     * @param coords (int),
     * @param destination (int),
     *@param connector (ConnectorInRoom),
     *
     */
    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        setRoomConnectorDestination(coords,destination,connector);
        int idx=connector.getIndex();
        map[coords.x][coords.y].setConnectorState(idx,Connector.ConnectorState.CLOSED);
    }
    /**
     *Resume methods: locks a given connector
     * @param coords (int),
     * @param keyId (int),
     *@param connector (ConnectorInRoom),
     *
     */
    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){
        int idx=connector.getIndex();
        map[coords.x][coords.y].setConnectorState(idx,Connector.ConnectorState.LOCKED);
        map[coords.x][coords.y].setConnectorKeyID(idx,keyId);

    }
    /**
     *Resume methods: sets a room name
     * @param coordinates (DiscreteCoordinates),
     *
     */
    protected void setRoomName(DiscreteCoordinates coordinates){
        roomName=map[coordinates.x][coordinates.y].getTitle();
    }
    /**
     *Resume methods: initialises level
     * @param randomMap (boolean),
     * @param startPosition (DiscreteCoordinates),
     *
     */
    public Level(boolean randomMap,DiscreteCoordinates startPosition,
                 int[] roomDistribution, int x, int y){
        bossRoomKeyID= RandomHelper.roomGenerator.nextInt(0,50);
        if(!randomMap){
            arrivalcoordinates=startPosition;
            map=new ICRogueRoom[x][y];
            bossPosition=new DiscreteCoordinates(0,0);
            generateFixedMap();
        }
        else{
            int i=0;
            for(int room:roomDistribution){
                i+=room;
            }
            map=new ICRogueRoom[i][i];
            this.roomDistribution=roomDistribution;
            generateRandomMap();
        }

    }
    /**
     *Resume methods: initalises player and register it in the starting room
     * @param startingroom (DiscreteCoordinates),
     *@return ICRoguePlayer ,
     *
     */
    public ICRoguePlayer addPlayer(DiscreteCoordinates startingroom){
        ICRoguePlayer player=new ICRoguePlayer(map[startingroom.x][startingroom.y],Orientation.UP,new DiscreteCoordinates(2,2));
        player.enterArea(map[startingroom.x][startingroom.y],new DiscreteCoordinates(2,2));
        map[startingroom.x][startingroom.y].visit();
        return player;
    }
    /**
     *Resume methods: makes player enter given area
     * @param transitionCoor (DiscreteCoordinates),
     * @param player (ICRoguePlayer),
     *@param roomName (String),
     *
     */
    public void enterArea(DiscreteCoordinates transitionCoor, ICRoguePlayer player, String roomName){
        for(int i=0;i<map.length;++i){
            for(int k=0;k<map[i].length;++k){
                if(map[i][k]!=null){
                    if(roomName.equals(map[i][k].getTitle())){
                        player.enterArea(map[i][k],transitionCoor);
                        map[i][k].visit();
                    }
                }
            }
        }
    }
    /**
     * adds area to the game
     * @param game (ICRogue),
     */
    public void addAreas(ICRogue game){
        for(int i=0;i<map.length;++i){
            for(int k=0;k<map[i].length;++k){
                if(map[i][k] != null){
                    game.addArea(map[i][k]);
                }

            }
        }
    }
    /**
     *
     * @param coordinates (DiscreteCoordinates),
     * @return  getRoomName (String),
     *
     */
    public String getRoomName(DiscreteCoordinates coordinates){
        return map[coordinates.x][coordinates.y].getTitle();
    }

    /**
     * sets the boss position
     * @param coordinates (DiscreteCoordinates),
     *
     */
    public void setBossPosition(DiscreteCoordinates coordinates){
        bossPosition=coordinates;
    }

    /**
     *@return  generateRandomRoomPlacement (apState[][]),
     * generates a random table of mapstates which is then used to create random map
     */
    protected MapState[][] generateRandomRoomPlacement(){
        ArrayList<DiscreteCoordinates>placed=new ArrayList<>();//arraylist with the coordinates of all the placed rooms
        ArrayList<DiscreteCoordinates>explored=new ArrayList<>();//arraylist with the coordinates of explored rooms


        MapState[][]output=new MapState[map.length][map[0].length];//creates MapState array with same size as the final map with rooms
        //decides the room to place based on the size of the map (since we decided the size of the map will be [n of rooms][n of rooms]
        int roomsToPlace=map.length;
        for(int i=0;i<map.length;++i){//makes all the entries of MapState table into MapState.NULL
            for(int k=0;k<map[i].length;++k){
                output[i][k]=MapState.NULL;
            }
        }
        output[(map.length+1)/2][(map[(map.length+1)/2].length+1)/2]=MapState.PLACED;//makes middle room placed
        placed.add(new DiscreteCoordinates ((map.length+1)/2,(map[(map.length+1)/2].length+1)/2));//adds the coordinates to the placed array
        --roomsToPlace;

        while (roomsToPlace>0){
            //goes through the placed array and everytime a new room is placed, the array increases
            //this means that it will go through the array until no more rooms are added and it reaches the end
            for(int i=0;i<placed.size();++i){
                int xcor=placed.get(i).x;
                int ycor=placed.get(i).y;
                output[xcor][ycor]=MapState.EXPLORED;
                explored.add(new DiscreteCoordinates(xcor,ycor));//adds coordinates to explored array

                //checks the free spots
                int freeSlots=4;
                ArrayList<DiscreteCoordinates>roomCoorAvailable= new ArrayList<>();
                if(xcor+1>=output.length||!output[xcor+1][ycor].equals(MapState.NULL)){
                    --freeSlots;
                }
                else{
                    roomCoorAvailable.add(new DiscreteCoordinates(xcor+1,ycor));
                }
                if(xcor-1<0||!output[xcor-1][ycor].equals(MapState.NULL)){
                    --freeSlots;
                }
                else{
                    roomCoorAvailable.add(new DiscreteCoordinates(xcor-1,ycor));
                }
                if(ycor+1>=output[0].length||!output[xcor][ycor+1].equals(MapState.NULL)){
                    --freeSlots;
                }
                else{
                    roomCoorAvailable.add(new DiscreteCoordinates(xcor,ycor+1));
                }
                if(ycor-1<0||!output[xcor][ycor-1].equals(MapState.NULL)){
                    --freeSlots;
                }
                else{
                    roomCoorAvailable.add(new DiscreteCoordinates(xcor,ycor-1));
                }
                //if the n of rooms that can be placed(Min(freeSlots,roomsToPlace)) is greater than 1, a random number between one and the min will be picked
                if(freeSlots>1&&roomsToPlace>1){
                    int rooms= RandomHelper.roomGenerator.nextInt(1,Math.min(freeSlots,roomsToPlace));

                    if(roomCoorAvailable.size()>0){
                        //creates and int array that has all the possible indexes of the coordinates array
                        //this is done so the chooseKInList method can be used since it takes an int array as parameter
                        ArrayList<Integer>nOfCoor=new ArrayList<>();
                        for(int z=0;z<roomCoorAvailable.size();++z){
                            nOfCoor.add(z);
                        }
                        //picks random indexes from the coordinate array using the random function (to place random rooms around the current room)
                        List<Integer> indexFromCoorArray=RandomHelper.chooseKInList(rooms,nOfCoor);
                        //places the rooms in the coordinates picked from the previous line
                        // (indexFromCoorArray represents the indexes that will be picked from the roomCoorAvailable)
                            for (Integer integer : indexFromCoorArray) {
                                int x=roomCoorAvailable.get(integer).x;
                                int y=roomCoorAvailable.get(integer).y;
                                if(roomsToPlace>0){//only places the room if there are still rooms to be placed
                                    output[x][y] = MapState.PLACED;//places the room
                                    roomsToPlace--;
                                    placed.add(new DiscreteCoordinates(x,y));//adds the new placed room coordinates to the placed array
                                }
                            }
                    }
                }
                //there is only one coordinate available, then it chooses that one coordinate and does the same as above but with 1 room
                else if(freeSlots==1||(roomsToPlace==1&&freeSlots!=0)){
                    int rooms= 1;

                    if(roomCoorAvailable.size()>0){
                        ArrayList<Integer>nOfCoor=new ArrayList<>();
                        for(int z=0;z<roomCoorAvailable.size();++z){
                            nOfCoor.add(z);
                        }
                        List<Integer> indexFromCoorArray=RandomHelper.chooseKInList(rooms,nOfCoor);
                            for (Integer integer : indexFromCoorArray) {
                                int x=roomCoorAvailable.get(integer).x;
                                int y=roomCoorAvailable.get(integer).y;
                                if(roomsToPlace>0){
                                    output[x][y] = MapState.PLACED;
                                    roomsToPlace--;
                                    placed.add(new DiscreteCoordinates(x,y));
                                }
                            }
                    }
                }


            }
        }

        //places the special rooms in random places, the bossRoom is placed the furthest away from the spawn room
        //this is done to avoid unplayable maps where the key is inaccessible and the level is impossible to complete
        int spawnindx=RandomHelper.roomGenerator.nextInt(0,placed.size());
        DiscreteCoordinates spawnroomCoords=placed.get(spawnindx);
        placed.remove(spawnindx);

        DiscreteCoordinates bossroomCoords=farthestRoom(placed,spawnroomCoords);
        placed.remove(bossroomCoords);

        int keyindx=RandomHelper.roomGenerator.nextInt(0,placed.size());
        DiscreteCoordinates keyroomCoords=placed.get(keyindx);
        placed.remove(keyindx);


        output[keyroomCoords.x][keyroomCoords.y]=MapState.BOSS_KEYROOM;

        bossPosition=bossroomCoords;
        output[bossroomCoords.x][bossroomCoords.y]=MapState.BOSS_ROOM;
        output[spawnroomCoords.x][spawnroomCoords.y]=MapState.SPAWN_ROOM;

        return output;
    }
    /**
     *finds the farthest room in a given mapstate from a given room. used to create boss room
     * @param placed (List<DiscreteCoordinates>),
     * @param room (int),
     * @return farthestRoom (DiscreteCoordinates),
     *
     */
    //finds fartherst room from a give coordinate in a given array
    private DiscreteCoordinates farthestRoom(List<DiscreteCoordinates>placed,DiscreteCoordinates room){
        double dist=0;
        DiscreteCoordinates output=room;
        for(DiscreteCoordinates coor:placed){
            double xdist=coor.x-room.x;
            double ydist=coor.y-room.y;
            double distance=Math.sqrt(xdist*xdist+ydist*ydist);
            if(distance>dist){
                dist=distance;
                output=coor;
            }
        }
        return output;
    }
    /**
     *@return  isOn (boolean),
     *checks if bossroom has been cleared
     */
    public boolean isOn() {
        if(map[bossPosition.x][bossPosition.y]==null){
            return false;
        }
        else{
            return map[bossPosition.x][bossPosition.y].isOn();
        }

    }
    /**
     *@return  isOff (boolean),
     *checks if bossroom has been cleared
     */
    public boolean isOff() {
        if(map[bossPosition.x][bossPosition.y]==null){
            return false;
        }
        else{
            return map[bossPosition.x][bossPosition.y].isOff();
        }
    }
    /**
     *@return  getIntensity (float),
     *
     */
    public float getIntensity() {
        return 0;
    }
    /**
     *@return  isResolved (boolean),
     *
     */
    public boolean isResolved(){
        return isOff()&&!isOn();
    }

    public abstract void generateFixedMap();

    public abstract void generateRandomMap();
}
