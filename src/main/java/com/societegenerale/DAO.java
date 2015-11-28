package com.societegenerale;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DAO {
    private static Connection conn = null;
    private static PreparedStatement st = null;
    
    static {
        String propfileName = "config.properties";
        Properties prop = new Properties();
        InputStream in = MemberStoring.class.getClassLoader()
                .getResourceAsStream(propfileName);
        try {
            if (in != null) {
                prop.load(in);
                in.close();
            } else {
                throw new FileNotFoundException("property file '" + propfileName
                        + "' not found in the classpath.");
            }

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(prop.getProperty("url"),
                    prop.getProperty("username"), prop.getProperty("password"));
            conn.setAutoCommit(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int getCount() {
        String sql = "SELECT COUNT(1) FROM SGMembers;";
        int count = -1;
        try {
            st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            rs.next();
            count = rs.getInt(1);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static List<Member> getMembers(String[][] finalParts) {
        String sql = "SELECT * FROM SGMembers WHERE ";
        for(String[] parts : finalParts) {
            sql += parts[0] + " LIKE '%" + parts[1] + "%' AND";
        }
        sql = sql.substring(0, sql.length()-3) + ";";
        
        return getMembersSQL(sql);
    }

    private static List<Member> getMembersSQL(String sql) {
        try {
            st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            List<Member> members = new ArrayList<>();
            while(rs.next()) {
                Member member = new Member();
                member.setId(rs.getInt(1));
                member.setStatus(rs.getString(2));
                member.setRace(rs.getString(3));
                member.setWeight(rs.getDouble(4));
                member.setHeight(rs.getDouble(5));
                member.setIs_veg(rs.getBoolean(6));
                members.add(member);
            }
            rs.close();
            return members;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Member> getMembers(String query) {
        String sql;
        if (query.isEmpty()) {
            sql = "SELECT * FROM SGMembers;";
        } else {
            sql = "SELECT * FROM SGMembers WHERE status LIKE '%" + query.split("=")[1] + "%';";
        }
        return getMembersSQL(sql);
    }

}
