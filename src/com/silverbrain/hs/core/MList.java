package com.silverbrain.hs.core;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;

public class MList<E> implements HSList<E> {

	DB db;
	List<E> values;
	
	public MList() {
		//db = DBMaker.memoryDB().make();
		//db = DBMaker.memoryDirectDB().make();
		//values = (List<E>) db.indexTreeList("mlist").createOrOpen();
		values = new ArrayList<E>();
	}
	

	
	public void add(E e) {
		values.add(e);
	}
	
	public E get(int index) {
		return values.get(index);
	}
	
	public void set(int index, E e) {
		values.set(index, e);
	}
	
	public void clear() {
		values.clear();
	}
	
	public void remove(int index) {
		values.remove(index);
	}
	
	public int size() {
		return values.size();
	}
	
	public void shuffle() {
		Collections.shuffle(values);
	}
	
	public void flush() {
	}
	
	
	
}//end of class MList