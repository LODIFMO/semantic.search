package com.gerasin.oleg.semanticsearch.beans;

import com.gerasin.oleg.semanticsearch.helpers.DbHelper;
import com.gerasin.oleg.semanticsearch.helpers.SparqlHelper;
import com.gerasin.oleg.semanticsearch.model.Publication;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author geras
 */
@Named
@ViewScoped
public class SearchBean
        implements Serializable
{
    private static Logger log = LogManager.getLogger(SearchBean.class.getName());

    @Inject
    private SparqlHelper sparqlHelper;
    @Inject
    private DbHelper dbHelper;
    @Inject
    private LoginBean loginBean;

    private String keyword;
    private Boolean cachedSearch = true;
    private String[] selectedSources;

    private List<Publication> publications;
    private List<Publication> interestingPublications;
    private List<Publication> otherPublications;
    private Publication selected;
    List<String> interests;

    private String output;
    private String goodOutput;
    private String otherOutput;

    @PostConstruct
    protected void init()
    {
         publications = new ArrayList<>();

         Map<String, String> parameterMap =
                 FacesContext.getCurrentInstance()
                         .getExternalContext()
                         .getRequestParameterMap();

         setKeyword(parameterMap.get("keyword"));
         setCachedSearch(Boolean.parseBoolean(parameterMap.get("cachedSearch")));
         String sources = parameterMap.get("selectedSources");
         if (sources != null && !sources.isEmpty())
            setSelectedSources(parameterMap.get("selectedSources").split(","));

         if (!validateKeyword())
         {
             return;
         }

         interests = loginBean.getUser().getInterests();

         validateInterests();
         searchPublications();
    }

    private boolean validateKeyword()
    {
        if (StringUtils.isEmpty(keyword))
        {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Your query is empty",
                            ""));
            return false;
        }
        return true;
    }

    private boolean validateInterests()
    {
        if (interests == null || interests.isEmpty())
        {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Please, fill in your interests to search relevant publications",
                            ""));
            return false;
        }
        return true;
    }

    private void searchPublications()
    {
        List<String> selectedSourcesList = Arrays.asList(selectedSources);
        List<Publication> cachedPublications = null;
        if (cachedSearch)
        {
            cachedPublications = dbHelper.getCachedPublications(keyword, selectedSourcesList);
        }
        if (cachedPublications == null)
        {
            log.info("cachedPublications == null ");
            publications = sparqlHelper.execSelect(keyword, selectedSourcesList);
            dbHelper.createLog(keyword, selectedSourcesList, publications);
        }
        else
        {
            log.info("size of cachedPublications == {}", cachedPublications.size());
            publications = cachedPublications.stream()
                    .filter(p -> selectedSourcesList.contains(p.getSource()))
                    .collect(Collectors.toList());
        }

        if (interests != null && !interests.isEmpty())
        {
            createPatternForInterests();

            Map<Boolean, List<Publication>> partitionMap
                    = publications
                            .stream()
                            .collect(Collectors.partitioningBy(this::isPublicationInteresting));

            interestingPublications = partitionMap.get(true);
            otherPublications = partitionMap.get(false);
        }
    }

    private void createPatternForInterests()
    {
        String regex = interests.stream().collect(Collectors.joining("|"));
        patternForInterests = Pattern.compile(regex);
    }

    Pattern patternForInterests;

    private boolean isPublicationInteresting(Publication publication)
    {
        return patternForInterests
                .matcher(publication.getAbstract() + publication.getTitle())
                .find();
    }

    //<editor-fold defaultstate="collapsed" desc="getters/setters">

    public String getOutput()
    {
        log.info("getOutput");
        output ="There are " + publications.size() + " publications for your query:";
        return output;
    }

    public String getGoodOutput()
    {
        log.info("getGoodOutput");
        goodOutput ="There are " + interestingPublications.size() + " interesting publications for your query:";
        return goodOutput;
    }

    public String getOtherOutput()
    {
        log.info("getOtherOutput");
        otherOutput ="There are " + otherPublications.size() + " other publications for your query:";
        return otherOutput;
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

    public String[] getSelectedSources()
    {
        return selectedSources;
    }

    public void setSelectedSources(String[] selectedSources)
    {
        log.info("selectedSources = {}", Arrays.toString(selectedSources));
        this.selectedSources = selectedSources;
    }

    public List<Publication> getPublications()
    {
        return publications;
    }

    public List<Publication> getInterestingPublications()
    {
        return interestingPublications;
    }

    public List<Publication> getOtherPublications()
    {
        return otherPublications;
    }

    public Publication getSelected() {
        return selected;
    }
     public void setSelected(Publication selected) {
        this.selected = selected;
    }

    public List<String> getInterests()
    {
        return interests;
    }
//</editor-fold>
}
