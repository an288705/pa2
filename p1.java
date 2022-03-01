import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
    public static ArrayList<Boolean> current = new ArrayList<Boolean>();
    public static AtomicInteger count= new AtomicInteger(0);
    public static AtomicInteger idx= new AtomicInteger(0);
    public static void main(String[] args)
    {
        ArrayList<Thread> threadList = new ArrayList<Thread>();
        int N=8;
        long start = System.nanoTime();

        Runnable leader = ()->{
            //we know its 0, no need to init
            while(count.get()!=N-1)
            {
                //getAndSet returns the previous value, which means that if prev was false,
                //it immediately becomes true. if prev was true, setting it to true again wont matter
                if(current.get(0) && !occupied.getAndSet(true))
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
                    current.set(0, false);
                }      
                //System.out.println("leader waiting");       
            }         
        };

        //set leader idx to 0
        threadList.add(new Thread(leader));
        current.add(false);
        threadList.get(0).start();

        Runnable guest = ()->{
            //thread can only access arr ele in its thread idx
            //if not eaten and cupcake is current, 
            //eat cupcake and set eaten to true
            int threadIdx=idx.incrementAndGet();
            boolean eaten = false;
            //System.out.println("thread "+threadIdx+" started");

            while(count.get()!=N-1)
            {
                //getAndSet returns the previous value, which means that if prev was false,
                //it immediately becomes true. if prev was true, setting it to true again wont matter
                if(current.get(threadIdx) && !occupied.getAndSet(true)) 
                {
                    //current guarantees that the thread can get in the room, 
                    //occupied means that the thread is the ONLY thread in the room
                    //System.out.println("thread "+threadIdx+" occupying room");
                    //System.out.println(occupied);
                    if(!eaten && cupcake.get())
                    {
                        cupcake.getAndSet(false);
                        eaten=true;
                        System.out.println("thread "+threadIdx+" has eaten cupcake");
                        // System.out.println(eaten);
                        // System.out.println(cupcake);
                    }
                    //now leave room
                    occupied.getAndSet(false);
                    current.set(threadIdx, false);
                    //System.out.println("thread "+threadIdx+" left room");
                    //System.out.println(current);
                }
                //System.out.println("thread "+threadIdx+" waiting");
            }
        };

        for(int i=1;i<N;i++)
        {
            threadList.add(new Thread(guest));
            current.add(false);
            threadList.get(i).start();          
        }

        while(count.get()!=N-1)
        {
            //start occupy in random and end it in thread
            //since only threads turn off availability,
            //there is no need to worry about threads not
            //making availability in time

            //current problem is that the random function 
            //needs to pick numbers that haven't been used
            Random random = new Random();
            int rand=random.nextInt(N);
            current.set(rand, true);     
        }

        long end = System.nanoTime();

        if(count.get()==N-1)
        {
            System.out.println("All guests have visited");
            System.out.println("The guests took "+String.valueOf((end-start)/1000000000)+" seconds to eat the cupcakes");
        }
    }
}

//choose idx then open occupy for idx
//problem with atomic current is that it would HAVE to be next thread
//current problem is that the leader might be putting cupcakes for himseld and not remembering
//basically, the first eaten cupcake is read correctly, but as the leader is in the process of putting a new cupcake, 
//a new prisoner eats the cupcake. this is a collision that causes missed counts