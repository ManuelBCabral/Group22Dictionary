package assignment.dictionary;

/*

 */

//
import java.util.*;
//import java.io.*;
import java.util.Dictionary;
//import java.util.function.BiConsumer;
//import java.util.function.Function;
//import java.util.function.BiFunction;

/**

 */
public class MyHashTable<K,V> extends Dictionary<K,V>
//
//
{
    private ArrayList<LinkedList<Node<K, V>>> buckets;
    private int capacity;
    private int size;

    private static class Node<K,V>{
        final K key;
        V value;

        Node(K key, V value)
        {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashTable(int capacity)
    {
        this.capacity = capacity;
        this.buckets = new ArrayList<>(capacity);
        for(int i = 0; i<capacity; i++)
        {
            buckets.add(new LinkedList<>());
        }
        this.size = 0;
    }

    public MyHashTable()
    {
        this(10);
    }

    public int size()
    {
        return size;
    }

    public boolean isEmpty()
    {
        if(size == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Enumeration<K> keys()
    {
        throw new UnsupportedOperationException("Method not implemented");
    }

    public Enumeration<V> elements()
    {
        throw new UnsupportedOperationException("Method not implemented");
    }

    public V get(Object key)
    {
        int index = getIndex(key);
        LinkedList<Node<K,V>> bucket = buckets.get(index);
        for(Node<K,V> node : bucket)
        {
            if(node.key.equals(key))
            {
                return node.value;
            }
        }
        return null;

    }

    public V put(K key, V value)
    {
        int index = getIndex(key);
        LinkedList<Node<K,V>> bucket = buckets.get(index);

        for(Node<K,V> node : bucket)
        {
            if(node.key.equals(key))
            {
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }
        bucket.add(new Node<>(key,value));
        size++;
        return null;
    }

    public V remove(Object key)
    {
        int index = getIndex(key);
        LinkedList<Node<K, V>> bucket = buckets.get(index);

        Iterator<Node<K,V>> iterator = bucket.iterator();
        while(iterator.hasNext())
        {
            Node<K,V> node = iterator.next();
            if(node.key.equals(key))
            {
                V value = node.value;
                iterator.remove();
                size--;
                return value;
            }
        }
        return null;

    }

    private int getIndex(Object key)
    {
        return Math.abs(key.hashCode()) % capacity;
    }

    public boolean containsKey(Object key)
    {
        int index = getIndex(key);
        LinkedList<Node<K,V>> bucket = buckets.get(index);

        for(Node<K,V> node : bucket)
        {
            if(node.key.equals(key))
            {
                return true;
            }
        }
        return false;
    }

    public Iterator<K> keyIterator()
    {
        return new Iterator<K>()
        {
            private int currentBucket = 0;
            private Iterator<Node<K, V>> bucketIterator = buckets.get(currentBucket).iterator();


            public boolean hasNext()
            {
                if (bucketIterator.hasNext())
                {
                    return true;
                }
                for (int i = currentBucket + 1; i < buckets.size(); i++)
                {
                    if (!buckets.get(i).isEmpty())
                    {
                        return true;
                    }
                }
                return false;
            }


            public K next()
            {
                while (currentBucket < buckets.size())
                {
                    if (bucketIterator.hasNext())
                    {
                        return bucketIterator.next().key;
                    } else
                    {
                        currentBucket++;
                        if (currentBucket < buckets.size())
                        {
                            bucketIterator = buckets.get(currentBucket).iterator();
                        }
                    }
                }
                throw new NoSuchElementException();
            }
        };
    }

    public Iterator<V> valueIterator()
    {
        return new Iterator<V>()
        {
            private int currentBucket = 0;
            private Iterator<Node<K, V>> bucketIterator = buckets.get(currentBucket).iterator();


            public boolean hasNext()
            {
                if (bucketIterator.hasNext())
                {
                    return true;
                }
                for (int i = currentBucket + 1; i < buckets.size(); i++)
                {
                    if (!buckets.get(i).isEmpty())
                    {
                        return true;
                    }
                }
                return false;
            }


            public V next() {
                while (currentBucket < buckets.size())
                {
                    if (bucketIterator.hasNext())
                    {
                        return bucketIterator.next().value;
                    } else
                    {
                        currentBucket++;
                        if (currentBucket < buckets.size())
                        {
                            bucketIterator = buckets.get(currentBucket).iterator();
                        }
                    }
                }
                throw new NoSuchElementException();
            }
        };
    }

    public void clear()
    {
        for(int i=0; i< capacity; i++)
        {
            buckets.get(i).clear();
        }
        size = 0;
    }


}
