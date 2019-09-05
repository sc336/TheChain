package com.example.thechain;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import android.util.Log;

//Consider structuring this as a proper LISP list
//Consider making contents 2d, with second dimension being "count" i.e. coefficient or index
public abstract class OperationList implements AlgebraObject {

	protected LinkedList<AlgebraObject> contents;
	public OperationList() {
		contents = new LinkedList<AlgebraObject>();
	}

	public OperationList(LinkedList<AlgebraObject> l) {
		contents = l;
	}

	//TODO: test or remove this
	private OperationList subclassConstructor() {  //Returns an object of whatever subclass the object actually is.  Only works for the default constructor.
		Class<? extends OperationList> subClass = this.getClass();
		try {
			Constructor<? extends OperationList> c = subClass.getConstructor();
			return c.newInstance((Object)null);
		} catch(Exception e) { throw new RuntimeException(e.getMessage()); }
	}
	
	public abstract OperationList clone();
	public abstract OperationList deepSimplify();
	
	//NB: Sum wrapper (single argument constructor) flattens
	public OperationList(AlgebraObject x) {
		contents = new LinkedList<AlgebraObject>();
		this.append(x);
		flatten();
	}
	
	public OperationList(AlgebraObject x, AlgebraObject y) {
		contents = new LinkedList<AlgebraObject>();
		this.append(x);
		this.append(y);
	}
	
	public AlgebraObject unwrap() { //For removing superfluous layers of OperationLists.  If the list only has one item, return that item.  Otherwise return the list.
		if(contents.size() == 1) return contents.getFirst();
		else return this;
	}
	
	public OperationList unwrapList() {  //As unwrap(), but only unwraps if the contents is another list.
		if(OperationList.class.isAssignableFrom(this.unwrap().getClass())) return (OperationList) this.unwrap();
		else return this;
	}
	
	public boolean flatten() {  //Removes nested identical OperationLists.  So Sum(1, Sum(x,y)).flatten = Sum(1, x, y)
		boolean changed = false;
		LinkedList<AlgebraObject> q = new LinkedList<AlgebraObject>();
		Iterator<AlgebraObject> i = contents.iterator();
		while(i.hasNext()) {
			AlgebraObject x = i.next();
			if(x.getClass().isAssignableFrom(this.getClass())) {
				i.remove();
				q.addAll(((OperationList)x).toList());
				changed = true;
			}
		}
		contents.addAll(q);
		q = new LinkedList<AlgebraObject>();
		i = contents.iterator();
		while(i.hasNext()) {
			AlgebraObject x = i.next();
			if(OperationList.class.isAssignableFrom(x.getClass())) {
				if(((OperationList)x).unwrap() != x) {
					i.remove();
					q.add(((OperationList)x).unwrap());
					changed = true;
				}
			}
		}
		contents.addAll(q);
		return changed;
	}
	

	public List<AlgebraObject> toList() {
		return contents;
	}
	
	public abstract Product multiply(AlgebraObject x);
	public abstract Sum add(AlgebraObject x);
	
	//From here on is the relatively trivial implementation of List, mostly passing through to contents

	
 	public boolean append(AlgebraObject object) {
		return contents.add(object);
	}

	
	public void insert(int location, AlgebraObject object) {
		contents.add(location, object);
	}

	
	public boolean insertAll(Collection<? extends AlgebraObject> arg0) {
		return contents.addAll(arg0);
	}

	
	public boolean insertAll(int arg0, Collection<? extends AlgebraObject> arg1) {
		return contents.addAll(arg0, arg1);
	}

	
	public void clear() {
		contents.clear();
	}

	
	public boolean contains(Object object) {
		return contents.contains(object);
	}

	public boolean containsAll(Collection<?> arg0) {
		return contents.containsAll(arg0);
	}

	
	public AlgebraObject get(int location) {
		return contents.get(location);
	}

	
	public int indexOf(Object object) {
		return contents.indexOf(object);
	}

	
	public boolean isEmpty() {
		return contents.isEmpty();
	}

	
	public Iterator<AlgebraObject> iterator() {
		return contents.iterator();
	}

	
	public int lastIndexOf(Object object) {
		return contents.lastIndexOf(object);
	}

	
	public ListIterator<AlgebraObject> listIterator() {
		return contents.listIterator();
	}

	
	public ListIterator<AlgebraObject> listIterator(int location) {
		return contents.listIterator(location);
	}

	
	public AlgebraObject remove(int location) {
		return contents.remove(location);
	}

	
	public boolean remove(Object object) {
		return contents.remove(object);
	}

	
	public boolean removeAll(Collection<?> arg0) {
		return contents.removeAll(arg0);
	}

	
	public boolean retainAll(Collection<?> arg0) {
		return contents.retainAll(arg0);
	}

	
	public AlgebraObject set(int location, AlgebraObject object) {
		return contents.set(location, object);
	}

	
	public int size() {
		return contents.size();
	}

	
	public List<AlgebraObject> subList(int start, int end) {
		return contents.subList(start, end);
	}

	
	public Object[] toArray() {
		return contents.toArray();
	}

	
	public <T> T[] toArray(T[] array) {
		return contents.toArray(array);
	}


}
