// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.config;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
@Profile("!utest")
public class WebsocketConfig extends AbstractWebSocketMessageBrokerConfigurer implements EnvironmentAware {
    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        Integer maxCore = propertyResolver.getProperty("core-pool-size", Integer.class,5);
        Integer maxPool = propertyResolver.getProperty("max-pool-size", Integer.class,60);
        Integer queueCapacity = propertyResolver.getProperty("queue-capacity", Integer.class,100000);
        registration.taskExecutor()
        .corePoolSize(maxCore)
        .maxPoolSize(maxPool)
        .queueCapacity(queueCapacity);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        Integer maxCore = propertyResolver.getProperty("core-pool-size", Integer.class,5);
        Integer maxPool = propertyResolver.getProperty("max-pool-size", Integer.class,60);
        Integer queueCapacity = propertyResolver.getProperty("queue-capacity", Integer.class,100000);
        registration.taskExecutor()
        .corePoolSize(maxCore)
        .maxPoolSize(maxPool)
        .queueCapacity(queueCapacity);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/betting-match").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "ngaythobet.websocket.");
        
    }
}