package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class Login {

    private static Connection conn = null;
    public static String userName;

    @FXML private TextField tb_nome;
    @FXML private PasswordField tb_password;
    @FXML private Label lbl_test;

    @FXML
    void login_mouseClick(MouseEvent event) throws IOException {

        lbl_test.setText("label de teste");

        CallableStatement cstmt = null;
        ResultSet rs = null;

        //------------------
        try(Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=playlist;user=javaTeste;password=123");) {

            cstmt = conn.prepareCall("{call usp_login(?,?,?)}");

            cstmt.setString("utilizador",tb_nome.getText());
            cstmt.setString("pw",tb_password.getText());
            cstmt.registerOutParameter("retorno", Types.INTEGER);
            cstmt.execute();

            int resposta = cstmt.getInt("retorno");

            lbl_test.setText(String.valueOf(resposta));

            if(resposta == 0)
            {
                lbl_test.setText("Utilizador ou password inválidos");
                System.out.println("stored procedure devolveu 0 na variavel de output, não encontrou utilizadores com aquela password associada");
            }
            if(resposta == 1)
            {
                lbl_test.setText("Autenticou bem");
                System.out.println("stored procedure devolveu 1 na variavel de output, autenticação correcta.");
                userName = tb_nome.getText();


                Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/view/MenuPlaylist.fxml"));
                Scene mainMenuScene = new Scene(mainMenuParent);

                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                window.setTitle("MenuPlaylist");
                window.setScene(mainMenuScene);
                window.show();

            }

        } catch (SQLException | IOException e) {
            System.out.println(e);

        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        //---------------------------
    }

    @FXML
    void register_mouseClick(MouseEvent event) throws IOException {

        Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/CreateAccount.fxml"));
        Scene createAccountScene = new Scene(createAccountParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(createAccountScene);
        window.show();
    }
}

