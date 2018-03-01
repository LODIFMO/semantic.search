package com.gerasin.oleg.semanticsearch.model;

import java.io.Serializable;

/**
 *
 * @author geras
 */
public class Publication
        implements Serializable
{
    private String Source;
    private String Uri;
    private String Title;
    private String Authors;
    private String Date;
    private String Abstract;

    public Publication()
    {
    }

    public Publication(String Source, String URI, String Title, String Authors, String Date, String Abstract)
    {
        this.Source = Source;
        this.Uri = URI;
        this.Title = Title;
        this.Authors = Authors;
        this.Date = Date;
        this.Abstract = Abstract;
    }

    public String getSource()
    {
        return Source;
    }

    public void setSource(String Source)
    {
        this.Source = Source;
    }

    public String getUri()
    {
        return Uri;
    }

    public void setUri(String Uri)
    {
        this.Uri = Uri;
    }

    public String getTitle()
    {
        return Title;
    }

    public void setTitle(String Title)
    {
        this.Title = Title;
    }

    public String getDate()
    {
        return Date;
    }

    public void setDate(String Date)
    {
        this.Date = Date;
    }

    public String getAuthors()
    {
        return Authors;
    }

    public void setAuthors(String Authors)
    {
        this.Authors = Authors;
    }

    public String getAbstract()
    {
        return Abstract;
    }

    public void setAbstract(String Abstract)
    {
        this.Abstract = Abstract;
    }
}
