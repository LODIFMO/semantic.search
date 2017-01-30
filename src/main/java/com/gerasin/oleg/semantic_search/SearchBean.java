package com.gerasin.oleg.semantic_search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;

/**
 *
 * @author geras
 */
@ManagedBean
@SessionScoped
public class SearchBean
        implements Serializable
{

    public static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(SearchBean.class.getName());

    private String keyword;

    private List<Publication> publications = new ArrayList<>();

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
        if (keyword.isEmpty())
        {
            output = "Your query is empty";
            return output;
        }
        execSelect();
        log.info("SearchBean: getOutput");
        return output;
    }

    private void execSelect()
    {
        execSelectToOU();
        execSelectToEurope();
    }

//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private void execSelectToEurope()
    {
        Query query = QueryFactory.create(getStringForEurope(),
                Syntax.syntaxARQ);
        query.setLimit(10);
        QueryExecution qe = QueryExecutionFactory.sparqlService("http://publications.europa.eu/webapi/rdf/sparql",
                query);
        ResultSet results = qe.execSelect();
//        ResultSetFormatter.outputAsJSON(outputStream, results);
//        output = outputStream.toString();

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

    private void execSelectToOU()
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

    private String getStringForEurope()
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

    private String getStringForOU()
    {
        return "PREFIX bibo: <http://purl.org/ontology/bibo/>  "
                + "PREFIX foaf: <http://xmlns.com/foaf/0.1/>  "
                + "PREFIX dcterms: <http://purl.org/dc/terms/>  "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + ""
                + "SELECT "
                + "distinct (group_concat(distinct ?URI_) as ?URI) ?title ?authors ?date ?abstract "
                + "FROM <http://data.open.ac.uk/context/oro> "
                + "WHERE { "
                + "      [] a bibo:Article ;"
                + "       bibo:uri ?URI_ ;"
                + "       bibo:authorList ?authors ;"
                + "       bibo:abstract ?abstract ;"
                + "       dcterms:title ?title ;"
                + "       dcterms:date ?date ."
                + "FILTER (regex( str(?title), \""
                + keyword
                + "\", \"i\")). "
                + " "
                + "} "
                + "GROUP BY ?title ?authors ?date ?abstract "
                + "LIMIT 10";
    }

}
