#!/usr/bin/env bash

# For the purposes of testing this project using run_tests.sh, we will omit the -d bin flag from the javac command.
javac -cp src/main/java/ src/main/java/Client.java
java -cp src/main/java/ Client ./log_input/log.txt ./log_output/hosts.txt ./log_output/resources.txt ./log_output/hours.txt  ./log_output/blocked.txt
#java -cp src/main/java/ Client ./insight_testsuite/tests/test_features/log_input/log.txt ./log_output/hosts.txt ./log_output/resources.txt ./log_output/hours.txt  ./log_output/blocked.txt