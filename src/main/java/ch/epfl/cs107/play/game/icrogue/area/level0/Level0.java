package ch.epfl.cs107.play.game.icrogue.area.level0;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Level0 extends Level {
     enum Level0RoomType{
        TurretRoom,
        StaffRoom,
        Boss_Key,
        Boss,
        Spawn,
        Normal;

        public static int[] setroomArrangement(){
            int size=Level0RoomType.values().length;
            int[] roomArrangement = new int[size];
            roomArrangement[0]=RandomHelper.roomGenerator.nextInt(1,2);
            roomArrangement[1]=1;
            roomArrangement[2]=1;
            roomArrangement[3]=1;
            roomArrangement[4]=1;
            roomArrangement[5]=RandomHelper.roomGenerator.nextInt(1,3);
            return roomArrangement;
        }
    }
    public static DiscreteCoordinates startingroom=new DiscreteCoordinates(1,0);
    private static final DiscreteCoordinates arrivalCoordinates=new DiscreteCoordinates(2,0);
    private static int[] roomArrangement;

    public Level0() {
        super(true,startingroom,createRoomArrangement(), 4, 2);
    }

    private static int[]createRoomArrangement(){
        roomArrangement=Level0RoomType.setroomArrangement();
        return roomArrangement;
    }

    public Level0(boolean randomMap) {
        super(randomMap,startingroom,roomArrangement, 4, 2);
    }

    public void generateFixedMap(){
        generateFinalMap();
    }

    public void printMap(MapState[][] map){
        System.out.println("generated map:");

        System.out.print(" / ");
        for(int j=0;j<map[0].length;++j){
            System.out.print(j+" ");
        }
        System.out.println();
        System.out.print("--/-");
        for(int j=0;j<map[0].length;j++){
            System.out.println("--");
        }
        System.out.println();

        for(int i=0;i<map.length;i++){
            System.out.print(i+" / ");
            for(int j=0;j<map[i].length;++j){
                System.out.print(map[j][i]+ " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    //generates the map based on MapStates and the roomDistribution table
    public void generateRandomMap(){
        MapState[][]mapStates=generateRandomRoomPlacement();
        printMap(mapStates);
        List<DiscreteCoordinates> roomsToCreate=new ArrayList<>();
        List<Level0Room>rooms=new ArrayList<>();

        for(int i=0;i<mapStates.length;++i){
            for(int k=0;k<mapStates[i].length;++k){
                if(mapStates[i][k].equals(MapState.PLACED)||mapStates[i][k].equals(MapState.EXPLORED)){
                    roomsToCreate.add(new DiscreteCoordinates(i,k));//adds coordinates to an array of coordinates
                }

                else if(mapStates[i][k].equals(MapState.BOSS_ROOM)){//creates boss room
                    DiscreteCoordinates coordinates=new DiscreteCoordinates(i,k);
                    Level0BossRoom bossRoom=new Level0BossRoom(coordinates);
                    rooms.add(bossRoom);
                    setRoom(coordinates,bossRoom);
                    mapStates[i][k]=MapState.CREATED;
                }

                else if(mapStates[i][k].equals(MapState.BOSS_KEYROOM)){//creates key room
                    DiscreteCoordinates coordinates=new DiscreteCoordinates(i,k);
                    Level0KeyRoom keyRoom=new Level0KeyRoom(coordinates,getBossRoomKeyID());
                    rooms.add(keyRoom);
                    setRoom(coordinates,keyRoom);
                    mapStates[i][k]=MapState.CREATED;
                }
                else if(mapStates[i][k].equals(MapState.SPAWN_ROOM)){//creates spawn room and sets startingroom coordinates
                    DiscreteCoordinates coordinates=new DiscreteCoordinates(i,k);
                    Level0Room Room=new Level0Room(coordinates);
                    rooms.add(Room);
                    setRoom(coordinates,Room);
                    startingroom=coordinates;
                    mapStates[i][k]=MapState.CREATED;
                }
            }
        }


        //creates the room based on the roomArrangement table (entry 0 corresponds to turret room and so on)
        for(int i=0;i<roomArrangement[0];++i){
            int randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
            DiscreteCoordinates coordinates=roomsToCreate.get(randomcoor);
            int xcor=roomsToCreate.get(randomcoor).x;
            int ycor=roomsToCreate.get(randomcoor).y;
            Level0TurretRoom turretRoom=new Level0TurretRoom(roomsToCreate.get(randomcoor));
            rooms.add(turretRoom);
            setRoom(coordinates,turretRoom);
            mapStates[xcor][ycor]=MapState.CREATED;
            roomsToCreate.remove(randomcoor);
        }

        int randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
        DiscreteCoordinates coordinates=roomsToCreate.get(randomcoor);
        int xcor=roomsToCreate.get(randomcoor).x;
        int ycor=roomsToCreate.get(randomcoor).y;
        Level0StaffRoom staffRoom=new Level0StaffRoom(roomsToCreate.get(randomcoor));
        rooms.add(staffRoom);
        setRoom(coordinates,staffRoom);
        mapStates[xcor][ycor]=MapState.CREATED;
        roomsToCreate.remove(randomcoor);

        for(int i=0;i<roomArrangement[5];++i){
            randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
            coordinates=roomsToCreate.get(randomcoor);
            xcor=roomsToCreate.get(randomcoor).x;
            ycor=roomsToCreate.get(randomcoor).y;
            Level0Room Room=new Level0Room(coordinates);
            rooms.add(Room);
            setRoom(coordinates,Room);
            mapStates[xcor][ycor]=MapState.CREATED;
            roomsToCreate.remove(randomcoor);
        }


        printMap(mapStates);

        //set up the connectors for all the rooms placed
        for(Level0Room room:rooms){
            setUpConnector(mapStates,room);
        }



    }


    //sets the connector destination based on orientation, the connector and the coordinates given as parameter
    //used in setUpConnector
    private void setConnectorsGeneral(Orientation orientation, Level0Room.Level0Connectors connectors, DiscreteCoordinates coordinates){
        DiscreteCoordinates destination=coordinates.jump(orientation.toVector());
        String destinationName= getRoomName(destination);
        setRoomConnector(coordinates,destinationName,connectors);
        boolean bossDestination=isNextToBossRoom(destination);
        if(bossDestination){//checks if the next room is a boss room
            lockRoomConnector(coordinates,connectors,getBossRoomKeyID());
        }
    }

    protected void setUpConnector(MapState[][] roomsplacement,ICRogueRoom room){
        DiscreteCoordinates roomCoordinates=room.getCoordinates();
        int xcor=roomCoordinates.x;
        int ycor=roomCoordinates.y;
        //checks the neighboring rooms
        if(xcor<roomsplacement.length-1&&(roomsplacement[xcor+1][ycor].equals(MapState.CREATED)||
                roomsplacement[xcor+1][ycor].equals(MapState.EXPLORED)||roomsplacement[xcor+1][ycor].equals(MapState.PLACED))){
            setConnectorsGeneral(Orientation.RIGHT,Level0Room.Level0Connectors.E,roomCoordinates);
        }
        if(xcor>0&&(roomsplacement[xcor-1][ycor].equals(MapState.CREATED)||
                roomsplacement[xcor-1][ycor].equals(MapState.EXPLORED)||roomsplacement[xcor-1][ycor].equals(MapState.PLACED))){
            setConnectorsGeneral(Orientation.LEFT,Level0Room.Level0Connectors.W,roomCoordinates);
        }
        if(ycor<roomsplacement[0].length-1&&(roomsplacement[xcor][ycor+1].equals(MapState.CREATED)||
                roomsplacement[xcor][ycor+1].equals(MapState.EXPLORED)||roomsplacement[xcor][ycor+1].equals(MapState.PLACED))){
            setConnectorsGeneral(Orientation.UP,Level0Room.Level0Connectors.N,roomCoordinates);
        }
        if(ycor>0 &&(roomsplacement[xcor][ycor-1].equals(MapState.CREATED)||
                roomsplacement[xcor][ycor-1].equals(MapState.EXPLORED)||roomsplacement[xcor][ycor-1].equals(MapState.PLACED))){
            setConnectorsGeneral(Orientation.DOWN,Level0Room.Level0Connectors.S,roomCoordinates);
        }


    }

    private void generateMap1() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0KeyRoom(room00, 1));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);
        lockRoomConnector(room00, Level0Room.Level0Connectors.E,  1);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1, 0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level000", Level0Room.Level0Connectors.W);
    }

    public void generateFinalMap(){
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0TurretRoom(room00));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level011", Level0Room.Level0Connectors.S);
        setRoomConnector(room10, "icrogue/level020", Level0Room.Level0Connectors.E);

        lockRoomConnector(room10, Level0Room.Level0Connectors.W,  2);
        setRoomConnectorDestination(room10, "icrogue/level000", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2,0);
        setRoom(room20,  new Level0StaffRoom(room20));
        setRoomConnector(room20, "icrogue/level010", Level0Room.Level0Connectors.W);
        setRoomConnector(room20, "icrogue/level030", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level0KeyRoom(room30, 2));
        setRoomConnector(room30, "icrogue/level020", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom (room11, new Level0Room(room11));
        setRoomConnector(room11, "icrogue/level010", Level0Room.Level0Connectors.N);
    }
}
