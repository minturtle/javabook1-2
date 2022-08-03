package next.dao.template;

import core.jdbc.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    
    private PreParedStatementSetter preParedStatementSetter;
    private RowMapper rowMapper;
    
    public JdbcTemplate(PreParedStatementSetter preParedStatementSetter) {
        this.preParedStatementSetter = preParedStatementSetter;
    }

    public JdbcTemplate(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    public JdbcTemplate(PreParedStatementSetter preParedStatementSetter, RowMapper rowMapper) {
        this.preParedStatementSetter = preParedStatementSetter;
        this.rowMapper = rowMapper;
    }

    //사용하기 위해선 PreParedStatementSetter Interface를 구현해 생성자에 넣어줘야 합니다.
    /*
     * 1, sql문을 입력받는다.
     * 2, pstmt를 만들어 preParedStatementSetter로 SQL값을 입력한다.
     * 3, DB에 쿼리를 실행한다.
     * */
    public void update(String sql){
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql))
        {
            preParedStatementSetter.setValues(pstmt); //2
            pstmt.executeUpdate(); //3
        } catch (SQLException e){}

    }

    //사용하기 위해선 RowMapper Interface를 구현해 생성자에 넣어줘야 합니다.
    /*
     * 1, sql문을 입력받는다.
     * 2, DB에 쿼리를 실행해 결과값인 ResultSet을 받는다.
     * 3, ResultSet에서 rowMapper로 DB값을 자바 객체로 매핑해 리스트에 추가한다.
     * 4, 리스트를 반환한다.
     * */
    public List<Object> select(String sql){
        ArrayList<Object> list = new ArrayList<>();
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql))
        {
            ResultSet resultSet = pstmt.executeQuery(); //2

            //3
            while(resultSet.next()){
                list.add(rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e){}
        //4
        return list;
    }

    //사용하기 위해선 PreParedStatementSetter와 RowMapper Interface를 구현해 생성자에 넣어줘야 합니다.
    /*
    * 1, sql문을 입력받는다.
    * 2, pstmt를 만들어 preParedStatementSetter로 SQL값을 입력한다.
    * 3, DB에 쿼리를 실행해 결과값인 ResultSet을 받는다.
    * 4, ResultSet에서 rowMapper로 DB값을 자바 객체로 매핑한다.
    * 5, 매핑한 자바객체를 반환한다.
    * */
    public Object selectForObject(String sql){
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql))
        {

            preParedStatementSetter.setValues(pstmt);  //2
            ResultSet resultSet = pstmt.executeQuery(); //3

            // 4 & 5
            if(resultSet.next()){
                Object obj = rowMapper.mapRow(resultSet);
                return obj;
            }
            
        } catch (SQLException e){e.printStackTrace();}
        return null;

    }

    
    //SQL에 값을 채워주는 인터페이스
    public interface PreParedStatementSetter{
        void setValues(PreparedStatement pstmt) throws SQLException;
    }
    //SQL로 받은 값을 자바 객체로 Mapping하는 인터페이스
    public interface RowMapper{
        Object mapRow(ResultSet resultSet) throws SQLException;
    }
}
