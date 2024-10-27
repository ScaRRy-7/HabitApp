package habitapp.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Менеджер конфигурации для загрузки свойств из файла конфигурации.
 * Предоставляет методы для получения значений свойств по ключу.
 */
public class ConfigurationManager {

    private static final String CONFIG_FILE = "application.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = ConfigurationManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new RuntimeException("Не удалось найти файл " + CONFIG_FILE);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить файл конфигурации: " + CONFIG_FILE, e);
        }
    }

    /**
     * Получает значение свойства по заданному ключу.
     *
     * @param key Ключ свойства.
     * @return Значение свойства, или null, если свойство не найдено.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}