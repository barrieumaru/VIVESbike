package common.datastructures;

public class Pair<K,V>
{
    private K key;
    private V value;

    Pair(K key, V value)
    {
        setKey(key);
        setValue(value);
    }

    /**
     * Returns the key.
     *
     * @return key
     */
    public K getKey()
    {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Returns the value.
     *
     * @return value
     */
    public V getValue()
    {
        return value;
    }

    public void setValue(V value)
    {
        this.value = value;
    }
}
