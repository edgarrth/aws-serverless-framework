package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.util.ApiGatewayResponse;
import com.serverless.util.Response;
import java.util.Collections;
import java.util.Map;
import com.serverless.repository.Product;

public class GetProductHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {


	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

    try {
        // get the 'pathParameters' from input
        Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
        String productId = pathParameters.get("id");

        // get the Product by id
        Product product = new Product().get(productId);

        // send the response back
        if (product != null) {
          return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(product)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda, Serverless and Qualifacts"))
      				.build();
        } else {
          return ApiGatewayResponse.builder()
      				.setStatusCode(404)
              .setObjectBody("Product with id: '" + productId + "' not found.")
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda, Serverless and Qualifacts"))
      				.build();
        }
    } catch (Exception ex) {

        // send the error response back
  			Response responseBody = new Response("Error in retrieving product: ", input);
  			return ApiGatewayResponse.builder()
  					.setStatusCode(500)
  					.setObjectBody(responseBody)
  					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda, Serverless and Qualifacts"))
  					.build();
    }
	}
}
