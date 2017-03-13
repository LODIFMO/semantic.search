package com.gerasin.oleg.semanticsearch;

import model.Publication;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author geras
 */
@ManagedBean
@ViewScoped
public class SearchBean
        implements Serializable
{

    public static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(SearchBean.class.getName());
    private final SparqlHelper sparqlHelper = new SparqlHelper();
    private final DbHelper dbHelper = new DbHelper();

    private String keyword;
    private Boolean cachedSearch = true;

    private List<Publication> publications;
    private Publication selected;

    public Publication getSelected() {
        return selected;
    }
     public void setSelected(Publication selected) {
        this.selected = selected;
    }

    @PostConstruct
    protected void init()
    {
         publications = new ArrayList<>();
    }
    public String getKeyword()
    {
        log.info("SearchBean: getKeyword with value: " + keyword);
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
        log.info("SearchBean: setKeyword with value: " + keyword);
    }

    public Boolean getCachedSearch()
    {
        return cachedSearch;
    }

    public void setCachedSearch(Boolean cachedSearch)
    {
        this.cachedSearch = cachedSearch;
    }

    public List<Publication> getPublications()
    {
        return publications;
    }

    private String output = "";

    public String getOutput()
    {
        log.info("SearchBean: getOutput");
        if (keyword.isEmpty())
        {
            output = "Your query is empty";
            return output;
        }
        else
        {
            searchPublications();
            output ="There are " + publications.size() + " publications for your query:";
        }
        return output;
    }

    private void searchPublications()
    {
        List<Publication> cachedPublications = dbHelper.getCachedPublications(keyword);
        if (!cachedSearch || cachedPublications == null)
        {
            log.info("SearchBean: cachedPublications == null ");
            publications = sparqlHelper.execSelect(keyword);
            dbHelper.createLog(keyword, publications);
        }
        else
        {
            log.log(Level.INFO, "SearchBean: cachedPublications == {0}", cachedPublications);
            publications = cachedPublications;
        }
    }

}
