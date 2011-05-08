/*
 * File: InternalNode.java
 * Description: An internal node in the BTree
 * Author: Benjamin David Mayes <bdm8233@rit.edu>
 */

import java.util.Arrays;

/**
 * A node that is internal to the BTree
 */
class InternalNode<K extends Comparable, V> extends Node<K,V> {
    private Node<K,V>[] children;

    /**
     * Constructs an InternalNode with K as the key and parent as the parent.
     *
     * @param key The initial key in this node.
     * @param parent The parent of this node.
     */
	@SuppressWarnings({"unchecked"})
    public InternalNode( K key ) {
        super(key);
        children = new Node[numKeysPerNode+1];
    }

    public InternalNode( K[] keys, Node<K,V>[] children, Node<K,V> parent, Node<K,V> next ) {
        super( keys, parent );
        children = Arrays.copyOf( children, numKeysPerNode + 1 );
        this.next = next;
    }

    /**
     * Get a child node of this node.
     *
     * @param key The key to get the child of.
     * @return A Node in a Union.
     */
	@SuppressWarnings({"unchecked"})
    public Union.Left<Node<K,V>,V> getChild( K key ) {
		// Linear search, get the K'th child
		int sentry = 0;
		while( sentry < keys.length && keys[sentry].compareTo(key) < 0 )
		{
			sentry++;
		}
		return new Union.Left<Node<K,V>,V>(children[sentry]);
    }

    /** {@inheritDoc} */
    public Union.Left<Node<K,V>,V> split()
    {
        InternalNode<K,V> newNode;
        newNode = new InternalNode<K,V>( 
                Arrays.copyOfRange( this.keys, (1+keys.length)/2, numKeysPerNode ),
                Arrays.copyOfRange( this.children, (1+children.length)/2, children.length ),
               this.parent,
               this.next );
        this.next = newNode;
       
        //TODO: Do we want resizing here?
        // If so uncomment the next line
        //this.numKeys = (1+keys.length)/2
        return new Union.Left<Node<K,V>,V>(newNode);
    }
}

