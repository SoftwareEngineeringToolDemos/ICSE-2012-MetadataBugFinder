package util;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class UtilSAX {

	// - Test main method -
	//
	// public static void main(String argv[]) {
	// String xmlfile = "C:\\smk\\work3.5c\\Metadata.Invariant.PBSE[LOCAL]\\" + "test3\\pizza3h\\src\\java\\hibernate.cfg.xml";
	// String qName = "mapping";
	// String attr = "resource";
	//
	// ArrayList<String> list = UtilSAX.getResult(xmlfile, qName, attr);
	// for (int i = 0; i < list.size(); i++) {
	// String elem = list.get(i);
	// System.out.println(elem);
	// }
	// }
	//
	// /**
	// * @METHOD
	// * Get sigle value from a specific element, for example
	// * '<class name='package.A' .. />' returns 'package.A'.
	// */
	// public static String getValue(final String xmlfile, final String className, final String attr) {
	// final ArrayList<String> list = new ArrayList<String>();
	//
	// try {
	// SAXParserFactory factory = SAXParserFactory.newInstance();
	// SAXParser saxParser = factory.newSAXParser();
	//
	// DefaultHandler handler = new DefaultHandler() {
	// public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
	// String value = attrs.getValue(attr);
	//
	// if (qName.equals(className) && (value != null) && (list.size() == 0)) {
	// list.add(value);
	// return;
	// }
	// }
	// };
	// // * Call the parser.
	// saxParser.parse(xmlfile, handler);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return list.isEmpty() ? null : list.get(0);
	// // return list.get(0);
	// }

	final ArrayList<String>	tableMapList	= new ArrayList<String>();
	final ArrayList<String>	columnMapList	= new ArrayList<String>();

	public ArrayList<String> getTableMapList() {
		return tableMapList;
	}

	public ArrayList<String> getColumnMapList() {
		return columnMapList;
	}

	/**
	 * @METHOD
	 * Get sigle value from a specific element, for example
	 * '<class name='package.A' .. />' returns 'package.A'.
	 */
	public void getValue(final String xmlfile) {

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler() {

				boolean	classflag	= false, propertyflag = false;
				String	className	= "", tableName = "", propertyName = "", columnName = "", progress = "";

				public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {

					if (qName.equals("class") && (attrs.getValue("name") != null)) {
						className = attrs.getValue("name");
						progress += "#";
//						System.out.println("[DBG] \t| " + className);
						System.out.print(progress);
						classflag = true;
					}
					if (qName.equals("class") && (attrs.getValue("table") != null)) {
						tableName = attrs.getValue("table");
					}
					if (qName.equals("property") && (attrs.getValue("name") != null)) {
						propertyName = attrs.getValue("name");
					}
					if (qName.equals("property") && (attrs.getValue("column") != null)) {
						columnName = attrs.getValue("column");
						propertyflag = true;
					}
				}

				public void endElement(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws SAXException {
					if (classflag) {
						StringBuilder sbuf1 = new StringBuilder();
						sbuf1.append(xmlfile + ",");
						sbuf1.append(className + ",");
						sbuf1.append(tableName + ",");
						tableMapList.add(sbuf1.toString());
						classflag = false;
					}
					if (propertyflag) {
						columnMapList.add(className + "," + columnName + "," + propertyName);
						propertyflag = false;
					}
				}
			};
			// * Call the parser.
			saxParser.parse(xmlfile, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
