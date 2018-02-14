package com.gerasin.oleg.semanticsearch.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gerasin.oleg.semanticsearch.DbHelper;
import com.gerasin.oleg.semanticsearch.SparqlHelper;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author geras
 */
@Path("/")
public class API
{
    private SparqlHelper sparqlHelper;
    private DbHelper dbHelper;

    @Context
    private ServletContext sc;

    @GET
    @Path("publications")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPublicationsByKeyword(@QueryParam("keyword") String keyword)
    {
        sparqlHelper = new SparqlHelper(sc);
        try
        {
            return sparqlHelper.getJsonOutputForKeyword(keyword);
        }
        catch (JsonProcessingException ex)
        {
            return ex.getMessage();
        }
    }

    @GET
    @Path("log")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLog(@QueryParam("keyword") String keyword)
    {
        dbHelper = new DbHelper();
        return dbHelper.getLogsAsJson();
    }


}
