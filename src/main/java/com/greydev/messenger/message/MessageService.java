package com.greydev.messenger.message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greydev.messenger.database.DatabaseMock;
import com.greydev.messenger.exception.DataNotFoundException;
import com.greydev.messenger.exception.ErrorMessage;

public class MessageService {

	private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);
	private static long idCount = 0;

	// saving some dummy messages to the database
	static {
		Message message1 = new Message(getNextId(), "Can", "Such a lovely weather today!",
				new GregorianCalendar(2015, 11, 11));
		Message message2 = new Message(getNextId(), "Jason", "I own a grocery store!");

		DatabaseMock.addMessage(message1.getId(), message1);
		DatabaseMock.addMessage(message2.getId(), message2);
	}

	public List<Message> getAllMessages() {
		return DatabaseMock.getAllMessagesAsList();
	}

	public Message getMessage(Long id) throws DataNotFoundException, UnknownHostException {
		Message message = DatabaseMock.getMessage(id);

		if (message == null) {
			throw new DataNotFoundException("GET", InetAddress.getLocalHost().getHostName() + ":8080/messenger/webapi/messages/" + id);
		}
		LOG.info(" -> returning: {}, {}", message.getAuthor(), message.getText());
		return message;
	}

	public Message addMessage(Message message) {
		Message newMessage = new Message(getNextId(), message.getAuthor(), message.getText());


		if (isMessageValid(newMessage)) {
			DatabaseMock.addMessage(newMessage.getId(), newMessage);
			return newMessage;
		}
		// TODO if the message is not valid, return a proper exception message
		return new Message(111L, "ERROR", "Invalid message, some properties are missing");
	}

	public Message updateMessage(Long queryParamMessageId, Message message) {
		message.setId(queryParamMessageId);
		// ignore the id inside the received message, use the url param id
		// TODO proper exception
		if (isMessageValid(message)) {
			if (doesMessageExist(message.getId())) {
				return DatabaseMock.updateMessage(message);
			} else {
				DatabaseMock.addMessage(message.getId(), message);
				return message;
			}
		}
		return new Message(111L, "ERROR", "Invalid message, some properties are missing");
	}

	public Message deleteMessage(Long id) throws DataNotFoundException {
		Message response = DatabaseMock.deleteMessage(id);
		// Send back a message 'message with requested id is not found'
		if (response == null) {
			throw new DataNotFoundException();
		}
		return response;
	}

	public List<Message> getAllMessagesForYear(int year) {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);

		if (year > currentYear) {
			LOG.info("passed year parameter is not valid");
			return null;
		}
		return DatabaseMock.getAllMessagesForYear(year);
	}

	public List<Message> getAllMessagesPaginated(int start, int size) {
		List<Message> messageList = DatabaseMock.getAllMessagesAsList();

		if ((start + size) >= messageList.size()) {
			return messageList.subList(start, messageList.size());
		}
		return messageList.subList(start, start + size);
	}

	// mandatory properties: Author, Text
	public boolean isMessageValid(Message message) {
		return StringUtils.isNoneBlank(message.getAuthor(), message.getText());
	}

	public boolean doesMessageExist(Long id) {
		return (DatabaseMock.getMessage(id) != null);
	}

	private static long getNextId() {
		return idCount++;
	}

}