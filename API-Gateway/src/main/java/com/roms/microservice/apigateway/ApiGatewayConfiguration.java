package com.roms.microservice.apigateway;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class ApiGatewayConfiguration {

	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p.path("/get")
					//	.filters(f -> f.addRequestHeader("MyHeader", "MyURI").addRequestParameter("Param", "MyValue"))
						.uri("http://httpbin.org:80"))
				.route(p -> p.path("/auth/**")
						
						.filters(f -> f.circuitBreaker(c->c.setName("codedTribeCB").setFallbackUri("/defaultFallback")))			
					//	.filters(f -> f.circuitBreaker(c -> c.setName("codedTribeCB").setFallbackUri("/defaultFallback")))
						.uri("lb://authentication-service"))
				.route(p -> p.path("/return-component/**")
						.filters(f -> f.circuitBreaker(c->c.setName("codedTribeCB").setFallbackUri("/defaultFallback")))
					//	.filters(f -> f.circuitBreaker(c -> c.setName("codedTribeCB").setFallbackUri("/defaultFallback")))
						.uri("lb://component-processing-service"))
				.route(p -> p.path("/packaginganddelivery/**")
						.filters(f -> f.circuitBreaker(c->c.setName("codedTribeCB").setFallbackUri("/defaultFallback")))
					//	.filters(f -> f.circuitBreaker(c -> c.setName("codedTribeCB").setFallbackUri("/defaultFallback")))
						.uri("lb://packaging-and-delivery"))
//				.route(p -> p.path("/currency-conversion-new/**")
//						.filters(f -> f.rewritePath(
//								"/currency-conversion-new/(?<segment>.*)", 
//								"/currency-conversion-feign/${segment}"))
//						.uri("lb://currency-conversion"))
				.build();
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(1800)).build()).build());
	}
	
//	@Bean
//	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
//	    return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
//	        .circuitBreakerConfig(CircuitBreakerConfig.custom()
//	           .slidingWindowSize(5)
//	           .permittedNumberOfCallsInHalfOpenState(5)
//	           .failureRateThreshold(50.0F)
//	           .waitDurationInOpenState(Duration.ofMillis(50))
//	           .slowCallDurationThreshold(Duration.ofMillis(200))
//	           .slowCallRateThreshold(50.0F)
//	           .build())
//	        .build());
//	}
	
//	@Bean
//	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
//	    return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
//	            .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
//	            .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
//	}

}