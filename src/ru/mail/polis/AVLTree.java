package ru.mail.polis;

import java.util.*;

//TODO: write code here
public class AVLTree<E extends Comparable<E>> implements ISortedSet<E> {
    class Node {

        Node(E value) {
            height = 1;
            this.value = value;
        }

        E value;
        Node left;
        Node right;
        int height;

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

    private Node root;
    private int size;
    private final Comparator<E> comparator;

    public AVLTree() {
        this.comparator = null;
    }

    public AVLTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no first element");
        }
        Node node = root;
        while (node.left != null)
            node = node.left;
        return node.value;
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no last element");
        }
        Node node = root;
        while (node.right != null)
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
        if (curr == null) {
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
        return root == null;
    }

    @Override
    public boolean contains(E value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        Node node = root;
        while (node != null) {
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
            root = new Node(value);
        } else {
            if (!add(root, null, value))
                return false;
        }
        size++;
        return true;
    }

    private boolean add(Node node, Node parent, E value) {
        boolean result = false;
        int cmp = compare(node.value, value);
        if (cmp == 0) {
            return false;
        } else {
            if (cmp > 0) {
                if (node.left != null) {
                    result = add(node.left, node, value);
                } else {
                    node.left = new Node(value);
                    result = true;
                }
            } else {
                if (node.right != null) {
                    result = add(node.right, node, value);
                } else {
                    node.right = new Node(value);
                    result = true;
                }
            }
            if (Math.abs(bFactor(node)) > 1) {
                Node n = balance(node);
                if (parent == null)
                    root = n;
                else if (compare(parent.value, n.value) > 0) {
                    parent.left = n;
                } else {
                    parent.right = n;
                }
            }
            setHeight(node);
        }
        return result;
    }

    private int bFactor(Node node) {
        int l = 0, r = 0;
        if (node.left != null) l = node.left.height;
        if (node.right != null) r = node.right.height;
        return l - r;
    }

    private Node balance(Node node) {
        int bf = bFactor(node);
        int l = 0, r = 0;
        if (bf > 1) {
            Node b = node.left;
            if (b.left != null) l = b.left.height;
            if (b.right != null) r = b.right.height;
            if (r <= l) {
                return rightRotation(node);
            } else {
                return bigRightRotation(node);
            }
        } else {
            Node b = node.right;
            if (b.left != null) l = b.left.height;
            if (b.right != null) r = b.right.height;
            if (l <= r) {
                return leftRotation(node);
            } else {
                return bigLeftRotation(node);
            }
        }
    }

    private Node leftRotation(Node node) {
        Node c = node.right;
        node.right = c.left;
        c.left = node;
        setHeight(node);
        setHeight(c);
        return c;
    }

    private Node bigLeftRotation(Node node) {
        node.right = rightRotation(node.right);
        return leftRotation(node);
    }

    private Node rightRotation(Node node) {
        Node c = node.left;
        node.left = c.right;
        c.right = node;
        setHeight(node);
        setHeight(c);
        return c;
    }

    private Node bigRightRotation(Node node) {
        node.left = leftRotation(node.left);
        return rightRotation(node);
    }

    private void setHeight(Node node) {
        if (node.left == null && node.right == null) {
            node.height = 1;
            return;
        }
        int r = 0, l = 0;
        if (node.left != null) l = node.left.height;
        if (node.right != null) r = node.right.height;
        node.height = Math.max(r, l) + 1;
    }

    @Override
    public boolean remove(E value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (isEmpty()) {
            return false;
        } else {
            if (!remove(root, null, value))
                return false;
        }
        size--;
        return true;
    }

    private boolean remove(Node node, Node parent, E value) {
        boolean result = false;
        int cmp = compare(node.value, value);
        if (cmp == 0) {
            result = true;
            Node n = remove(node);
            if (parent == null)
                root = n;
            else if (compare(parent.value, value) > 0) {
                parent.left = n;
            } else {
                parent.right = n;
            }
        } else {
            if (cmp > 0) {
                if (node.left != null) {
                    result = remove(node.left, node, value);
                } else {
                    return false;
                }
            } else {
                if (node.right != null) {
                    result = remove(node.right, node, value);
                } else {
                    return false;
                }
            }
            setHeight(node);
            if (Math.abs(bFactor(node)) > 1) {
                Node n = balance(node);
                if (parent == null)
                    root = n;
                else if (compare(parent.value, n.value) > 0) {
                    parent.left = n;
                } else {
                    parent.right = n;
                }
            }
        }
        return result;
    }

    private Node remove(Node node) {
        if (node.left == null && node.right == null) {
            return null;
        } else {
            Node a = node;
            Node parent = node;
            boolean leftChild = true;
            if (bFactor(node)>=0){
                a = a.left;
                leftChild = true;
                while (a.right!=null){
                    parent = a;
                    a = a.right;
                    leftChild = false;
                }
                swap(node, a);
            }else {
                a = a.right;
                leftChild = false;
                while (a.left!=null){
                    parent = a;
                    a = a.left;
                    leftChild = true;
                }
                swap(node, a);
            }
            Node n = remove(a);
            if (leftChild) {
                parent.left = n;
            } else {
                parent.right = n;
            }
            setHeight(node);
            setHeight(parent);
        }
        return node;
    }

    private void swap(Node node, Node a){
        E value = node.value;
        node.value = a.value;
        a.value = value;
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    private boolean check(Node node){
        boolean result = Math.abs(bFactor(node))<=1;
        if (node.left!=null)result &= check(node.left);
        if (node.right!=null) result &= check(node.right);
        if (result == false){
            System.out.println("Error ");
            System.out.println(node);
        }
        return  result;
    }


    @Override
    public String toString() {
        return "AVL{" + root + "}";
    }

    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        System.out.println(tree.inorderTraverse());
        System.out.println(tree.size);
        System.out.println(tree);
        tree.remove(10);
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
        tree = new AVLTree<>();
        //int[] a = {39, 23, 22, 29, 32, 48, 38, 30, 12, 22, 31, 12, 34, 0, 17};
        for (int i = 0; i < 20; i++) {
            tree.add(rnd.nextInt(20));
        }
        System.out.println(tree);
        System.out.println(true);
        for (int i = 0; i < 20; i++) {
            tree.remove(rnd.nextInt(20));
            System.out.println(tree.check(tree.root));
            System.out.println(tree);
        }
        System.out.println(tree.inorderTraverse());
        tree = new AVLTree<>((v1, v2) -> {
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
