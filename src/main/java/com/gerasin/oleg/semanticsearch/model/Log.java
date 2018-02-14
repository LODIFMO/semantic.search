package com.gerasin.oleg.semanticsearch.model;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author geras
 */
@Entity(Log.ENTITY_NAME)
public class Log
{
    public static final String ENTITY_NAME = "Log";

    public static final String KEYWORD = "keyword";
    public static final String DATE = "date";
    public static final String SOURCES = "sources";
    public static final String PUBLICATIONS = "publications";

    @Id
    private ObjectId id;
    private String keyword;
    private Date date;
    private List<String> sources;
    private List<Publication> publications;

    public Log()
    {
    }

    public Log(String keyword, List<String> sources, List<Publication> publications)
    {
        this.keyword = keyword;
        this.date = new Date();
        this.sources = sources;
        this.publications = publications;
    }

    public List<Publication> getPublications()
    {
        return publications;
    }

    public void setPublications(List<Publication> publications)
    {
        this.publications = publications;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public List<String> getSources()
    {
        return sources;
    }

    public void setSources(List<String> sources)
    {
        this.sources = sources;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }
}