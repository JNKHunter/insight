/**
 * Created by jhunter on 3/31/17.
 * First we parse in N lines contained in the file one line at a time, collecting hosts/ip names into a hashmap.
 *
 * We then take a pass through the hashmap, storing the top M hosts in a priority queue.
 *
 * By peeking at the root of the pq, we know the minimum of the top 10 requesting hosts so far. If we find a larger host
 * request value than the current minimum of the top 10 requesting hosts, we remove the root of the pq, and add the new
 * host and value to the pq.
 *
 * add() in a pq is an O(log(n)) operation, and peek()/remove() is an 0(log(1)) operation. This will give us the ability
 * to scale our solution to the top million or even billion requesting hosts, with only a log(n)
 * negative effect time performance.
 */