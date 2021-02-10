package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class Music_DaoImpl implements IDao_Music {

    @Override
    public ObservableList<Music> getAllMusicElements(String selectedPlaylist) {
        ObservableList<Music> obList_collectedMusic = FXCollections.observableArrayList();

        try (Connection con = Database_Connector.getConnection(); //try with resources nao precisa de close connections
             CallableStatement cstmt = con.prepareCall("{call usp_recolheInfoMusic(?)}");) {

            cstmt.setString("playlistName", selectedPlaylist);
            cstmt.execute();
            ResultSet rset = cstmt.getResultSet();

            while(rset.next()) {

                    //enquanto encontrar elementos no resultado da query, vai passando uma nova instancia da classe Music para dentro da lista
                    obList_collectedMusic.add(new Music(rset.getString("musicTitle"), rset.getString("path")));
                    //instancia Music com: [titulo ,PATH] e passa para uma lista
                    System.out.println("Adiciona:" + rset.getString("musicTitle"));

            }
            System.out.println("Musica(s) apanhada(s) com sucesso para lista");
            return obList_collectedMusic;
            //devolve a lista com as instancias de Music
        }   catch (SQLException e) {
            e.printStackTrace();
        }
        return null; //só chega a este null caso por algum motivo não tenha conseguido o try
    }


    @Override
    public String insertIntoDb(String songName, String path, String selectedPlaylist){
        try (Connection con = Database_Connector.getConnection();

             CallableStatement cstmt = con.prepareCall("{call usp_insertMusic(?,?,?)}");) {

                cstmt.setString("songName",songName);
                cstmt.setString("path",path);
                cstmt.setString("selectedPlaylist",selectedPlaylist);
                //cstmt.setString("user", currentUser);
                cstmt.execute();
            System.out.println("passou musica");

        }   catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String removeSongFromDb(String songName, String selectedPlaylist){
        try (Connection con = Database_Connector.getConnection();

             CallableStatement cstmt = con.prepareCall("{call usp_deleteMusic(?,?)}");) {

            cstmt.setString("songName",songName);
            cstmt.setString("selectedPlaylist",selectedPlaylist);
            cstmt.execute();
            System.out.println("apagou musica");

        }   catch (SQLException e) {
            e.printStackTrace();
            System.out.println("NAO apagou musica");
        }
        return null;
    }

}
