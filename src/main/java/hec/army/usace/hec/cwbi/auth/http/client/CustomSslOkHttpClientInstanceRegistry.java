package hec.army.usace.hec.cwbi.auth.http.client;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;

final class CustomSslOkHttpClientInstanceRegistry {

    private static final Logger LOGGER = Logger.getLogger(CustomSslOkHttpClientInstanceRegistry.class.getName());
    static final String CALL_TIMEOUT_PROPERTY_KEY = "cwbi.auth.http.client.calltimeout.seconds";
    static final Duration CALL_TIMEOUT_PROPERTY_DEFAULT = Duration.ofSeconds(0);
    static final String CONNECT_TIMEOUT_PROPERTY_KEY = "cwbi.auth.http.client.connecttimeout.seconds";
    static final Duration CONNECT_TIMEOUT_PROPERTY_DEFAULT = Duration.ofSeconds(5);
    static final String READ_TIMEOUT_PROPERTY_KEY = "cwbi.auth.http.client.readtimeout.seconds";
    static final Duration READ_TIMEOUT_PROPERTY_DEFAULT = Duration.ofSeconds(TimeUnit.MINUTES.toSeconds(5));
    private static final String SETTING_MSG = "Setting ";
    public static final String NOT_SET_IN_SYSTEM_PROPERTIES_DEFAULTING_TO = " is not set in system properties. Defaulting to ";
    public static final String READ_FROM_SYSTEM_PROPERTIES_AS = " read from system properties as ";
    private static final Map<SslSocketData, OkHttpClient> REGISTRY = new ConcurrentHashMap<>();

    private CustomSslOkHttpClientInstanceRegistry() {
        //singleton class
    }

    static CustomSslOkHttpClientInstanceRegistry getRegistry() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Get shared instance of OkHttpClient, or create new instance if doesn't exist for a given SSLSocketFactory.
     * This Registry ensures only one OkHttpClient instance exists for a given SSLSocketFactory
     * @param sslSocketData - data object containing SSLSocketFactory and X509TrustManager
     * @return OkHttpClient instance
     */
    public OkHttpClient getOkHttpClientInstance(SslSocketData sslSocketData) {
        OkHttpClient retVal;
        OkHttpClient okHttpClientInstance = REGISTRY.get(sslSocketData);
        if (okHttpClientInstance != null) {
            retVal = okHttpClientInstance;
        } else {
            retVal = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketData.getSslSocketFactory(), sslSocketData.getX509TrustManager())
                .callTimeout(getCallTimeout())
                .connectTimeout(getConnectTimeout())
                .readTimeout(getReadTimeout())
                .build();
            REGISTRY.put(sslSocketData, retVal);
        }
        return retVal;
    }

    private static Duration getReadTimeout() {
        String readTimeoutPropertyValue = System.getProperty(READ_TIMEOUT_PROPERTY_KEY);
        Duration readTimeout = READ_TIMEOUT_PROPERTY_DEFAULT;
        if (readTimeoutPropertyValue == null) {
            LOGGER.log(Level.FINE,
                () -> SETTING_MSG + READ_TIMEOUT_PROPERTY_KEY + NOT_SET_IN_SYSTEM_PROPERTIES_DEFAULTING_TO + READ_TIMEOUT_PROPERTY_DEFAULT);
        } else {
            LOGGER.log(Level.FINE,
                () -> SETTING_MSG + READ_TIMEOUT_PROPERTY_KEY + READ_FROM_SYSTEM_PROPERTIES_AS + readTimeoutPropertyValue);
            readTimeout = Duration.parse(readTimeoutPropertyValue);
        }
        return readTimeout;
    }

    private static Duration getConnectTimeout() {
        String connectTimeoutPropertyValue = System.getProperty(CONNECT_TIMEOUT_PROPERTY_KEY);
        Duration connectTimeout = CONNECT_TIMEOUT_PROPERTY_DEFAULT;
        if (connectTimeoutPropertyValue == null) {
            LOGGER.log(Level.FINE,
                () -> SETTING_MSG + CONNECT_TIMEOUT_PROPERTY_KEY + NOT_SET_IN_SYSTEM_PROPERTIES_DEFAULTING_TO
                    + CONNECT_TIMEOUT_PROPERTY_DEFAULT);
        } else {
            LOGGER.log(Level.FINE,
                () -> SETTING_MSG + CONNECT_TIMEOUT_PROPERTY_KEY + READ_FROM_SYSTEM_PROPERTIES_AS + connectTimeoutPropertyValue);
            connectTimeout = Duration.parse(connectTimeoutPropertyValue);
        }
        return connectTimeout;
    }

    private static Duration getCallTimeout() {
        String callTimeoutPropertyValue = System.getProperty(CALL_TIMEOUT_PROPERTY_KEY);
        Duration callTimeout = CALL_TIMEOUT_PROPERTY_DEFAULT;
        if (callTimeoutPropertyValue == null) {
            LOGGER.log(Level.FINE,
                () -> SETTING_MSG + CALL_TIMEOUT_PROPERTY_KEY + NOT_SET_IN_SYSTEM_PROPERTIES_DEFAULTING_TO + CALL_TIMEOUT_PROPERTY_DEFAULT);
        } else {
            LOGGER.log(Level.FINER,
                () -> SETTING_MSG + CALL_TIMEOUT_PROPERTY_KEY + READ_FROM_SYSTEM_PROPERTIES_AS + callTimeoutPropertyValue);
            callTimeout = Duration.parse(callTimeoutPropertyValue);
        }
        return callTimeout;
    }

    private static class InstanceHolder {
        static final CustomSslOkHttpClientInstanceRegistry INSTANCE = new CustomSslOkHttpClientInstanceRegistry();
    }
}
