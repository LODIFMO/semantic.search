package com.gerasin.oleg.semanticsearch.helpers;

import com.gerasin.oleg.semanticsearch.model.Log;
import com.gerasin.oleg.semanticsearch.model.Publication;
import com.gerasin.oleg.semanticsearch.model.User;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final MongoDatabase database;
    private final Datastore datastore;

    private static final Logger log = LogManager.getLogger(DbHelper.class.getName());

    public DbHelper()
    {
        final Morphia morphia = new Morphia();
        morphia.mapPackage("model");
        MongoClientURI uri  = new MongoClientURI(MONGO_CLIENT_URI);
        MongoClient mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase(uri.getDatabase());
        datastore = morphia.createDatastore(mongoClient, DB_NAME);
        datastore.ensureIndexes();
    }

    public void createLog(String keyword, List<String> sources, List<Publication> publications)
    {
        Log newLog = new Log(keyword, sources, publications);
        datastore.save(newLog);
    }

    public List<Publication> getCachedPublications(String keyword, List<String> sources)
    {
        final List<Log> logs =
                datastore.createQuery(Log.class)
                                       .field(Log.KEYWORD).equal(keyword)
                                       .field(Log.SOURCES).hasAllOf(sources)
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

        MongoCollection<Document> logCollection = database.getCollection(Log.ENTITY_NAME);

        for ( Document cur : logCollection.find().sort(Sorts.descending(Log.DATE)) )
        {
            result.append(cur.toJson()).append("\n");
        }

        return result.toString();
    }

    //<editor-fold defaultstate="collapsed" desc="wotrk with user">
    public User validateUser(String user, String password)
    {
        return datastore
                .createQuery(User.class)
                .field(User.NAME).equal(user)
                .field(User.PASSWORD).equal(password)
                .get();
    }

    public User createUser(String user, String password)
    {
        boolean isAlreadyExists = datastore
                .createQuery(User.class)
                .field(User.NAME).equal(user)
                .get() != null;

        if (isAlreadyExists)
        {
            return null;
        }

        User newUser = new User(user, password);

        datastore.save(newUser);

        return newUser;
    }

    public void updateInterests(User user)
    {
        datastore.update(
                user,
                datastore.createUpdateOperations(User.class)
                        .set(User.INTERESTS, user.getInterests()));
    }
    //</editor-fold>
}
