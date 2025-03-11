package avplayer.avplayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//class MyController
public class MyController extends Application implements Initializable {

    //creating objects
    public List<String> argsS;
    FXMLLoader loader;
    Parent firstScreenRoot;
    @FXML
    public AnchorPane anchorPaneMain;
    @FXML
    public MenuBar menuBar;
    @FXML
    public AnchorPane mediaAnchorPane;
    @FXML
    public BorderPane borderPane;
    @FXML
    public VBox vBoxMain;
    @FXML
    public VBox vBox;
    @FXML
    public Label leftTimeStamp;
    @FXML
    public Label rightTimeStamp;
    @FXML
    public Label volumeBarLabel;
    public Stage myStage;
    public Scene myScene;
    public MediaPlayer mediaPlayer;
    public Media media;
    @FXML
    public MediaView mediaView;

    @FXML
    public Button previousButton;
    @FXML
    public Button backSeekButton;
    @FXML
    public Button playButton;
    @FXML
    public Button forSeekButton;
    @FXML
    public Button nextButton;
    @FXML
    public Button fullScreenButton;
    @FXML
    public Slider volumeBar;

    @FXML
    public Slider timeSlider;

    public static String filePath = null;


    File selectedDirectory, folder, file;
    File [] filesList;
    ArrayList<File> playList = new ArrayList<File>();
    int mediaNumber=0;


    //first method to get called automatic
    @Override
    public void start(Stage primaryStage) throws Exception {

        argsS = getParameters().getRaw();
        System.out.println(argsS);
        if (argsS.size() > 0) {
            filePath = argsS.get(0);
            System.out.println(filePath);
        }
//        filePath = "F:\\zz.mp3";
//        System.out.println(filePath);

        System.out.println("start method is called myStage is called primary stage .......");
        myStage = new Stage();
        //getting fxml file
        loader = new FXMLLoader(getClass().getResource("my-view.fxml"));
        firstScreenRoot = loader.load();
        myScene = new Scene(firstScreenRoot);
        myStage.setTitle("AV Player");
        myStage.getIcons().add(new Image("file:icons\\logo.png"));
        myStage.setScene(myScene);
        myStage.setMaximized(true);
        myStage.show();

    }


    //second method to get called automatic
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("initialize is called.....");
            //setting all icons of buttons
            previousButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/previous.png")))));
            backSeekButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/backSeek.png")))));
            playButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/play.png")))));
            forSeekButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/forSeek.png")))));
            nextButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/next.png")))));
            fullScreenButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/fullScreen.png")))));

            //setting all buttons tooltip
            previousButton.setTooltip(new Tooltip("switch to previous media"));
            backSeekButton.setTooltip(new Tooltip("seek 10 seconds before from current playing duration"));
            playButton.setTooltip(new Tooltip("play/pause a media playing"));
            forSeekButton.setTooltip(new Tooltip("seek 10 seconds after from current playing duration"));
            nextButton.setTooltip(new Tooltip("switch to next media"));
            fullScreenButton.setTooltip(new Tooltip("only way to exit full screen mode"));
            volumeBar.setTooltip(new Tooltip("adjust Volume"));

            if (filePath != null){
                //this method will play media when double click open file or open using open with
                System.out.println(filePath);
                autoCallPlay(filePath);
            }else {
                System.out.println("filepath is  ------- " + null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //method called to open file
    @FXML
    void openFile() {
        try {
            System.out.println("openFile clicked method called......");
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                    "Media Files", "*.mp3", "*.mp4", "*.wav", "*.m4a", "*.mkv", "*.mov", "*.wmv", "*.avi"));
            file = fileChooser.showOpenDialog(null);
            System.out.println(file);
            if (file != null) {
                // If the selected file is a media file, add its absolute path to the list
                if (isMediaFile(file)) {
                    playList.add(file);
                }
            }
            mediaNumber = playList.size()-1;
            media = new Media(playList.get(mediaNumber).toURI().toURL().toString());
            System.out.println("calling playMedia by folder.......");
            //calling playMedia
            playMedia();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //this method used to open all files in a folder to play
    @FXML
    void openFolder() {

        try {
            System.out.println("openFolder clicked method called.......");
            DirectoryChooser directoryChooser = new DirectoryChooser();
            selectedDirectory = directoryChooser.showDialog(null);
            if (selectedDirectory != null) {
                folder = new File(selectedDirectory.getAbsolutePath());
                System.out.println(folder);
                filesList = folder.listFiles();
            }
            if (filesList != null) {
                for (File value : filesList) {
                    if (isMediaFile(value)) {
                        playList.add(value);
                        System.out.println(value);
                    }
                }
            }
            mediaNumber = playList.size()-1;
            media = new Media(playList.get(mediaNumber).toURI().toURL().toString());
            System.out.println("calling playMedia by folder.......");
            //calling playMedia
            playMedia();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //this method check the file is media type or not
    public boolean isMediaFile(File file2) {
        // Check if the file has a media file extension
        String fileName = file2.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("mp3") || extension.equalsIgnoreCase("mp4")
                || extension.equalsIgnoreCase("wav") || extension.equalsIgnoreCase("m4a")
                || extension.equalsIgnoreCase("mov") || extension.equalsIgnoreCase("mkv")
                || extension.equalsIgnoreCase("wmv") || extension.equalsIgnoreCase("avi");
    }

    //this method close the application
    @FXML
    public void exitPlayer() {

        System.out.println("exitPlayer method Called........");
        myStage = (Stage) anchorPaneMain.getScene().getWindow();
        myStage.close();
        Platform.exit();
        System.exit(0);

    }


    // this method play media file
    @FXML
    void playMedia() {
        System.out.println("called playMedia method.................");
        try {

            if (mediaPlayer != null) {
                mediaPlayer.dispose();
            }

            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaView.setPreserveRatio(true);
            double w1 = mediaAnchorPane.getWidth();
            double h1 = mediaAnchorPane.getHeight();
            mediaView.setFitWidth(w1);
            mediaView.setFitHeight(h1);


            mediaPlayer.setOnReady(() -> {
                System.out.println("when player gets ready setOnReady......");
                timeSlider.setMin(0);
                timeSlider.setMax(mediaPlayer.getMedia().getDuration().toMinutes());
                timeSlider.setValue(0);

                //right Time Stamp time changer
                Duration duration= mediaPlayer.getMedia().getDuration();
                int hours = (int) duration.toHours();
                int minutes = (int) (duration.toMinutes() % 60);
                int seconds = (int) (duration.toSeconds() % 60);
                String timeString = hours + ":" + minutes + ":" + seconds;
                rightTimeStamp.setText(timeString);
                mediaPlayer.setVolume(volumeBar.getValue()*0.01);

                mediaPlayer.setAutoPlay(true);
                try {
                    playButton.setGraphic(new ImageView(new Image(new FileInputStream("icons/pause.png"))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            });

            //calling afterPlayMedia method
            afterPlayMedia();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //this method look after any changes made in stage
    @FXML
    void afterPlayMedia(){
        try {

            //listener on player change leftTimeStamp and timeSlider according to change in duration ...
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    //coding...
                    Duration duration = mediaPlayer.getCurrentTime();

                    timeSlider.setValue(duration.toMinutes());

                    //left Time Stamp time changer
                    int hours = (int) duration.toHours();
                    int minutes = (int) (duration.toMinutes() % 60);
                    int seconds = (int) (duration.toSeconds() % 60);
                    String timeString = hours + ":" + minutes + ":" + seconds;
                    leftTimeStamp.setText(timeString);

                }
            });

            //play next media after ending of current one.....
            mediaPlayer.setOnEndOfMedia(() ->{
                if (mediaNumber < playList.size() -1){
                    mediaNumber++;
                    mediaPlayer.stop();
                    try {
                        playButton.setGraphic(new ImageView(new Image(new FileInputStream("icons/play.png"))));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        media = new Media(playList.get(mediaNumber).toURI().toURL().toString());
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    playMedia();
                } else if (mediaNumber == playList.size() -1) {
                    mediaPlayer.stop();
                    mediaNumber = 0;
                    try {
                        media = new Media(playList.get(mediaNumber).toURI().toURL().toString());
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        playButton.setGraphic(new ImageView(new Image(new FileInputStream("icons/play.png"))));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    try {

                        if (mediaPlayer != null) {
                            mediaPlayer.dispose();
                        }

                        mediaPlayer = new MediaPlayer(media);
                        mediaView.setMediaPlayer(mediaPlayer);
                        mediaView.setPreserveRatio(true);
                        double w1 = mediaAnchorPane.getWidth();
                        double h1 = mediaAnchorPane.getHeight();
                        mediaView.setFitWidth(w1);
                        mediaView.setFitHeight(h1);

                        mediaPlayer.setOnReady(() -> {
                            System.out.println("when player gets ready setOnReady......");
                            timeSlider.setMin(0);
                            timeSlider.setMax(mediaPlayer.getMedia().getDuration().toMinutes());
                            timeSlider.setValue(0);

                            //right Time Stamp time changer
                            Duration duration= mediaPlayer.getMedia().getDuration();
                            int hours = (int) duration.toHours();
                            int minutes = (int) (duration.toMinutes() % 60);
                            int seconds = (int) (duration.toSeconds() % 60);
                            String timeString = hours + ":" + minutes + ":" + seconds;
                            rightTimeStamp.setText(timeString);
                            mediaPlayer.setVolume(volumeBar.getValue()*0.01);
                        });

                        afterPlayMedia();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            //time slider click change response.........
            timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (timeSlider.isPressed()) {
                        double val = timeSlider.getValue();
                        mediaPlayer.seek(new Duration(val * 60 * 1000));
                        System.out.println("timeSlider.valueProperty used to change media playing to timeSlider by mouse click.....");
                    }
                }
            });

            //volume bar click change response.........
            volumeBar.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    mediaPlayer.setVolume(volumeBar.getValue()*0.01);
                    int volumeValue = (int) (mediaPlayer.getVolume()*100);
                    volumeBarLabel.setText(volumeValue + "%");
                    System.out.println("volume bar is used.....");
                }
            });

            //hover on screen and change volume..................
            mediaAnchorPane.setOnMouseEntered(event -> {
                mediaAnchorPane.setOnScroll(scrollEvent -> {
                    double delta = scrollEvent.getDeltaY()*0.05;
                    double newVolume = volumeBar.getValue() + delta;
                    volumeBar.setValue(newVolume);
                    System.out.println(volumeBar.getValue());
                    volumeBar.valueProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            mediaPlayer.setVolume(volumeBar.getValue()*0.01);
                            int volumeValue = (int) (mediaPlayer.getVolume()*100);
                            volumeBarLabel.setText(volumeValue + "%");
                            System.out.println("volume bar is used.....");
                        }
                    });
                    System.out.println("volume scroll is used.........................");
                    scrollEvent.consume();
                });
            });
            mediaAnchorPane.setOnMouseExited(event -> {
                mediaAnchorPane.setOnScroll(null);
            });


            //hover on volume slider and change volume..................
            volumeBar.setOnMouseEntered(event -> {
                volumeBar.setOnScroll(scrollEvent -> {
                    double delta = scrollEvent.getDeltaY()*0.05;
                    double newVolume = volumeBar.getValue() + delta;
                    volumeBar.setValue(newVolume);
                    System.out.println(volumeBar.getValue());
                    volumeBar.valueProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            mediaPlayer.setVolume(volumeBar.getValue()*0.01);
                            int volumeValue = (int) (mediaPlayer.getVolume()*100);
                            volumeBarLabel.setText(volumeValue + "%");
                            System.out.println("volume bar is used.....");
                        }
                    });
                    System.out.println("volume scroll is used.........................");
                    scrollEvent.consume();
                });
            });
            volumeBar.setOnMouseExited(event -> {
                volumeBar.setOnScroll(null);
            });


            //hover on time Slider and change duration of player (playing time)....................
            timeSlider.setOnMouseEntered(event -> {
                timeSlider.setOnScroll(scrollEvent -> {
                    double delta = scrollEvent.getDeltaY()*0.005;
                    double newValue = timeSlider.getValue() + delta;
                    timeSlider.setValue(newValue);
                    mediaPlayer.seek(new Duration(newValue * 60 * 1000));
                    System.out.println(timeSlider.getValue());
                    System.out.println("Scroll time slider is called......................");
                    scrollEvent.consume();
                });
            });
            timeSlider.setOnMouseExited(event -> {
                timeSlider.setOnScroll(null);
            });


            //respond on change in width of mediaAnchorPane and change the fit width of mediaView...........
            mediaAnchorPane.widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    System.out.println("width changed");

                    double w = mediaAnchorPane.getWidth();
                    mediaView.setFitWidth(w);

                }
            });

            //respond on change in height of mediaAnchorPane and change the fit height of mediaView...........
            mediaAnchorPane.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    System.out.println("Height changed");

                    double h = mediaAnchorPane.getHeight();
                    mediaView.setFitHeight(h);

                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //used for play and pause button
    @FXML
    public void play() {
        System.out.println("play method called do play/pause ........");

        try {

            MediaPlayer.Status status = mediaPlayer.getStatus();

            if (status == MediaPlayer.Status.PLAYING) {
                //pause...
                mediaPlayer.pause();
                playButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/play.png")))));
            } else {
                //play....
                mediaPlayer.play();
                playButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/pause.png")))));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //used for only play button in playback
    @FXML
    public void onlyPlay() {
        System.out.println("onlyPlay method called.........");

        try {

            MediaPlayer.Status status = mediaPlayer.getStatus();

            if (status == MediaPlayer.Status.PAUSED) {
                //play....
                mediaPlayer.play();
                playButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/pause.png")))));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //used for only pause button in playback
    @FXML
    public void onlyPause() {
        System.out.println("onlyPause method called........");

        try {

            MediaPlayer.Status status = mediaPlayer.getStatus();

            if (status == MediaPlayer.Status.PLAYING) {
                //pause...
                mediaPlayer.pause();
                playButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/play.png")))));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //used to switch to previous media file from list
    @FXML
    public void preButtonClick() {

        System.out.println("preButtonClick called change to previous media to play ..........");

        try {
            if (mediaNumber > 0) {
                mediaNumber--;
                mediaPlayer.stop();
                playButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/play.png")))));
                media = new Media(playList.get(mediaNumber).toURI().toURL().toString());
                //calling
                playMedia();
            } else if (mediaNumber == 0) {
                mediaNumber = playList.size() - 1;
                mediaPlayer.stop();
                playButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/play.png")))));
                media = new Media(playList.get(mediaNumber).toURI().toURL().toString());
                //calling
                playMedia();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //used to switch to next media file from list
    @FXML
    public void nextButtonClick() {

        System.out.println("nextButtonClick called change to next media to play ..........");

        try {
            if (mediaNumber < playList.size() - 1) {
                mediaNumber++;
                mediaPlayer.stop();
                playButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/play.png")))));
                media = new Media(playList.get(mediaNumber).toURI().toURL().toString());
                //calling
                playMedia();
            } else if (mediaNumber == playList.size() - 1) {
                mediaNumber = 0;
                mediaPlayer.stop();
                playButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/play.png")))));
                media = new Media(playList.get(mediaNumber).toURI().toURL().toString());
                //calling
                playMedia();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //used to seek 10 second previous to media playing currently
    @FXML
    public void backSeek() {

        System.out.println("backSeek method called .......");

        double d = mediaPlayer.getCurrentTime().toSeconds();

        d = d - 10;

        mediaPlayer.seek(new Duration(d * 1000));

    }

    //used to seek 10 second after to media playing currently
    @FXML
    public void forSeek() {

        System.out.println("forSeek method called........");

        double d = mediaPlayer.getCurrentTime().toSeconds();

        d = d + 10;

        mediaPlayer.seek(new Duration(d * 1000));

    }


    //used to do and undo fullscreen mode
    @FXML
    public void fullScreen(){

        System.out.println("full screen called ...........");
        Stage stage = (Stage) fullScreenButton.getScene().getWindow();


        stage.setFullScreenExitHint("use fullscreen button to exit fullscreen");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        if (!stage.isFullScreen()){

            stage.setFullScreen(!stage.isFullScreen());
            try {
                fullScreenButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/fullScreenOn.png")))));
            }catch (Exception e){
                e.printStackTrace();
            }


            menuBar.setVisible(false);
            vBox.setVisible(false);
            vBoxMain.setOnMouseEntered(event -> {
                vBox.setVisible(true);
            });
            vBoxMain.setOnMouseExited(event -> {
                vBox.setVisible(false);
            });

            vBox.setMaxWidth(720);
            anchorPaneMain.setStyle("-fx-background-color: #000000");
            double w11 = anchorPaneMain.getWidth();
            double h11 = anchorPaneMain.getHeight();
            mediaView.setFitWidth(w11);
            mediaView.setFitHeight(h11);

        } else if (stage.isFullScreen()) {

            stage.setFullScreen(!stage.isFullScreen());
            try {
                fullScreenButton.setGraphic(new ImageView(new Image(Files.newInputStream(Paths.get("icons/fullScreen.png")))));
            }catch (Exception e){
                e.printStackTrace();
            }

            menuBar.setVisible(true);
            vBox.setVisible(true);
            vBoxMain.setOnMouseEntered(event -> {
                vBox.setVisible(true);
            });
            vBoxMain.setOnMouseExited(event -> {
                vBox.setVisible(true);
            });
            vBox.setMaxWidth(Region.USE_COMPUTED_SIZE);
            vBox.setVisible(true);
            menuBar.setVisible(true);
            anchorPaneMain.setStyle(null);
            double w11 = mediaAnchorPane.getWidth();
            double h11 = mediaAnchorPane.getHeight();
            mediaView.setFitWidth(w11);
            mediaView.setFitHeight(h11);

        }


    }

    //used to play media file when open double-clicking or by using open with
    public void autoCallPlay(String filePath){
        try {

            System.out.println("initializing auto open file......");
            System.out.println(filePath);
            System.out.println("all printed");

            if (filePath!=null){

                file = new File(filePath);
                System.out.println("file is " +file);

                if (isMediaFile(file)) {
                    playList.add(file);
                }

                mediaNumber = playList.size()-1;
                media = new Media(playList.get(mediaNumber).toURI().toURL().toString());
                System.out.println(media);
                //calling
                playMedia();
            }

        }catch (Exception e){
            e.printStackTrace();

        }
    }


    //main method of MyController class
    public static void main(String[] args) {

        System.out.println("launching app....");
        //launching application
        launch(args);

    }


}
