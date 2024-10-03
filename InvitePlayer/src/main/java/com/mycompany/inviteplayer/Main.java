package com.mycompany.inviteplayer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import java.util.HashMap;
import java.util.Map;

public class Main {
    
    private static final String SQS_QUEUE_URL = "https://sqs.us-east-2.amazonaws.com/606600291685/tictactoeinvites";
    
    public void handleRequest(DynamodbEvent event, Context context) {
        AmazonSQS sqsClient = AmazonSQSClientBuilder.defaultClient();
        
        event.getRecords().forEach(record -> {
            if (record.getEventName().equals("INSERT")) {
                String newImage = record.getDynamodb().getNewImage().toString();
                
                Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
                AttributeValue data = (AttributeValue) record.getDynamodb().getNewImage().values().toArray()[2];
                
                data.getM().forEach((key, value) -> {
                messageAttributes.put(key, new MessageAttributeValue()
                        .withStringValue(value.getS())
                        .withDataType("String"));
                });   

                SendMessageRequest sendRequest = new SendMessageRequest()
                        .withQueueUrl(SQS_QUEUE_URL)
                        .withMessageBody(newImage)
                        .withMessageAttributes(messageAttributes);
                sqsClient.sendMessage(sendRequest);
                
                context.getLogger().log("-----DADOS-----: " + newImage);
            }
        });
    }
}
