package com.greydev.messenger.profile;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greydev.messenger.exception.DataNotFoundException;
import com.greydev.messenger.exception.InvalidRequestDataException;
import com.greydev.messenger.post.PostResource;

@Path("/profiles")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//@Produces({ "application/json;qs=1", "application/xml;qs=.5" })	// jersey will prefer to send json
//the order is important! If no 'Accept' header is specified, jersey will send the first media type back
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class ProfileResource {

	private static final Logger LOG = LoggerFactory.getLogger(ProfileResource.class);
	private static final ProfileService profileService = new ProfileService();

	@GET
	public List<Profile> getAllProfiles() {
		LOG.info("GET: getAllProfiles");
		return profileService.getAllProfiles();
	}

	@GET
	@Path("/{profileName}")
	public Profile getProfile(@PathParam("profileName") String profileName) throws DataNotFoundException {
		LOG.info("GET: getProfile(name: {})", profileName);
		return profileService.getProfile(profileName);
	}

	@POST
	public Profile addProfile(Profile profile) throws InvalidRequestDataException {
		LOG.info("POST: addProfile(profile: {}, {})", profile.getProfileName(), profile.getFirstName());
		return profileService.addProfile(profile);
	}

	@PUT
	@Path("/{profileName}")
	public Profile updateProfile(@PathParam("profileName") String profileName, Profile profile)
			throws InvalidRequestDataException, DataNotFoundException {
		LOG.info("PUT: updateProfile(Name: {})", profileName);
		return profileService.updateProfile(profileName, profile);
	}

	@DELETE
	@Path("/{profileName}")
	public Profile deleteProfile(@PathParam("profileName") String profileName) throws DataNotFoundException {
		LOG.info("DELETE: deleteProfile(Name: {})", profileName);
		return profileService.deleteProfile(profileName);
	}

	// no parameter means: for all methods.
	@Path("/{profileName}/posts")
	public PostResource getPosts(@PathParam("profileName") String profileName) {
		return new PostResource();
	}

}
