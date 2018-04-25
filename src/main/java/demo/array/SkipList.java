package demo.array;

import java.util.Random;

public class SkipList<K, V> {

	private Entity<K, V> frist = null;

	private int maxLevel = 20;

	private Random ran = new Random();

	private int randomLevel() {// 抛硬币

		int k = 1;

		while (ran.nextBoolean()) {
			k++;
		}
		if (k > maxLevel) {
			k = maxLevel;
		}
		return k;
	}

	public void put(K key, V value) {

		if (key != null) {
			if (frist == null) {
				synchronized (this) {
					if (frist == null) {

						frist = new Entity("#", 0, maxLevel);

					}
				}
			}

			int i = randomLevel();
			// System.out
			// .println("key=" + key + " value=" + value + " level=" + i);
			Entity<K, V> x = new Entity<K, V>(key, value, i);

			putObj(x);

		}
	}

	public V get(K key) {

		if (frist != null) {

			return find(frist, new Entity<K, V>(key, null, 1), 0);

		}

		return null;
	}

	private V find(Entity<K, V> f, Entity<K, V> x, int i) {
		// TODO Auto-generated method stub

		return find(f, x, f.next[i], i);

	}

	public V delete(K key) {
		if (frist != null) {
			return delete(frist, new Entity<K, V>(key, null, 1), 0);
		}
		return null;
	}

	private V delete(Entity<K, V> f, Entity<K, V> x, int i) {
		// TODO Auto-generated method stub

		delete(f, x, f.next[i], i);

		return x.value;
	}

	private void delete(Entity<K, V> f, Entity<K, V> x, Entity<K, V> n, int i) {
		// TODO Auto-generated method stub

		if (n != null && compare(x, n)) {

			if (i < (maxLevel - 1)) {
				
				delete(n, x, n.next[i + n.level - maxLevel], i);

			} else {
				delete(f,x, n.next[i + n.level - maxLevel]);
			}

		} else if (n != null && equals(x, n)) {
			f.next[f.level + i - maxLevel] = n.next[n.level + i - maxLevel];
			i++;
			if (i < (maxLevel - 1)) {
				delete(f, x, f.next[i + f.level - maxLevel], i);
			}else {
				delete(f,x, f.next[i + f.level - maxLevel]);
			}
		} else {
			i++;
			if (i < (maxLevel - 1)) {
				delete(f, x, f.next[i + f.level - maxLevel], i);
			} else {
				delete(f,x, f.next[i + f.level - maxLevel]);
			}
		}

	}

	private void delete(Entity<K, V> f,Entity<K, V> x, Entity<K, V> n) {
		// TODO Auto-generated method stub
		if(n!=null && compare(x, n)){
			delete(n,x, n.next[n.level-1]);
		}else if(n!=null&& equals(x, n)){
			f.next[f.level -1] = n.next[n.level -1];
			x.value = n.value;
		}else{
			return;
		}

	}

	private V find(Entity<K, V> f, Entity<K, V> x, Entity<K, V> n, int i) {
		// TODO Auto-generated method stub

		// //view
		// if (n != null) {
		// System.out.println(n.key + "=" + n.value + " level="
		// + (maxLevel - i));
		// }

		if (n != null && compare(x, n)) {
			// ����
			if (i < (maxLevel - 1)) {

				return find(n, x, n.next[i + n.level - maxLevel], i);

			} else {
				return find(x, n.next[i + n.level - maxLevel]);
			}

		} else if (n != null && equals(x, n)) {
			return n.value;
		} else {

			i++;
			if (i < (maxLevel - 1)) {
				return find(f, x, f.next[i + f.level - maxLevel], i);
			} else {
				return find(x, f.next[i + f.level - maxLevel]);
			}

		}
	}

	private V find(Entity<K, V> x, Entity<K, V> n) {
		// TODO Auto-generated method stub
		// if (n != null) {
		// System.out.println(n.key + "=" + n.value + " level=1");
		// }

		if (n != null && compare(x, n)) {
			return find(x, n.next[n.level - 1]);
		} else if (n != null && equals(x, n)) {
			return n.value;
		} else {
			return null;
		}
	}

	private void putObj(Entity<K, V> x) {
		// TODO Auto-generated method stub

		// /if(frist.next[maxLevel-x.level]!=null){
		compareAdd(frist, x, frist.next[0], 0);// maxLevel - x.level

	}

	private void compareAdd(Entity<K, V> f, Entity<K, V> x, Entity<K, V> l,
			int f_index) {
		// TODO Auto-generated method stub

		if (l != null && compare(x, l)) {

			if ((maxLevel - f_index) > x.level) {

				Entity<K, V> ll = l.next[l.level + f_index - maxLevel];

				if (ll != null) {
					compareAdd(l, x, ll, f_index);
				} else {
					f_index++;
					compareAdd(l, x, l.next[l.level + f_index - maxLevel],
							f_index);
				}

			} else {

				if (f_index < (maxLevel - 1)) {

					Entity<K, V> ll = l.next[l.level + f_index - maxLevel];
					if (ll != null) {
						if (f_index < maxLevel - 1) {
							compareAdd(l, x, ll, f_index);
						} else {
							compareAdd(l, x, l.next[l.level - 1]);
							return;
						}

					} else {
						l.next[l.level + f_index - maxLevel] = x;
						x.next[x.level + f_index - maxLevel] = null;
						f_index++;

						if (f_index < (maxLevel - 1)) {
							compareAdd(l, x, l.next[l.level + f_index
									- maxLevel], f_index);
						} else {
							compareAdd(l, x, l.next[l.level - 1]);
							return;
						}

					}

				} else {
					compareAdd(l, x, l.next[l.level - 1]);
					return;
				}
			}

		} else if (l != null && equals(x, l)) {
			if ((maxLevel - f_index) > x.level) {
				f.next[f.level + f_index - maxLevel] = l.next[l.level + f_index
						- maxLevel];
			} else {
				f.next[f.level + f_index - maxLevel] = x;
				x.next[x.level + f_index - maxLevel] = l.next[l.level + f_index
						- maxLevel];
			}

		} else {
			// С�� null
			if ((maxLevel - f_index) <= x.level) {
				f.next[f.level + f_index - maxLevel] = x;
				x.next[x.level + f_index - maxLevel] = l;
			}

		}
		f_index++;

		if (f_index < (maxLevel - 1)) {
			compareAdd(f, x, f.next[f.level + f_index - maxLevel], f_index);
		} else {
			compareAdd(f, x, f.next[f.level - 1]);
		}

	}

	private void compareAdd(Entity<K, V> f, Entity<K, V> x, Entity<K, V> n) {
		// TODO Auto-generated method stub

		if (n != null && compare(x, n)) {// && f != n && n.next[n.level - 1] !=
											// n&& n.next[n.level - 1] != x
			compareAdd(n, x, n.next[n.level - 1]);
		} else if (n != null && equals(x, n)) {

			f.next[f.level - 1] = x;
			x.next[x.level - 1] = n.next[n.level - 1];
			return;

		} else {
			f.next[f.level - 1] = x;
			x.next[x.level - 1] = n;
			return;
		}

	}

	public void view() {

		view(frist);
		System.out.println();
		System.out.println("end");

	}

	private void view(Entity<K, V> o) {
		// TODO Auto-generated method stub
		// /System.out.println(o);
		System.out.print(o.key + "=" + o.value + "__|");
		for (int i = o.level - 1; i >= 0; i--) {
			System.out.print(o.next[i] == null ? "^" : o.next[i].key);
			System.out.print("|");
		}

		System.out.println();

		Entity<K, V> next = o.next[o.level - 1];
		if (next != null) {
			view(next);
		}

	}

	public static void main(String[] args) {

		SkipList<String, Object> t = new SkipList<String, Object>();

		t.put("d", "d");
		t.put("e", "e");
		t.put("a", "a");
		t.put("x", "x");
		t.put("c", "c");
		t.put("z", "z");
		t.put("y", "y");
		t.put("b", "b");

		t.put("c", "4");
		t.put("c", "5");
		t.put("c", "1");
		t.put("c", "2");
		t.put("c", "3");
		t.put("c", "6");

		/*
		 * try { Thread.sleep(3000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		t.view();
		System.out.println(t.get("c"));
		
		System.out.println("______________________");
		
		System.out.println("delete:y="+t.delete("y"));
		
		t.view();
		
		System.out.println("______________________");
		
		System.out.println("delete:d="+t.delete("d"));
		
		t.view();
		
		System.out.println("______________________");
		
		t.put("d","9");
		System.out.println("put:"+"d=9");
		
		t.view();
		
		System.out.println("______________________");
		
		System.out.println("delete:d="+t.delete("d"));
		
		t.view();
		
		// System.out.println(System.currentTimeMillis());
		// long l=System.nanoTime();
		//
		// SkipList<String, Object> t = new SkipList<String, Object>();
		// for (int i = 0; i < 10000000; i++) {
		//
		//
		// t.put("d", "d");
		// t.put("e", "e");
		// t.put("a", "a");
		// t.put("x", "x");
		// t.put("c", "c");
		// t.put("z", "z");
		// t.put("y", "y");
		// t.put("b", "b");
		//
		// t.put("c", "4");
		// t.put("c", "5");
		// t.put("c", "1");
		// t.put("c", "2");
		// t.put("c", "3");
		// t.put("c", "6");
		// if(!t.get("c").equals("6")){
		// System.out.println("eee");
		// }
		//
		// }
		// System.out.println(System.nanoTime()-l);
		// System.out.println(System.currentTimeMillis());
		// t.view();

		// String[] s=new
		// String[]{"a","b","c","d","e","f","g","h","j","k","l","m"
		// ,"n","o","p","q","r","s","t","u","v","w","x","y","z"};
		//
		//
		// for (int i = 0; i < s.length; i++) {
		//
		// for (int j = 0; j < s.length; j++) { t.add(s[i]+s[j], s[i]+s[j]); }
		//
		// }

		// t.add("f", "f");*/

	}

	/*
	 * private Entity<K, V> find(Entity<K, V> x, int[] index) { // TODO
	 * Auto-generated method stub
	 * 
	 * return find(x,frist.next[index[0]],index); }
	 * 
	 * private Entity<K, V> find(Entity<K, V> x, Entity<K, V> entity, int[]
	 * index) { // TODO Auto-generated method stub if(entity!=null){
	 * 
	 * if(compare(x, entity)){
	 * 
	 * }else{ index[0]++; find }
	 * 
	 * }else{
	 * 
	 * } return null; }
	 */

	/*
	 * private Entity<K, V> find(Entity<K, V> ff, Entity<K, V> ll, Entity<K, V>
	 * x, int c) { // TODO Auto-generated method stub
	 * 
	 * if (ll != null) { if (compare(x, ll)) { // ���� return find(ll, ll.next[c],
	 * x, c);
	 * 
	 * } else {
	 * 
	 * if (c < (maxLevel - 1)) { c++; Entity<K, V> bb = ff.next[c]; if (bb ==
	 * ll) { return bb;
	 * 
	 * } else { return find(ff, bb, x, c - 1); } } else { Entity<K, V> bb =
	 * ff.next[ff.level - 1];
	 * 
	 * if (bb == ll) { return bb; }
	 * 
	 * return find(ff, bb, x, 1); } }
	 * 
	 * } else { if (c < (maxLevel - 1)) { c++; } return find(ff, ff.next[c], x,
	 * c);
	 * 
	 * }
	 * 
	 * }
	 */

	private boolean compare(Entity<K, V> obj1, Entity<K, V> obj2) {

		if (obj1.hash() > obj2.hash()) {
			return true;
		}

		return false;
	}

	private boolean equals(Entity<K, V> obj1, Entity<K, V> obj2) {
		if (obj1.hash() == obj2.hash()) {

			return true;
		}
		return false;
	}

	private static class Entity<K, V> {

		K key;
		V value;
		Entity<K, V>[] next;
		int level;

		Entity(K key, V value, int k) {

			this.next = new Entity[k];
			this.key = key;
			this.value = value;
			level = k;
		}

		V getValue() {
			return value;
		}

		int hash() {
			return key.hashCode();
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			/*
			 * StringBuffer sb=new StringBuffer("value="+value); if(next!=null){
			 * sb.append("  next=["); for (int i=0;i<next.length;i++) { if(i>0){
			 * sb.append(" , "); } if(next[i]!=null){
			 * sb.append(next[i].toString()); }else{ sb.append("null"); }
			 * 
			 * } sb.append("]"); }else{ sb.append(" next=null"); } return
			 * sb.toString();
			 */

			if (value == null) {
				return "frist";
			}
			
			return value.toString();
		}

	}

}

