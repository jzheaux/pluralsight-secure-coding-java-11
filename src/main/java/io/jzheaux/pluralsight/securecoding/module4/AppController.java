package io.jzheaux.pluralsight.securecoding.module4;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

@Controller("module4AppController")
public class AppController {
	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	DataSource dataSource;

	@GetMapping("/messages")
	String messages(@RequestParam("forumId") String id, Model model) {
		String query = "SELECT text FROM messages WHERE forum_id = '" + id + "'";
		try (Connection connection = this.dataSource.getConnection();
			Statement statement = connection.createStatement()) {
			Collection<String> messages = toMessages(() -> statement.executeQuery(query));
			model.addAttribute("messages", messages);
			model.addAttribute("forumId", id);
			return "messages";
		} catch (SQLException ex) {
			throw new IllegalArgumentException("Invalid forum id", ex);
		}
	}

	@PostMapping("/message")
	String message(@RequestParam("forumId") Long id, @RequestParam("text") String text) {
		insertMessage(new Message(id, text));
		return "redirect:/messages?forumId=" + id;
	}

	@GetMapping("/message/{id}")
	@ResponseBody String message(@PathVariable("id") Long id) {
		String query = "SELECT text FROM messages WHERE id = ?";
		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, id);
			Collection<String> messages = toMessages(() -> statement.executeQuery());
			if (messages.isEmpty()) {
				return null;
			}
			return HtmlUtils.htmlEscape(messages.iterator().next());
		} catch (SQLException ex) {
			throw new IllegalArgumentException("Invalid forum id", ex);
		}
	}

	@PostMapping(value="/message", consumes="application/xml")
	@ResponseBody
	String message(@RequestBody String xml) {
		try {
			DocumentBuilder builder = documentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
			NodeList list = doc.getDocumentElement().getChildNodes();
			Long id = Long.parseLong(list.item(0).getTextContent());
			String text = list.item(1).getTextContent();
			Message message = new Message(id, text);
			insertMessage(message);
			return "OK";
		} catch (Exception ex) {
			throw new IllegalArgumentException("invalid xml", ex);
		}
	}

	private DocumentBuilder documentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setNamespaceAware(true);
		factory.setAttribute(
				"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");
		factory.setAttribute(
				"http://java.sun.com/xml/jaxp/properties/schemaSource",
				"message.xsd");
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new SimpleSaxErrorHandler(logger));
		return builder;
	}

	private boolean insertMessage(Message message) {
		String query = "INSERT INTO messages (forum_id, text) VALUES (?, ?)";
		try (Connection connection = this.dataSource.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setLong(1, message.getForumId());
			statement.setString(2, message.getText().replaceAll("[#\r\n\t]", " "));
			return statement.execute();
		} catch (SQLException ex) {
			throw new IllegalArgumentException("Bad request", ex);
		}
	}

	private Collection<String> toMessages(ResultSetSupplier supplier) throws SQLException {
		Collection<String> messages = new ArrayList<>();
		try (ResultSet resultSet = supplier.get()) {
			while (resultSet.next()) {
				String text = resultSet.getString("text");
				messages.add(text);
			}
		}
		return messages;
	}

	private interface ResultSetSupplier {
		ResultSet get() throws SQLException;
	}
}
