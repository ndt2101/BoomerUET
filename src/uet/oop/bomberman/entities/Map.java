package uet.oop.bomberman.entities;

import edu.princeton.cs.algs4.UF;
//import uet.oop.bomberman.Solution;
import uet.oop.bomberman.entities.enemy.Doll;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.entities.airEntities.*;
import uet.oop.bomberman.entities.enemy.Balloon;
import uet.oop.bomberman.entities.enemy.Oneal;
import uet.oop.bomberman.entities.groundEntities.Grass;
import uet.oop.bomberman.entities.groundEntities.Portal;
import uet.oop.bomberman.entities.groundEntities.Wall;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by hello on 11/10/2020.
 */
public class Map {
    public int level;
    public int row;
    public int colum;

    public ArrayList<ArrayList<Character>> map = new ArrayList<>();
    public List<Entity> portals = new ArrayList<>();
    public int[][] mesh = new int[0][0];

    public UF uf ;

    public Map() {

        try {
            randomMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        insertFromFile();
        loadMesh();

        uf = new UF((row - 2) * (colum - 2));
        loadUF();
    }

    public void insertFromFile(){

        File file = new File("res/levels/Level1.txt");
        Scanner scan = null;
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        level = scan.nextInt();
        row = scan.nextInt();
        colum = scan.nextInt();

        scan.nextLine();

        for(int rowIndex = 0; rowIndex < row; rowIndex++){
            String line = scan.nextLine();
            ArrayList<Character> lineChar = new ArrayList<>();
            for(int colIndex = 0; colIndex < colum; colIndex++){
                lineChar.add(line.charAt(colIndex));
            }
            map.add(lineChar);
        }

//        for(int rowIndex = 0; rowIndex < row; rowIndex++){
//            ArrayList<Character> lineChar = map.get(rowIndex);
//            for(int colIndex = 0; colIndex < colum; colIndex++){
//                System.out.print(lineChar.get(colIndex));
//            }
//            System.out.println();
//        }

    }

    /**
     * add airEntity to entities list
     * @param entities
     */
    public void loadEntities(List<AirEntity> entities){
        for(int rowIndex = 0; rowIndex < map.size(); rowIndex++){
            ArrayList<Character> lineChar = map.get(rowIndex);
            for(int colIndex = 0; colIndex < lineChar.size(); colIndex++){
                AirEntity object = null;
                switch (lineChar.get(colIndex)){
                    case '*':{
                        object = new Brick(colIndex, rowIndex,"Brick", Sprite.brick.getFxImage());
                        break;
                    }
                    case 'p':{
                        object = new Bomber(colIndex, rowIndex,"Bomber", Sprite.player_right.getFxImage());
                        break;
                    }
                    case '1':{
                        object = new Balloon(colIndex, rowIndex, "Balloon", Sprite.balloom_right1.getFxImage());
                        break;
                    }
                    case '2':{
                        object = new Oneal(colIndex, rowIndex,"Oneal", Sprite.oneal_right1.getFxImage());
                        break;
                    }
                    case '3':{
                        object = new Doll(colIndex, rowIndex,"Doll", Sprite.doll_right1.getFxImage());
                        break;
                    }
                    case 'b':{
                        object = new BombItem(colIndex, rowIndex,"BombItem", Sprite.powerup_bombs.getFxImage());
                        entities.add(object);
                        object = new Brick(colIndex, rowIndex, "Brick", Sprite.brick.getFxImage());
                        break;
                    }
                    case 'f':{
                        object = new FlameItem(colIndex, rowIndex,"FlameItem", Sprite.powerup_flames.getFxImage());
                        entities.add(object);
                        object = new Brick(colIndex, rowIndex, "Brick", Sprite.brick.getFxImage());
                        break;
                    }
                    case 's':{
                        object = new SpeedItem(colIndex, rowIndex,"SpeedItem", Sprite.powerup_speed.getFxImage());
                        entities.add(object);
                        object = new Brick(colIndex, rowIndex, "Brick", Sprite.brick.getFxImage());
                        break;
                    }
                    case 'x':{
                        object = new Brick(colIndex, rowIndex, "Brick", Sprite.brick.getFxImage());
                        break;
                    }
                    default:{
                        break;
                    }
                }
                if(object != null){
                    entities.add(object);
                }

            }

        }
    }

    /**
     * add groundEntity to stillObject list
     * @param stillObjects
     */
    public void loadStillObjects(List<Entity> stillObjects ){
        for(int rowIndex = 0; rowIndex < map.size(); rowIndex++){
            ArrayList<Character> lineChar = map.get(rowIndex);
            for(int colIndex = 0; colIndex < lineChar.size(); colIndex++){
                Entity object = null;
                switch (lineChar.get(colIndex)){
                    case '#':{
                        object = new Wall(colIndex, rowIndex, Sprite.wall.getFxImage());
                        break;
                    }
                    case 'x':{
                        object = new Portal(colIndex, rowIndex, Sprite.portal.getFxImage());
                        Entity portal = new Portal(colIndex, rowIndex, Sprite.portal.getFxImage());
                        portals.add(portal);
                        break;
                    }
                    default:{
                        object = new Grass(colIndex, rowIndex, Sprite.grass.getFxImage());
                        break;
                    }
                }
                stillObjects.add(object);
            }

        }
    }

    /**
     * load map dung de move
     * @return
     */
    public int[][] loadMesh(){
        mesh = new int[row][colum];

        for(int rowIndex = 0; rowIndex < map.size(); rowIndex++){
            ArrayList<Character> lineChar = map.get(rowIndex);
            for(int colIndex = 0; colIndex < lineChar.size(); colIndex++){
                switch (lineChar.get(colIndex)){
                    case '#':{    //tuong
                        mesh[rowIndex][colIndex] = -1;
                        break;
                    }
                    case 'x':{   //potal
                        mesh[rowIndex][colIndex] = 2;
                        break;
                    }
                    case '*':{   //gach
                        mesh[rowIndex][colIndex] = 2;
                        break;
                    }
                    case '1':{   //balloon
                        mesh[rowIndex][colIndex] = 0;
                        break;
                    }
                    case '2':{   //oneal
                        mesh[rowIndex][colIndex] = 0;
                        break;
                    }
                    case '3':{   //doll
                        mesh[rowIndex][colIndex] = 0;
                        break;
                    }
                    case 'b':{   // boomitem
                        mesh[rowIndex][colIndex] = 3;
                        break;
                    }
                    case 'f':{   //flame
                        mesh[rowIndex][colIndex] = 3;
                        break;
                    }
                    case 's':{   //speed
                        mesh[rowIndex][colIndex] = 3;
                        break;
                    }
                    default:{
                        mesh[rowIndex][colIndex] = 0;
                        break;
                    }
                }
            }
        }

//        for(int i = 0; i < row; i++){
//            for(int j = 0; j < colum; j++){
//                if(mesh[i][j] < 0){
//                    System.out.print(mesh[i][j]);
//                }
//                else {
//                    System.out.print(mesh[i][j] + " ");
//                }
//            }
//            System.out.println();
//        }
        return mesh;
    }

    public void loadUF(){

        for(int i = 1; i < row - 1; i++){
            for(int j = 1; j < colum - 1; j++){
                if(mesh[i][j] == 0){
                    if(mesh[i][j+1] == 0){
                        uf.union(getUFId(i,j), getUFId(i, j+1));
                    }
                    if(mesh[i+1][j] == 0){
                        uf.union(getUFId(i,j), getUFId(i+1, j));
                    }
                }
            }
        }
//        for(int i = 1; i < row - 1; i++){
//            for(int j = 1; j < colum - 1; j++){
//                System.out.print(getUFId(i, j) + " ");
//            }
//            System.out.println();
//        }
    }

    public int getUFId(int x, int y){
        return (x - 1) * (colum - 2) + y - 1;
    }

    public String ch = "   *f2b   f       3 x  s        f s     1x ";
    public String ch2 = "   *fb   f        x  s        f s     x ";
    public void randomMap() throws IOException {
        Random rd = new Random();
        int row = rd.nextInt(5) + 15;
        int colum = rd.nextInt(10) + 20;

        char[][] map = new char[row][colum];

        for(int i = 0; i < row; i++){
            for(int j = 0; j < colum; j++){
                if(i == 0 || i == row - 1 || j == 0 || j == colum - 1){
                    map[i][j] = '#';
                }
                else if(i % 2 == 0  && j % 2 == 0){
                    map[i][j] = '#';
                }
                else if( i < 6 && j < 6){
                    int index = rd.nextInt(ch2.length());
                    map[i][j] = ch2.charAt(index);
                }
                else{
                    int index = rd.nextInt(ch.length());
                    map[i][j] = ch.charAt(index);
                }

            }
        }

        map[1][1] = 'p';

        File file = new File("res/levels/Level0.txt");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("1 " + row + " " + colum + "\n");
        for(int i = 0; i < row; i++){
            for(int j = 0; j < colum; j++){
                fileWriter.write(map[i][j]);
            }
            fileWriter.write("\n");
        }
        fileWriter.write("\n");
        fileWriter.close();
        return;
    }

    public static void main(String[] args){
        Map map = new Map();
        map.insertFromFile();
        map.loadMesh();
    }
}
