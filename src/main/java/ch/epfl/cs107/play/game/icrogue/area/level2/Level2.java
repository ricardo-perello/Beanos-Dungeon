package ch.epfl.cs107.play.game.icrogue.area.level2;
/*
 *  Author:  Mateus Vital Nabholz
 *  Date:
 */

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.area.level1.rooms.Level1Room;
import ch.epfl.cs107.play.game.icrogue.area.level2.rooms.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

/**
 * Resume methods: work the same as in level0 but with different room types
 */
public class Level2 extends Level {
    public enum Level2RoomType{
        TurretRoom,
        StaffRoom,
        Boss_Key,
        Boss,
        Spawn,
        Normal,
        BowRoom,
        SwordRoom,
        PoisonTurretRoom,
        CherryRoom;

        public static int[] setroomArrangement(){
            int size=Level2RoomType.values().length;
            int[] roomArrangement = new int[size];
            roomArrangement[0]=RandomHelper.roomGenerator.nextInt(2,3);
            roomArrangement[1]=1;
            roomArrangement[2]=1;
            roomArrangement[3]=1;
            roomArrangement[4]=1;
            roomArrangement[5]=RandomHelper.roomGenerator.nextInt(1,2);
            roomArrangement[6]=1;
            roomArrangement[7]=1;
            roomArrangement[8]=RandomHelper.roomGenerator.nextInt(1,2);
            roomArrangement[9]=RandomHelper.roomGenerator.nextInt(2,3);
            return roomArrangement;
        }
    }
    public static DiscreteCoordinates startingroom=new DiscreteCoordinates(1,0);
    private static final DiscreteCoordinates arrivalCoordinates=new DiscreteCoordinates(2,0);
    private static int[] roomArrangement;

    public Level2() {
        super(true,startingroom,createRoomArrangement(), 4, 2);
    }

    private static int[]createRoomArrangement(){
        roomArrangement=Level2RoomType.setroomArrangement();
        return roomArrangement;
    }

    public Level2(boolean randomMap) {
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
        ArrayList<Level2Room>rooms=new ArrayList<>();

        for(int i=0;i<mapStates.length;++i){
            for(int k=0;k<mapStates[i].length;++k){
                if(mapStates[i][k].equals(MapState.PLACED)||mapStates[i][k].equals(MapState.EXPLORED)){
                    roomsToCreate.add(new DiscreteCoordinates(i,k));
                }

                else if(mapStates[i][k].equals(MapState.BOSS_ROOM)){
                    DiscreteCoordinates coordinates=new DiscreteCoordinates(i,k);
                    Level2BossRoom bossRoom=new Level2BossRoom(coordinates);
                    rooms.add(bossRoom);
                    setRoom(coordinates,bossRoom);
                    mapStates[i][k]=MapState.CREATED;
                }

                else if(mapStates[i][k].equals(MapState.BOSS_KEYROOM)){
                    DiscreteCoordinates coordinates=new DiscreteCoordinates(i,k);
                    Level2KeyRoom keyRoom=new Level2KeyRoom(coordinates,getBossRoomKeyID());
                    rooms.add(keyRoom);
                    setRoom(coordinates,keyRoom);
                    mapStates[i][k]=MapState.CREATED;
                }
                else if(mapStates[i][k].equals(MapState.SPAWN_ROOM)){
                    DiscreteCoordinates coordinates=new DiscreteCoordinates(i,k);
                    Level2Room Room=new Level2Room(coordinates);
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
            Level2TurretRoom turretRoom=new Level2TurretRoom(roomsToCreate.get(randomcoor));
            rooms.add(turretRoom);
            setRoom(coordinates,turretRoom);
            mapStates[xcor][ycor]=MapState.CREATED;
            roomsToCreate.remove(randomcoor);
        }

        int randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
        DiscreteCoordinates coordinates=roomsToCreate.get(randomcoor);
        int xcor=roomsToCreate.get(randomcoor).x;
        int ycor=roomsToCreate.get(randomcoor).y;
        Level2StaffRoom staffRoom=new Level2StaffRoom(roomsToCreate.get(randomcoor));
        rooms.add(staffRoom);
        setRoom(coordinates,staffRoom);
        mapStates[xcor][ycor]=MapState.CREATED;
        roomsToCreate.remove(randomcoor);

        for(int i=0;i<roomArrangement[5];++i){
            randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
            coordinates=roomsToCreate.get(randomcoor);
            xcor=roomsToCreate.get(randomcoor).x;
            ycor=roomsToCreate.get(randomcoor).y;
            Level2Room Room=new Level2Room(coordinates);
            rooms.add(Room);
            setRoom(coordinates,Room);
            mapStates[xcor][ycor]=MapState.CREATED;
            roomsToCreate.remove(randomcoor);
        }

        randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
        coordinates=roomsToCreate.get(randomcoor);
        xcor=roomsToCreate.get(randomcoor).x;
        ycor=roomsToCreate.get(randomcoor).y;
        Level2BowRoom bowRoom=new Level2BowRoom(roomsToCreate.get(randomcoor));
        rooms.add(bowRoom);
        setRoom(coordinates,bowRoom);
        mapStates[xcor][ycor]=MapState.CREATED;
        roomsToCreate.remove(randomcoor);

        randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
        coordinates=roomsToCreate.get(randomcoor);
        xcor=roomsToCreate.get(randomcoor).x;
        ycor=roomsToCreate.get(randomcoor).y;
        Level2SwordRoom swordRoom=new Level2SwordRoom(roomsToCreate.get(randomcoor));
        rooms.add(swordRoom);
        setRoom(coordinates,swordRoom);
        mapStates[xcor][ycor]=MapState.CREATED;
        roomsToCreate.remove(randomcoor);

        for(int i=0;i<roomArrangement[8];++i){
            randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
            coordinates=roomsToCreate.get(randomcoor);
            xcor=roomsToCreate.get(randomcoor).x;
            ycor=roomsToCreate.get(randomcoor).y;
            Level2PoisonTurretRoom Room=new Level2PoisonTurretRoom(coordinates);
            rooms.add(Room);
            setRoom(coordinates,Room);
            mapStates[xcor][ycor]=MapState.CREATED;
            roomsToCreate.remove(randomcoor);
        }

        for(int i=0;i<roomArrangement[9];++i){
            randomcoor=RandomHelper.roomGenerator.nextInt(0, roomsToCreate.size());
            coordinates=roomsToCreate.get(randomcoor);
            xcor=roomsToCreate.get(randomcoor).x;
            ycor=roomsToCreate.get(randomcoor).y;
            Level2HeartRoom Room=new Level2HeartRoom(coordinates);
            rooms.add(Room);
            setRoom(coordinates,Room);
            mapStates[xcor][ycor]=MapState.CREATED;
            roomsToCreate.remove(randomcoor);
        }


        printMap(mapStates);

        for(Level2Room room:rooms){
            setUpConnector(mapStates,room);
        }



    }

    //works the same as in level0
    private void setConnectorsGeneral(Orientation orientation, Level2Room.Level2Connectors connectors, DiscreteCoordinates coordinates){
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
            setConnectorsGeneral(Orientation.RIGHT,Level2Room.Level2Connectors.E,roomCoordinates);
        }
        if(xcor>0&&(roomsplacement[xcor-1][ycor].equals(MapState.CREATED)||
                roomsplacement[xcor-1][ycor].equals(MapState.EXPLORED)||roomsplacement[xcor-1][ycor].equals(MapState.PLACED))){
            setConnectorsGeneral(Orientation.LEFT,Level2Room.Level2Connectors.W,roomCoordinates);
        }
        if(ycor<roomsplacement[0].length-1&&(roomsplacement[xcor][ycor+1].equals(MapState.CREATED)||
                roomsplacement[xcor][ycor+1].equals(MapState.EXPLORED)||roomsplacement[xcor][ycor+1].equals(MapState.PLACED))){
            setConnectorsGeneral(Orientation.UP,Level2Room.Level2Connectors.N,roomCoordinates);
        }
        if(ycor>0 &&(roomsplacement[xcor][ycor-1].equals(MapState.CREATED)||
                roomsplacement[xcor][ycor-1].equals(MapState.EXPLORED)||roomsplacement[xcor][ycor-1].equals(MapState.PLACED))){
            setConnectorsGeneral(Orientation.DOWN,Level2Room.Level2Connectors.S,roomCoordinates);
        }


    }

    private void generateMap1() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level2KeyRoom(room00, 1));
        setRoomConnector(room00, "icrogue/level210", Level2Room.Level2Connectors.E);
        lockRoomConnector(room00, Level2Room.Level2Connectors.E,  1);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1, 0);
        setRoom(room10, new Level2Room(room10));
        setRoomConnector(room10, "icrogue/level200", Level2Room.Level2Connectors.W);
    }

    public void generateFinalMap(){
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level2TurretRoom(room00));
        setRoomConnector(room00, "icrogue/level210", Level2Room.Level2Connectors.E);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level2Room(room10));
        setRoomConnector(room10, "icrogue/level211", Level2Room.Level2Connectors.S);
        setRoomConnector(room10, "icrogue/level220", Level2Room.Level2Connectors.E);

        lockRoomConnector(room10, Level2Room.Level2Connectors.W,  2);
        setRoomConnectorDestination(room10, "icrogue/level200", Level2Room.Level2Connectors.W);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2,0);
        setRoom(room20,  new Level2StaffRoom(room20));
        setRoomConnector(room20, "icrogue/level210", Level2Room.Level2Connectors.W);
        setRoomConnector(room20, "icrogue/level230", Level2Room.Level2Connectors.E);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level2KeyRoom(room30, 2));
        setRoomConnector(room30, "icrogue/level220", Level2Room.Level2Connectors.W);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom (room11, new Level2Room(room11));
        setRoomConnector(room11, "icrogue/level210", Level2Room.Level2Connectors.N);
    }
}
