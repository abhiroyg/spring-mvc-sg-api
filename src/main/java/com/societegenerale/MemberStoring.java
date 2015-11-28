package com.societegenerale;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class MemberStoring {
    public static void main(String[] args) throws FileNotFoundException,
            IOException, ClassNotFoundException, SQLException {
        InputStream in = MemberStoring.class.getClassLoader()
                .getResourceAsStream("member_detail.csv");
        List<CSVRecord> members = CSVFormat.DEFAULT.withQuote('\"')
                .parse(new InputStreamReader(in, "UTF-8")).getRecords();
        in.close();

        in = MemberStoring.class.getClassLoader()
                .getResourceAsStream("ethnic-group.csv");
        BufferedReader br = new BufferedReader(
                new InputStreamReader(in, "UTF-8"));
        Map<String, String> ethnicGroups = new HashMap<>();
        String line = null;
        while ((line = br.readLine()) != null) {
            String str[] = line.split(",");
            ethnicGroups.put(str[0].trim(), str[1].trim());
        }
        in.close();
        br.close();

        String propfileName = "config.properties";
        Properties prop = new Properties();
        in = MemberStoring.class.getClassLoader()
                .getResourceAsStream(propfileName);
        if (in != null) {
            prop.load(in);
            in.close();
        } else {
            throw new FileNotFoundException("property file '" + propfileName
                    + "' not found in the classpath.");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(prop.getProperty("url"),
                    prop.getProperty("username"), prop.getProperty("password"));
            conn.setAutoCommit(false);

            String sqlFormat = "INSERT INTO SGMembers VALUES (?, ?, ?, ?, ?, ?);";
            st = conn.prepareStatement(sqlFormat);

            for (CSVRecord record : members) {
                st.setInt(1, Integer.parseInt(record.get(0)));
                st.setString(2, record.get(1));
                st.setString(3, ethnicGroups.get(record.get(2)));
                st.setDouble(4, Double.parseDouble(record.get(3)) / 1000);
                st.setDouble(5, Double.parseDouble(record.get(4)));
                st.setInt(6, Integer.parseInt(record.get(5)));
                st.executeUpdate();
            }
            conn.commit();
            System.out.println("Inserted " + members.size() + " records.");
        } finally {
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.rollback();
                conn.close();
            }
        }
    }
}
