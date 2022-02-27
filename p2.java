import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// 1) Any guest could stop by and check whether the showroom’s door is open at any time and try to enter the room. While this would allow the guests to roam around the castle and enjoy the party, this strategy may also cause large crowds of eager guests to gather around the door. A particular guest wanting to see the vase would also have no guarantee that she or he will be able to do so and when.
//This strategy is the least effective. While it does
//have the advantage of no waiting, it is vulnerable
//to having collisions where a guest may forget to 
//close the door when he leaves.

// 2) The Minotaur’s second strategy allowed the guests to place a sign on the door indicating when the showroom is available. The sign would read “AVAILABLE” or “BUSY.” Every guest is responsible to set the sign to “BUSY” when entering the showroom and back to “AVAILABLE” upon exit. That way guests would not bother trying to go to the showroom if it is not available.
//This strategy is the most effective. the guests can
//carry on with their normal tasks then enter when
//it is available. one disadvantage is that not 
//everyone is guaranteed to be in the room


// 3) The third strategy would allow the quests to line in a queue. Every guest exiting the room was responsible to notify the guest standing in front of the queue that the showroom is available. Guests were allowed to queue multiple times.
//this strategy is also an effective strategy. a 
//queue does a good job at avoiding collisions. one
//downside of a queue is waiting, as guests will 
//be waiting the most here out of the three solutions

//we will be implementing strategy #2

public class p2 {
    
}