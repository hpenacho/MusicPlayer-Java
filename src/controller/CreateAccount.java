package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateAccount {

    private static Connection conn = null;

    @FXML private TextField tb_nome;
    @FXML private PasswordField tb_password;
    @FXML private TextField tb_email;
    @FXML private PasswordField tb_confirmPw;
    @FXML private Label lbl_message;

    @FXML
    void login_mouseClick(MouseEvent event) throws IOException {

        Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Scene createAccountScene = new Scene(createAccountParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(createAccountScene);
        window.show();

    }

    @FXML
    void register_mouseClick(MouseEvent event) {


        if(tb_nome.getText().isEmpty() || tb_email.getText().isEmpty() || tb_password.getText().isEmpty() || tb_confirmPw.getText().isEmpty())

        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Missing Fields");
            alert.setContentText("One or more fields are missing." + "\nPlease fill every field.");
            alert.showAndWait();
        }

        else if(!tb_password.getText().equals(tb_confirmPw.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Password Error");
            alert.setContentText("Password fields must match." + "\nPlease try again.");
            alert.showAndWait();
        }

        else
        {

            CallableStatement cstmt = null;
            ResultSet rs = null;

            //------------------
            try(Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=playlist;user=javaTeste;password=123");) {

                cstmt = conn.prepareCall("{call usp_registo(?,?,?,?)}");

                cstmt.setString("util",tb_nome.getText());
                cstmt.setString("pw",tb_password.getText());
                cstmt.setString("email",tb_email.getText());
                cstmt.registerOutParameter("retorno", Types.INTEGER);
                cstmt.execute();

                int resposta = cstmt.getInt("retorno");


                if(resposta == 0)
                {
                    lbl_message.setText("User or Email already in use.");
                    System.out.println("Base de dados retornou 0, portanto user ou email ja estava em uso na BD.");
                }
                if(resposta == 1)
                {
                    lbl_message.setText("Account created successfully!");
                    System.out.println("Base de dados retornou 1, portanto insert foi feito com sucesso na BD");
                }

            } catch (SQLException e) {
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

        }
    }

}
