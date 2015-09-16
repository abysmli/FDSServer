/**
 * @author Li, Yuan
 * Project: PMS
 */

package com.PMS.router;

import java.sql.SQLException;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.PMS.controller.UserController;

@Path("/user")
public class PMSUserController {

	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@FormParam("username") String username, @FormParam("password") String password) {
		UserController userController = new UserController();
		JSONObject mResult = new JSONObject();
		try {
			mResult = userController.getUser(username, password);
			mResult.remove("password");
		} catch (NamingException | SQLException | JSONException e) {
			mResult.put("status", "fail");
		}
		return Response.status(200).entity(mResult.toString()).build();
	}
}