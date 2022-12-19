package dash.internal.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A sorted arraylist using binary search.
 * Slow append but fast read. suitable for {@link dash.internal.event.DashEventBus}
 *
 * @param <E>
 */
@ApiStatus.Internal
public class SortedArrayList<E extends Comparable<E>> implements List<E> {
    private final List<E> list;

    public SortedArrayList() {
        list = new ArrayList<>();
    }

    public SortedArrayList(int capacity) {
        list = new ArrayList<>(capacity);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return list.toArray(a);
    }

    // layout: 0 1 2 3 4 5 6 .... 999
    @Override
    public boolean add(E object) {
        if (size() == 0) {
            return list.add(object);
        } else if (size() == 1) {
            if (list.get(0).compareTo(object) > 0) {
                list.add(0, object);
                return true;
            } else {
                list.add(object);
                return true;
            }
        }
        int l = 0;
        int r = list.size() - 1;
        while (l != r) {
            int mid = (l + r + 1) >> 1;
            if (get(mid).compareTo(object) > 0) r = mid - 1;
            else l = mid;
        }
        list.add(l + 1, object);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        return list.addAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public E set(int index, E element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        list.add(index, element);
    }

    @Override
    public E remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }

    @NotNull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}
