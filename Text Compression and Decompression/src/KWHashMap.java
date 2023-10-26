/*This program is a modified hashmap interface that declares the prototypes used within the hashtableChain class.
 *@author Shayna Bello
 *@author Kimil Thomas
 *since 05-11-2023
 */

import java.util.*;

public interface KWHashMap<K,V>{

    V get(Object key);

    boolean isEmpty();

    V put(K key, V value);

    V remove(Object key);

    int size();
}
