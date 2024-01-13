import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * AVL Tree class
 * It also contains the Node class
 * The Node class is used to create the nodes of the tree
 * The AVL Tree class contains the methods to insert, delete, find the common ancestor, find the route, find the node, find the max independent members, find the rank, and monitor the rank
 */
public class AVLTree {

    public class Node{
        double GMS;
        String name;
        int height;
        Node left, right, parent;
        Node(double d, String name){
            this.name = name;
            GMS = d;
            height = 1;
        }
    }

    Node root;

    // A utility function to get the height of the tree
    int getHeight(Node N) {
        if (N == null)
            return 0;

        return N.height;
    }

    // A utility function to get maximum of two integers
    int max(int a, int b) {
        return Math.max(a, b);
    }

    // A utility function to right rotate subtree rooted with y
    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;

        // Return new root
        return x;
    }

    // A utility function to left rotate subtree rooted with x
    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        //  Update heights
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1;

        // Return new root
        return y;
    }

    // Get Balance factor of node N
    int getBalance(Node N) {
        if (N == null)
            return 0;

        return getHeight(N.left) - getHeight(N.right);
    }

    /**
     * method to insert a node
     * @param node the node to be inserted
     * @param key gms of the node
     * @param name name of the node
     * @return the node to be inserted
     */
    public AVLTree.Node insert(Node node, double key, String name) {

        if (node == null)
            return (new Node(key, name));

        if (key < node.GMS)
            node.left = insert(node.left, key, name);
        else if (key > node.GMS)
            node.right = insert(node.right, key, name);
        else // Duplicate keys not allowed
            return node;

        /* 2. Update height of this ancestor node */
        node.height = 1 + max(getHeight(node.left),
                getHeight(node.right));

        /* 3. Get the balance factor of this ancestor
              node to check whether this node became
              unbalanced */
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && key < node.left.GMS)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && key > node.right.GMS)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && key > node.left.GMS) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key < node.right.GMS) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        /* return the (unchanged) node pointer */
        return node;
    }

    /**
     * method to find the node with minimum value
     * @param node node to be checked
     * @return
     */
    Node minMember(Node node)
    {
        Node current = node;

        /* loop down to find the leftmost leaf */
        while (current.left != null)
            current = current.left;

        return current;
    }

    /**
     * method to delete a node
     * @param r, the root of the tree
     * @param key, the key of the node to be deleted
     * @param name, the name of the node to be deleted
     * AFTER EACH DELETION, WE WANT TO PRINT THE REPLACING MEMBER AND THE MEMBER WHO LEFT
     * EXAMPLE: "A left the family, replaced by B"
     */

    public AVLTree.Node delete(AVLTree.Node r, double key, String name, int checker, FileWriter output) throws IOException {

        if (r == null)
            return r;

        // If the key to be deleted is smaller than the root's key, then it lies in left subtree
        if (key < r.GMS)
            r.left = delete(r.left, key, name, checker, output);

        // If the key to be deleted is greater than the root's key, then it lies in right subtree
        else if (key > r.GMS)
            r.right = delete(r.right, key, name, checker, output);

        // if key is same as root's key, then this is the node to be deleted
        else {

           // node with only one child or no child
            if ((r.left == null) || (r.right == null)) {
                AVLTree.Node temp = null;
                if (temp == r.left)
                    temp = r.right;
                else
                    temp = r.left;

                // No child case
                if (temp == null) {
                    if (checker == 1){
                        output.write(name + " left the family, replaced by " + "nobody" + "\n");
                        output.flush();
                    }
                    ///
                    r = null;
                }
                else{ // One child case
                    if (checker == 1){
                        r = temp;
                        output.write(name + " left the family, replaced by " + temp.name + "\n");
                        output.flush();
                        checker = 0;
                    }
                }
            }
            else {
                // node with two children: Get the inorder successor (smallest in the right subtree)
                AVLTree.Node temp = minMember(r.right);

                // Copy the inorder successor's data to this node
                output.write(name + " left the family, replaced by " + temp.name + "\n");
                output.flush();
                // update the checker to 0
                checker = 0;
                r.name = temp.name;
                r.GMS = temp.GMS;

                // Delete the inorder successor
                r.right = delete(r.right, temp.GMS, temp.name, checker, output);
            }
        }

        // If the tree had only one node then return
        if (r == null)
            return r;

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        r.height = max(getHeight(r.left), getHeight(r.right)) + 1;

        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        // this node became unbalanced)
        int balance = getBalance(r);

        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && getBalance(r.left) >= 0){
            return rightRotate(r);
        }

        // Left Right Case
        if (balance > 1 && getBalance(r.left) < 0)
        {
            r.left = leftRotate(r.left);
            return rightRotate(r);
        }

        // Right Right Case
        if (balance < -1 && getBalance(r.right) <= 0){
            Node a = leftRotate(r);
            return a;
        }

        // Right Left Case
        if (balance < -1 && getBalance(r.right) > 0) {
            r.right = rightRotate(r.right);
            Node a = leftRotate(r);
            return a;
        }
        return r;
    }

    /**
     * method to find the common ancestor of two nodes
     * @param root the root of the tree
     * @param gms1 the gms of the first node
     * @param gms2 the gms of the second node
     * @return the common ancestor of the two nodes which has the lowest rank
     */
    public Node commonAncestor(Node root, double gms1, double gms2){
        if (root == null)
            return null;
        if (gms1 > root.GMS && gms2 > root.GMS) { return commonAncestor(root.right, gms1, gms2); }
        else if (gms1 < root.GMS && gms2 < root.GMS) { return commonAncestor(root.left, gms1, gms2); }
        else { return root; }
    }

    public String findRoute(AVLTree.Node root, double gms) {
        if (root == null || root.GMS == gms)
            return "";
        else if (gms > root.GMS)
            return "r" + findRoute(root.right, gms);
        else
            return "l" + findRoute(root.left, gms);
    }

    // a method to find the node when the route starting from the root is given
    // the route is a string of l and r, l means left, r means right
    public Node findNode(Node root, String route){
        if (route.length() == 0){
            return root;
        }
        else if (route.charAt(0) == 'l'){
            return findNode(root.left, route.substring(1));
        }
        else{
            return findNode(root.right, route.substring(1));
        }
    }

    /**
     * method to find the max number of independent members in the AVL tree
     * @param root the root of the tree
     * @param c array containing the max number of independent members, initially 0, to be incremented if the node is independent
     * @return true if the node is independent, false if not
     */
    public boolean maxIndependent(Node root, int[] c) {  /////////FLUSH

        if (root == null) {return false;}

        else if (root.right == null && root.left == null){
            c[0]++;
            return true;
        }
        else {
            boolean a = maxIndependent(root.right, c);
            boolean b = maxIndependent(root.left, c);
            if (!a && !b) {
                c[0]++;
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * method to find the rank of a node
     * @param root the root of the tree
     * @param gms the gms of the node
     * @return the rank of the node
     */
     public int getRank(Node root, double gms){

         if (gms > root.GMS)
             return 1 + getRank(root.right, gms);
         else if (gms < root.GMS)
             return 1 + getRank(root.left, gms);
         else
             return 0;
     }

    /**
     * method to write the nodes having the same rank
     * @param root the root of the tree
     * @param rank the rank of the node
     * @param currentRank the current rank of the node
     * @param output the output file
     */
    public void monitoreRank(Node root, int rank, int currentRank, FileWriter output) throws IOException {
        if (root == null)
            return;
        if (currentRank == rank) {
            String a = String.format(Locale.US, "%.3f", root.GMS);
            output.write(root.name + " " + a + " ");
            output.flush();
        } else if (currentRank < rank) {
            monitoreRank(root.left, rank, currentRank+1, output);
            monitoreRank(root.right, rank, currentRank+1, output);
        }
    }
}