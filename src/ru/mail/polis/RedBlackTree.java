package ru.mail.polis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

//TODO: write code here
public class RedBlackTree<E extends Comparable<E>> implements ISortedSet<E> {
    class Node {

        Node(E value, Node parent) {
            height = 1;
            this.value = value;
            isBlack = true;
            this.parent = parent;
        }

        E value;
        Node left;
        Node right;
        Node parent;
        int height;
        boolean isBlack;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("N{");
            sb.append("d=").append(value);
            if (left != null) {
                sb.append(", l=").append(left);
            }
            if (right != null) {
                sb.append(", r=").append(right);
            }
            sb.append('}');
            return sb.toString();
        }
    }

    private int size;
    private Node root;
    private Node nil;
    private final Comparator<E> comparator;

    public RedBlackTree() {
        this.comparator = null;
        nil = new Node(null, null);
        root = nil;
    }

    public RedBlackTree(Comparator<E> comparator) {
        this.comparator = comparator;
        nil = new Node(null, null);
        root = nil;
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no first element");
        }
        Node node = root;
        while (node.left.value != null)
            node = node.left;
        return node.value;
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no last element");
        }
        Node node = root;
        while (node.right.value != null)
            node = node.right;
        return node.value;
    }

    @Override
    public List<E> inorderTraverse() {
        List<E> list = new ArrayList<E>(size);
        inorderTraverse(root, list);
        return list;
    }

    private void inorderTraverse(Node curr, List<E> list) {
        if (curr.value == null) {
            return;
        }
        inorderTraverse(curr.left, list);
        list.add(curr.value);
        inorderTraverse(curr.right, list);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root.value == null;
    }

    @Override
    public boolean contains(E value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        Node node = root;
        while (node.value != null) {
            int cmp = compare(node.value, value);
            if (cmp == 0) {
                return true;
            } else {
                node = cmp > 0 ? node.left : node.right;
            }
        }
        return false;
    }

    @Override
    public boolean add(E value) {
        return false;
    }

    @Override
    public boolean remove(E value) {
        return false;
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }
}
