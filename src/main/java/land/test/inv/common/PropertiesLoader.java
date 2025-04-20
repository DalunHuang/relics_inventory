package land.test.inv.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

	public static Properties getProperties() {
		Properties properties = new Properties();
        try (InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
		return properties;
	}
	
}
