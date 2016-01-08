/**
 * @author Li, Yuan
 * Project: PMS
 */

package com.PMS.router;

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

import com.PMS.controller.ErrorController;
import com.PMS.model.DatabaseHandler;

@Path("/")
public class PMSController {

	ErrorController errorController = new ErrorController();
	private DatabaseHandler database = new DatabaseHandler();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String welcomePMS() {
		return "<html><title>PMS Backend</title><link rel='stylesheet' type='text/css' href='http://fonts.googleapis.com/css?family=Jura'><body><p style=\"margin-top:3em; text-align:center; font-size: 4em; font-family: 'Frutiger', serif;\">Welcome to PMS System!</p><p style='text-align:center;'>Cataline Base: "+System.getProperty("catalina.base")+ "</p></body></html>";
	}
	
	@Path("/status")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStatus() throws JSONException {
		JSONObject mStatus = new JSONObject();
		mStatus.put("status", "running");
		return Response.status(200).entity(mStatus.toString()).build();
	}

	@Path("/reportError")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response reportError(@FormParam("component_id") int component_id, @FormParam("error_type") String error_type,
			@FormParam("error_desc") String error_desc) throws JSONException, SQLException, NamingException {
		JSONObject mResult = errorController.handleError(component_id, error_type, error_desc);
		return Response.status(200).entity(mResult.toString()).build();
	}

	@Path("/updateStatus")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateStatus(@FormParam("updateMeta") String updateMeta)
			throws JSONException, SQLException, NamingException {
		JSONObject mResult = new JSONObject(updateMeta);
		errorController.updateStatus(mResult);
		JSONObject mResponse = new JSONObject();
		mResponse.put("result", "success");
		return Response.status(200).entity(mResponse.toString()).build();
	}
	
	@Path("/postComponentsValue")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response postComponentsValue(@FormParam("component_data") String componentData)
			throws JSONException, SQLException, NamingException {
		JSONObject mResult = new JSONObject(componentData);
		database.updateComponentValue(mResult);
		JSONObject mResponse = new JSONObject();
		mResponse.put("result", "success");
		return Response.status(200).entity(mResponse.toString()).build();
	}
}