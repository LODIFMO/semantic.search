package com.gerasin.oleg.semanticsearch;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author geras
 */
@ManagedBean
@SessionScoped
public class IndexBean
        implements Serializable
{
    public static final long serialVersionUID = 1L;

    private String keyword;
    private Boolean cachedSearch = true;

    private static Logger log = Logger.getLogger(IndexBean.class.getName());

    public String getKeyword()
    {
        log.info("IndexBean: getKeyword with value: " + keyword);
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
        log.info("IndexBean: setKeyword with value: " + keyword);
    }

    public Boolean getCachedSearch()
    {
        return cachedSearch;
    }

    public void setCachedSearch(Boolean cachedSearch)
    {
        this.cachedSearch = cachedSearch;
    }
}
