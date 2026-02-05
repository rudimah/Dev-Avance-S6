package org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private String url = "jdbc:postgresql://database-etudiants:5432/hrahman";
    private String user = "hrahman";
    private String passwd = "Bangladesh.2021";

    /**
     * Objet Connection
     */
    private static Connection connect;
    /**
     * Constructeur privé
     * @throws ClassNotFoundException
     */
    private ConnectionDB() throws ClassNotFoundException{
        try {
            Class.forName("org.postgresql.Driver");
            connect = DriverManager.getConnection(url, user, passwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Methode qui va nous retourner notre instance
     * et la creer si elle n'existe pas...
     * @return
     * @throws ClassNotFoundException
     */
    public static Connection getInstance() throws ClassNotFoundException{
        if(connect == null){
            new ConnectionDB();
        }
        return connect;
    }
}