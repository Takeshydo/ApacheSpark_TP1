package fr.nexus.erp;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

public class DataEngine {

    public static void runPipeline(){
        System.setProperty("hadoop.home.dir","C:\\Hadoop\\hadoop-3.3.6");
        System.load("C:\\Hadoop\\hadoop-3.3.6\\bin\\hadoop.dll");

        SparkSession spark = SparkSession.builder()
                .appName("spark")
                .config("spark.master", "local[*]")
                .getOrCreate();

        String url ="jdbc:mysql://localhost:3306/nexus_erp";
        String usr = "root";
        String password ="";

        Properties connectionProp = new Properties();
        connectionProp.put("user",usr);
        connectionProp.put("password", password);
        connectionProp.put("driver","com.mysql.cj.jdbc.Driver");

        System.out.println("=========================================");
        System.out.println("Connection DB Mysql");
        System.out.println("=========================================");

        Dataset<Row> dfMySQL = spark.read().jdbc(url,"server_logs", connectionProp);
        Dataset<Row> dfFilter = dfMySQL.select("serverName","Country","Status");

        System.out.println("=========================================");
        System.out.println("ETAPE MAP");
        System.out.println("=========================================");

        Dataset<Row> dfMap = dfFilter
                .select("serverName", "Country", "Status")
                .filter(dfMySQL.col("Country").isNotNull());

        dfMap.show();

        System.out.println("=========================================");
        System.out.println("ETAPE Reduce a shuffle");
        System.out.println("=========================================");

        Dataset<Row> dfReduce = dfMap.orderBy("Country", "serverName");

        String cheminExportJSON = "archives_logs_server";

        dfReduce.write()
                .mode(SaveMode.Overwrite)
                .partitionBy("country")
                .json(cheminExportJSON);

        System.out.println("Export realiser : " + cheminExportJSON);
    }
}
