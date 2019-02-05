import java.sql.*;

public class DatabaseCon {
    //save the point in database
    public static int save(int point){
        String url="jdbc:mysql://localhost:3306/point";
        String user="root";
        String password="root";
        try{
            Connection con= DriverManager.getConnection(url,user,password);//setting connection to database
            Statement stmt = con.createStatement();//setting for statement to proceed and to pass query in database
            String query="update userpoint set point=('"+point+"') where id=1";///this is query
            stmt.executeUpdate(query);//it execute query in database
            con.close();
            return point;
        }catch(Exception e){System.out.println(e);}
        return point;
    }
    //retrive the point from database when game is started
    public static int retrive() {
        int value=10;

        String url="jdbc:mysql://localhost:3306/point";
        String user="root";
        String password="root";
        try {
            Connection Con = DriverManager.getConnection(url,user,password);
            Statement stmt = Con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from userpoint");
            while (rs.next()) {
                value = rs.getInt("point");
            }
            /*String query = "update userpoint set point=('" + point + "') where id=1";*/
            /*stmt.executeUpdate(query);*/

            return value;

        } catch (SQLException e) {
            e.printStackTrace();
            return value;
        }
    }
}

