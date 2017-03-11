package com.gerasin.oleg.semanticsearch.api;

import com.gerasin.oleg.semanticsearch.DbHelper;
import com.gerasin.oleg.semanticsearch.SparqlHelper;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

    public API()
    {
        this.sparqlHelper = new SparqlHelper();
        this.dbHelper = new DbHelper();
    }

    @GET
    @Path("publications")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPublicationsByKeyword(@QueryParam("keyword") String keyword)
    {
        return sparqlHelper.getJsonOutputForKeyword(keyword);
    }

    @GET
    @Path("log")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLog(@QueryParam("keyword") String keyword)
    {
        return dbHelper.getLogsAsJson();
    }


}
