package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.repository.Product;
import com.serverless.util.SNSAdapter;
import org.apache.log4j.Logger;

public class CreateProductEventHandler implements RequestHandler<SQSEvent, Void>{
	private final Logger logger = Logger.getLogger(this.getClass());
	private final ObjectMapper om = new ObjectMapper();

	@Override
	public Void handleRequest(SQSEvent sqsEvent, Context context) {
		logger.info("Event: " + sqsEvent);

      try {
		  for(SQSEvent.SQSMessage msg: sqsEvent.getRecords()){
			  JsonNode body = new ObjectMapper().readTree((String) msg.getBody());
			  JsonNode msgBody = body.get("Message");


			  Product product = om.readValue(msgBody.asText(), Product.class);

			  product.save(product);

			  logger.info("Event registered in DB: " + body);

		  }
      } catch (Exception ex) {
          logger.error("Error in saving product: " + ex);
      }
        return null;
    }
}
