package com.vians.admin.config;

import com.vians.admin.emqx.MqttDataHandler;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Resource;

/**
 * MQTT配置
 * @ClassName MqttReceiveConfig
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/6/2 14:56
 * @Version 1.0
 **/
@Configuration
@IntegrationComponentScan
public class MqttReceiveConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String hostUrl;

    @Value("${spring.mqtt.default.topicReq}")
    private String reqTopic;

    @Value("${spring.mqtt.default.topicRsp}")
    private String rspTopic;

    @Value("${spring.mqtt.default.completionTimeout}")
    private int completionTimeout;//连接超时

    @Resource
    private MqttDataHandler mqttDataHandler;

    //初始化连接
    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        mqttConnectOptions.setKeepAliveInterval(50);
        return mqttConnectOptions;
    }


    //初始化mqtt工厂
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    //接收通道
    @Primary
    @Bean("mqttInputChannel")
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    //配置client,监听的topic
    @Bean
    public MessageProducer inbound(@Qualifier("mqttInputChannel") MessageChannel messageChannel) {
        String clientId = "mqttservice_" + String.valueOf(Math.random()).substring(2, 8);
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactory(), reqTopic, rspTopic); //这里的defaultTopic是发布者的主题
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(messageChannel);
        return adapter;
    }

    //订阅消费数据,通过通道获取数据
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {

            logger.info("主题：{}，数据：{}", message.getHeaders().get("mqtt_receivedTopic"), message.getPayload());
//            logger.info("message：{}", message.toString());
            mqttDataHandler.handleData(message.getHeaders().get("mqtt_receivedTopic").toString(), message.getPayload().toString());
        };
    }
}
