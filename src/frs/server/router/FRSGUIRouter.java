/**
 * @author Li, Yuan
 * Project: FDS
 */

package frs.server.router;

import frs.server.model.FaultDatabaseHandler;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import frs.server.model.SymptomDatabaseHandler;
import frs.server.model.SystemDatabaseHandler;

@Path("/client")
public class FRSGUIRouter {
	
	private final SymptomDatabaseHandler databaseSymptom = new SymptomDatabaseHandler();
	private final FaultDatabaseHandler databaseFault = new FaultDatabaseHandler();
        private final SystemDatabaseHandler databaseSystem = new SystemDatabaseHandler();
	
	@Path("/resetDatabase")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response resetDatabase() throws JSONException, NamingException, SQLException {
		JSONObject result = databaseSystem.resetDatabase();
		return Response.status(200).entity(result.toString()).build();
	}
	
	@Path("/getFaults")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getFaults() throws JSONException, NamingException, SQLException {
		JSONArray faults = databaseFault.getFaultKnowledge();
		return Response.status(200).entity(faults.toString()).build();
	}
	
	@Path("/getSymptoms")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getSymptoms() throws JSONException, NamingException, SQLException {
		JSONArray symptoms = databaseSymptom.getSymptomSubsystem();
		return Response.status(200).entity(symptoms.toString()).build();
	}
	
	@Path("/getComponents")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getComponents() throws JSONException, NamingException, SQLException {
		JSONArray components = databaseSystem.getComponents();
		return Response.status(200).entity(components.toString()).build();
	}
	
		@Path("/getSubsystems")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getSubsystems() throws JSONException, NamingException, SQLException {
		JSONArray subsystems = databaseSystem.getSubsystems();
		return Response.status(200).entity(subsystems.toString()).build();
	}
	
	@Path("/getFunctions")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getFunctions() throws JSONException, NamingException, SQLException {
		JSONArray functions = databaseSystem.getFunctions();
		return Response.status(200).entity(functions.toString()).build();
	}
	
	@Path("/getSubfunctions")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getSubfunctions() throws JSONException, NamingException, SQLException {
		JSONArray subfunctions = databaseSystem.getSubfunctions();
		return Response.status(200).entity(subfunctions.toString()).build();
	}
	
	@Path("/getMainfunctions")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getMainfunctions() throws JSONException, NamingException, SQLException {
		JSONArray mainfunctions = databaseSystem.getMainfunctions();
		return Response.status(200).entity(mainfunctions.toString()).build();
	}
	
	@Path("/getSubsystemComponentRel")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getSubsystemComponentRel() throws JSONException, NamingException, SQLException {
		JSONArray subsystemcomponentrel = databaseSystem.getSubsystemComponentRel();
		return Response.status(200).entity(subsystemcomponentrel.toString()).build();
	}
	
	@Path("/getComponentFunctionRel")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getComponentFunctionRel() throws JSONException, NamingException, SQLException {
		JSONArray componentfunctionrel = databaseSystem.getComponentFunctionRel();
		return Response.status(200).entity(componentfunctionrel.toString()).build();
	}
	
	@Path("/getSubfunctionFunctionRel")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getSubfunctionFunctionRel() throws JSONException, NamingException, SQLException {
		JSONArray subfunctionfunctionrel = databaseSystem.getSubfunctionFunctionRel();
		return Response.status(200).entity(subfunctionfunctionrel.toString()).build();
	}
	
	@Path("/getMainfunctionSubfunctionRel")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getMainfunctionSubfunctionRel() throws JSONException, NamingException, SQLException {
		JSONArray mainfunctionsubfunctionrel = databaseSystem.getMainfunctionSubfunctionRel();
		return Response.status(200).entity(mainfunctionsubfunctionrel.toString()).build();
	}

	@Path("/getAnalysisProcedure")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getAnalysisProcedure() throws JSONException, NamingException, SQLException {
		JSONArray faultprocedures = databaseSystem.getAnalysisProcedure();
		return Response.status(200).entity(faultprocedures.toString()).build();
	}
        
        @Path("/getLastRuntimeData")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getLastRuntimeData() throws JSONException, NamingException, SQLException {
		JSONObject lastComponentValue = databaseSystem.getLastRuntimeData();
		return Response.status(200).entity(lastComponentValue.toString()).build();
	}
	
	@Path("/getRuntimeData")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getRuntimeData() throws JSONException, NamingException, SQLException {
		JSONArray componentValue = databaseSystem.getRuntimeData();
		return Response.status(200).entity(componentValue.toString()).build();
	}
	
}
