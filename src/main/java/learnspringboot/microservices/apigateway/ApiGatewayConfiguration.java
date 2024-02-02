package learnspringboot.microservices.apigateway;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder){

        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f
                                .addRequestHeader("MyHeader", "MyURI")
                                .addRequestParameter("Parameter", "MyValue"))
                        .uri("http://httpbin.org:80"))
                .route(p -> p
                        .path("/currency-exchange/**")
                        .uri("lb://currency-exchange"))
                .route(p -> p
                        .path("/currency-conversion/**")
                        .uri("lb://currency-conversion"))
                .route(p -> p
                        .path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion"))
                .route(p -> p
                        .path("/currency-conversion-test/**")
                        .filters(f -> f
                                .rewritePath(
                                "/currency-conversion-test/(?<segment>.*)",
                                "/currency-conversion-feign/${segment}"))
                        .uri("lb://currency-conversion"))
                .build();
    }
}
