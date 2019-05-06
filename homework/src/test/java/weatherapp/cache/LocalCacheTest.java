package weatherapp.cache;

import com.google.gson.*;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Test class used to ensure the correct functioning of the LocalCache.
 * 
 * @author Filipe Pires
 */
public class LocalCacheTest {
    
    /**
     * Instance of the target class to be tested.
     */
    private LocalCache<String,Integer> instance;
    
    /**
     * Method called every time a method annotated with @Test is executed, before its execution.
     */
    @BeforeEach
    public void setUp() {
        instance = new LocalCache();
    }
    
    /**
     * Method called every time a method annotated with @Test is executed, after its execution.
     */
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of put method, of class LocalCache.
     */
    @Test
    public void testPutPrimitives() {
        System.out.println("put primitives");
        // arrange
        String key = "";
        Integer value = null;
        // act
        instance.put(key, value);
        // assert
        assertThat(instance.size()).isEqualTo(1);//assertEquals(1,instance.size());
    }
    
    /**
     * Test of put method, of class LocalCache.
     */
    @Test
    public void testPutNonPrimitives() {
        System.out.println("put non-primitives");
        // arrange
        JsonObject key = new JsonObject();
        JsonArray value = new JsonArray();
        LocalCache<JsonObject,JsonArray> instance2 = new LocalCache();
        // act
        instance2.put(key, value);
        // assert
        assertThat(instance2.size()).isEqualTo(1);//assertEquals(1,instance2.size());
    }

    /**
     * Test of get method, of class LocalCache.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        // arrange
        String key = "k1";
        Integer expResult = 1;
        instance.put(key, expResult);
        // act
        Integer result = instance.get(key);
        // assert
        assertThat(result).isEqualTo(expResult);//assertEquals(expResult, result);
    }

    /**
     * Test of getAll method, of class LocalCache.
     */
    @Test
    public void testGetAll() {
        System.out.println("getAll");
        // arrange
        String[] keys = new String[]{"k1","k2","k3"};
        Integer[] expResult = new Integer[]{1,2,3};
        for(int i=0; i<keys.length; i++) {
            instance.put(keys[i], expResult[i]);
        }
        // act
        Object[] resultObj = instance.getAll(true);
        Integer[] result = new Integer[resultObj.length];
        for(int i=0; i<resultObj.length; i++) {
            result[i] = (Integer)resultObj[i];
        }
        // assert
        assertThat(result).isEqualTo(expResult);//assertArrayEquals(expResult, result);
    }

    /**
     * Test of remove method, of class LocalCache.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        // arrange
        String key = "k1";
        Integer value = null; 
        instance.put(key, value);
        Integer originalSize = instance.size();
        // act
        instance.remove(key);
        // assert
        assertThat(instance.containsKey(key)).isFalse();//assertEquals(false,instance.containsKey(key));
        assertThat(instance.size()).isEqualTo(originalSize-1);
    }
    
    /**
     * Test of remove method, of class LocalCache.
     */
    @Test
    public void testRemoveEmpty() {
        System.out.println("remove empty");
        // arrange
        Integer originalSize = instance.size();
        // act
        instance.remove("k1");
        // assert
        assertThat(instance.size()).isEqualTo(originalSize);//assertEquals(false,instance.containsKey(key));
    }

    /**
     * Test of clear method, of class LocalCache.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        // arrange
        LocalCache<String,Integer> instance2 = new LocalCache(1,10);
        String[] keys = new String[]{"k1","k2","k3"};
        Integer[] expResult = new Integer[]{1,2,3};
        for(int i=0; i<keys.length; i++) {
            instance2.put(keys[i], expResult[i]);
        }
        // act
        instance2.clear();
        // assert
        assertThat(instance.size()).isEqualTo(0);//assertEquals(0,instance.size());
    }

    /**
     * Test of size method, of class LocalCache.
     */
    @Test
    public void testSizeEmpty() {
        System.out.println("size empty");
        // arrange
        int expResult = 0;
        // act
        int result = instance.size();
        // assert
        assertThat(result).isEqualTo(expResult);//assertEquals(expResult, result);
    }
    
    /**
     * Test of size method, of class LocalCache.
     */
    @Test
    public void testSizeNonEmpty() {
        System.out.println("size non-empty");
        // arrange
        int expResult = 1;
        String key = "k1";
        Integer value = null;
        instance.put(key, value);
        // act
        int result = instance.size();
        // assert
        assertThat(result).isEqualTo(expResult);//assertEquals(expResult, result);
    }

    /**
     * Test of containsKey method, of class LocalCache.
     */
    @Test
    public void testContainsKeyTrue() {
        System.out.println("containsKey True");
        // arrange
        String key = "k1";
        boolean expResult = true;
        Integer value = null;
        instance.put(key, value);
        // act
        boolean result = instance.containsKey(key);
        // assert
        assertThat(result).isEqualTo(expResult);//assertEquals(expResult, result);
    }
    
    /**
     * Test of containsKey method, of class LocalCache.
     */
    @Test
    public void testContainsKeyFalse() {
        System.out.println("containsKey False");
        // arrange
        String key = "k1";
        boolean expResult = false;
        Integer value = null;
        instance.put(key, value);
        // act
        boolean result = instance.containsKey("k2");
        // assert
        assertThat(result).isEqualTo(expResult);//assertEquals(expResult, result);
    }
    
}
