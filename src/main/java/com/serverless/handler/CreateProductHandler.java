package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.util.ApiGatewayResponse;
import com.serverless.util.Response;
import com.serverless.util.SNSAdapter;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;
import com.serverless.repository.Product;

public class CreateProductHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());
	private final SNSAdapter snsAdapter =  new SNSAdapter();
	private final String productTopic = System.getenv("PRODUCTS_TOPIC");

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

      try {
          // get the 'body' from input
          JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

		  logger.error("Body Request: " + body);

          // create the Product object for post
          Product product = new Product();
          // product.setId(body.get("id").asText());
          product.setName(body.get("name").asText());
          product.setPrice((float) body.get("price").asDouble());
          product.save(product);

		  snsAdapter.publishToSNS(product.getName(), productTopic);

          // send the response back
      		return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(product)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();

      } catch (Exception ex) {
          logger.error("Error in saving product: " + ex);

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
