#!/bin/bash
nohup java -jar /home/ubuntu/target/*.jar > /home/ubuntu/dms-service.log 2>&1 &
