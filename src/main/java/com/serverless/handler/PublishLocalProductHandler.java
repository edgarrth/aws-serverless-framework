package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.serverless.repository.Product;
import com.serverless.util.ApiGatewayResponse;
import com.serverless.util.Response;
import com.serverless.util.SNSAdapter;
import com.serverless.util.SNSLocalStackAdapter;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class PublishLocalProductHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());
	private final SNSLocalStackAdapter snsLocalStackAdapter = new SNSLocalStackAdapter();
	private final String productTopicArn = "arn:aws:sns:us-east-1:000000000000:testTopic";
	private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

      try {
          JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

		  logger.info("[LOCAL-STACK] Body Request: " + body);
		  Product product = new Product();
		  product.setName(body.get("name").asText());
		  product.setPrice((float) body.get("price").asDouble());

		  snsLocalStackAdapter.publishToSNS(ow.writeValueAsString(product), productTopicArn);

		  return ApiGatewayResponse.builder()
				  .setStatusCode(200)
				  .setObjectBody("Ok...")
				  .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
				  .build();

      } catch (Exception ex) {
          logger.error("[LOCAL-STACK] Error in saving product: " + ex);

          // send the error response back
    			Response responseBody = new Response("Error in saving product: ", input);
    			return ApiGatewayResponse.builder()
    					.setStatusCode(500)
    					.setObjectBody(responseBody)
    					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
    					.build();
      }
	}
}
