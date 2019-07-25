package Platforms1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private ArrayList<Node> platforms = new ArrayList<Node>();
    private Pane mainRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();
    private Node personaje;

    private Point2D playerSpeed = new Point2D(0,0);
    private int levelWidth;
    private boolean canJump = true;

    private void initCont(){
        //Background Rectangle
        Rectangle bg = new Rectangle(1200, 720);
        bg.setFill(Color.BLACK);
        levelWidth = LevelData.level1[0].length() * 60;

        for (int x = 0; x<LevelData.level1.length; x++){
            String line = LevelData.level1[x];
            for(int y = 0; y<line.length(); y++){
                switch (line.charAt(y)){
                    case '0':
                        break;

                    case '1':
                        Node platform = createEntity(x*10, y*10, 10, 10, Color.GREEN);
                        platforms.add(platform);
                        break;
                }
            }
        }

        personaje = createEntity(0, 600, 40, 40, Color.BLUE);

        //Make the map move using the Game Pane
        //yo la tengo electropura ////
        personaje.translateXProperty().addListener((obs, old, newValue) ->{
                int offset = newValue.intValue();

                    if(offset> 640 && offset< levelWidth - 640){
                        gameRoot.setLayoutX(-(offset - 640));
                    }
                });
            for(Node platform :  platforms) {
                gameRoot.getChildren().addAll(platform);
            }
        mainRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }

    private void update(){
        if(isPressed(KeyCode.W) && personaje.getTranslateY() >=5){
            jumpPlayer();
        }

        if(isPressed(KeyCode.A) && personaje.getTranslateX() >=5){
            movePlayerX(-5);
        }


        if(isPressed(KeyCode.D) && personaje.getTranslateX() + 40 <= levelWidth - 5){
            movePlayerX(5);
        }

        //Gravity
        if(playerSpeed.getY() < 10){
            playerSpeed = playerSpeed.add(0, 1);
        }

        movePlayerY((int) playerSpeed.getY());
    }

    //Collision Logic
    private void movePlayerX(int value){
        boolean movingRight = value > 0;

        for(int i = 0; i < Math.abs(value); i++){
            for(Node platform : platforms){
                //if statement to know if the bounds of the platform intersect and will have a collision
                if(personaje.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingRight){
                        //getX + player width
                        if(personaje.getTranslateX() + 40 == platform.getTranslateX())
                            return;
                    }
                    else{
                        //get x + width of the block
                        if(personaje.getTranslateX() == platform.getTranslateX() + 60)
                            return;
                    }
                }
            }
            //If moving right by 1 unit, if moving left by 1 unit
            personaje.setTranslateX(personaje.getTranslateX() + (movingRight ? 1 : -1));
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
                        if(personaje.getTranslateY() + 40 == platform.getTranslateY())
                            return;
                    }
                    else{
                        //get x + width of the block
                        if(personaje.getTranslateY() == platform.getTranslateY() + 60)
                            return;
                    }
                }
            }
            //If moving right by 1 unit, if moving left by 1 unit
            personaje.setTranslateX(personaje.getTranslateY() + (movingDown ? 1 : -1));
        }


    }

    private void jumpPlayer(){
        if(canJump){
            playerSpeed = playerSpeed.add(0, 30);
            canJump = false;
        }
    }

    private Node createEntity(int x, int y, int w, int h, Color color){
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);

        gameRoot.getChildren().add(entity);
        return entity;
    }

    private boolean isPressed(KeyCode key){return keys.getOrDefault(key, false);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        initCont();

        Scene scene = new Scene(mainRoot);
        scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
        scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));

        primaryStage.setTitle("This is a game m8");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();

    }



    public static void main(String[] ar){launch(ar);}

}
