/**
 * @author Li, Yuan
 * Project: FDS
 */

package frs.server.router;

import java.sql.SQLException;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import frs.server.controller.FaultController;
import frs.server.model.FaultDatabaseHandler;
import frs.server.model.SymptomDatabaseHandler;
import frs.server.model.SystemDatabaseHandler;

@Path("/")
public class FDSSimulatorRouter {

	FaultController faultController = new FaultController();
	private final SymptomDatabaseHandler databaseSymptom = new SymptomDatabaseHandler();
	private final FaultDatabaseHandler databaseFault = new FaultDatabaseHandler();
        private final SystemDatabaseHandler databaseSystem = new SystemDatabaseHandler();
        
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String welcomeFDS() {
		return "<html><title>FRS Server Backend</title><link rel='stylesheet' type='text/css' href='http://fonts.googleapis.com/css?family=Jura'><body><p style=\"margin-top:3em; text-align:center; font-size: 4em; font-family: 'Frutiger', serif;\">Welcome to FRS Server System!</p><p style='text-align:center;'>Cataline Base: "+System.getProperty("catalina.base")+ "</p></body></html>";
	}
	
	@Path("/status")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStatus() throws JSONException {
		JSONObject mStatus = new JSONObject();
		mStatus.put("status", "running");
		return Response.status(200).entity(mStatus.toString()).build();
	}
        
	@Path("/reportFault")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response reportFault(@FormParam("fault_data") String faultdata) throws JSONException, SQLException, NamingException {
		JSONObject mFaultData = new JSONObject(faultdata);
		JSONObject mResult = new JSONObject();
                mResult = faultController.handleFault(mFaultData);
		return Response.status(200).entity(mResult.toString()).build();
	}

	@Path("/updateStatus")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateStatus(@FormParam("updateMeta") String updateMeta)
			throws JSONException, SQLException, NamingException {
		JSONObject mResult = new JSONObject(updateMeta);
		faultController.updateStatus(mResult);
		JSONObject mResponse = new JSONObject();
		mResponse.put("result", "success");
		return Response.status(200).entity(mResponse.toString()).build();
	}
	
	@Path("/postRuntimeData")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response postRuntimeData(@FormParam("component_data") String componentData)
			throws JSONException, SQLException, NamingException {
		JSONObject mResult = new JSONObject(componentData);
		databaseSystem.updateRuntimeData(mResult);
		JSONObject mResponse = new JSONObject();
		mResponse.put("result", "success");
		return Response.status(200).entity(mResponse.toString()).build();
	}
}