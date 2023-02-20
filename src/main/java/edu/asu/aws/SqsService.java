package edu.asu.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import edu.asu.utils.Properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqsService {
    static Properties prop = new Properties();
    public void publish(String fileName, String message) {

        AWSCredentials credentials = new BasicAWSCredentials(
                prop.getAccessKey(),
                prop.getSecretKey()
        );

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        final Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("filename", new MessageAttributeValue()
                .withDataType("String")
                .withStringValue(fileName));

        SendMessageRequest msgReq = new SendMessageRequest()
                .withQueueUrl(prop.getMsgReqQueue())
                .withMessageBody(message)
                .withMessageAttributes(messageAttributes)
                .withDelaySeconds(0);

        sqs.sendMessage(msgReq);
    }

    public void consume(){
        AWSCredentials credentials = new BasicAWSCredentials(
                prop.getAccessKey(),
                prop.getSecretKey()
        );

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        while(true){
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(prop.getMsgResQueue()).withMaxNumberOfMessages(1);
            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

            if (messages.isEmpty()) {
                break;
            }

            for (Message message : messages) {
                System.out.println("Message ID: " + message.getMessageId());

                // Delete the message from the queue
                DeleteMessageRequest deleteRequest = new DeleteMessageRequest(prop.getMsgResQueue(), message.getReceiptHandle());
                sqs.deleteMessage(deleteRequest);
            }
        }

    }
}
