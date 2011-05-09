/*
 * Sequential B*-Tree implementation for the 
 * Concurrent Search Tree Project for
 * Parallel Computing I
 *
 * Author: David C. Larsen <dcl9934@cs.rit.edu>
 * Date: April. 12, 2011
 */

import java.util.Map;
import java.lang.reflect.Array;

public class BTreeSeq<K extends Comparable,V> implements BTree<K,V>
{
    private int size = 0;

    private Node<K,V> root = null;

    /** {@inheritDoc} */
    public void clear()
    {
        // We'll let the garbage collector worry about it.
        root = null;
    }

    /** {@inheritDoc} */
    public boolean containsKey( K key )
    {
        assert(false);
        return false;
    }

    /** {@inheritDoc} */
    public boolean containsValue( V value )
    {
        assert(false);
        return false;
    }

    /** {@inheritDoc} */
    public V get( K key )
    {
        Node<K,V> currentNode = root;

        while( currentNode instanceof InternalNode )
        {
            currentNode = currentNode.getChild(key).left();
        }
        if( currentNode instanceof LeafNode ) {
            return currentNode.getChild(key).right();
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    public boolean isEmpty()
    {
        return size == 0;
    }

    /** {@inheritDoc} */
    public V put( K key, V value )
    {
        // find the leaf node that would contain this value
        Node<K,V> currentNode = root;
        while( currentNode instanceof InternalNode ) {
            currentNode = currentNode.getChild(key).left();
        }

        if( currentNode != null ) {
            // Lets save the current node
            LeafNode<K,V> leaf = (LeafNode<K,V>)currentNode; 
                
            // can we fit the new value into this node?
            if( !leaf.addValue( key, value ) ) {
                // We have to split the node
                LeafNode<K,V> right = leaf.split().right();

                // add the value to the correct leaf
                if ( key.compareTo( right.lowerBound() ) >= 0 ) {
                    right.addValue( key, value );
                } else {
                    leaf.addValue( key, value );
                }
                 
                Node<K,V> newRight = right;
                // we need to add the new node to the parent node, we then need to repeat this process.
                InternalNode<K,V> parent = (InternalNode<K,V>)right.parent;
                // loop until we reach the root node or we are successfully able to add a child node
                while( parent != null && !parent.addChild(newRight.lowerBound(), newRight) ) {
                    // split the parent node
                    InternalNode<K,V> parentRight =  parent.split().left();

                    // add the pointer to the child to the correct parent
                    K newLB = newRight.lowerBound();
                    if( newLB.compareTo( parentRight.lowerBound() ) >= 0 ) {
                        parentRight.addChild( newLB, newRight );
                        newRight.parent = parentRight;
                    } else {
                        parent.addChild( newLB, newRight );
                    }

                    // update the parent and the right node
                    parent = (InternalNode<K,V>)parent.parent;
                    newRight = parentRight;
                }

                // The root has been split, we need to create a new root.
                if( parent == null ) {
                    Node<K,V> newRoot = new InternalNode<K,V>( root, newRight );
                    root.parent = newRoot;
                    newRight.parent = newRoot;
                    root = newRoot;
                } 
            }
        } else {    // There isn't a root node yet
            root = new LeafNode<K,V>( key, value );
        }

        // we need to return an old value here.
        // TODO: How do we get this?
        return null;
    }

    /** {@inheritDoc} */
    public V remove( K key )
    {
        assert(false);
        return null;
    }

    /** {@inheritDoc} */
    public int size()
    {
        return this.size;
    }
}
