package model;

import javafx.collections.ObservableList;

public interface IDao_Music {
    ObservableList<Music> getAllMusicElements(String selectedPlaylist);  // *1
    String insertIntoDb (String songName,String path, String selectedPlaylist); //*2
    String removeSongFromDb (String songName, String selectedPlaylist); //*3
}
          /*
                 *1) recebe nome da playlist escolhida, pergunta à bd atraves de SP quais as respectivas músicas
                       e devolve o respectivo conteudo para uma lista de objectos Music.
                 *2) passa informação de novas músicas para Bd.
                 *3) apaga a musica selecionada, respectiva à playlist em uso.
          */
