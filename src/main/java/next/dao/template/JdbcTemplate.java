package next.dao.template;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract public class JdbcTemplate {

    public void update(){
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(createQuery()))
        {
            setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e){}

    }

    public abstract String createQuery();
    public abstract void setValues(PreparedStatement pstmt) throws SQLException;
}
