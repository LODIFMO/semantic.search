package com.gerasin.oleg.semanticsearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    private SparqlHelper sparqlHelper = new SparqlHelper();

    private String keyword;

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
            publications = sparqlHelper.execSelect(keyword);
            output ="There are " + publications.size() + " publications for your query:";
        }
        return output;
    }
}
