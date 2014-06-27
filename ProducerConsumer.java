import java.util.*;
import java.util.concurrent.locks.*;
import java.io.*;
public class ProducerConsumer {
	
	public static void main(String []args){
		
		Buffer<Integer>buffer =new Buffer<Integer>(10);
		
		new Thread(new Producer(buffer)).start();
		new Thread(new Consumer(buffer)).start();
	}

	}
class Producer implements Runnable{
	
	Buffer<Integer>buffer;
	public Producer(Buffer<Integer>k){
		buffer=k;
	}
	Scanner in=new Scanner(new File("source.txt"));
	public void run(){
		for(int j=0;j<50;j++){
			Integer x = new Integer(j);
			buffer.put(x);
			//set production speed
			try{
				Thread.sleep(100);
			}catch(InterruptedException e){}
		}
	}
}
class Consumer implements Runnable{
	Buffer <Integer>buffer;
	public Consumer(Buffer<Integer>k){
		buffer = k;
	}
	public void run(){
		System.out.println ("Buffer data");
			for(int j=0;j<50;j++){
			Integer x =buffer.get();
			System.out.println (x+" ");
			//set consumption cycle
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){}
		}
		
	}
}
class Buffer<E>{
	   private int max;
	   private int size = 0;
	   private ArrayList<E> buffer;
	    private Lock lock = new ReentrantLock();
	   private Condition notFull = lock.newCondition(); 
	   private Condition notEmpty = lock.newCondition();
	   public Buffer(int s){
	   	 buffer = new ArrayList<E>();
	   	 max = s;
	   }
	   public void put(E x){
	   	lock.lock();
	   	try{
		   	while(size == max){
		   		try{
		   			notFull.await();
		   		}catch(InterruptedException e){}
		   	}
		   	buffer.add(x);
		   	size++;
		   	if(size - 1 == 0) notEmpty.signal();
		}finally{lock.unlock();}
	   }
	   public E get(){
	   	lock.lock();
	   	try{
		   	while(size == 0){
		   		try{
		   			notEmpty.await();
		   		}catch(InterruptedException e){}
		   	}
		   	E temp = buffer.get(0);
		   	buffer.remove(0);
		   	size--;
		   	if(size + 1 == max) notFull.signal();
		   	return temp;
		} finally{lock.unlock();}
	   }   	
	}




