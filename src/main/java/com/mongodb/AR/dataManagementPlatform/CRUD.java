package com.mongodb.AR.dataManagementPlatform;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import static com.mongodb.client.model.Filters.eq;

/**
 * Contains all generic methods needed by the CRUD operations
 */
public class CRUD {
    /**
     * Here a printWriter is instantiated once to be used for all CRUD operations and keep the log file up to date
     * with successful and unsuccessful operations and the reason behind failures
     */
    static PrintWriter out;
    static {
        try {
            out = new PrintWriter(new FileOutputStream("data/LogFile.txt", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the filesCollection from the MongoDB
     * @return
     */
    protected static MongoCollection<Document> getFilesCollection(){
        MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"));
        MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_training");
        return sampleTrainingDB.getCollection("ARObjectsDatabase");
    }

    /**
     * Returns the statsCollection from the MongoDB
     * @return
     */
    protected static MongoCollection<Document> getStatsCollection(){
        MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"));
        MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_training");
        return sampleTrainingDB.getCollection("AR_DB_Stats");
    }

    /**
     * String manipulation to retrieve fileName from filePath
     * @param filePath
     * @return
     */
    protected static String getFileName(String filePath){
        int lastSlash = filePath.lastIndexOf("/");
        String fileName = filePath.substring(lastSlash + 1, filePath.length());
        return fileName;
    }

    /**
     * Retrieves the file size of files using the filePaths
     * @param file
     * @return
     */
    protected static Double getFileSizeKiloBytes(File file) {
        return (double) file.length() / 1024;
    }

    /**
     * Clears the log file
     */
    protected static void clearLogFile(){
        try (PrintWriter writer = new PrintWriter(new FileOutputStream("data/LogFile.txt"))) {
            writer.print("");
            writer.close();
        }
        catch (FileNotFoundException e){

        }
    }

    /**
     * Takes as input an Arraylist of FilePaths and returns a
     * hashset of no duplicate files and no invalid filePaths
     * @param filePaths
     * @return
     */
    protected static HashSet<String> getValidFilePaths(ArrayList<String> filePaths){
        File file;
        String filePath;
        HashSet<String> hsFilePaths = new HashSet<>();
        MongoCollection<Document> filesCollection = getFilesCollection();

        for (int i = 0; i <filePaths.size() ; i++) {
            filePath = filePaths.get(i);
            file = new File(filePath);
            if (!file.exists() || !file.isFile()){
                out.println(filePath + " is an invalid file path and was not added to the database on " + new Date());
                continue;
            }
            if(filesCollection.countDocuments(eq("FilePath", filePath)) == 0){
                if(hsFilePaths.contains(filePath)){
                    out.println(filePath + " was not added to the database as there is another file with the same file path in the same insertMany operation on " + new Date());
                }else{
                    hsFilePaths.add(filePath);
                    out.println(filePath + " has been added to the database successfully on " + new Date());
                }
            }else{
                out.println(filePath + " was not added to the database as there is another file with the same file path on " + new Date());
            }
        }
        out.flush();
        return hsFilePaths;
    }
}
