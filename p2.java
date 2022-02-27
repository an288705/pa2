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
    public static AtomicBoolean busy = new AtomicBoolean(false);
    public static ArrayList<AtomicBoolean> seen = new ArrayList<AtomicBoolean>();
    public static AtomicInteger idx= new AtomicInteger(0);
    public static void main(String[] args)
    {
        ArrayList<Thread> threadList = new ArrayList<Thread>();
        long t= System.currentTimeMillis();
        long end = t+10000;
        int N=8;

        Runnable guest = ()->{
            //thread can only access arr ele in its thread idx
            //if not seen and cupcake is available, 
            //eat cupcake and set seen to true
            int threadIdx=idx.incrementAndGet()-1;
            // System.out.println("thread "+threadIdx+" started");

            while(true)
            {
                //getAndSet returns the previous value, which means that if prev was false,
                //it immediately becomes true. if prev was true, setting it to true again wont matter
                if(!busy.getAndSet(true)) 
                {
                    //available guarantees that the thread can get in the room, 
                    //busy means that the thread is the ONLY thread in the room
                    //System.out.println("thread "+threadIdx+" occupying room");
                    //System.out.println(busy);
                    if(!seen.get(threadIdx).get())
                    {
                        seen.get(threadIdx).getAndSet(true);
                        System.out.println("thread "+threadIdx+" has seen the vase for first time");
                        // System.out.println(seen);
                        // System.out.println(cupcake);
                    }
                    //now leave room
                    busy.getAndSet(false);
                    //System.out.println("thread "+threadIdx+" left room");
                    //System.out.println(available);
                }
                //System.out.println("thread "+threadIdx+" waiting");
            }
        };

        System.out.println("The minotaur opened his showroom! It will only be open for 10 seconds\n");

        for(int i=0;i<N;i++)
        {
            threadList.add(new Thread(guest));
            seen.add(new AtomicBoolean(false));
            threadList.get(i).start();          
        }
        
        while(System.currentTimeMillis() < end) {
            //keep main process running for 10 seconds
        }

        //kill the main process
        System.out.println("\nThe minotaur has closed the showroom");
        System.exit(0);
    }
}