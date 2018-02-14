package com.gerasin.oleg.semanticsearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author geras
 */
@Named
@ViewScoped
public class IndexBean
        implements Serializable
{
    private String keyword;
    private String[] selectedSources;
    private final List<String> sources = new ArrayList<>();
    private Boolean cachedSearch = true;

    private static Logger log = LogManager.getLogger(IndexBean.class.getName());

    @PostConstruct
    private void init()
    {
        sources.add(SparqlHelper.OU);
        sources.add(SparqlHelper.AALTO);
        sources.add(SparqlHelper.EUROPE);
        sources.add(SparqlHelper.SPRINGER);
    }

    public String getKeyword()
    {
        log.info("getKeyword with value: {}", keyword);
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
        log.info("setKeyword with value: {}", keyword);
    }

    public Boolean getCachedSearch()
    {
        return cachedSearch;
    }

    public void setCachedSearch(Boolean cachedSearch)
    {
        this.cachedSearch = cachedSearch;
    }

    public void setSelectedSources(String[] selectedSources)
    {
        this.selectedSources = selectedSources;
    }

    public String[] getSelectedSources()
    {
        return selectedSources;
    }

    public String getSelectedSourcesString()
    {
        if (selectedSources != null)
        {
            return String.join(",", selectedSources);
        }
        else
        {
            return "";
        }
    }

    public List<String> getSources()
    {
        return sources;
    }
}
