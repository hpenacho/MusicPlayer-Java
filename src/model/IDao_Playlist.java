package model;

import javafx.collections.ObservableList;

public interface IDao_Playlist {
    ObservableList<Playlist> getAllPlaylistElements(String userName);  //*1
    String createPlaylist (String newPlaylistName, String currentUser);    //*2
    String deletePlaylist (String selectedPlaylist);    //*3
}
        /* 1) recebe nome do utilizador activo , pergunta à bd atraves de SP quais as respectivas Playlists
                devolve o respectivo conteudo para uma lista de objectos Playlist
           2) recebe string da playlist selecionada, passa como parametro ao metodo que irá ligar à bd para CRIAR uma nova Playlist
           3) APAGAR a Playlist selecionada e todas as musicas nesta contida
         */