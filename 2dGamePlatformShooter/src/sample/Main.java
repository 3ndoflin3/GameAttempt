package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;

public class Main extends Application {

    //Variables
    Scene scene;
    String path = "PixelArt1.gif";
    InputStream is;
    boolean goUp, goDown, goLeft, goRight, run;
    Node personaje;
    int width = 500, height = 500;
    Group grupo;

    //-----------------------------------------------------------------------------------
    //Start method
    @Override
    public void start(Stage primaryStage) throws Exception{
       // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        loadImage();
        grupo = new Group(personaje);

        //root.getChildren().addAll(grupo);
        moveHeroTo(width / 2, height / 2);
        primaryStage.setTitle("Hello World");
        scene = new Scene(grupo, width, height);
        listeners();

        primaryStage.setScene(scene);
        primaryStage.show();



        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                int ejeX = 0, ejeY = 0;
                if(goUp) ejeY -= 5;
                if(goDown) ejeY += 5;
                if(goRight) ejeX += 5;
                if(goLeft) ejeX -=5;
                if(run) {ejeX *=3; ejeY *= 3;}

                moveHeroBy(ejeX, ejeY);
            }
        };

        timer.start();

    }


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

    //-----------------------------------------------------------------------------------

    private void moveHeroBy(int ejeX, int ejeY){
        if(ejeX == 0 && ejeY == 0) return;

        final double cx = personaje.getBoundsInLocal().getWidth() /2;
        final double cy = personaje.getBoundsInLocal().getHeight() /2;

        double x = cx + personaje.getLayoutX() + ejeX;
        double y = cy + personaje.getLayoutY() + ejeY;

        moveHeroTo(x, y);
    }


    //-----------------------------------------------------------------------------------
    private void moveHeroTo(double x, double y){
        final double cx = personaje.getBoundsInLocal().getWidth() /2;
        final double cy = personaje.getBoundsInLocal().getHeight() /2;

        if(x - cx>=0&&
                x + cx <= width &&
                y - cy >= 0 &&
                y + cy <= height){
            personaje.relocate(x - cx, y -cy);
        }
    }


    //-----------------------------------------------------------------------------------
    void loadImage(){

        try{
            is = new FileInputStream(path);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Image personImage = new Image(is);
        personaje = new ImageView();
        ((ImageView) personaje).setImage(personImage);
        ((ImageView)personaje).setFitWidth(150);
        ((ImageView) personaje).setFitHeight(150);
        personaje.setFocusTraversable(true);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
