package com.gerasin.oleg.semanticsearch;

import com.gerasin.oleg.semanticsearch.model.Log;
import com.gerasin.oleg.semanticsearch.model.Publication;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 *
 * @author geras
 */
@Named
@SessionScoped
public class DbHelper
        implements Serializable
{
    private static final String DB_NAME = "heroku_6pwfv8hk";
    private static final String MONGO_CLIENT_URI = "mongodb://heroku_6pwfv8hk:ppmjcftfp61mitqs1gs72g1di2@ds231228.mlab.com:31228/heroku_6pwfv8hk";
    private final Datastore logDatastore;

    private final MongoCollection<Document> logCollection;

    public DbHelper()
    {
        final Morphia morphia = new Morphia();
        morphia.mapPackage("model");
        MongoClientURI uri  = new MongoClientURI(MONGO_CLIENT_URI);
        MongoClient mongoClient = new MongoClient(uri);
        logDatastore = morphia.createDatastore(mongoClient, DB_NAME);
        MongoDatabase database = mongoClient.getDatabase(uri.getDatabase());
        logCollection = database.getCollection(Log.ENTITY_NAME);
        logDatastore.ensureIndexes();
    }

    public void createLog(String keyword, List<Publication> publications)
    {
        Log newLog = new Log(keyword, publications);
        logDatastore.save(newLog);
    }

    public List<Publication> getCachedPublications(String keyword)
    {
        //TO DO: Return log, not publications to show the date of result query
        final List<Log> logs =
                logDatastore.createQuery(Log.class)
                                       .field(Log.KEYWORD).equal(keyword)
                                       .order("-" + Log.DATE)
                                       .asList();
        if (!logs.isEmpty())
        {
            return logs.get(0).getPublications();
        }

        return null;
    }

    public String getLogsAsJson()
    {
        StringBuilder result = new StringBuilder();

        for ( Document cur : logCollection.find().sort(Sorts.descending(Log.DATE)) )
        {
            result.append(cur.toJson()).append("\n");
        }

        return result.toString();
    }

}
