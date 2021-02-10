package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;

public class Main extends Application {

    private static Connection conn = null;
    private static void sqlcont(){

        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=playlist;user=javaTeste;password=123");

            System.out.println("Teste de conecção feito com sucesso, comunica");

        } catch (SQLException e) {
            throw new Error("Não comunicou", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }   //nao esquecer de trocar por Database_connector

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 280, 305));
        primaryStage.show();
    }

       public static void main(String[] args) {

           sqlcont();
           launch(args);

    }
}
