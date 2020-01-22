package com.silverbrain.hs.core;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.util.Collections;
import java.util.List;

public class DList<E> implements HSList<E> {
	
	DB db;
	List<E> values;
	
	public DList() {
		db = DBMaker.fileDB("testMapDB" + System.nanoTime() + ".db").make();
		//db = DBMaker.tempFileDB().make();
		values = (List<E>) db.indexTreeList("dlist").createOrOpen();
	}

	public void add(E e) {
		values.add(e);
	}
	
	public E get(int index) {
		return this.values.get(index);
	}
	
	public void set(int index, E e) {
		values.set(index, e);
	}
	
	public int size() {
		return values.size();
	}
	
	public void remove(int index) {
		values.remove(index);
	}
	
	public void clear() {
		values.clear();
	}
	
	public void flush() {
		db.commit();
	}
	
	public void shuffle() {
		Collections.shuffle(values);
	}
	
	
	
	public String toString() {
		String txt = "DList[";
		int sz = size();
		for(int i=0;i<sz;i++) {
			if(i == sz-1)
				txt += get(i);
			else
				txt += get(i) + ",";
		}
		txt += "]";
		return txt;
	}

}//end of class DList