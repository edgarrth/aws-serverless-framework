package com.serverless.util;

//import com.amazonaws.regions.Regions;
import com.ctc.wstx.shaded.msv_core.util.Uri;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.net.URI;
import java.net.URISyntaxException;

public class SNSLocalStackAdapter {

    private static String region = System.getenv("REGION");


    public static void publishToSNS(String message, String topicArn) throws URISyntaxException {
        URI localStackUri = new URI("http://localhost:4566");

         SnsClient snsClient = SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("user","pass")))
                .endpointOverride(localStackUri)
                .build();

        try {
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(topicArn)
                    .build();

            PublishResponse result = snsClient.publish(request);
            System.out.println("[LOCAL-STACK] SNS-CLIENT: Mensaje enviado. Estatus:  " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
          catch (Exception ex) {
              System.err.println(ex.getMessage());
              System.exit(1);
        }
    }

}
