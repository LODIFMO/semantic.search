package com.gerasin.oleg.semanticsearch.api;

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
@Path("publications")
public class API
{
    private SparqlHelper sparqlHelper;

    public API()
    {
        this.sparqlHelper = new SparqlHelper();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBy(@QueryParam("keyword") String keyword)
    {
        return sparqlHelper.getJsonOutputForKeyword(keyword);
    }
}
