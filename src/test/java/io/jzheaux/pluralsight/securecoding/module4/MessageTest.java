package io.jzheaux.pluralsight.securecoding.module4;

import org.apache.commons.logging.LogFactory;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.PluggableSchemaResolver;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class MessageTest {
	@Test
	public void testEtcHosts() {
		File file = new File("C:\\Windows\\system32\\drivers\\etc\\hosts");
		System.out.println(file.exists());
		System.out.println(Files.linesOf(file, StandardCharsets.UTF_8)
				.stream().collect(Collectors.joining("\r\n")));
	}

	@Test
	public void testXMLParsing() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
				"<!DOCTYPE root [\n" +
				" <!ELEMENT includeme ANY>\n" +
				" <!ENTITY xxe SYSTEM \"c:\\Windows\\System32\\drivers\\etc\\hosts\">\n" +
				"]>\n" +
				"<root>&xxe;</root>";
		Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		System.out.println(doc.getDocumentElement().getChildNodes().item(0).getTextContent());
	}

	@Test
	public void testXMLValidating() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setNamespaceAware(true);
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");
		DocumentBuilder builder = factory.newDocumentBuilder();
		String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
				"<root xmlns=\"https://example.org\"" +
				"   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
				"	xsi:schemaLocation=\"https://example.org http://springframework.org/schema/spring-beans.xsd\"></root>";
		DocumentLoader loader = new DefaultDocumentLoader();
		Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		Document d = loader.loadDocument(new InputSource(new ByteArrayInputStream(xml.getBytes())),
				new PluggableSchemaResolver(this.getClass().getClassLoader()), new SimpleSaxErrorHandler(LogFactory.getLog(this.getClass())), 3, true);
		System.out.println(doc.getDocumentElement().getChildNodes().item(0).getTextContent());
	}
}
