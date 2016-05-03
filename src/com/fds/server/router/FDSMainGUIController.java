/**
 * @author Li, Yuan
 * Project: FDS
 */

package com.fds.server.router;

import java.sql.SQLException;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fds.server.model.DatabaseHandler;

@Path("/client")
public class FDSMainGUIController {
	
	private final DatabaseHandler database = new DatabaseHandler();
	
	@Path("/resetDatabase")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response resetDatabase() throws JSONException, NamingException, SQLException {
		JSONObject result = database.resetDatabase();
		return Response.status(200).entity(result.toString()).build();
	}
	
	@Path("/getFaults")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getFaults() throws JSONException, NamingException, SQLException {
		JSONArray faults = database.getFaults();
		return Response.status(200).entity(faults.toString()).build();
	}
	
	@Path("/getSymptoms")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getSymptoms() throws JSONException, NamingException, SQLException {
		JSONArray symptoms = database.getSymptoms();
		return Response.status(200).entity(symptoms.toString()).build();
	}
	
	@Path("/getComponents")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getComponents() throws JSONException, NamingException, SQLException {
		JSONArray components = database.getComponents();
		return Response.status(200).entity(components.toString()).build();
	}
	
	@Path("/getLastComponentValue")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getLastComponentValue() throws JSONException, NamingException, SQLException {
		JSONObject lastComponentValue = database.getLastComponentValue();
		return Response.status(200).entity(lastComponentValue.toString()).build();
	}
	
	@Path("/getComponentValue")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getComponentValue() throws JSONException, NamingException, SQLException {
		JSONArray componentValue = database.getComponentValue();
		return Response.status(200).entity(componentValue.toString()).build();
	}
	
	@Path("/getSubsystems")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getSubsystems() throws JSONException, NamingException, SQLException {
		JSONArray subsystems = database.getSubsystems();
		return Response.status(200).entity(subsystems.toString()).build();
	}
	
	@Path("/getFunctions")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getFunctions() throws JSONException, NamingException, SQLException {
		JSONArray functions = database.getFunctions();
		return Response.status(200).entity(functions.toString()).build();
	}
	
	@Path("/getSubfunctions")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getSubfunctions() throws JSONException, NamingException, SQLException {
		JSONArray subfunctions = database.getSubfunctions();
		return Response.status(200).entity(subfunctions.toString()).build();
	}
	
	@Path("/getMainfunctions")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getMainfunctions() throws JSONException, NamingException, SQLException {
		JSONArray mainfunctions = database.getMainfunctions();
		return Response.status(200).entity(mainfunctions.toString()).build();
	}
	
	@Path("/getSubsystemComponentRel")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getSubsystemComponentRel() throws JSONException, NamingException, SQLException {
		JSONArray subsystemcomponentrel = database.getSubsystemComponentRel();
		return Response.status(200).entity(subsystemcomponentrel.toString()).build();
	}
	
	@Path("/getComponentFunctionRel")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getComponentFunctionRel() throws JSONException, NamingException, SQLException {
		JSONArray componentfunctionrel = database.getComponentFunctionRel();
		return Response.status(200).entity(componentfunctionrel.toString()).build();
	}
	
	@Path("/getSubfunctionFunctionRel")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getSubfunctionFunctionRel() throws JSONException, NamingException, SQLException {
		JSONArray subfunctionfunctionrel = database.getSubfunctionFunctionRel();
		return Response.status(200).entity(subfunctionfunctionrel.toString()).build();
	}
	
	@Path("/getMainfunctionSubfunctionRel")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getMainfunctionSubfunctionRel() throws JSONException, NamingException, SQLException {
		JSONArray mainfunctionsubfunctionrel = database.getMainfunctionSubfunctionRel();
		return Response.status(200).entity(mainfunctionsubfunctionrel.toString()).build();
	}

	@Path("/getFaultProcedureInfos")
	@GET
	@Produces("application/json;charset=utf-8")
	public Response getFaultProcedureInfos() throws JSONException, NamingException, SQLException {
		JSONArray faultprocedures = database.getFaultProcedureInfos();
		return Response.status(200).entity(faultprocedures.toString()).build();
	}
	
}
