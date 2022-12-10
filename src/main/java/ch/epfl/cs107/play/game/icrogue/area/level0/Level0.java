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

public class Level0 extends Level {
    public enum Level0RoomType{
        TurretRoom,
        StaffRoom,
        Boss_Key,
        Boss,
        Spawn,
        Normal;
    }
    public static DiscreteCoordinates startingroom=new DiscreteCoordinates(1,0);
    private static final DiscreteCoordinates arrivalCoordinates=new DiscreteCoordinates(2,0);
    private static int[] roomArrangement;

    public Level0() {
        super(true,startingroom,setroomArrangement(), 4, 2);
    }

    public Level0(boolean randomMap) {
        super(randomMap,startingroom,setroomArrangement(), 4, 2);
    }

    public static int[] setroomArrangement(){
        roomArrangement = new int[Level0RoomType.values().length];
        roomArrangement[0]=RandomHelper.roomGenerator.nextInt(0,1);
        roomArrangement[1]=1;
        roomArrangement[2]=1;
        roomArrangement[3]=1;
        roomArrangement[4]=1;
        roomArrangement[5]=RandomHelper.roomGenerator.nextInt(0,1);
        return roomArrangement;
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

    public void generateRandomMap(){
        MapState[][]mapStates=generateRandomRoomPlacement();
        printMap(mapStates);
        ArrayList<DiscreteCoordinates>roomsToCreate=new ArrayList<>();

        for(int i=0;i<mapStates.length;++i){
            for(int k=0;k<mapStates[i].length;++k){
                if(mapStates[i][k].equals(MapState.EXPLORED)){
                    roomsToCreate.add(new DiscreteCoordinates(i,k));
                }
            }
        }

        ArrayList<Level0Room>rooms=new ArrayList<>();

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

        randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
        coordinates=roomsToCreate.get(randomcoor);
        xcor=roomsToCreate.get(randomcoor).x;
        ycor=roomsToCreate.get(randomcoor).y;
        Level0KeyRoom keyRoom=new Level0KeyRoom(roomsToCreate.get(randomcoor),1);
        rooms.add(keyRoom);
        setRoom(coordinates,keyRoom);
        mapStates[xcor][ycor]=MapState.CREATED;
        roomsToCreate.remove(randomcoor);

        randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
        coordinates=roomsToCreate.get(randomcoor);
        xcor=roomsToCreate.get(randomcoor).x;
        ycor=roomsToCreate.get(randomcoor).y;
        Level0BossRoom bossRoom=new Level0BossRoom(roomsToCreate.get(randomcoor));
        rooms.add(bossRoom);
        setRoom(coordinates,bossRoom);
        mapStates[xcor][ycor]=MapState.CREATED;
        roomsToCreate.remove(randomcoor);

        randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
        coordinates=roomsToCreate.get(randomcoor);
        xcor=roomsToCreate.get(randomcoor).x;
        ycor=roomsToCreate.get(randomcoor).y;
        Level0Room Room=new Level0Room(roomsToCreate.get(randomcoor));
        startingroom=coordinates;
        rooms.add(Room);
        setRoom(coordinates,Room);
        mapStates[xcor][ycor]=MapState.CREATED;
        roomsToCreate.remove(randomcoor);

        for(int i=0;i<roomArrangement[5];++i){
            randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
            coordinates=roomsToCreate.get(randomcoor);
            xcor=roomsToCreate.get(randomcoor).x;
            ycor=roomsToCreate.get(randomcoor).y;
            Room=new Level0Room(coordinates);
            rooms.add(Room);
            setRoom(coordinates,Room);
            mapStates[xcor][ycor]=MapState.CREATED;
            roomsToCreate.remove(randomcoor);
        }

        for(Level0Room room:rooms){
            setUpConnector(mapStates,room);
        }



    }

    protected void setUpConnector(MapState[][] roomsplacement,ICRogueRoom room){
        DiscreteCoordinates roomCoordinates=room.getCoordinates();
        int xcor=roomCoordinates.x;
        int ycor=roomCoordinates.y;
        if(xcor<roomsplacement.length-1&&(roomsplacement[xcor+1][ycor].equals(MapState.PLACED)||
                roomsplacement[xcor+1][ycor].equals(MapState.CREATED)||
                roomsplacement[xcor+1][ycor].equals(MapState.EXPLORED))){
            DiscreteCoordinates destination=roomCoordinates.jump(Orientation.RIGHT.toVector());
            String destinationName= getRoomName(destination);
            setRoomConnector(roomCoordinates,destinationName,Level0Room.Level0Connectors.E);
            if(room instanceof Level0BossRoom){
                lockRoomConnector(roomCoordinates,Level0Room.Level0Connectors.E,1);
            }
        }
        if(xcor>0&&(roomsplacement[xcor-1][ycor].equals(MapState.PLACED)||
                roomsplacement[xcor-1][ycor].equals(MapState.CREATED)||
                roomsplacement[xcor-1][ycor].equals(MapState.EXPLORED))){
            DiscreteCoordinates destination=roomCoordinates.jump(Orientation.LEFT.toVector());
            String destinationName= getRoomName(destination);
            setRoomConnector(roomCoordinates,destinationName,Level0Room.Level0Connectors.W);
            if(room instanceof Level0BossRoom){
                lockRoomConnector(roomCoordinates,Level0Room.Level0Connectors.W,1);
            }
        }
        if(ycor<roomsplacement[0].length-1&&(roomsplacement[xcor][ycor+1].equals(MapState.PLACED)||
                roomsplacement[xcor][ycor+1].equals(MapState.CREATED)||
                roomsplacement[xcor][ycor+1].equals(MapState.EXPLORED))){
            DiscreteCoordinates destination=roomCoordinates.jump(Orientation.UP.toVector());
            String destinationName= getRoomName(destination);
            setRoomConnector(roomCoordinates,destinationName,Level0Room.Level0Connectors.N);
            if(room instanceof Level0BossRoom){
                lockRoomConnector(roomCoordinates,Level0Room.Level0Connectors.N,1);
            }
        }
        if(ycor>0&&(roomsplacement[xcor][ycor-1].equals(MapState.PLACED)||
                roomsplacement[xcor][ycor-1].equals(MapState.CREATED)||
                roomsplacement[xcor][ycor-1].equals(MapState.EXPLORED))){
            DiscreteCoordinates destination=roomCoordinates.jump(Orientation.DOWN.toVector());
            String destinationName= getRoomName(destination);
            setRoomConnector(roomCoordinates,destinationName,Level0Room.Level0Connectors.S);
            if(room instanceof Level0BossRoom){
                lockRoomConnector(roomCoordinates,Level0Room.Level0Connectors.S,1);
            }
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
