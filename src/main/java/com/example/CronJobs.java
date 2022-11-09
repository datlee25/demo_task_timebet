package com.example;

import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CronJobs {

  @Scheduled(every = "1s")
  public void Sing(){
    System.out.println("Println cronjob");
  }
}
