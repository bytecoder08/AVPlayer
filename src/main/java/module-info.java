module avplayer.avplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens avplayer.avplayer to javafx.fxml;
    exports avplayer.avplayer;
}