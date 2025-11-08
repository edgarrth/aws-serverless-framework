package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.repository.Product;
import com.serverless.util.SNSAdapter;

public class CreateProductEventHandler implements RequestHandler<SQSEvent, Void>{

	private final SNSAdapter snsAdapter =  new SNSAdapter();
	private final String productTopic = System.getenv("PRODUCTS_TOPIC");

	private final ObjectMapper om = new ObjectMapper();

	@Override
	public Void handleRequest(SQSEvent sqsEvent, Context context) {
		LambdaLogger logger = context.getLogger();

      try {
		  for(SQSEvent.SQSMessage msg: sqsEvent.getRecords()){
			  JsonNode body = new ObjectMapper().readTree((String) msg.getBody());
			  JsonNode msgBody = body.get("Message");

			  logger.log("Body Request: " + body, LogLevel.INFO);
			  logger.log("Message Request: " + msgBody, LogLevel.INFO);

			  Product product = om.readValue(msgBody.asText(), Product.class);

			  product.save(product);

			  logger.log("Event registered in DB: " + body, LogLevel.INFO);

		  }
      } catch (Exception ex) {
          logger.log("Error in saving product: " + ex, LogLevel.ERROR);
      }
        return null;
    }
}
