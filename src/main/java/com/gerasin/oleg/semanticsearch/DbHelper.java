package com.gerasin.oleg.semanticsearch;

import model.Publication;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import java.util.List;
import model.Log;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 *
 * @author geras
 */
public class DbHelper
{
    private final Datastore logDatastore;

    private MongoCollection<Document> publicationCollection;

    public DbHelper()
    {
        final Morphia morphia = new Morphia();
        morphia.mapPackage("model");
        logDatastore = morphia.createDatastore(new MongoClient(), "morphia_example_log__1");
        logDatastore.ensureIndexes();
    }

    public void createLog(String keyword, List<Publication> publications)
    {
        Log newLog = new Log(keyword, publications);
        logDatastore.save(newLog);
    }

    public List<Publication> getCachedPublications(String keyword)
    {
        final List<Log> logs =
                logDatastore.createQuery(Log.class)
                                       .field("keyword").equal(keyword)
                                       .order("-date")
                                       .asList();
        if (!logs.isEmpty())
        {
            return logs.get(0).getPublications();
        }

        return null;
    }

}
