package pl.owolny.gatling.monitoring.actions;

import io.gatling.javaapi.http.HttpRequestActionBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static io.gatling.javaapi.http.HttpDsl.http;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MonitoringActions {

    public static final String LOGIN_PATH = "localhost:9000";

    public static final HttpRequestActionBuilder TEST_EXAMPLE_ACTION = http(LOGIN_PATH)
            .get("/login");
}