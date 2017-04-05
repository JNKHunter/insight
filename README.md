### Client.java
* Instantiates all of the feature classes 
* Loads the log file and parses it one line at a time
* Each line is passed into each feature class for processing
* Once each line is processed, we output each report by calling outputResults() on each feature object.

### Hosts.java
* Parses each host out from log entries and stores them in a hashmap. This makes lookups by Host/IP a log(n) operation.
* The hashmap uses the host (ip or hostname) as the key, and number of occurrences as the value.
* We want to avoid sorting a large number of hosts by number of occurrences, so when we're ready to output, we create a
priority queue to store  the top X hosts. By peeking at the root of the pq, we know the minimum of the top 10
requesting hosts so far. If we find a larger host request value than the current minimum of the top 10 requesting hosts,
we remove the root of the pq, and add the new host and value to the pq.

##### :fire: Bonus top hosts feature!
Return any number of top hosts (not only the top 10). The add() method in a pq is an O(log(n)) operation, and peek()/remove() is
an 0(log(1)) operation. This gives us the ability to scale our solution to return the top million or even billion resources,
with only a log(n) negative effect on time performance. In the constructor, just pass the number of top X hosts you'd
like returned.

### Resources.java
* Similar to Hosts.java, we parse out each resource from log entries and store them in a hashmap. This makes lookups
by resource a log(n) operation.
* The hashmap uses the resource path as the key, and number of occurrences as the value.
* We want to avoid sorting a large number of resources by number of requests, so when we're ready to output, we create a
priority queue to store  the top X requests. By peeking at the root of the pq, we know the minimum of the top 10
resource paths so far. If we find a larger request value than the current minimum of the top 10 requested resources,
we remove the root of the pq, and add the new resource and value to the pq.

##### :fire: Bonus top resources feature!
Just like in Hosts.java, return any number of top resources (not only the top 10). This feature is extremely time
efficient for the reasons explained under Hosts.java.

### Hours.java
* Parses each host out from log entries and stores them to an array list.
* The list is already sorted by date, which is exactly what we need to process the list quickly.
* One solution would be to check each second between the first date in the list, and the last date in the list, and
base our hours on that. For this exercise, to make the resulting data a little more interesting I've elected to use
each event as the start of the next hour we test. This way we won't drop any events by testing say every 5 minutes.
Yet another solution would be to test every hour on the hour.

##### :fire: Bonus top hours feature!
Just like in Hosts.java, return any number of top traffic hours (not only the top 10). This feature is extremely time
efficient for the reasons explained under Hosts.java.