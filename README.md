# pa2

## Prerequisites
* You have installed Java and the JDK. These are available for download at https://www.oracle.com/java/technologies/downloads/

## How to run
* Go to the directory where the java files are located using cd
* To execute Problem 1, type java p1.java
* To execute Problem 2, type java p2.java

## Proof of correctness, efficiency, and experimental evaluation
My solution for problem 1 goes as follows: There is only one guest allowed in the room at a time. If a guest has never turned eaten a cupcake, he eats it if one is available. If he does eat the cupcake, he remembers he has eaten and never eats again. When the leader is selected, he checks whether the cupcake is there or not. If there is no cupcake, he knows a new guest has eaten the cupcake and increments his count of guests who've visited the room. The leader will always make sure there is a cupcake before he leaves the room. When he sees the cupcakes haven't been there N-1 times, then all the guests have visited.

To implement this, I have three flags, two lists and two atomic integers. The three flags are occupied and cupcake, and eaten. The occupied flag is very important in ensuring only one thread is allowed in the room. The cupcake flag is responsible for letting the visitor know if there is a cupcake or not. The eaten flag ensures that guests only eat the cupcake once. The two lists are the threadlist and the current list. The threadlist stored the threads and the current list represents whether a visitor at an index was randomly chosen to visit the room. The two atomic integers are count, idx and eaten. Count represents the number of guests who've eaten the cupcake, and idx represents the thread of the index.

I believe my solution is efficient for various reasons. For one, my solution efficiently assigns threads to an index using the idx variable. That index is then used in the algorithm to randomly pick a next guest to choose optimally. What makes this so efficient is that even though all the threads are using one list, the threads only know what is going on in their index. This means that they are NOT using this array to communicate, but simply know whether they were randomly chosen. The three flags let the threads optimally know where the room is occupied, the cupcake is available and whether they have eaten a cupcake. The occupied flag is especcially efficient because it optimally avoids collisions where there are two threads in the same room. Count and idx are efficient since they atomically keep track of the thread indexes and the number of guests who've visited.

For experimental evaluation, I set a timer for before and after the code execution. In my trial runs, the code takes less than a second when N=8. As N gets very large , the runtime gets very large as well. This is because of the random number generator. In theory, guests are allowed to visit multiple times. My program implements this as desired, but guests who have eaten the cupcake are taking spots from guests who haven't eaten the cupcake. So thread 2330 might have visited 6 times, but other threads might not have visited at all. This takes time away from threads that haven't visited. In reality, processors do not have 2330 threads available. 8 is a reasonable amount of threads to expect a processor to have available. 

My solution for problem two is the second strategy that was given. The minotaur places a sign and the sign would read “AVAILABLE” or “BUSY.” Every guest is responsible to set the sign to “BUSY” when entering the showroom and back to “AVAILABLE” upon exit. This strategy is the most effective since the guests can
carry on with their normal tasks then enter when
it is available. This is also advantageous for avoiding collisions. One disadvantage is that not 
everyone is guaranteed to be in the room with this implementation. 

The code that I use for this problem is similar to the code that I use in problem 1. They actually have many similarities and share many of the same advantages and disadvantages. One big difference between my code for problems one and two is that problem two has a hard timer for 10 seconds. The minotaur will only make his room available for 10 seconds before closing it. 