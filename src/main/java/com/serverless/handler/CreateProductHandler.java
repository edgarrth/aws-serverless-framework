package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.serverless.util.ApiGatewayResponse;
import com.serverless.util.Response;
import com.serverless.util.SNSAdapter;
import java.util.Collections;
import java.util.Map;
import com.serverless.repository.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateProductHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final SNSAdapter snsAdapter =  new SNSAdapter();
	private final String productTopic = System.getenv("PRODUCTS_TOPIC");
	private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	private static final Logger logger = LoggerFactory.getLogger(CreateProductHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		//LambdaLogger logger = context.getLogger();

      try {
          JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

		  logger.info("Body Request: " + body);

          Product product = new Product();
          product.setName(body.get("name").asText());
          product.setPrice((float) body.get("price").asDouble());

		  logger.info("Product: " + product);

		  snsAdapter.publishToSNS(ow.writeValueAsString(product), productTopic);

		  logger.info("Prepare Msg: " + product);

		  return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				//.setObjectBody(product)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda, Serverless and Qualifacts"))
      				.build();

      } catch (Exception ex) {
		  logger.error("Error in saving product: " + ex);

          // send the error response back
    			Response responseBody = new Response("Error in saving product: ", input);
    			return ApiGatewayResponse.builder()
    					.setStatusCode(500)
    					.setObjectBody(responseBody)
    					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda, Serverless and Qualifacts"))
    					.build();
      }
	}
}
