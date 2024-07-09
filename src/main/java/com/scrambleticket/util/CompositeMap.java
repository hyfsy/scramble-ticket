
package com.scrambleticket.util;

import java.lang.reflect.Array;
import java.util.*;

public class CompositeMap<K, V> implements Map<K, V> {

    List<Map<K, V>> maps;

    public CompositeMap(List<Map<K, V>> maps) {
        this.maps = maps;
    }

    @Override
    public int size() {
        int size = 0;
        for (Map<K, V> map : maps) {
            size += map.size();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        for (Map<K, V> map : maps) {
            if (!map.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        for (Map<K, V> map : maps) {
            if (map.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Map<K, V> map : maps) {
            if (map.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        for (Map<K, V> map : maps) {
            V v = map.get(key);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException("Composite");
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("Composite");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Composite");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Composite");
    }

    @Override
    public Set<K> keySet() {
        List<Collection<K>> values = new ArrayList<>();
        for (Map<K, V> map : maps) {
            values.add(map.keySet());
        }
        return new CompositeSet<>(values);
    }

    @Override
    public Collection<V> values() {
        List<Collection<V>> values = new ArrayList<>();
        for (Map<K, V> map : maps) {
            values.add(map.values());
        }
        return new CompositeSet<>(values);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        List<Collection<Entry<K, V>>> values = new ArrayList<>();
        for (Map<K, V> map : maps) {
            values.add(map.entrySet());
        }
        return new CompositeSet<>(values);
    }

    public static class CompositeSet<E> implements Set<E> {

        List<Collection<E>> sets;

        public CompositeSet(List<Collection<E>> sets) {
            this.sets = sets;
        }

        @Override
        public int size() {
            int size = 0;
            for (Collection<E> c : sets) {
                size += c.size();
            }
            return size;
        }

        @Override
        public boolean isEmpty() {
            for (Collection<E> c : sets) {
                if (!c.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean contains(Object o) {
            for (Collection<E> c : sets) {
                if (c.contains(o)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Iterator<E> iterator() {
            List<Iterator<E>> iterators = new ArrayList<>();
            for (Collection<E> c : sets) {
                iterators.add(c.iterator());
            }
            return new CompositeIterator<>(iterators);
        }

        @Override
        public Object[] toArray() {
            Object[] result = new Object[this.size()];
            int i = 0;
            for (Iterator<E> it = this.iterator(); it.hasNext(); ++i) {
                result[i] = it.next();
            }
            return result;
        }

        @Override
        public <T> T[] toArray(T[] array) {
            int size = this.size();
            T[] result = null;
            if (array.length >= size) {
                result = array;
            } else {
                result = (T[])Array.newInstance(array.getClass().getComponentType(), size);
            }

            int offset = 0;

            for (int i = 0; i < this.sets.size(); ++i) {
                for (Iterator<E> it = this.sets.get(i).iterator(); it.hasNext(); result[offset++] = (T)it.next()) {
                }
            }

            if (result.length > size) {
                result[size] = null;
            }

            return result;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            throw new UnsupportedOperationException("Composite");
        }

        @Override
        public boolean add(E e) {
            throw new UnsupportedOperationException("Composite");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("Composite");
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException("Composite");
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException("Composite");
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException("Composite");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Composite");
        }
    }

    public static class CompositeIterator<E> implements Iterator<E> {

        private List<Iterator<E>> iterators;

        public CompositeIterator(List<Iterator<E>> iterators) {
            this.iterators = iterators;
        }

        @Override
        public boolean hasNext() {
            for (Iterator<E> iterator : iterators) {
                if (iterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public E next() {
            for (Iterator<E> iterator : iterators) {
                if (iterator.hasNext()) {
                    return iterator.next();
                }
            }
            throw new NoSuchElementException();
        }
    }
}
