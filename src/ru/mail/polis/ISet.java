package ru.mail.polis;

/**
 * Created by Nechaev Mikhail
 * Since 13/12/16.
 */
public interface ISet<E extends Comparable<E>> {

    /**
     * Returns the number of elements in this set (its cardinality).  If this
     * set contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this set (its cardinality)
     */
    int size();

    /**
     * @return true if this set contains no elements
     */
    boolean isEmpty();

    /**
     * @param el element whose presence in this set is to be tested
     * @return true if this set contains the specified element
     * @throws NullPointerException if the specified element is null
     */
    boolean contains(E el);

    /**
     * @param el element to be added to this set
     * @return true if this set did not already contain the specified element
     * @throws NullPointerException if the specified element is null
     */
    boolean add(E el);

    /**
     * @param el object to be removed from this set, if present
     * @return true if this set contained the specified element
     * @throws NullPointerException if the specified element is null
     */
    boolean remove(E el);
}