### Client.java
* Instantiates all of the feature classes 
* Loads the log file and parses it one line at a time
* Each line is passed into each feature class for processing
* Once each line is processed, we output each report by calling outputResults() on each feature object.

### Hosts.java
* Parses each host out from log entries and stores them in a hashmap.
* The hashmap uses the host (ip or hostname) as the key, and number of occurrences as the value.
* We want to avoid sorting a large number of hosts by number of occurrences, so when we're ready to output, we create a
priority queue to store  the top X hosts. By peeking at the root of the pq, we know the minimum of the top 10
requesting hosts so far. If we find a larger host request value than the current minimum of the top 10 requesting hosts,
we remove the root of the pq, and add the new host and value to the pq.
##### :fire: Bonus top hosts feature!
Return any number of top hosts (not only the top 10). Because add() in a pq is an O(log(n)) operation, and peek()/remove() is
an 0(log(1)) operation we have the ability to scale our solution to return the top million or even billion resources,
with only a log(n) negative effect on time performance.

### Resources.java
* Similar to Hosts.java, we parse out each resource from log entries and store them in a hashmap.
* The hashmap uses the resource path as the key, and number of occurrences as the value.
* We want to avoid sorting a large number of resources by number of requests, so when we're ready to output, we create a
priority queue to store  the top X requests. By peeking at the root of the pq, we know the minimum of the top 10
resource paths so far. If we find a larger request value than the current minimum of the top 10 requested resources,
we remove the root of the pq, and add the new resource and value to the pq.
##### :fire: Bonus top resources feature!
Just like in Hosts.java, return any number of top resources (not only the top 10). Because add() in a pq is an O(log(n))
operation, and peek()/remove() is an 0(log(1)) operation we have the ability to scale our solution to return the top
million or even billion resources, with only a log(n) negative effect on time performance.


First we parse in N lines contained in the file one line at a time, collecting hosts/ip names into a hashmap.

We then take a pass through the hashmap, storing the top M hosts in a priority queue.
 
add() in a pq is an O(log(n)) operation, and peek()/remove() is an 0(log(1)) operation. This will give us the ability
to scale our solution to the top million or even billion requesting hosts, with only a log(n)
negative effect time performance.
 
 

Created by jhunter on 4/1/17.
First we parse in N lines contained in the file one line at a time, collecting resource paths and accumulated bytes
into a hashmap.

We then take a pass through the hashmap, storing the top M resources in a priority queue.

By peeking at the root of the pq, we know the minimum of the top 10 requests so far. If we find a larger resource
value than the current minimum of the top 10 resoursces, we remove the root of the pq, and add the new
resource node and value to the pq.


 
 
Extra features, pass in the number of top X items you'd like to see into the constructor of Hosts, Resources, or HOurs.
You will get that many results back with only a log(n) increase in time execution!  