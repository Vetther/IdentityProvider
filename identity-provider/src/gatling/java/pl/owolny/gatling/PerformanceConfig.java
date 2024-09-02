package pl.owolny.gatling;

import io.gatling.javaapi.http.HttpProtocolBuilder;
import lombok.Getter;

import static io.gatling.javaapi.http.HttpDsl.http;

@Getter
public class PerformanceConfig {

    private static PerformanceConfig instance;

    private HttpProtocolBuilder httpProtocol;

    private PerformanceConfig() {
    }

    public static PerformanceConfig getInstance() {
        if (instance == null) {
            instance = new PerformanceConfig();
            instance.httpProtocol = http.baseUrl("http://localhost:9000")
                    .header("Content-Type", "application/json")
                    .inferHtmlResources();
        }
        return instance;
    }

}