/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.server.router;

import frs.server.model.FaultDatabaseHandler;
import frs.server.model.SymptomDatabaseHandler;
import frs.server.model.SystemDatabaseHandler;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author abysmli
 */
@Path("/expert")
public class ExpertGUIRouter {

    private final SymptomDatabaseHandler databaseSymptom = new SymptomDatabaseHandler();
    private final FaultDatabaseHandler databaseFault = new FaultDatabaseHandler();
    private final SystemDatabaseHandler databaseSystem = new SystemDatabaseHandler();

    @Path("/getFaults")
    @GET
    @Produces("application/json;charset=utf-8")
    public Response getFaults() throws JSONException, NamingException, SQLException {
        JSONArray faults = databaseFault.getFaultKnowledge();
        return Response.status(200).entity(faults.toString()).build();
    }
}
