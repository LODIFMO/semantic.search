package com.gerasin.oleg.semanticsearch;

import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import model.Publication;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.Syntax;

/**
 *
 *
 * @author geras
 */
public class SparqlHelper
{
    private static final String OU_FILENAME = "ou.ftl";
    private static final String AALTO_FILENAME = "aalto.ftl";
    private static final String EUROPE_FILENAME = "europe.ftl";

    private final Configuration templateConfig;
    private final List<Publication> publications;
    private String keyword;
    private Map<String, Object> data;

    public SparqlHelper()
    {
        publications = new ArrayList<>();

        templateConfig = new Configuration(Configuration.VERSION_2_3_25);
        templateConfig.setDefaultEncoding("UTF-8");
        templateConfig.setTemplateLoader(
                    new WebappTemplateLoader(
                            (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext(),
                            "/WEB-INF/query/") );
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
        data = new HashMap<>();
        data.put("keyword", keyword);
    }

    public List<Publication> execSelect(String keyword)
    {
        setKeyword(keyword);
        execSelectToOu();
        //execSelectToEurope();
        //execSelectToAalto();
        return publications;
    }

    public void execSelectToEurope()
    {
        Query query = QueryFactory.create(getStringForEurope(),
                Syntax.syntaxARQ);
        query.setLimit(10);
        QueryExecution qe = QueryExecutionFactory.sparqlService("http://publications.europa.eu/webapi/rdf/sparql",
                query);
        ResultSet results = qe.execSelect();

        while (results.hasNext())
        {
            QuerySolution qs = results.next();
            String source = "Europe";
            String uri = qs.get("URI").toString();
            String title = qs.get("title").toString();
            String authors = qs.get("authors").toString();
            String date = qs.get("date").toString();
            date = date.replace("^^http://www.w3.org/2001/XMLSchema#date", "");
            publications.add(new Publication(source, uri, title, authors, date, null));
        }
    }

    public void execSelectToAalto()
    {
        Query query = QueryFactory.create(getStringForAalto(),
                Syntax.syntaxARQ);
        query.setLimit(10);
        QueryExecution qe = QueryExecutionFactory.sparqlService("http://data.aalto.fi/endpoint?force=true",
                query);
        ResultSet results = qe.execSelect();

        while (results.hasNext())
        {
            QuerySolution qs = results.next();
            if (!qs.contains("title"))
            {
                return;
            }
            String source = "Aalto";
             String title = qs.get("title").toString();
             String authors = qs.get("authors").toString();
             String date = qs.get("date").toString();
             publications.add(new Publication(source, null, title, authors, date, null));
         }
     }

    public String getJsonOutputForKeyword(String keyword)
    {
        this.keyword = keyword;
        Query query = QueryFactory.create(getStringForOu(),
                Syntax.syntaxARQ);
        query.setLimit(10);
        QueryExecution qe = QueryExecutionFactory.sparqlService("http://data.open.ac.uk/sparql?force=true",
                query);
        ResultSet results = qe.execSelect();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(outputStream, results);
        return outputStream.toString();
    }

    public void execSelectToOu()
    {
        Query query = QueryFactory.create(getStringForOu(),
                Syntax.syntaxARQ);
        query.setLimit(10);
        QueryExecution qe = QueryExecutionFactory.sparqlService("http://data.open.ac.uk/sparql?force=true",
                query);
        ResultSet results = qe.execSelect();

        while (results.hasNext())
        {
            QuerySolution qs = results.next();
            if ( !qs.contains("URI") )
            {
                return;
            }
            String source = "Open University";
            String uri = qs.get("URI").toString();
            String title = qs.get("title").toString();
            String authors = qs.get("authors").toString();
            String date = qs.get("date").toString();
            date = date.replace("^^http://www.w3.org/2001/XMLSchema#date", "");
            String description = qs.get("abstract").toString();
            publications.add(new Publication(source, uri, title, authors, date, description));
        }
    }

    public String getStringForEurope()
    {
        return getStringFromTemplate(EUROPE_FILENAME);
    }

    public String getStringForOu()
    {
        return getStringFromTemplate(OU_FILENAME);

    }

    public String getStringForAalto()
    {
        return getStringFromTemplate(AALTO_FILENAME);
    }

    public String getStringFromTemplate(String templateName)
    {
        try
        {
            Template temp = templateConfig.getTemplate(templateName);
            Writer out = new StringWriter();
            temp.process(data, out);
            return out.toString();
        }
        catch (IOException | TemplateException ex)
        {
            Logger.getLogger(SparqlHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }
}
