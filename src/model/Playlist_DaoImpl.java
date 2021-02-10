package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class Playlist_DaoImpl implements IDao_Playlist {

    @Override
    public ObservableList<Playlist> getAllPlaylistElements(String userName) {
        ObservableList<Playlist> obList_collectedPlaylists = FXCollections.observableArrayList();

        try (Connection con = Database_Connector.getConnection();
             CallableStatement cstmt = con.prepareCall("{call usp_recolheInfoPlaylist(?)}");) {

            cstmt.setString("userName", userName);
            cstmt.execute();
            ResultSet rset = cstmt.getResultSet();

            while(rset.next()) {
                obList_collectedPlaylists.add(new Playlist(rset.getString("playlistName")));
            }
            System.out.println("Playlist(s) apanhada(s) com sucesso para lista");
            return obList_collectedPlaylists;

        }   catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String createPlaylist(String newPlaylistName, String currentUser){
        try (Connection con = Database_Connector.getConnection();

             CallableStatement cstmt = con.prepareCall("{call usp_createPlaylist(?,?)}");) {

            cstmt.setString("newPlaylistName",newPlaylistName);
            cstmt.setString("currentUser",currentUser);
            cstmt.execute();
            System.out.println("criou playlist");

        }   catch (SQLException e) {
            e.printStackTrace();
            System.out.println("NAO criou playlist");
        }
        return null;
    }

    @Override
    public String deletePlaylist(String selectedPlaylist){
        try (Connection con = Database_Connector.getConnection();

             CallableStatement cstmt = con.prepareCall("{call usp_deletePlaylist(?)}");) {

            cstmt.setString("selectedPlaylist",selectedPlaylist);
            cstmt.execute();
            System.out.println("apagou playlist");

        }   catch (SQLException e) {
            e.printStackTrace();
            System.out.println("NAO apagou playlist");
        }
        return null;
    }
}
