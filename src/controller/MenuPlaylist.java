package controller;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import model.Music;
import javafx.scene.control.TableView;
import model.Music_DaoImpl;
import model.Playlist;
import model.Playlist_DaoImpl;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MenuPlaylist {

    private static MediaPlayer mp;
    private boolean mpCreated = false;
    private int currentIndex;
    Image pause = new Image ("/files/pause.png");
    Image play = new Image ("/files/play.png");
    Image unmute = new Image ("/files/unmute.png");
    Image muted =  new Image ("/files/muted.png");
    Image albumPlaceHolder = new Image("/files/albumPlaceHolder.png");
    public ObservableList<Music> obList_musicCollected = FXCollections.observableArrayList();
    public ObservableList<Playlist> oblist_playlistsCollected = FXCollections.observableArrayList();
    public ObservableList<String> list2 = FXCollections.observableArrayList();
    public String currentUser = Login.userName;

    @FXML private Slider sliderVolume;
    @FXML private Label lbl_userName;
    @FXML private ToggleButton btn_mute;
    @FXML private ToggleButton btn_shuffle;
    @FXML private ToggleButton btn_repeat;
    @FXML private Label lbl_musicTitle;
    @FXML private Label lbl_musicGenre;
    @FXML private Label lbl_musicDuration;
    @FXML private Label lbl_artistName;
    @FXML private Label lbl_musicAlbum;
    @FXML private Label lbl_musicYear;
    @FXML private Label lbl_playlistName;
    @FXML private ImageView imageView_albumCover;
    @FXML private ImageView ImgView_PlayPause;
    @FXML private ComboBox<String> cbBox_Playlist;
    @FXML private TableColumn<Music, String> table_musicCell;
    @FXML private TableColumn<Music, String> table_pathCell;
    @FXML private TableView<Music> tableView_Playlist;
    @FXML private Label lbl_nowPlaying;
    @FXML private Label lbl_totalDuration;
    @FXML private Button btn_delSong;
    @FXML private ImageView ImgView_mute;
    @FXML private TextField tb_newPlaylistName;
    @FXML private Slider sliderProgress;
    @FXML private Label lbl_elapsedTime;


    @FXML public void initialize(){
        //solução mais limpa do que fazer implements initializable à classe
        lbl_userName.setText(currentUser);
    }  //funciona como page load
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @FXML
    void playlistLoad_mouseClick(MouseEvent event) {
        if(cbBox_Playlist != null){
            list2.clear();
        }

        Playlist_DaoImpl playlistDao = new Playlist_DaoImpl();
        oblist_playlistsCollected = playlistDao.getAllPlaylistElements(currentUser);

        for (Playlist s: oblist_playlistsCollected
        ) {
            list2.add(s.getPlaylistName());
            cbBox_Playlist.setItems(list2);
        }
        cbBox_Playlist.setItems(list2);
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

     @FXML
     void musicLoad(){
        //action event
        //carrega da playlist as musicas da playlist escolhida na comboBox

         Music_DaoImpl musicDao = new Music_DaoImpl();
         obList_musicCollected = musicDao.getAllMusicElements(cbBox_Playlist.getSelectionModel().getSelectedItem());

         table_musicCell.setCellValueFactory(new PropertyValueFactory<>("songName"));
         table_pathCell.setCellValueFactory(new PropertyValueFactory<>("path"));  //path fica invisivel ao utilizador, mas acessivel pelo programa para recolher paths das musicas

        //para debug abaixo
         int count = 0;
         for(Music s : obList_musicCollected)
         {
             System.out.println(s.getSongName());
             count++;
         }
         System.out.println(count);
         //para debug acima

         tableView_Playlist.setItems(obList_musicCollected);  //carrega finalmente o conteudo dessa lista para todas as respectivas celulas
         lbl_playlistName.setText(cbBox_Playlist.getSelectionModel().getSelectedItem());
     }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public String convertDuration(int totalSecs)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
        String time = LocalTime.MIN.plusSeconds(totalSecs).format(formatter);
        return time;
    }
    //------------------------------------------
    private void toggleBtnColors(ToggleButton btn){

        if(btn.isSelected())
            btn.setStyle("-fx-background-color: lightgreen;");
        else
            btn.setStyle(""); //reset à instrução, css volta a ter prioridade e não perco hover effect
    }
    //----------------------------------------
    public void prepareMetaData() {

        mp.setOnReady(() -> {
            lbl_musicDuration.setText(convertDuration(((int) mp.getTotalDuration().toSeconds())));
            lbl_totalDuration.setText(convertDuration(((int) mp.getTotalDuration().toSeconds())));
            mp.setVolume(sliderVolume.getValue()/100); //agarra o valor de som do slider e atribui à proxima musica
        });

        mp.getMedia().getMetadata().addListener((MapChangeListener<String, Object>) map -> {

            if (map.getKey().equals("album")) {
                lbl_musicAlbum.setText(map.getValueAdded().toString());
            }if (map.getKey().equals("artist")) {
                lbl_artistName.setText(map.getValueAdded().toString());
            } if (map.getKey().equals("title")) {
                lbl_musicTitle.setText(map.getValueAdded().toString());
            } if (map.getKey().equals("year")) {
                lbl_musicYear.setText(map.getValueAdded().toString());
            }
              if (map.getMap().containsKey("image")) { //contem chave de imagem? contem album cover
                if(map.getKey().equals("image")){
                imageView_albumCover.setImage((Image) map.getValueAdded());
                }
                }else {  //nao ha chave de imagem? utiliza imagem de placeholder
                imageView_albumCover.setImage(albumPlaceHolder);
            }
              if(map.getKey().equals("genre")) {
                lbl_musicGenre.setText(map.getValueAdded().toString());
            }
        });
    }

    @FXML
    void tabPlaylist_mouseClick(MouseEvent event) {
        if(event.getClickCount()==2) {
            if (mpCreated == true) {
                mp.dispose();
            }
            if (!tableView_Playlist.getSelectionModel().isEmpty()) { //para nao dar excepçao ao selecionar celulas vazias
            try {
                mp = new MediaPlayer(new Media(new File(tableView_Playlist.getSelectionModel().getSelectedItem().getPath()).toURI().toString()));
                lbl_nowPlaying.setText(tableView_Playlist.getSelectionModel().getSelectedItem().getSongName());
                mpCreated = true;
                currentIndex = tableView_Playlist.getSelectionModel().getSelectedIndex();
                System.out.println(currentIndex);
                prepareMetaData();
                trackMusicProgress();
                playPauseMusic();
                playNextMusic();
            }
            catch(MediaException e){ //caso a musica seja mudada de sitio, dará este tipo especifico de excepçao, a qual é apanhada
                System.out.println(e); //imprime para consola o tipo de erro, neste caso que não encontrada a musica com o url especificado
                lbl_nowPlaying.setText("Music was moved, original path not found."); //o utilizador é informado da causa de não tocar a musica
            }
            }
        }
    }

    public void playNextMusic() {
        //sem intervenção, tocará recursivamente todas as musicas desde a selecionada até à ultima


                if(currentIndex >= obList_musicCollected.size()-1){
                    System.out.println("entrou na condição de saída");
                    return; //condição de saída da recursividade
                }

                    mp.setOnEndOfMedia(() -> {
                        currentIndex++;
                      mp.dispose();

                      mp = new MediaPlayer(new Media(new File(obList_musicCollected.get(currentIndex).getPath()).toURI().toString())); //tocando assim, a musica imediatamente a seguir na lista

                          prepareMetaData();
                          playPauseMusic();
                          trackMusicProgress();

                      tableView_Playlist.getSelectionModel().selectBelowCell();  //seleciona visualmente a celula abaixo, quando passa à prox musica
                      playNextMusic();
                  });
        }

        public void trackMusicProgress(){
            mp.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                sliderProgress.setValue((newValue.toSeconds() / mp.getTotalDuration().toSeconds()) * 100);
                lbl_elapsedTime.setText(convertDuration((int)(mp.getCurrentTime().toSeconds())));
            });
        }

    public void playPauseMusic() {

        if(mpCreated == true) {
            if ((mp.getStatus() == MediaPlayer.Status.PLAYING)) {
                ImgView_PlayPause.setImage(play);
                mp.pause();
            } else {
                ImgView_PlayPause.setImage(pause);
                   mp.play();
                   System.out.println("Music Playing");
               }
        }
    }
    //----------------------------------------
    @FXML
    void logout_mouseClick(MouseEvent event) {

    }

    @FXML
    void settings_mouseClick(MouseEvent event) {

    }

    @FXML
    void volume_mouseDrag(MouseEvent event) {
        if(mpCreated)
        mp.setVolume(sliderVolume.getValue()/100);
    }

    @FXML
    void progress_mouseDrag(MouseEvent event) {
        if(mpCreated)
            mp.seek(Duration.seconds((sliderProgress.getValue() / 100) * mp.getTotalDuration().toSeconds()));
    }

    @FXML
    void mute_mouseClick(MouseEvent event) {
        if(mpCreated) {
            if (mp.isMute()){
                mp.setMute(false);   //caso já esteja mute (isMute = true), passa o estado do mute a false, retirando o mute na musica
                ImgView_mute.setImage(unmute);  //muda icone
            }
            else {
                mp.setMute(true);   //else faz mute
                ImgView_mute.setImage(muted);
            }
        }
    }

    @FXML
    void playPause_mouseClick(MouseEvent event) {
        playPauseMusic();
    }

    @FXML
    void musicForwardTime_mouseClick(MouseEvent event) {
        Duration forward = mp.getCurrentTime().add(Duration.seconds(5));
        mp.seek(forward);
    }

    @FXML
    void musicRewindTime_mouseClick(MouseEvent event) {
        Duration rewind = mp.getCurrentTime().subtract(Duration.seconds(5));
        mp.seek(rewind);
    }

    @FXML
    void repeat_mouseClick(MouseEvent event) {
        toggleBtnColors(btn_repeat);
    }

    @FXML
    void shuffle_mouseClick(MouseEvent event) {
        toggleBtnColors(btn_shuffle);
    }

    @FXML
    void stop_mouseClick(MouseEvent event) {
        if(mpCreated){
            ImgView_PlayPause.setImage(play);
            mp.stop();
        }
    }

    @FXML
    void nextSong_mouseClick(MouseEvent event) {

        if(currentIndex >= obList_musicCollected.size()-1){
            return;
        }
            currentIndex++;
            tableView_Playlist.getSelectionModel().selectBelowCell();
            changeSong();
    }

    @FXML
    void previousSong_mouseClick(MouseEvent event) {

        if(currentIndex == 0){
            return;
        }
            currentIndex--;
            tableView_Playlist.getSelectionModel().selectAboveCell();
            changeSong();
    }

    @FXML
    void firstSong_mouseClick(MouseEvent event) {
        currentIndex = 0;
        changeSong();
        tableView_Playlist.getSelectionModel().select(0);
    }

    public void changeSong() {

        try{
        mp.dispose();
        System.out.println("Index = " + currentIndex);
        mp = new MediaPlayer(new Media(new File(obList_musicCollected.get(currentIndex).getPath()).toURI().toString()));
        prepareMetaData();
        playPauseMusic();
        playNextMusic();

        }catch(MediaException e){
        System.out.println(e);
        lbl_nowPlaying.setText("Music was moved, original path not found.");
        }
    }

    @FXML
    void musicUpload(DragEvent event) {

    tableView_Playlist.setOnDragOver(new EventHandler<DragEvent>() {

            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles())
                    event.acceptTransferModes(TransferMode.COPY);
                else
                    event.consume();
            }
        });

        tableView_Playlist.setOnDragDropped(new EventHandler<DragEvent>() {

            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;  //poe a booleana no seu estado inicial, falso.
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    String fileName = null;
                    for (File file:db.getFiles()) {
                        //percorre cada ficheiro contido na dragboard e todas as respectivas instruções, ciclicamente
                        filePath = file.getAbsolutePath();
                        fileName = file.getName(); //atribuição a variaveis antes de tratar, para maior legibilidade

                        obList_musicCollected.add(new Music(fileName,filePath));   //adiciona directamente as novas musicas na lista, nesta altura ainda nao as pus na BD
                        tableView_Playlist.setItems(obList_musicCollected);        //e depois adiciona no tableView

                        Music_DaoImpl musicDao = new Music_DaoImpl();   //instancia Music_DaoImpl, para poder usar os respectivos metodos
                        musicDao.insertIntoDb(fileName,filePath,cbBox_Playlist.getSelectionModel().getSelectedItem());
                        //e por ultimo, as musicas são enviadas para a Bd, passando nome, path, e respectiva playlist, tratamento de redundancias de dados feitos na bd
                    }
                }
                event.setDropCompleted(success); //passa o estado da booleana para encerrar o drop do ficheiro.
                event.consume();
            }
        });
    }

    @FXML
    void songDel_mouseClick(MouseEvent event) {
       String selectedSong = tableView_Playlist.getSelectionModel().getSelectedItem().getSongName();
       String selectedPlaylist = cbBox_Playlist.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Music Deletion Confirmation");
        alert.setHeaderText("Deleting music : " + selectedSong +" ?");
        alert.setContentText("By pressing OK, the selected music will be deleted from the database.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {      //caso clique em OK, será corrido o metodo abaixo
                Music_DaoImpl musicDao = new Music_DaoImpl();
                musicDao.removeSongFromDb(selectedSong,selectedPlaylist);
            }
        });
    }

    @FXML
    void delPlaylist_mouseClick(MouseEvent event) {
        String selectedPlaylist = cbBox_Playlist.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Playlist Deletion Confirmation");
        alert.setHeaderText("Deleting playlist : " + selectedPlaylist +" ?");
        alert.setContentText("By pressing OK, the selected Playlist will be deleted from the database.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {      //caso clique em OK, será corrido o metodo abaixo
                Playlist_DaoImpl playlistDao = new Playlist_DaoImpl();
                playlistDao.deletePlaylist(selectedPlaylist);
            }
        });
    }

    @FXML
    void createPlaylist_mouseClick(MouseEvent event) {

        String newPlaylistName = tb_newPlaylistName.getText();
        String currentUser = lbl_userName.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Playlist Creation Confirmation");
        alert.setHeaderText("Creating playlist : " + newPlaylistName +" ?");
        alert.setContentText("By pressing OK, this Playlist will be created on the database.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {      //caso clique em OK, será corrido o metodo abaixo
                Playlist_DaoImpl playlistDao = new Playlist_DaoImpl();
                playlistDao.createPlaylist(newPlaylistName,currentUser);
            }
        });
    }

}

