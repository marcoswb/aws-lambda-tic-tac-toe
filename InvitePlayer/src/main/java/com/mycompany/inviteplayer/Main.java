package com.mycompany.inviteplayer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class Main {
    
    private static final String SQS_QUEUE_URL = "https://sqs.us-east-2.amazonaws.com/606600291685/tictactoeinvites";
    
    public void handleRequest(DynamodbEvent event, Context context) {
        AmazonSQS sqsClient = AmazonSQSClientBuilder.defaultClient();
        
        event.getRecords().forEach(record -> {
            if (record.getEventName().equals("INSERT")) {
                String newImage = record.getDynamodb().getNewImage().toString();
                context.getLogger().log("MARCOSSSSSS: " + newImage);
                

                SendMessageRequest sendRequest = new SendMessageRequest(SQS_QUEUE_URL, newImage);
                sqsClient.sendMessage(sendRequest);

                context.getLogger().log("PASSSOUUUUUUU: " + newImage);
            }
        });
    }
}
