package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.serverless.util.ApiGatewayResponse;
import com.serverless.util.Response;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import com.serverless.repository.Product;

public class ListProductsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		LambdaLogger logger = context.getLogger();
    try {
        // get all products
        List<Product> products = new Product().list();
		logger.log("Products Retrieved: " + products, LogLevel.INFO);

        // send the response back
        return ApiGatewayResponse.builder()
    				.setStatusCode(200)
    				.setObjectBody(products)
    				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda, Serverless and Qualifacts"))
    				.build();
    } catch (Exception ex) {
        logger.log("Error in listing products: " + ex, LogLevel.ERROR);

        // send the error response back
  			Response responseBody = new Response("Error in listing products: ", input);
  			return ApiGatewayResponse.builder()
  					.setStatusCode(500)
  					.setObjectBody(responseBody)
  					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda, Serverless and Qualifacts"))
  					.build();
    }
	}
}
