package com.greydev.messenger.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greydev.messenger.resource.exception.DatabaseOperationException;
import com.greydev.messenger.resource.filter.MessageFilterBean;
import com.greydev.messenger.resource.model.Message;
import com.greydev.messenger.resource.service.MessageService;

@Path("/messages")
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class MessageResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(MessageResource.class);
	private static final MessageService messageService= new MessageService();
	
	@GET
	public List<Message> getAllMessages(@BeanParam MessageFilterBean filterBean) {
		
		int year = filterBean.getYear();
		int start = filterBean.getStart();
		int size = filterBean.getSize();

		// TODO optimize filtering
		if (year != 0) {
			return messageService.getAllMessagesForYear(year);
		}
		else if (start >= 0 && size > 0) {
			return messageService.getAllMessagesPaginated(start, size);
		}
		
		LOG.info("GET: getAllMessages");
		return messageService.getAllMessages();
	}
	
	@GET
	@Path("/{messageId}")
	public Message getMessage(@PathParam("messageId") Long messageId) {
		LOG.info("GET: getMessage(id: {})", messageId);
		return messageService.getMessage(messageId);
	}
	
	@POST
	public Response addMessage(@Context UriInfo uriInfo, Message message) throws DatabaseOperationException, URISyntaxException {
		LOG.info("POST: addMessage(message: {}, {})", message.getAuthor(), message.getText());
		Message addedMessage = messageService.addMessage(message);
		String id = Long.toString(addedMessage.getId());
		URI locationUri = uriInfo.getAbsolutePathBuilder().path(id).build();
		return Response.created(locationUri).entity(addedMessage).build();
	}
	
	@PUT
	@Path("/{messageId}")
	public Message updateMessage(@PathParam("messageId") Long messageId, Message message) throws DatabaseOperationException {
		LOG.info("PUT: updateMessage(id: {})", messageId);
		return messageService.updateMessage(messageId, message);
	}
	
	@DELETE
	@Path("/{messageId}")
	public Message deleteMessage(@PathParam("messageId") Long messageId) throws DatabaseOperationException {
		LOG.info("DELETE: deleteMessage(id: {})", messageId);
		return messageService.deleteMessage(messageId);
	}
	
	// not giving a method parameter means: for all methods.
	@Path("{messageId}/comments")
	public CommentResource getCommentResource() {
		LOG.info("getAllComments");
		return new CommentResource();
	}
}
