package demo_jdbc.respositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import demo_jdbc.models.DriverResult;

public class DriverResultRepository {

    String jdbcUrl = "jdbc:postgresql://localhost:5432/Formula_01";
    String user = "postgres";
    String password = "admi1234";

    public List<Integer> getAvailableYears() {
        List<Integer> years = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
            String sql = "SELECT DISTINCT year FROM races ORDER BY year";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                years.add(rs.getInt("year"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return years;
    }

    public List<DriverResult> getResultByYear(int year) {
        List<DriverResult> results = new ArrayList<>();
        try {
            // Establecer la conexion
            Connection conn = DriverManager.getConnection(jdbcUrl, user, password);

            // Ejecutar la consulta
            String sql = "SELECT\n"
                    + "    r.year,\n"
                    + "    d.forename,\n"
                    + "    d.surname,\n"
                    + "    COUNT(CASE WHEN res.position = 1 THEN 1 END) AS wins,\n"
                    + "    SUM(res.points) AS total_points,\n"
                    + "    RANK() OVER (PARTITION BY r.year ORDER BY SUM(res.points) DESC) AS season_rank\n"
                    + "FROM\n"
                    + "    results res\n"
                    + "JOIN\n"
                    + "    races r ON res.race_id = r.race_id\n"
                    + "JOIN\n"
                    + "    drivers d ON res.driver_id = d.driver_id\n"
                    + "WHERE r.year = ? \n"
                    + "GROUP BY\n"
                    + "    r.year, d.driver_id, d.forename, d.surname\n"
                    + "ORDER BY\n"
                    + "    r.year, season_rank;";

            // Crear una sentencia
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, year);

            ResultSet rs = statement.executeQuery();

            // Procesar los resultados
            while (rs.next()) {
                String forename = rs.getString("forename");
                String surname = rs.getString("surname");
                int wins = rs.getInt("wins");
                int total_points = rs.getInt("total_points");
                int season_rank = rs.getInt("season_rank");

                DriverResult result = new DriverResult(forename + " " + surname, wins, total_points, season_rank);
                results.add(result);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return results;
    }
}