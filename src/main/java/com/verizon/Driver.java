package com.verizon;


import java.io.*;
import java.sql.*;

public class Driver {

    private Connection oracleConnection;
    private Statement statement;
    private ResultSet resultSet;

    public Driver(){
        this.oracleConnection = oracleConnection;
        this.statement = statement;
        this.resultSet = resultSet;
    }

    private static Connection getConnection() throws SQLException {
        IConnector oracleConnector = new OracleConnectorImpl();
        Connection oracleConnection = oracleConnector.
                createConnection("jdbc:mysql://localhost:3306/test", "root", "admin123");
        return oracleConnection;
    }

    public void executeStatement(String sql, String fileName) throws SQLException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

        oracleConnection = Driver.getConnection();

        System.out.println("SQL Connection to database established!");

        // this allows to stream the records on heap issues
        statement = oracleConnection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                java.sql.ResultSet.CONCUR_READ_ONLY);
        statement.setFetchSize(Integer.MIN_VALUE);

        if(sql.length() > 0) {
            resultSet = statement.executeQuery(sql);
            writeResultSetToWriter(resultSet, fileName);
        }
        else{
            System.out.println("Please enter valid query...");
        }
    }

        private static void writeResultSetToWriter(ResultSet resultSet, String fileName) throws SQLException, IOException {
            FileWriter fileWriter = new FileWriter(fileName, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            //printWriter.print(true);
            ResultSetMetaData metadata = resultSet.getMetaData();
            int numColumns = metadata.getColumnCount();
            int numRows = 0;
            while (resultSet.next())
            {
                ++numRows;
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 1; i <= numColumns; ++i)
                {
                    String column_name = metadata.getColumnName(i);
                    stringBuffer.append(resultSet.getObject(column_name));
                    //if to write as JSON use below
                    //obj.put(column_name, resultSet.getObject(column_name));
                    //writer.println(obj.toJSONString());
                }
                printWriter.println(stringBuffer.toString());
                //writer.write(stringBuffer.toString());
                if (numRows % 1000 == 0)
                    printWriter.flush();
            }
            printWriter.close();
        }
}
