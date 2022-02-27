import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Solution: if a prisoner has never turned on the switch, he turns it on, if the switch was off.
// If he does turn it on, he remembers he has turned on the switch and never turns it on again.
// The leader sometimes gets selected, when he is selected, he checks the switch, if it is on then he
// counts the times the switch was on, if the number of times the switch was turned on equals the
// number of prisoners then he knows for sure that everyone has visited the switch room and they are
// free. Because the leader does not know the state of the switch when they start, he waits until he
// has visited the room and set the switch to off to begin counting and counts hiimself as having
// entered the room.

//It is known that there is already a birthday cupcake 
//left at labyrinth’s exit at the start of the game.
//If a guest has never eaten a cupcake, he eats the
//cupcake if there is one on the table. The leader
//is the only one who can order a cupcake. If he sees
//there is no cupcake, he increments his guest count.
//he declares that all have visited when count==N-1,
//since leader doesn't have to eat cupcake to count himself

class p1{
    public static AtomicBoolean occupied = new AtomicBoolean(false);
    public static AtomicBoolean cupcake = new AtomicBoolean(true);
    public static ArrayList<Boolean> available = new ArrayList<Boolean>();
    public static ArrayList<AtomicBoolean> eaten = new ArrayList<AtomicBoolean>();
    public static AtomicInteger count= new AtomicInteger(0);
    public static AtomicInteger idx= new AtomicInteger(0);
    public static void main(String[] args)
    {
        ArrayList<Thread> threadList = new ArrayList<Thread>();
        int N=8;

        Runnable leader = ()->{
            //we know its 0, no need to init
            while(count.get()!=N-1)
            {
                //getAndSet returns the previous value, which means that if prev was false,
                //it immediately becomes true. if prev was true, setting it to true again wont matter
                if(available.get(0) && !occupied.getAndSet(true))
                {
                    //System.out.println("leader in room");
                    if(!cupcake.get())
                    {
                        //someone ate cupcake, inc count and order
                        count.incrementAndGet();
                        cupcake.getAndSet(true);
                        //System.out.println(count.get());
                    }
                    //now leave room
                    occupied.getAndSet(false);
                    available.set(0, false);
                }      
                //System.out.println("leader waiting");       
            }         
        };

        //set leader idx to 0
        threadList.add(new Thread(leader));
        available.add(false);
        eaten.add(new AtomicBoolean(true));
        threadList.get(0).start();

        Runnable guest = ()->{
            //thread can only access arr ele in its thread idx
            //if not eaten and cupcake is available, 
            //eat cupcake and set eaten to true
            int threadIdx=idx.incrementAndGet();
            //System.out.println("thread "+threadIdx+" started");

            while(count.get()!=N-1)
            {
                //getAndSet returns the previous value, which means that if prev was false,
                //it immediately becomes true. if prev was true, setting it to true again wont matter
                if(available.get(threadIdx) && !occupied.getAndSet(true)) 
                {
                    //available guarantees that the thread can get in the room, 
                    //occupied means that the thread is the ONLY thread in the room
                    //System.out.println("thread "+threadIdx+" occupying room");
                    //System.out.println(occupied);
                    if(!eaten.get(threadIdx).get() && cupcake.get())
                    {
                        cupcake.getAndSet(false);
                        eaten.get(threadIdx).getAndSet(true);
                        System.out.println("thread "+threadIdx+" has eaten cupcake");
                        // System.out.println(eaten);
                        // System.out.println(cupcake);
                    }
                    //now leave room
                    occupied.getAndSet(false);
                    available.set(threadIdx, false);
                    //System.out.println("thread "+threadIdx+" left room");
                    //System.out.println(available);
                }
                //System.out.println("thread "+threadIdx+" waiting");
            }
        };

        for(int i=1;i<N;i++)
        {
            threadList.add(new Thread(guest));
            available.add(false);
            eaten.add(new AtomicBoolean(false));
            threadList.get(i).start();          
        }

        while(count.get()!=N-1)
        {
            //start occupy in random and end it in thread
            //since only threads turn off availability,
            //there is no need to worry about threads not
            //making availability in time
            Random random = new Random();
            int rand=random.nextInt(N);
            available.set(rand, true);     
        }

        if(count.get()==N-1)
        {
            System.out.println("All guests have visited");
        }
    }
}

//choose idx then open occupy for idx
//problem with atomic available is that it would HAVE to be next thread
//current problem is that the leader might be putting cupcakes for himseld and not remembering
//basically, the first eaten cupcake is read correctly, but as the leader is in the process of putting a new cupcake, 
//a new prisoner eats the cupcake. this is a collision that causes missed counts