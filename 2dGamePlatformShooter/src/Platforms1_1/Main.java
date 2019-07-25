package Platforms1_1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {


    //Variables
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    Scene scene;
    String path = "PixelArt1.gif";
    FileInputStream is;
    boolean goUp, goDown, goLeft, goRight, run;
    Node personaje;
    int width = 1100, height = 650;
    int bgWidth = width+700, bgHeight = height+300;
    Group grupo;
    TextField txt;
    private Pane mainRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();
    private ArrayList<Node> platforms = new ArrayList<Node>();
    private boolean canJump = true;
    private Point2D playerSpeed = new Point2D(0,0);

    public static void main(String[] args) {
        launch(args);
    }


    private void initCont(){
        Rectangle backGround = new Rectangle(bgWidth, bgHeight);
        backGround.setFill(Color.GOLD);
        //grupo = new Group(new TextField());

        gameRoot.getChildren().addAll(backGround);
        mainRoot.getChildren().addAll(gameRoot);


        for (int x = 0; x<LevelData.level1.length; x++){
            String line = LevelData.level1[x];

            for(int y = 0; y<line.length(); y++){
                switch (line.charAt(y)){
                    case '0':
                        System.out.println("Caso 0" + y);
                        break;

                    case '1':
                        System.out.println("Caso 1");
                        Node platform = createEntity(y*50, x*50, 50, 50, Color.GREEN, "R");
                        platforms.add(platform);
                        break;
                }
            }
        }
        personaje = createEntity(0, 400, 100, 100, Color.BLUE, "P");

        mainRoot.getChildren().add(personaje);
        personaje.translateXProperty().addListener((obs, old, newValue) ->{
                    int offset = newValue.intValue();

                    if(offset> 600 && offset< width - 600){
                        gameRoot.setLayoutX(-(offset - 640));
                    }
                });
    }
    private Node createEntity(int x, int y, int w, int h, Color color, String type){

        if(type.equals("R")) {
            Rectangle entity = new Rectangle(w, h);
            entity.setTranslateX(x);
            entity.setTranslateY(y);
            entity.setFill(color);
            gameRoot.getChildren().add(entity);
            return entity;
        }
        if(type.equals("P")){
            try{
                is = new FileInputStream(path);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
            Image personImage = new Image(is);
            personaje = new ImageView();
            ((ImageView) personaje).setImage(personImage);
            ((ImageView)personaje).setFitWidth(w);
            ((ImageView) personaje).setFitHeight(h);
            personaje.setFocusTraversable(true);
            return personaje;
        }
        return null;
    }
    @Override
    public void start(Stage primaryStage) {
        initCont();

        scene = new Scene(mainRoot, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                listeners();
                update();
                /*int ejeX = 0, ejeY = 0;
                if(goUp) ejeY -= 5;
                if(goDown) ejeY += 5;
                if(goRight) ejeX += 5;
                if(goLeft) ejeX -=5;
                if(run) {ejeX *=3; ejeY *= 3;}

                moveHeroBy(ejeX, ejeY);*/
            }
        };
        timer.start();
    }

    //-----------------------------------------------------------------------------------
    private void update(){
        if((goUp) && personaje.getLayoutY() <=1000){
            System.out.println("W pressed" + isPressed(KeyCode.W));
            jumpPlayer();
        }

        if(goLeft && personaje.getLayoutX() >=5){
            System.out.println("A pressed" + isPressed(KeyCode.A));
            movePlayerX(-5);
        }


        if(goRight && personaje.getLayoutX() + 40 <= width - 5){
            System.out.println("D pressed" + isPressed(KeyCode.D));
            movePlayerX(5);
        }

        //Gravity
        if(playerSpeed.getY() < 10){
            playerSpeed = playerSpeed.add(0, 1);
        }

        movePlayerY((int) playerSpeed.getY());
    }


    //-----------------------------------------------------------------------------------
    //Collision Logic
    private void movePlayerX(int value){
        boolean movingRight = value > 0;

        for(int i = 0; i < Math.abs(value); i++){
            for(Node platform : platforms){
                //if statement to know if the bounds of the platform intersect and will have a collision
                if(personaje.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingRight){
                        //getX + player width
                        if(personaje.getLayoutX() + 40 == platform.getLayoutX())
                            return;
                    }
                    else{
                        //get x + width of the block
                        if(personaje.getLayoutX() == platform.getLayoutX() + 60)
                            return;
                    }
                }
            }
            //If moving right by 1 unit, if moving left by 1 unit
            personaje.setLayoutX(personaje.getLayoutX() + (movingRight ? 1 : -1));
        }

    }


    //MovePlayer methods are almost the same, you just need to change 2 var
    private void movePlayerY(int value){
        boolean movingDown = value > 0;

        for(int i = 0; i < Math.abs(value); i++){
            for(Node platform : platforms){
                //if statement to know if the bounds of the platform intersect and will have a collision
                if(personaje.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingDown){
                        //getX + player width
                        if(personaje.getLayoutY() + 40 == platform.getLayoutY())
                            movingDown = false;
                            return;
                    }
                    else{
                        //get x + width of the block
                        if(personaje.getLayoutY() == platform.getLayoutY() + 60)
                            return;
                    }
                }
            }
            //If moving right by 1 unit, if moving left by 1 unit
            personaje.setLayoutY(personaje.getLayoutY() + (movingDown ? 1 : -1));
        }


    }

    private void jumpPlayer(){

        boolean attempt = true;

        playerSpeed = playerSpeed.add(0, -30);
        try {
            Thread.sleep(100);
            attempt = false;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        if(attempt)
            playerSpeed = playerSpeed.add(0, 30);

        /*
        for(int x = 0 ; x<=10; x++) {
            personaje.setLayoutY(personaje.getLayoutY() - 1);
            System.out.println(personaje.getLayoutY() + "Bucle: " + x);
            try{
            Thread.sleep(100);}catch (Exception ex){

            }
        }*/
        /*if(canJump){
            playerSpeed = playerSpeed.add(0, 30);
            canJump = false;
        }*/
    }


    //-----------------------------------------------------------------------------------



    private boolean isPressed(KeyCode key){return keys.getOrDefault(key, false);}



    //-----------------------------------------------------------------------------------

    private void listeners(){

        scene.setOnKeyPressed(e -> {

            switch(e.getCode()){
                case UP: goUp =true; break;
                case DOWN: goDown = true; break;
                case LEFT: goLeft = true; break;
                case RIGHT: goRight = true; break;
                case SHIFT: run = true; break;

            }
        });

        scene.setOnKeyReleased(e ->{
            switch(e.getCode()){
                case UP: goUp =false; break;
                case DOWN: goDown = false; break;
                case LEFT: goLeft = false; break;
                case RIGHT: goRight = false; break;
                case SHIFT: run = false; break;

            }

        });




    }



}
