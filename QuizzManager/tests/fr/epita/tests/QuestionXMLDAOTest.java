package fr.epita.tests;

import java.io.BufferedWriter;
import org.w3c.dom.Attr;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.epita.datamodel.Question;
import fr.epita.datamodel.QuestionAndAnswers;
import fr.epita.services.dao.QuestionJDBCDAO;
import fr.epita.services.dao.QuestionXMLDAO;

public class QuestionXMLDAOTest {

	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerException {
		//xmlRead();
		xmlWrite();
//		QuestionXMLDAO dao = new QuestionXMLDAO();
//		Question question = new  Question();
//		question.setId(5);
//		question.setDifficulty(8);
//		question.setQuestion("What are the main differences between Java and C++ ?");
//		String[] topics = new  String[2];
//		topics[0] = "Java";
//		topics[1] = "C++";
//		question.setTopics(topics);
//		dao.create(question);
		
		QuestionXMLDAO dao = new QuestionXMLDAO();
		List<Question> searchResults = dao.searchUsingXPATH(new Question("JVM"));
		System.out.println(searchResults);
	}

	private static void xmlWrite() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
		QuestionJDBCDAO dao = new QuestionJDBCDAO();
	List<QuestionAndAnswers>	queAns=dao.getAnswers();
		
	
	 DocumentBuilderFactory dbFactory =
	         DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.newDocument();
	     	Element rootElement = doc.createElement("questions");
	 		doc.appendChild(rootElement);
	     	for (QuestionAndAnswers qa:queAns) {
	     	
		 		
		 		 Element question = doc.createElement("question");
		 		rootElement.setAttribute("order", String.valueOf(qa.getId())); // attribute setting

		         rootElement.appendChild(question);
		     	
		         Element newLabel = doc.createElement("label");
		 		newLabel.setTextContent(qa.getQuestion());
		 		question.appendChild(newLabel); 
		 		
		 		
		 		 Element difficulty = doc.createElement("difficulty");
		 		difficulty.setTextContent(String.valueOf(qa.getDifficulty()));
			 		question.appendChild(difficulty);  // put the label element as a child of the new question element
		 		if(qa.getAnswers()!=null && ! qa.getAnswers().isEmpty()){
		 			 Element options = doc.createElement("Options");
				 		
				 		question.appendChild(options);
				 		for(String str:qa.getAnswers()){
				 			 Element option = doc.createElement("Option");
				 			options.setTextContent(str);	
				 			options.appendChild(option);
				 			
				 		}
		 			
		 			Element topicList = (Element) question.getElementsByTagName("Options").item(0);
					NodeList topics = topicList.getElementsByTagName("topic");
					for (int j=0; j<topics.getLength(); j++) {
						Element topic = (Element) topics.item(j);
						System.out.println("Topic : " + topic.getTextContent());
					}
		 		}
		 			
			}
	      // new element creation with the tag question
	 		
	 		
	
	         
	         TransformerFactory transformerFactory = TransformerFactory.newInstance();
	         Transformer transformer = transformerFactory.newTransformer();
	         DOMSource source = new DOMSource(doc);
	         StreamResult result = new StreamResult(new File("answers.xml"));
	         transformer.transform(source, result);
	         
	         // Output to console for testing
	         StreamResult consoleResult = new StreamResult(System.out);
	         transformer.transform(source, consoleResult);
	
		
		
		final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter("answers.xml", true)));
		System.out.println(documentToString(doc));
		printWriter.flush();
		printWriter.close();
		
	}

	private static String documentToString(Document doc) {
		String result = "";
		try {
			final StringWriter sw = new StringWriter();
			final TransformerFactory tf = TransformerFactory.newInstance();
			final Transformer transformer = tf.newTransformer(); //creation of the object that transfoms the xml file
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(doc), new StreamResult("questions.xml")); // applying modifications in java to the actual xml file
			result = sw.toString();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static void xmlRead() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder = fact.newDocumentBuilder(); // builder used to parse the xml file
		
		Document doc = builder.parse(new File("questions.xml")); //extraction of the data in xml 
																// and put in the doc object
		NodeList listQuestions = doc.getElementsByTagName("question");  // get all the elements going by the "question" tag
		QuestionJDBCDAO dao = new QuestionJDBCDAO();
		for (int i=0; i<listQuestions.getLength(); i++) {
			Question questionToInsert = new  Question();
			Element question = (Element) listQuestions.item(i);
			String order = question.getAttribute("order");   //get the "order" attribute of the element
			if(order!=null && !order.isEmpty()){
				questionToInsert.setId(Integer.parseInt(order));			
			System.out.println("Order : " + order);
			
			NodeList listLabels = question.getElementsByTagName("label"); //get all the elements of the "label" tag (here, only one)
			String label = listLabels.item(0).getTextContent(); // get the text content of the label element
			System.out.println("Question : " + label);
			questionToInsert.setQuestion(label);
			
			NodeList listDifficulty = question.getElementsByTagName("difficulty"); //get all the elements of the "label" tag (here, only one)
			String difficulty = listDifficulty.item(0).getTextContent(); // get the text content of the label element
			System.out.println("difficulty : " + difficulty);
			questionToInsert.setDifficulty(Integer.parseInt(difficulty));
			
			Element topicList = (Element) question.getElementsByTagName("topics").item(0);
			NodeList topics = topicList.getElementsByTagName("topic");
			for (int j=0; j<topics.getLength(); j++) {
				Element topic = (Element) topics.item(j);
				System.out.println("Topic : " + topic.getTextContent());
			}
			dao.create(questionToInsert);

		}
		}
	}
}
