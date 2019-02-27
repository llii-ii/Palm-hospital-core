package com.kasite.core.common.util.wxmsg;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public final class IDSeed {
	private final static char[] LETTERS="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private final static int LEN=LETTERS.length;
	private static Random globalRandom=new Random();
	private static Random[] RANDOMS;
	private static final int RANDOM_LENGTH=5;
	private static int R_INDEX=0;
	static{
		RANDOMS=new Random[RANDOM_LENGTH];
		for(int i=0;i<RANDOM_LENGTH;i++){
			RANDOMS[i]=new Random();
		}
	}
	
	private static Random getRandom(){
		int k=R_INDEX++;
		int index=k%RANDOM_LENGTH;
		if(k<10000){
			return RANDOMS[index];
		}
		R_INDEX=globalRandom.nextInt(RANDOM_LENGTH);
		Random r=new Random();
		RANDOMS[index]=r;
		return r;
	}
	
	static void fill(char[] source,final int from,long number,int bytes){
		if(number==Long.MIN_VALUE){
			number=Long.MAX_VALUE;
		}
		if(number<0){
			number=0-number;
		}
		int index=from+bytes-1;
		while(number>0){
			int k=(int) (number%LEN);
			source[index]=LETTERS[k];
			if(index==from){
				return;
			}
			number=number/LEN;
			index--;
		}
		while(index>=from){
			source[index--]='0';
		}
	}
	
	public static long toLong(String s){
		String all=new String(LETTERS);
		long ret=0;
		for(int i=s.length()-1,k=0;i>=0;i--,k++){
			char c=s.charAt(i);
			int v=all.indexOf(c+"");
			ret+=(v*Math.pow(LEN, k));
		}
		return ret;
	}
	private final static int DOUBLE_LEN=LEN*LEN;
	private static AtomicInteger current=new AtomicInteger(6538);

	
	public static String next(){
		char[] ret=new char[20];
		fill(ret,0,globalRandom.nextInt(DOUBLE_LEN),2);
		fill(ret,2,System.currentTimeMillis(),8);
		fill(ret,10,System.nanoTime(),4);
		int addNum=getRandom().nextInt(LEN)+1;
		int next=current.addAndGet(addNum);
		if(next>1000000000){
			current=new AtomicInteger(next%(DOUBLE_LEN*DOUBLE_LEN)+DOUBLE_LEN);
		}
		fill(ret,14,next,4);
		fill(ret,18,addNum,1);
		fill(ret,19,getRandom().nextInt(LEN),1);
		return reOrder(ret);
	}

	private static String reOrder(char[] cs) {
		char[] temp=new char[cs.length];
		int len=cs.length/2;
		for(int k=0;k<2;k++){
			for(int i=0;i<len;i++){
				temp[i*2]=cs[i];
				temp[2*i+1]=cs[i+len];
			}
			cs=temp;
			temp=new char[cs.length];
		}
		return new String(cs);
	}
	
	public static String parse(long number,int bytes){
		if(number==Long.MIN_VALUE){
			number=Long.MAX_VALUE;
		}
		if(number<0){
			number=0-number;
		}
		String ret="";
		while(number>0){
			int k=(int) (number%LEN);
			ret=LETTERS[k]+ret;
			number=number/LEN;
		}
		while(ret.length()<bytes){
			ret="0"+ret;
		}
		if(ret.length()>bytes){
			return ret.substring(ret.length()-bytes,ret.length());
		}
		return ret;
	}
}
