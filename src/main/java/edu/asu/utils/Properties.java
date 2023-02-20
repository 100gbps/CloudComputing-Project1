package edu.asu.utils;

//Ajit
// Think about implementing the role to access a sqs from ec2 in this code
public class Properties {
    //IAM cred
    private static String accessKey="AKIARPCESCZDKGPZRBOG";
    private static String secretKey="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";
    //AWS request queue
    private static String msgReqQueue="https://sqs.us-east-1.amazonaws.com/101075523142/_RequestQueue";
    //aws response queue
    private static String msgResQueue="https://sqs.us-east-1.amazonaws.com/101075523142/GfResponseQueue";

    public String getAccessKey(){
        return this.accessKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public String getMsgReqQueue() {
        return this.msgReqQueue;
    }

    public String getMsgResQueue() {
        return this.msgResQueue;
    }
}
