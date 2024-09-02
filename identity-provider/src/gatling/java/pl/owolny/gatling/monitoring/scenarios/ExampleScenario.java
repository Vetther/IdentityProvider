package pl.owolny.gatling.monitoring.scenarios;


import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.Simulation;
import pl.owolny.gatling.PerformanceConfig;
import pl.owolny.gatling.monitoring.actions.MonitoringActions;

import java.time.Duration;
import java.util.Random;

import static io.gatling.javaapi.core.CoreDsl.*;

public class ExampleScenario extends Simulation {

    private static final Random RANDOM = new Random();

    private static Session setSessionAttr(Session session) {
        return session.set("exampleName", "test-%d".formatted(RANDOM.nextInt(1, 300)))
                .set("exampleCount", RANDOM.nextInt(1, 69));
    }

    public static final ScenarioBuilder EXAMPLE = scenario("example scenario")
            .exec(during(Duration.ofSeconds(10))
                    .on(pace(Duration.ofMillis(200))
                            .exec(MonitoringActions.TEST_EXAMPLE_ACTION)));

    {
        setUp(EXAMPLE.injectOpen(atOnceUsers(100)))
                .assertions(details(MonitoringActions.LOGIN_PATH).successfulRequests().percent().is(100d))
                .protocols(PerformanceConfig.getInstance().getHttpProtocol());
    }
}

