package com.mongodb.AR.dataManagementPlatform;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.File;
import java.util.Date;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class Delete extends CRUD{

    public static void main(String[] args) {
        MongoCollection<Document> filesCollection = getFilesCollection();
        deleteFile("C:/Users/Zeina Kandil/Downloads/ergonomic-bottle-1.snapshot.2/Bottle.obj");
//        dropAndDeleteAllFiles();
    }

    /*
    To delete one record from the Database without deleting its corresponding file (if any) in the file system.
     */
    public static boolean deleteRecord(String filePath){
        MongoCollection<Document> fileCollections = getFilesCollection();

        Bson filter = eq("FilePath", filePath);
        if(fileCollections.countDocuments(filter) > 0) {
            Document fileDocument = fileCollections.findOneAndDelete(filter);
            Double fileSize = (Double) fileDocument.get("FileSize in (KB)");
            out.println(filePath +" deleted successfully from database on " + new Date());
            updateStatistics(fileSize);
        }
        else{
            out.println(filePath +" could not be deleted from database on " + new Date() + " as there is no such filePaths in the database");
            out.flush();
            return false;
        }
        out.flush();
        return true;
    }

    /*
    To delete a single record from the database along with its corresponding file (if any) in the file system.
     */
    public static boolean deleteFile(String filePath){
        MongoCollection<Document> fileCollections = getFilesCollection();
        Bson filter = eq("FilePath", filePath);
        if(fileCollections.countDocuments(filter) == 1){
            File file = new File(filePath);
            Document fileDocument = fileCollections.findOneAndDelete(filter);
            Double fileSize = (Double) fileDocument.get("FileSize in (KB)");
            updateStatistics(fileSize);
            if(file.delete()){
                out.println(filePath +" deleted successfully from database and file system on " + new Date());
                out.flush();
                return true;
            }
            else{
                out.println(filePath +" has been deleted from database successfully, but failed to be deleted on the file system, on " + new Date() + " as there is no such filePaths in the file system. Good riddance!");
                out.flush();
                return false;
            }
        }
        out.println(filePath +" could not be deleted from database or file system, on " + new Date() + " as there is no such filePaths in the database.");
        out.flush();
        return false;
    }

    /*
    Deletes all records in the filesCollection database and drops the statsCollection as well since it is no longer valid
    Deletes Table Structure
     */
    public static boolean drop(){
        MongoCollection<Document> fileCollections = getFilesCollection();
        MongoCollection<Document> statsCollection = getStatsCollection();
        fileCollections.drop();
        statsCollection.drop();
        out.println("FilesCollection has been dropped successfully on " + new Date());
        out.flush();
        return true;
    }

    /*
    Deletes all records in the filesCollection database and their corresponding files if any and
    drops the statsCollection as well since it is no longer valid
    Deletes table structure
     */
    public static boolean dropAndDeleteAllFiles(){
        MongoCollection<Document> fileCollections = getFilesCollection();
        MongoCollection<Document> statsCollection = getStatsCollection();
        statsCollection.drop();

        // Delete files from file system then drop filesCollection
        for (Document document: fileCollections.find()) {
            String filePath = (String) document.get("FilePath");
            File file = new File(filePath);
            file.delete();
        }
        fileCollections.drop();
        out.println("FilesCollection has been dropped successfully and all valid corresponding files on file system have been removed on " + new Date());
        out.flush();
        return true;
    }

    /*
    After a single deletion statistics can be updated by simply deducting 1 file from count and decrementing the totalFileSize
     */
    private static void updateStatistics(Double fileSize){
        //Retrieve collections
        MongoCollection<Document> statsCollection = getStatsCollection();

        /*
        Retrieve existing statsDocument and update it
         */
        Bson filter = eq("Name","Statistics Document");
        Document statsDoc = statsCollection.find(filter).first();
        int filesCount = (int) statsDoc.get("Files Count") - 1;
        Double totalFileSizes = (Double) statsDoc.get("Total Files Storage Space in KB") - fileSize;
        int totalWritesCount = (int) statsDoc.get("CountTotalWrites") + 1;
        Double avgFileSize = totalFileSizes / filesCount;
        Date lastUpdated = new Date();

        Bson update1 = set("Files Count", filesCount);
        Bson update2 = set("Total Files Storage Space in KB", totalFileSizes);
        Bson update3 = set("Average File Size", avgFileSize);
        Bson update4 = set("FilesCollection lastUpdated", lastUpdated);
        Bson update5 = set("CountTotalWrites", totalWritesCount);
        Bson updateOperations = combine(update1, update2, update3, update4, update5);
        statsCollection.findOneAndUpdate(filter, updateOperations);
    }
}
