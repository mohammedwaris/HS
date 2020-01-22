package com.silverbrain.hs.core;

public interface HSList<E> {
	
	public void add(E e);
	public E get(int index);
	public void set(int index, E e);
	public void remove(int index);
	public void clear();
	public int size();
	public void shuffle();
	public void flush();
	
}