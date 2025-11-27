# Set Enviroment
Install nodejs \
Install npm  \
Install serverless framework: npm install serverless -g \
Install serverless plugins npm install serverless-offline, etc 

# Run localstack

# Create SNS y SQS
create topic: aws --endpoint-url http://localhost:4566 sns create-topic --name testTopic \
create queque: aws --endpoint-url http://localhost:4566 sqs create-queue --queue-name testQueue 
# Show Queue Attributes
create queque: aws --endpoint-url http://localhost:4566 sqs get-queue-attributes --queue-url http://localhost:4566/000000000000/testQueue --attribute-names All 

# Subscribir SQS a SNS
aws --endpoint-url=http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:testTopic --protocol sqs --notification-endpoint arn:aws:sqs:us-east-1:000000000000:testQueue \

# List Subs:
aws --endpoint-url http://localhost:4566 sns list-subscriptions 

# Read Msg:
aws --endpoint-url=http://localhost:4566 sqs receive-message --queue-url http://localhost:4566/000000000000/testQueue



