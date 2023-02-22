package edu.asu.webTier.controller;


import edu.asu.aws.SqsService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@RestController
@Log4j
public class ImageClassification {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");


    @PostMapping("/startPoint")
    @ResponseStatus(value = HttpStatus.OK)
    public String startPoint(@RequestPart("myfile") MultipartFile myfile) throws IOException, InterruptedException {

        if(myfile.isEmpty()){
            System.out.println("input image provided is null at "+dtf.format(now));
            return "No image provided";
        }

        String fileName = myfile.getOriginalFilename();
        System.out.println("sending "+fileName+" image to request queue at "+dtf.format(now));
        //serialized image file
        String message= Base64.getEncoder().encodeToString(myfile.getBytes());

        SqsService sqs = new SqsService();

        //write data into sqs
        sqs.publish(fileName,message);
        System.out.println("Image sent to SQS");

        //read from sqs
//        return sqs.consume();
        return "Job Failed";
    }

}

