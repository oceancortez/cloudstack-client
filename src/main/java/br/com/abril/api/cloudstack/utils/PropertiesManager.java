package br.com.abril.api.cloudstack.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesManager {

	private static final Logger log = Logger.getLogger(PropertiesManager.class);
	private String systemPath;
	private Properties properties;
	private static PropertiesManager instance;

	private static final String PROPERTIES_FILE_NAME = "/cloudstack-client.properties";

	private PropertiesManager() throws IOException {
		
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		systemPath = path.contains("jar")?path.substring(0, path.lastIndexOf("/")):new File(".").getCanonicalPath();
		
		File file = new File(systemPath + PROPERTIES_FILE_NAME);
		if (!file.exists()) {
			log.warn("Arquivo cloudstack-client.properties não pode ser encontrado!");
			throw new FileNotFoundException();
		}
		
		properties = new Properties();
		FileInputStream fis = new FileInputStream(file);
		properties.load(fis);
		
		fis.close();
	}

	@SuppressWarnings("unchecked")
	public <T>T getPropertyAs(String propertyName, Class<T> type) throws ParseException {
		String strValue = properties.getProperty(propertyName);
		if( strValue == null ) return null;
		
		if( type.isAssignableFrom(String.class) ) {
			return (T) strValue;
		} else if( type.isAssignableFrom(Long.class) ) {
			return (T) Long.valueOf(strValue);
		} else if( type.isAssignableFrom(Integer.class) ) {
			return (T) Integer.valueOf(strValue);
		} else if( type.isAssignableFrom(Boolean.class) ) {
			return (T) Boolean.valueOf(strValue);
		}

		throw new RuntimeException("Tipo de conversor inexistente.");
	}

	public static PropertiesManager getInstance() throws IOException {
		if (instance == null)
			instance = new PropertiesManager();

		return instance;
	}

	public String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public String getSystemPath() {
		return systemPath;
	}
	
	public String getFileName(String fileProp) {
		return getSystemPath().concat(fileProp);
	}
}