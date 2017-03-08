package model;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author geras
 */
@Entity("Log")
public class Log
{
    @Id
    private ObjectId id;
    private String keyword;
    private Date date;
    private List<Publication> publications;

    public Log()
    {
    }

    public Log(String keyword, List<Publication> publications)
    {
        this.keyword = keyword;
        this.date = new Date();
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

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }
}