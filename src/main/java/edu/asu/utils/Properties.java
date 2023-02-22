package edu.asu.utils;

//Ajit
// Think about implementing the role to access a sqs from ec2 in this code
public class Properties {
    //AWS request queue
    private static String msgReqQueue="https://sqs.us-east-1.amazonaws.com/101075523142/GfRequestQueue";
    //aws response queue
    private static String msgResQueue="https://sqs.us-east-1.amazonaws.com/101075523142/GfResponseQueue";
    public String getMsgReqQueue() {
        return this.msgReqQueue;
    }

    public String getMsgResQueue() {
        return this.msgResQueue;
    }
}
