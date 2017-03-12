package com.gerasin.oleg.semanticsearch;

import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
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
    private List<Publication> publications = new ArrayList<>();
    private String keyword;

    public List<Publication> execSelect(String keyword)
    {
        this.keyword = keyword;
        execSelectToOu();
        //execSelectToEurope();
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

    public String getJsonOutputForKeyword(String keyword)
    {
        this.keyword = keyword;
        Query query = QueryFactory.create(getStringForOU(),
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
        Query query = QueryFactory.create(getStringForOU(),
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
        return "PREFIX cdm:<http://publications.europa.eu/ontology/cdm#> "
                + "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
                + "PREFIX dc:<http://purl.org/dc/elements/1.1/> "
                + "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#> "
                + "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                + " "
                + "SELECT DISTINCT  (group_concat(distinct ?work;separator=\",\") as ?URI)  "
                + "   (group_concat(distinct ?title_;separator=\",\") as ?title)  "
                + "  (group_concat(distinct ?agentName;separator=\",\") as ?authors)  "
                + "  ?date "
                + " "
                + "WHERE  "
                + "{ "
                + "?work cdm:work_date_document ?d. "
                + "?exp cdm:expression_belongs_to_work ?work . "
                + "?exp cdm:expression_title ?title_ . "
                + "OPTIONAL {?work cdm:work_created_by_agent/skos:prefLabel ?agentName . "
                + "filter (lang(?agentName)=\"en\")}. "
                + "?work cdm:work_date_document ?date . "
                + " ?exp cdm:expression_uses_language <http://publications.europa.eu/resource/authority/language/ENG>. "
                + "FILTER (regex( str(?title_), \""
                + keyword
                + "\", \"i\")). "
                + "} "
                + "GROUP BY ?work ?date";
    }

    public String getStringForOU()
    {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);
         try
        {
            cfg.setTemplateLoader(
                    new WebappTemplateLoader(
                            (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext(),
                            "/WEB-INF/query/") );

            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
            cfg.setLogTemplateExceptions(false);

            Template temp = cfg.getTemplate("ou.ftl");
            Map<String, Object> data = new HashMap<>();
            data.put("keyword", keyword);
            Writer out = new StringWriter();
            temp.process(data, out);
            Logger.getLogger(SparqlHelper.class.getName()).log(Level.INFO, "return {0}", out.toString());
            return out.toString();
        }
        catch (IOException | TemplateException ex)
        {
            Logger.getLogger(SparqlHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        Logger.getLogger(SparqlHelper.class.getName()).info("return empty");
        return "";
    }
}
