package ch.epfl.cs107.play.game.icrogue.area.level1;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.area.level1.rooms.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

public class Level1 extends Level {
    public enum Level1RoomType{
        TurretRoom,
        StaffRoom,
        Boss_Key,
        Boss,
        Spawn,
        LeverRoom,
        Normal;

        public static int[] setroomArrangement(){
            int size=Level1RoomType.values().length;
            int[] roomArrangement = new int[size];
            roomArrangement[0]=RandomHelper.roomGenerator.nextInt(2,3);
            roomArrangement[1]=1;
            roomArrangement[2]=1;
            roomArrangement[3]=1;
            roomArrangement[4]=1;
            roomArrangement[5]=RandomHelper.roomGenerator.nextInt(1,2);
            roomArrangement[6]=RandomHelper.roomGenerator.nextInt(1,3);
            return roomArrangement;
        }
    }
    public static DiscreteCoordinates startingroom=new DiscreteCoordinates(1,0);
    private static final DiscreteCoordinates arrivalCoordinates=new DiscreteCoordinates(2,0);
    private static int[] roomArrangement;

    public Level1() {
        super(true,startingroom,createRoomArrangement(), 4, 2);
    }

    private static int[]createRoomArrangement(){
        roomArrangement=Level1RoomType.setroomArrangement();
        return roomArrangement;
    }

    public Level1(boolean randomMap) {
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

    //works the same as in level0
    public void generateRandomMap(){
        MapState[][]mapStates=generateRandomRoomPlacement();
        printMap(mapStates);
        ArrayList<DiscreteCoordinates>roomsToCreate=new ArrayList<>();
        ArrayList<Level1Room>rooms=new ArrayList<>();

        for(int i=0;i<mapStates.length;++i){
            for(int k=0;k<mapStates[i].length;++k){
                if(mapStates[i][k].equals(MapState.PLACED)||mapStates[i][k].equals(MapState.EXPLORED)){
                    roomsToCreate.add(new DiscreteCoordinates(i,k));
                }

                else if(mapStates[i][k].equals(MapState.BOSS_ROOM)){
                    DiscreteCoordinates coordinates=new DiscreteCoordinates(i,k);
                    Level1BossRoom bossRoom=new Level1BossRoom(coordinates);
                    rooms.add(bossRoom);
                    setRoom(coordinates,bossRoom);
                    mapStates[i][k]=MapState.CREATED;
                }

                else if(mapStates[i][k].equals(MapState.BOSS_KEYROOM)){
                    DiscreteCoordinates coordinates=new DiscreteCoordinates(i,k);
                    Level1KeyRoom keyRoom=new Level1KeyRoom(coordinates,getBossRoomKeyID());
                    rooms.add(keyRoom);
                    setRoom(coordinates,keyRoom);
                    mapStates[i][k]=MapState.CREATED;
                }
                else if(mapStates[i][k].equals(MapState.SPAWN_ROOM)){
                    DiscreteCoordinates coordinates=new DiscreteCoordinates(i,k);
                    Level1Room Room=new Level1Room(coordinates);
                    rooms.add(Room);
                    setRoom(coordinates,Room);
                    startingroom=coordinates;
                    mapStates[i][k]=MapState.CREATED;
                }
            }
        }



        for(int i=0;i<roomArrangement[0];++i){
            int randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
            DiscreteCoordinates coordinates=roomsToCreate.get(randomcoor);
            int xcor=roomsToCreate.get(randomcoor).x;
            int ycor=roomsToCreate.get(randomcoor).y;
            Level1TurretRoom turretRoom=new Level1TurretRoom(roomsToCreate.get(randomcoor));
            rooms.add(turretRoom);
            setRoom(coordinates,turretRoom);
            mapStates[xcor][ycor]=MapState.CREATED;
            roomsToCreate.remove(randomcoor);
        }

        int randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
        DiscreteCoordinates coordinates=roomsToCreate.get(randomcoor);
        int xcor=roomsToCreate.get(randomcoor).x;
        int ycor=roomsToCreate.get(randomcoor).y;
        Level1StaffRoom staffRoom=new Level1StaffRoom(roomsToCreate.get(randomcoor));
        rooms.add(staffRoom);
        setRoom(coordinates,staffRoom);
        mapStates[xcor][ycor]=MapState.CREATED;
        roomsToCreate.remove(randomcoor);

        for(int i=0;i<roomArrangement[5];++i){
            randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
            coordinates=roomsToCreate.get(randomcoor);
            xcor=roomsToCreate.get(randomcoor).x;
            ycor=roomsToCreate.get(randomcoor).y;
            Level1Room Room=new Level1PuzzleRoom(coordinates);
            rooms.add(Room);
            setRoom(coordinates,Room);
            mapStates[xcor][ycor]=MapState.CREATED;
            roomsToCreate.remove(randomcoor);
        }

        for(int i=0;i<roomArrangement[6];++i){
            randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
            coordinates=roomsToCreate.get(randomcoor);
            xcor=roomsToCreate.get(randomcoor).x;
            ycor=roomsToCreate.get(randomcoor).y;
            Level1Room Room=new Level1Room(coordinates);
            rooms.add(Room);
            setRoom(coordinates,Room);
            mapStates[xcor][ycor]=MapState.CREATED;
            roomsToCreate.remove(randomcoor);
        }


        printMap(mapStates);

        for(Level1Room room:rooms){
            setUpConnector(mapStates,room);
        }



    }
    //works the same as in level0
    private void setConnectorsGeneral(Orientation orientation, Level1Room.Level1Connectors connectors, DiscreteCoordinates coordinates){
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
            setConnectorsGeneral(Orientation.RIGHT,Level1Room.Level1Connectors.E,roomCoordinates);
        }
        if(xcor>0&&(roomsplacement[xcor-1][ycor].equals(MapState.CREATED)||
                roomsplacement[xcor-1][ycor].equals(MapState.EXPLORED)||roomsplacement[xcor-1][ycor].equals(MapState.PLACED))){
            setConnectorsGeneral(Orientation.LEFT,Level1Room.Level1Connectors.W,roomCoordinates);
        }
        if(ycor<roomsplacement[0].length-1&&(roomsplacement[xcor][ycor+1].equals(MapState.CREATED)||
                roomsplacement[xcor][ycor+1].equals(MapState.EXPLORED)||roomsplacement[xcor][ycor+1].equals(MapState.PLACED))){
            setConnectorsGeneral(Orientation.UP,Level1Room.Level1Connectors.N,roomCoordinates);
        }
        if(ycor>0 &&(roomsplacement[xcor][ycor-1].equals(MapState.CREATED)||
                roomsplacement[xcor][ycor-1].equals(MapState.EXPLORED)||roomsplacement[xcor][ycor-1].equals(MapState.PLACED))){
            setConnectorsGeneral(Orientation.DOWN,Level1Room.Level1Connectors.S,roomCoordinates);
        }


    }

    private void generateMap1() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level1KeyRoom(room00, 1));
        setRoomConnector(room00, "icrogue/level110", Level1Room.Level1Connectors.E);
        lockRoomConnector(room00, Level1Room.Level1Connectors.E,  1);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1, 0);
        setRoom(room10, new Level1Room(room10));
        setRoomConnector(room10, "icrogue/level100", Level1Room.Level1Connectors.W);
    }

    public void generateFinalMap(){
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level1TurretRoom(room00));
        setRoomConnector(room00, "icrogue/level110", Level1Room.Level1Connectors.E);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level1Room(room10));
        setRoomConnector(room10, "icrogue/level111", Level1Room.Level1Connectors.S);
        setRoomConnector(room10, "icrogue/level120", Level1Room.Level1Connectors.E);

        lockRoomConnector(room10, Level1Room.Level1Connectors.W,  2);
        setRoomConnectorDestination(room10, "icrogue/level100", Level1Room.Level1Connectors.W);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2,0);
        setRoom(room20,  new Level1StaffRoom(room20));
        setRoomConnector(room20, "icrogue/level110", Level1Room.Level1Connectors.W);
        setRoomConnector(room20, "icrogue/level130", Level1Room.Level1Connectors.E);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level1KeyRoom(room30, 2));
        setRoomConnector(room30, "icrogue/level120", Level1Room.Level1Connectors.W);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom (room11, new Level1Room(room11));
        setRoomConnector(room11, "icrogue/level110", Level1Room.Level1Connectors.N);
    }
}
