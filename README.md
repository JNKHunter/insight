### Client.java
* Instantiates all of the feature classes 
* Loads the log file and parses it one line at a time
* Each line is passed into each feature class for processing
* Once each line is processed, we output each report by calling outputResults() on each feature object.

### Hosts.java
* Parses hosts out from log entries and stores each host in a Hashmap. A Hashmap allows for log(n) by Host/IP a log(n).
* The hashmap uses the host (ip or hostname) as the key, and number of occurrences as the value.
* We want to avoid sorting a large number of hosts by number of requests, so when we're ready to output, we create a
priority queue to store the top X hosts. By peeking at the root of the pq, we know the minimum of the top 10
requesting hosts so far. If we find a larger host request value than the current minimum of the top 10 requesting hosts,
we remove the root of the pq, and add the new host and value to the pq.
* :fire:**Bonus top hosts feature!**:fire: Return any number of top hosts (not only the top 10). The add() method in a
pq is an O(log(n)) operation, and peek()/remove() is an O(1) operation. This gives us the ability to scale our
solution to return the top million or even billion resources, with only a n + log(n) negative effect on time performance.
In the Hosts() constructor, just pass the number of top X hosts you'd like returned.

### Resources.java
* Similar to Hosts.java, we parse out each resource from log entries and store them in a hashmap. This makes lookups
by resource a log(n) operation.
* The hashmap uses the resource path as the key, and a ResourceNode as the value which stores the number of occurrences
as the value.
* We want to avoid sorting a large number of resources by number of requests, so when we're ready to output, we create a
priority queue to store  the top X requests. By peeking at the root of the pq, we know the minimum of the top 10
resource paths so far. If we find a larger request value than the current minimum of the top 10 requested resources,
we remove the root of the pq, and add the new resource and value to the pq.

* :fire:**Bonus top resources feature!**:fire: Just like in Hosts.java, return any number of top resources
(not only the top 10). This feature is extremely time efficient for the reasons explained under Hosts.java.

### Hours.java
* Parses each host out from log entries and stores them as HourNodes in an array list.
* The list is already sorted by date, which is exactly what we need to process the list quickly.
* We then check second by second over the course of the dataset and store the top 10 traffic hours in a priority queue.

* :fire:**Bonus top hours feature!**:fire: Just like in Hosts.java, return any number of top traffic hours
(not only the top 10). This feature is extremely time efficient for the reasons explained under Hosts.java.

#### Blocked.java
* Parses each failed request out from log entries and stores them in a hashmap. This makes lookups by Host/IP a
log(n) operation.
* The hashmap uses the host (ip or hostname) as the key, and a ResourceNode as the value. A ResourceNode is largely
responsible to tracking the number of failed login requests and determining if a request should be logged as potentially
malicious.
* In particular, we're using a LinkedHashmap to preserve the insertion order of the failed logins, and get a faster
iteration speed when we're writing out to a file.

#### Run time performance
* ~2 min to completely process and create reports from 4.4mm logs on a 2013 MacBook Pro.
* ~2 min to completely process and create reports from 4.4mm logs on a 2016 Dell XSP laptop running Ubuntu 16.04