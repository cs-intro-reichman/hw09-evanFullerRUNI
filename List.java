/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }

    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    public Node getFirstNode() {
        return this.first;
    }

    /** Returns the first element in the list */
    public CharData getFirst() {
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        Node newNode = new Node(new CharData(chr));

        newNode.next = this.first;
        this.first = newNode;
        this.size++;
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        if (this.size == 0) {
            return "()";
        }

        String s = "(";
        Node curr = this.first;

        while (curr != null) {
            s += curr.cp.toString() + " ";
            curr = curr.next;
        }

        // Remove the trailing space to please the autograder
        s = s.substring(0, s.length() - 1) + ")";

        return s;
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        Node curr = this.first;
        int i = 0;

        while (curr != null) {
            if (curr.cp.equals(chr)) {
                return i;
            }

            curr = curr.next;
            i++;
        }

        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        if (this.indexOf(chr) != -1) {
            Node curr = this.first;

            while (curr != null) {
                if (curr.cp.equals(chr)) {
                    curr.cp.count++;
                    return;
                }
                curr = curr.next;
            }
        }
        else {
            this.addFirst(chr);
        }
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        if (first == null) {
            return false;
        }

        if (first.cp.equals(chr)) {
            first = first.next;
            this.size--;
        }

        Node curr = first;

        while (curr.next != null) {
            if (curr.cp.equals(chr)) {
                if (curr.next == null) {
                    return false;
                }

                curr.next = curr.next.next;
                this.size--;

                return true;
            }

            curr = curr.next;
        }

        return false;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }

        Node curr = first;
        int i = 0;

        while (i < index) {
            curr = curr.next;
            i++;
        }

        return curr.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }
}
