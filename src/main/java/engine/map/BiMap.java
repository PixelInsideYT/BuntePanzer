package engine.map;

import java.util.HashMap;
import java.util.Set;

public class BiMap<K, V> {

	private HashMap<K, V> map = new HashMap<>();
	private HashMap<V, K> inversedMap = new HashMap<>();

	public void put(K k, V v) {
		map.put(k, v);
		inversedMap.put(v, k);
	}

	public V get(K k) {
		return map.get(k);
	}

	public K getKey(V v) {
		return inversedMap.get(v);
	}

	public Set<K> getSet() {
		return map.keySet();
	}

	public Set<V> getValueSet() {
		return inversedMap.keySet();
	}

}
