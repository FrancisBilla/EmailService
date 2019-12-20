package io.turntabl.emailservice.controllers;


import io.turntabl.emailservice.models.MissedEmployees;
import jdk.vm.ci.meta.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

@Component
public class SendMail {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JdbcTemplate template;

    LocalDate date =  LocalDate.now();

    SimpleMailMessage msg = new SimpleMailMessage();

//    @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000, initialDelay = 5000)
    public void toManager(){
        System.out.println();
        msg.setTo("dennis.effa@turntabl.io");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello Dennis	 \n You will receive this each minute");

        javaMailSender.send(msg);
    }
    List<LocalDate> days =new ArrayList<>();
    List<MissedEmployees> emps= Arrays.asList();
    Map<LocalDate, List<MissedEmployees>> tosend = new HashMap<>();


    @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000, initialDelay = 5000)
    public void toDeveloper() throws IOException, MessagingException {
//        System.out.println(date);
//        msg.setTo("dennis.effa@turntabl.io");
//        msg.setSubject("Testing from Spring Boot");
//        msg.setText("Hello Dennis	 \n You will receive this each minute");
//        javaMailSender.send(msg);
//        System.out.println(date.minusDays(1));
//        msg.setTo("dennis.effa@turntabl.io");
//        msg.setSubject("Stubborn employees");

//        System.out.println("Written");
        File f = new File("sample.txt");
        if (f.exists())
        {
            f.delete();
        }

          IntStream.of(0,1,2,3,4)
                  .mapToObj(daynumber -> date.minusDays(daynumber))
                    .forEach(e->{
                       emps =  template.query("select t1.emp_id, t3.emp_name, t3.emp_email, t4.title from currentprojects as t1 inner join employees as t3 on t3.emp_id=t1.emp_id inner join projects as t4 on t4.project_id = t1.project_id where not exists (select * from activitylogging as t2 where t1.emp_id=t2.emp_id and date = ?)",
                                new Object[]{e},
                                BeanPropertyRowMapper.newInstance(MissedEmployees.class));
//                        System.out.print(e);
//                        System.out.println(emps);
                        tosend.put(e, emps);
                    });
        BufferedWriter writer = new BufferedWriter(new FileWriter("sample.txt", true));
        writer.write("");
        writer.flush();
        for (Map.Entry<LocalDate, List<MissedEmployees>> entry : tosend.entrySet()) {
            LocalDate key = entry.getKey();
//            msg.setText(key+"\n"+entry.getValue());
            writer.write(String.valueOf(key) + "\n" + entry.getValue() +"\n");
        }

        writer.close();
        System.out.println("written");
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo("dennis.effa@turntabl.io");

        helper.setSubject("Stubborn employees");
        helper.setText("<h1>Find attached the stubborn employees. </h1>", true);

//
//                 tosend.entrySet().forEach(localDateListEntry -> {
//                     System.out.println(localDateListEntry.getKey());
//                 });
//                  .forEach(e-> days.add(e));
//        for (Map.Entry<LocalDate, List<MissedEmployees>> entry : tosend.entrySet()) {
//            LocalDate key = entry.getKey();
//                    msg.setText(key+"\n"+entry.getValue());
//        }
//        helper.addAttachment("sample.txt", new ClassPathResource("sample.txt"));
        FileSystemResource file = new FileSystemResource(new File("sample.txt"));
        helper.addAttachment("content.txt", file);

        javaMailSender.send(msg);
    }

//    @Scheduled(fixedRate = 5000,initialDelay = 4000)
    public void getMissed(){
        emps = template.query("select t1.emp_id, t3.emp_name, t3.emp_email, t4.title from currentprojects as t1 inner join employees as t3 on t3.emp_id=t1.emp_id inner join projects as t4 on t4.project_id = t1.project_id where not exists (select * from activitylogging as t2 where t1.emp_id=t2.emp_id and date = ?)",
                new Object[]{date},
                BeanPropertyRowMapper.newInstance(MissedEmployees.class));
//        emps = employees;
        System.out.println("getting");
    }

//    @Scheduled(fixedRate = 5000,initialDelay = 6000)
    public void print(){
        System.out.println(emps);
    }





}
