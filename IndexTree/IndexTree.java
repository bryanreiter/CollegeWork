import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Your class. Notice how it has no generics.
// This is because we use generics when we have no idea what kind of data we are getting
// Here we know we are getting two pieces of data:  a string and a line number
public class IndexTree {

    // This is your root
    // again, your root does not use generics because you know your nodes
    // hold strings, an int, and a list of integers
    private IndexNode root;

    // Make your constructor
    // It doesn't need to do anything
    public IndexTree(){

    }


    // complete the methods below

    // this is your wrapper method
    // it takes in two pieces of data rather than one
    // call your recursive add method
    public void add(String word, int lineNumber){
        this.root=add(this.root,word,lineNumber);
    }



    // your recursive method for add
    // Think about how this is slightly different the regular add method
    // When you add the word to the index, if it already exists,
    // you want to  add it to the IndexNode that already exists
    // otherwise make a new indexNode
    private IndexNode add(IndexNode root, String word, int lineNumber){
        if(root==null){
            return new IndexNode(word, lineNumber);

        }
        int comparison = root.word.compareTo(word);
        if(comparison==0){
            //get line num and add to list.
            root.list.add(lineNumber);
            root.occurences++;
            return root;
        }else if(comparison<0) {
            root.left=add(root.left, word, lineNumber);
            return root;
        }else{
            root.right=add(root.right, word, lineNumber);
            return root;
        }

    }




    // returns true if the word is in the index
    public boolean contains(IndexNode root, String word){
        if(root==null){
            return false;
        }
        int comparison=root.word.compareTo(root.word);
        if(comparison==0){
            return true;
        }else if(comparison<0){
            return contains(root.left, word);
        }else{
            return contains(root.right, word);
        }
    }

    // call your recursive method
    // use book as guide
    public void delete(String word) {
        this.root=delete(this.root, word);
    }

    // your recursive case
    // remove the word and all the entries for the word
    // This should be no different from the regular technique.
    private IndexNode delete(IndexNode root, String word){
        if(root==null){
            return null;
        }
        int comparison=word.compareTo(root.word);
        if (comparison<0){
            root.left=delete(root.left, word);
            return root;
        }else if(comparison>0){
            root.right=delete(root.right, word);
            return root;
        }else{
            if(root.left==null&&root.right==null){
                return null;
            }else if(root.left!=null&&root.right==null){
                return root.left;
            }else if(root.left==null&&root.right!=null){
                return root.right;
            }else{
                IndexNode current = root.left;
                while(current.right!=null){
                    current=current.right;
                }
                root.word = current.word;
                root.left=delete(root.left,root.word);
                return root;
            }
        }
    }


    // prints all the words in the index in inorder order
    // To successfully print it out
    // this should print out each word followed by the number of occurrences and the list of all occurrences
    // each word and its data gets its own line
    public static void printIndex(IndexNode root){
    if(root==null){
        return;
    }
        printIndex(root.left);
        System.out.println(root);
        printIndex(root.right);
    }

    public static void main(String[] args){
        IndexTree index = new IndexTree();
        String fileName = "pg100.txt";
        int count=0;
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while(scanner.hasNextLine()){
                count++;
                String line = scanner.nextLine();
                String[] words = line.split("\\s+");
                for(String word : words){
                    word = word.toLowerCase();
                    word = word.replaceAll("[^\\w\\s']", "");

                    index.add(word,count);
                }
            }
            scanner.close();
            printIndex(index.root);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
