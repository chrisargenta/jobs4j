package jobs4j.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class Config {

	private static final String PROPERTY_FILE_NAME = "config.properties";
	public static final String PROPERTY_LOCAL_THREAD_COUNT = "backend.threads";

	/**
	 * ConfigProperties is a singleton class
	 */
	private static Config instance = null;

	public static Config getInstance() {
		if (instance == null) {
			try {
				instance = new Config();
			} catch (Exception e) {
				System.err.println("Error initializing the Configuration: " + e.getLocalizedMessage());
			}
		}
		return instance;
	}

	/**
	 * Holds the loaded properties
	 */
	private static final Map<String, String> properties = new TreeMap<>();

	/**
	 * Holds if the properties have already been loaded
	 */
	private static boolean propertiesLoaded = false;

	/**
	 * Constructor using the default file name in the current class loader, or the
	 * default filename in the current directory
	 */
	public Config() throws IOException {
		try (final InputStream propertiesStream = Config.class.getClassLoader()
				.getResourceAsStream(PROPERTY_FILE_NAME)) {
			if (propertiesStream != null) {
				loadProperties(propertiesStream);
			} else {
				if (!Files.exists(Paths.get(PROPERTY_FILE_NAME))) {
					throw new IOException(String.format("Properties file does not exit: %s", PROPERTY_FILE_NAME));
				}

				try (final InputStream fileStream = new FileInputStream(PROPERTY_FILE_NAME)) {
					loadProperties(fileStream);
				}
			}
		} catch (final IOException e) {
			throw new IOException("Error loading properties file from classloader!", e);
		}

		return;
	}

	/**
	 * Constructor using the given filename
	 *
	 * @param propertiesFilePath the file path to the file to load
	 */
	public Config(final String propertiesFilePath) throws IOException {
		if (!Files.exists(Paths.get(propertiesFilePath))) {
			throw new IOException(String.format("Properties file does not exit: %s", propertiesFilePath));
		}

		try (final FileInputStream propertiesStream = new FileInputStream(new File(propertiesFilePath))) {
			loadProperties(propertiesStream);
		} catch (final IOException e) {
			throw new IOException("Error loading properties file from local path!", e);
		}

		return;
	}

	private void loadProperties(final InputStream propertiesStream) throws IOException {
		if (propertiesLoaded) {
			return;
		}

		if (propertiesStream == null) {
			throw new IOException("Properties file does not exist!");
		} else {
			try {
				final Properties props = new Properties();
				props.load(propertiesStream);

				for (final String propertyName : props.stringPropertyNames()) {
					properties.put(propertyName, props.getProperty(propertyName));
				}
			} catch (final IOException e) {
				throw new IOException("Error loading properties!", e);
			}
		}

		propertiesLoaded = true;

		return;
	}

	private boolean exists(final String propertyName) throws IOException {
		if (propertyName == null || propertyName.isEmpty()) {
			throw new IOException("Property cannot be null or empty!");
		}

		return properties.containsKey(propertyName);
	}

	public String get(final String propertyName) throws IOException {
		if (propertyName == null || propertyName.isEmpty()) {
			throw new IOException("Property cannot be null or empty!");
		}

		if (!exists(propertyName)) {
			throw new IOException("Key \"" + propertyName + "\" does does exist in the config file!");
		}

		return properties.get(propertyName);
	}

	/**
	 * Gets all properties in the
	 *
	 * @return all properties
	 */
	public Map<String, String> getAll() {
		return new TreeMap<>(properties);
	}

}
