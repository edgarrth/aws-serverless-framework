package com.serverless.util;

//import com.amazonaws.regions.Regions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

public class SNSAdapter {

    private static String region = System.getenv("REGION");
    private static final Logger logger = LoggerFactory.getLogger(SNSAdapter.class);
    private static  SnsClient snsClient = SnsClient.builder().region(Region.of(region)).build();
    public static void publishToSNS(String message, String topicArn) {

        logger.info("Message Received: "+message+" "+ "Topic: "+topicArn);

        try {
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(topicArn)
                    .build();

            PublishResponse result = snsClient.publish(request);
            logger.info(" Message sent:  " + request);

        } catch (SnsException e) {
            logger.error(e.awsErrorDetails().errorMessage());
        }catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

}
