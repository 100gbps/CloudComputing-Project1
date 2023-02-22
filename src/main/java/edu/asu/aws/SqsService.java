package edu.asu.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import edu.asu.utils.Properties;
import io.micrometer.core.instrument.Measurement;
import lombok.val;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqsService {
    static Properties prop = new Properties();
    public void publish(String fileName, String message) {

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
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

//    public void publish(String message) {
//
//        AWSCredentials credentials = new BasicAWSCredentials(
//                prop.getAccessKey(),
//                prop.getSecretKey()
//        );
//
//        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withRegion(Regions.US_EAST_1)
//                .build();
//
//        SendMessageRequest msgReq = new SendMessageRequest()
//                .withQueueUrl(prop.getMsgReqQueue())
//                .withMessageBody(message)
//                .withDelaySeconds(0);
//
//        sqs.sendMessage(msgReq);
//    }

//    public String consume(String fileName) throws InterruptedException {
//        AWSCredentials credentials = new BasicAWSCredentials(
//                prop.getAccessKey(),
//                prop.getSecretKey()
//        );
//
//        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withRegion(Regions.US_EAST_1)
//                .build();
//
//        GetQueueAttributesRequest request = new GetQueueAttributesRequest()
//                .withQueueUrl(prop.getMsgResQueue())
//                .withAttributeNames("ApproximateNumberOfMessages");
//        GetQueueAttributesResult result = sqs.getQueueAttributes(request);
//
////        while(true){
////            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(prop.getMsgResQueue())
////                    .withMaxNumberOfMessages(5)
////                    .withVisibilityTimeout(10);
////            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
////            String msg = "UNKNOWN";
////            for (Message message : messages) {
////                if(message.getBody().split(",")[0]==fileName){
////                    System.out.println("*********************Recieved: "+message.getBody()+"***********************");
////                    msg=message.getBody();
////                    // Delete the message from the queue
////                    DeleteMessageRequest deleteRequest = new DeleteMessageRequest(prop.getMsgResQueue(), message.getReceiptHandle());
////                    sqs.deleteMessage(deleteRequest);
////                    break;
////                }
////                else{
////                    publish(message.getBody());
////                    DeleteMessageRequest deleteRequest = new DeleteMessageRequest(prop.getMsgResQueue(), message.getReceiptHandle());
////                    sqs.deleteMessage(deleteRequest);
////                }
////            }
////            if(msg=="UNKNOWN"){
////                continue;
////            }
////            if(msg.split(",")[0]==fileName) {
////                return msg.split(",")[1];
////            }
////        }
//
//        return "Done\n";
//    }

    public String consume() throws InterruptedException {

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();

        GetQueueAttributesRequest request = new GetQueueAttributesRequest()
                .withQueueUrl(prop.getMsgResQueue())
                .withAttributeNames("ApproximateNumberOfMessages");
        GetQueueAttributesResult result = sqs.getQueueAttributes(request);
        while(true){
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(prop.getMsgResQueue())
                    .withMaxNumberOfMessages(1);
            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
            if (messages.isEmpty()){
                continue;
            } else {
                String res[]=messages.get(0).getBody().split(",");
                DeleteMessageRequest deleteRequest = new DeleteMessageRequest(prop.getMsgResQueue(), messages.get(0).getReceiptHandle());
                sqs.deleteMessage(deleteRequest);
                System.out.println("Classification result for "+ res[0]+": "+ res[1]);
                return res[0]+": "+ res[1];
            }

        }
    }
}
