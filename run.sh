#!/usr/bin/env bash

javac -cp src/main/java/ -d bin src/main/java/Client.java
java -cp bin Client ./insight_testsuite/tests/test_features/log_input/log.txt ./log_output/hosts.txt ./log_output/resources.txt ./log_output/hours.txt  ./log_output/blocked.txt