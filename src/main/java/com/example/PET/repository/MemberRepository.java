package com.example.PET.repository;

import com.example.PET.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberRepository {
    private final DataSource dataSource;

    public Member save(Member membership) {
        String sql = "insert into membership (user_id, user_pw, user_name,question_goal) values (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, membership.getUser_id());
            pstmt.setString(2, membership.getUser_pw());
            pstmt.setString(3, membership.getUser_name());
            pstmt.setInt(4, membership.getQuestion_goal());

            pstmt.executeUpdate();

            return membership;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, null);
        }

        return null;
    }

    public Member login(String user_id, String user_pw) {
        String sql = "select * from membership where user_id = ? and user_pw = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user_id);
            pstmt.setString(2, user_pw);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member membership = new Member();
                membership.setUser_id(rs.getString("user_id"));
                membership.setUser_pw(rs.getString("user_pw"));
                membership.setUser_name(rs.getString("user_name"));
                membership.setQuestion_goal(rs.getInt("question_goal"));
                sql = "update membership set login_check=1 where user_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1,user_id);
                pstmt.executeUpdate();
                return membership;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }

        return null;
    }

    public int idcheck(String user_id){
        String sql = "select count(*) from membership where user_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user_id);

            rs = pstmt.executeQuery();

            rs.next();

            if(rs.getInt(1) == 1) {
                return 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }

        return 0;
    }


    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(pstmt);
        JdbcUtils.closeConnection(conn);
    }

    private Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        log.info("get connection = {}, class = {}", conn, conn.getClass());
        return conn;
    }

    public boolean login_checkout(String user_id) {
        String sql = "update membership set login_check=0 where user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user_id);
            pstmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }

        return false;
    }
}

