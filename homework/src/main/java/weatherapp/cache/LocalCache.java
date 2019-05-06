package weatherapp.cache;

import java.util.*;

/**
 * Cache class to store in memory the latest results 
 * of the calls done to the external API - DarkSky.
 * 
 * @author Filipe Pires
 */
public class LocalCache<K,V> {
    
    // Attributes
    
    /**
     * Long object holding the lifetime (in seconds) of a cache entry.
     */
    private final long timeToLive;
    
    /**
     * Map object holding the actual data form the cache.
     */
    private final Map<K,CacheObject> cache;
    
    /**
     * Internal class used to abstract the datatype of the values stored in cache
     * and to keep track of the cached object's time-to-live.
     */
    protected class CacheObject {
        /**
         * Long variable holding the time in milisseconds of the last access made to the cached object
         */
        private long lastAccessed;
        /**
         * Actual data to be cached in memory
         */
        private V value;
 
        /**
         * Default constructor accepting the data to be cached and calculating the last time accessed.
         * 
         * @param value actual data to be cached in memory
         */
        protected CacheObject(V value) {
            this.value = value;
            lastAccessed = System.currentTimeMillis();
        }
    }
    
    // Constructors

    /**
     * LocalCache default constructor, 
     * with time-to-life equal to 10 seconds and timer interval equal to 1 second.
     */
    public LocalCache() {
        long chosenTimeToLive = 10;
        final long timerInterval = 1;
        
        this.timeToLive = chosenTimeToLive * 1000;
        cache = new HashMap();
        
        boolean running = true;
 
        if (timeToLive > 0 && timerInterval > 0) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (running) {
                        try {
                            Thread.sleep(timerInterval * 1000);
                        } catch (InterruptedException ex) { 
                            System.err.println(ex);
                            System.exit(1);
                        }
                        clear(); // clears only those whose ttl has ended
                        
                    }
                }
            });
            
            t.setDaemon(true);
            t.start();
        }
    }
    
    /**
     * LocalCache main constructor.
     * 
     * @param timeToLive defined lifetime (in seconds) for every cache entry
     * @param timerInterval defined space of time between cache life checks
     */
    public LocalCache(long timeToLive, final long timerInterval) {
        this.timeToLive = timeToLive * 1000;
        cache = new HashMap();
        
        boolean running = true;
 
        if (timeToLive > 0 && timerInterval > 0) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (running) {
                        try {
                            Thread.sleep(timerInterval * 1000);
                        } catch (InterruptedException ex) { 
                            System.err.println(ex);
                            System.exit(1);
                        }
                        clear(); // clears only those whose ttl has ended
                    }
                }
            });
            
            t.setDaemon(true);
            t.start();
        }
    }
    
    // Methods
    
    /**
     * Associates the specified value with the specified key in the LocalCache.
     * 
     * @param key Identifier of the new element
     * @param value Data to be stored in cache
     */
    public void put(K key, V value){
        synchronized (cache) {
            cache.put(key, new CacheObject(value));
        }
    }
    
    /**
     * Gets an entry from the LocalCache.
     * 
     * @param key ID of the desired cached element
     * @return Data stored in cache associated with the specified key
     */
    public V get(K key){
        synchronized (cache) {
            CacheObject c = cache.get(key);
            if (c == null) {
                return null;
            } else {
                c.lastAccessed = System.currentTimeMillis();
                return c.value;
            }
        }
    }
    
    /**
     * Gets an array of all entries from the LocalCache.
     * 
     * @param updateLastAccessed tells the method whether it should update the last time the object was accessed or not
     * @return Array of JSON objects with the data stored in cache 
     */
    public V[] getAll(Boolean updateLastAccessed){
        Set<K> keys = cache.keySet();
        List<V> ca = new ArrayList<>();
        Long access = System.currentTimeMillis();
        synchronized (cache) {
            if(cache.isEmpty()) {
                return (V[]) ca.toArray();
            }
            CacheObject c;
            for(K k: keys) {
                c = cache.get(k);
                if (c != null) {
                    c.lastAccessed = access;
                    ca.add(c.value);
                } else {
                    // this should never happen
                }
            }
            return (V[]) ca.toArray();
        }
    }
    
    /**
     * Removes an entry from the LocalCache.
     * 
     * @param key ID of the element to be removed from the cache
     */
    public void remove(K key){
        synchronized (cache) {
            cache.remove(key);
        }
    }
    
    /**
     * Clears the contents of the LocalCache whose time-to-live has ended.
     * 
     */
    @SuppressWarnings("unchecked")
    public void clear(){
        ArrayList<K> deleteKey = null;
        long now = System.currentTimeMillis();
        
        synchronized (cache) {
            Iterator it = cache.entrySet().iterator();
            deleteKey = new ArrayList<>((cache.size() / 2) + 1);
            K key;
            CacheObject c;
 
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                key = (K) pair.getKey();
                c = (CacheObject) pair.getValue();
                if (c != null && (now > (timeToLive + c.lastAccessed))) {
                    deleteKey.add(key);
                }
            }
        }
 
        for (K key: deleteKey) {
            synchronized (cache) {
                cache.remove(key);
            }
            Thread.yield();
        }
    }
    
    /**
     * Returns the size of the cache.
     * 
     * @return integer value holding the number of entries in cache
     */
    public int size(){
        synchronized (cache) {
            return cache.size();
        }
    }
    
    /**
     * Determines if the LocalCache contains an entry for the specified key.
     * 
     * @param key ID of the desired cached element
     * @return True if the specified key is present in cache, False if not
     */
    public boolean containsKey(K key){
        synchronized (cache) {
            CacheObject c =  cache.get(key);
            return c != null;
        }
    }
}
