package ru.mail.polis;

import java.util.*;

//TODO: write code here
public class RedBlackTree<E extends Comparable<E>> implements ISortedSet<E> {
    class Node {

        Node(E value, Node parent) {
            height = 1;
            this.value = value;
            isBlack = false;
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
            if (isBlack)
                sb.append("black ");
            else
                sb.append("red ");
            sb.append("d=").append(value);
            if (left.value != null) {
                sb.append(", l=").append(left);
            }
            if (right.value != null) {
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
        nil.isBlack = true;
        root = nil;
    }

    public RedBlackTree(Comparator<E> comparator) {
        this.comparator = comparator;
        nil = new Node(null, null);
        nil.isBlack = true;
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
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (isEmpty()) {
            root = newLeaf(value, nil);
            root.isBlack = true;
        } else {
            Node x = root;
            Node y = nil;
            while (x.value != null) {
                y = x;
                int cmp = compare(x.value, value);
                if (cmp == 0) {
                    return false;
                } else {
                    if (cmp > 0) {
                        x = x.left;
                    } else {
                        x = x.right;
                    }
                }
            }
            Node node = newLeaf(value, y);
            int cmp = compare(y.value, value);
            if (cmp > 0) {
                y.left = node;
            } else {
                y.right = node;
            }
            fixup(node);
        }
        size++;
        return true;
    }

    private void fixup(Node node) {
        while (!node.parent.isBlack) {
            if (node.parent.parent.left == node.parent) {
                Node y = node.parent.parent.right;
                if (!y.isBlack) {
                    node.parent.isBlack = true;
                    y.isBlack = true;
                    node.parent.parent.isBlack = false;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right){
                        node = node.parent;
                        leftRotate(node);
                    }
                    node.parent.isBlack = true;
                    node.parent.parent.isBlack = false;
                    rightRotate(node.parent.parent);
                }
            } else {
                Node y = node.parent.parent.left;
                if (!y.isBlack) {
                    node.parent.isBlack = true;
                    y.isBlack = true;
                    node.parent.parent.isBlack = false;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left){
                        node = node.parent;
                        rightRotate(node);
                    }
                    node.parent.isBlack = true;
                    node.parent.parent.isBlack = false;
                    leftRotate(node.parent.parent);
                }
            }                                           //!!!!!!!!!!!!!!!!!!!!!!!!!!!!DELETE
        }
        root.isBlack = true;
    }

    private void leftRotate(Node node) {
        Node y = node.right;
        node.right = y.left;
        if (y.left!=nil){
            y.left.parent = node;
        }
        y.parent = node.parent;
        if (node.parent == nil){
            root = y;
        } else if (node == node.parent.left){
            node.parent.left = y;
        } else {
            node.parent.right = y;
        }
        y.left = node;
        node.parent = y;
    }

    private void rightRotate(Node node) {
        Node y = node.left;
        node.left = y.right;
        if (y.right!=nil){
            y.right.parent = node;
        }
        y.parent = node.parent;
        if (node.parent == nil){
            root = y;
        } else if (node == node.parent.left){
            node.parent.left = y;
        } else {
            node.parent.right = y;
        }
        y.right = node;
        node.parent = y;
    }

    private Node newLeaf(E value, Node parent) {
        Node node = new Node(value, parent);
        node.left = nil;
        node.right = nil;
        return node;
    }

    @Override
    public boolean remove(E value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (isEmpty()){
            return false;
        } else {
            Node x = root;
            Node y = nil;
            while (x.value != null) {
                y = x;
                int cmp = compare(x.value, value);
                if (cmp == 0) {
                    Delete(x);
                    size--;
                    return true;
                } else {
                    if (cmp > 0) {
                        x = x.left;
                    } else {
                        x = x.right;
                    }
                }
            }
        }
        return false;
    }

    private void Delete(Node z){
        Node y = z;
        Node x;
        boolean yOriginalColor = y.isBlack;
        if (z.left == nil){
            x= z.right;
            transplant(z,z.right);
        } else if (z.right == nil){
            x= z.left;
            transplant(z,z.left);
        } else {
            y = treeMinimum(z.right);
            yOriginalColor = y.isBlack;
            x = y.right;
            if (y.parent==z){
                x.parent = y;
            }
            else {
                transplant(y,y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z,y);
            y.left = z.left;
            y.left.parent = y;
            y.isBlack = z.isBlack;
        }
        if (yOriginalColor){
            deleteFixup(x);
        }
    }

    private void deleteFixup(Node x) {
        while (x!=root && x.isBlack){
            if (x == x.parent.left){
                Node w = x.parent.right;
                if (!w.isBlack){
                    w.isBlack = true;
                    x.parent.isBlack = false;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (w.left.isBlack && w.right.isBlack){
                    w.isBlack = false;
                    x = x.parent;
                } else {
                    if (w.right.isBlack){
                        w.left.isBlack = true;
                        w.isBlack = false;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.isBlack = x.parent.isBlack;
                    x.parent.isBlack = true;
                    w.right.isBlack = true;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (!w.isBlack){
                    w.isBlack = true;
                    x.parent.isBlack = false;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.isBlack && w.left.isBlack){
                    w.isBlack = false;
                    x = x.parent;
                } else {
                    if (w.left.isBlack){
                        w.right.isBlack = true;
                        w.isBlack = false;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.isBlack = x.parent.isBlack;
                    x.parent.isBlack = true;
                    w.left.isBlack = true;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.isBlack = true;
    }

    private Node treeMinimum(Node node) {
        while (node.left!=nil){
            node = node.left;
        }
        return node;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == nil){
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    public String toString() {
        return "RBTree{" + root + "}";
    }

    public static void main(String[] args) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.add(3);
        tree.add(5);
        tree.add(7);
        tree.add(10);
        tree.add(15);
        tree.add(30);
        System.out.println(tree.inorderTraverse());
        System.out.println(tree.size);
        System.out.println(tree);
        tree.remove(10);
        System.out.println(tree.size);
        System.out.println(tree);
        tree.remove(15);
        System.out.println(tree.size);
        System.out.println(tree);
        tree.remove(5);
        System.out.println(tree.size);
        System.out.println(tree);
        tree.add(15);
        System.out.println(tree.size);
        System.out.println(tree);

        System.out.println("------------");
        Random rnd = new Random();
        tree = new RedBlackTree<>();
        for (int i = 0; i < 10; i++) {
            tree.add(rnd.nextInt(50));
        }
        for (int i = 0; i < 10; i++) {
            int k = rnd.nextInt(50);
            tree.add(k);
            System.out.println(k);
            System.out.println(tree);
        }
        System.out.println(tree.inorderTraverse());
        tree = new RedBlackTree<>((v1, v2) -> {
            // Even first
            final int c = Integer.compare(v1 % 2, v2 % 2);
            return c != 0 ? c : Integer.compare(v1, v2);
        });
        for (int i = 0; i < 15; i++) {
            tree.add(rnd.nextInt(50));
        }
        System.out.println(tree.inorderTraverse());
    }
}
