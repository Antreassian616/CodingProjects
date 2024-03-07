import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
/**
 * This class will
 * * <p>
 * *     TwoDTree has two inner classes, TwoDTreeNodeX and TwoDTreeNodeY. They make decisions based on x and y values respectively.
 * *     They both have methods to insert, search, compare, and search over a range using, compare(), insert(), search(), and searchRange().
 * *     TwoDTree has a default and parameterized constructor that inserts an ArrayList of points to the TwoDTree as well as methods to call
 * *     the insert(), search(), and searchRange() methods
 * * </p>
 * * <p>
 * *     The TwoDTree class will make a TwoDTree that stores points.
 * *     The points branch on the x or y values depending on the node.
 * *     Y nodes branch based on y values and have x node children while x nodes do the opposite.
 * *     This class will allow you to insert points into the tree, search for specific points and search for a range of points.
 * </p>
 * @author Aaron Antreassian
 * @edu.uwp.cs.340.course CSCI 340 - Data Structures/Algorithm Design
 * @edu.uwp.cs.340.Section 001
 * @edu.uwp.cs.340.assignment 4
 * @bugs none
 */
public class TwoDTree {
    private class TwoDTreeNodeX implements Comparator<Point> {
        private Point xPoint;

        private TwoDTreeNodeY leftChild, rightChild;

        public TwoDTreeNodeX(Point x) {
            this.xPoint = x;
        }

        /**
         * Comparing x coordinate.
         * @param p1 the first object to be compared.
         * @param p2 the second object to be compared.
         * @return int signifying comparison
         */
        public int compare(Point p1, Point p2) {
            if(p1.x > p2.x)
                return 1;
            else if(p1.x < p2.x)
                return -1;
            else
                return 0;
        }

        /**
         * inserts the point into the 2D Tree
         * @param point
         * @returns null
         */
        public void insert(Point point) {
            if(this.compare(this.xPoint, point) > 0) {
                if(this.leftChild != null) {
                    this.leftChild.insert(point);
                } else {
                    this.leftChild = new TwoDTreeNodeY(point);
                }
            } else {
                if(this.rightChild != null) {
                    this.rightChild.insert(point);
                } else {
                    this.rightChild = new TwoDTreeNodeY(point);
                }
            }
        }

        /**
         * Searches for a given point in the 2D Tree
         * @param point
         * @returns boolean on if the point is in the tree or not
         */
        public boolean search(Point point) {
            if(compare(this.xPoint, point) == 0) {
                return this.xPoint.y == point.y;
            } else if(compare(this.xPoint, point) > 0) {
                if(this.leftChild != null) {
                    return this.leftChild.search(point);
                } else {
                    return false;
                }
            } else if(compare(this.xPoint, point) < 0) {
                if(this.rightChild != null) {
                    return this.rightChild.search(point);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        /**
         * Searches the tree to see if any given point is in a given range.
         * Makes decisions based on the x value.
         * of values
         * @param lowerLeft
         * @param upperRight
         * @param results
         * @returns array list of the points in the range
         */
        private ArrayList<Point> searchRangeX(Point lowerLeft, Point upperRight, ArrayList<Point> results) {
            //check to see if current node is inside the range
            if(xPoint.x >= lowerLeft.x && xPoint.x <= upperRight.x && xPoint.y >= lowerLeft.y && xPoint.y <= upperRight.y)
                results.add(xPoint);

            //if current node is greater than the lower bound, searchRange but on the left child
            if(compare(xPoint, lowerLeft) >= 0 && leftChild != null) //compare(xPoint, lowerLeft) >= 0 &&
                leftChild.searchRangeY(lowerLeft, upperRight, results);

            //if current node is less than the upper bound, searchRange but on the right child
            if(compare(xPoint, upperRight) <= 0 && rightChild != null) //compare(xPoint, upperRight) <= 0 &&
                rightChild.searchRangeY(lowerLeft, upperRight, results);
            return results;
        }


    }

    private class TwoDTreeNodeY implements Comparator<Point> {

        private Point yPoint; //point info of the current node

        private TwoDTreeNodeX leftChild, rightChild; //children of the node

        public TwoDTreeNodeY(Point y) {
            this.yPoint = y;
        }
        /**
         * Comparing y coordinate.
         * @param p1 the first object to be compared.
         * @param p2 the second object to be compared.
         * @return int signifying comparison
         */
        public int compare(Point p1, Point p2) {
            if(p1.y > p2.y)
                return 1;
            else if(p1.y < p2.y)
                return -1;
            else
                return 0;
        }
        /**
         * inserts the point into the 2D Tree
         * @param point
         * @returns null
         */
        public void insert(Point point) {
            if(this.compare(this.yPoint, point) > 0) {
                if(this.leftChild != null) {
                    this.leftChild.insert(point);
                } else {
                    this.leftChild = new TwoDTreeNodeX(point);
                }
            } else {
                if(this.rightChild != null) {
                    this.rightChild.insert(point);
                } else {
                    this.rightChild = new TwoDTreeNodeX(point);
                }
            }
        }
        /**
         * Searches for a given point in the 2D Tree
         * @param point
         * @returns boolean on if the point is in the tree or not
         */
        public boolean search(Point point) {
            if(compare(this.yPoint, point) == 0) {
                return this.yPoint.x == point.x;
            } else if(compare(this.yPoint, point) > 0) {
                if(this.leftChild != null) {
                    return this.leftChild.search(point);
                } else {
                    return false;
                }
            } else if(compare(this.yPoint, point) < 0) {
                if(this.rightChild != null) {
                    return this.rightChild.search(point);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        /**
         * Searches the tree to see if any given point is in a given range.
         * Makes decisions based on the y value.
         * of values
         * @param lowerLeft
         * @param upperRight
         * @param results
         * @returns array list of the points in the range
         */
        private ArrayList<Point> searchRangeY(Point lowerLeft, Point upperRight, ArrayList<Point> results) {
            if(yPoint.x >= lowerLeft.x && yPoint.x <= upperRight.x && yPoint.y >= lowerLeft.y && yPoint.y <= upperRight.y)
                results.add(yPoint);

            if(compare(yPoint, lowerLeft) >= 0 && leftChild != null)
                leftChild.searchRangeX(lowerLeft, upperRight, results);
            if(compare(yPoint, upperRight) <= 0 && rightChild != null)
                rightChild.searchRangeX(lowerLeft, upperRight, results);
            return results;
        }
    }

    private TwoDTreeNodeY root; //since start of tree is based on y, root starts as a y node

    public TwoDTree() {}

    /**
     * This Constructor that inserts an array of points into the tree
     * @param points
     */
    public TwoDTree(ArrayList<Point> points) {
        for(Point p: points) {
            insert(p);
        }
    }
    /**
     * inserts the point into the 2D Tree
     * @param p
     * @returns null
     */
    public void insert(Point p) {
        if(root == null)
            root = new TwoDTreeNodeY(p);
        else
            root.insert(p);
    }
    /**
     * Searches for a given point in the 2D Tree
     * @param p
     * @returns boolean on if the point is in the tree or not
     */
    public boolean search(Point p) {
        if(root == null)
            return false;
        else
            return root.search(p);
        }
    /**
     * Searches the tree to see if any given point is in a given range.
     * Makes decisions based on the y value since the root is y.
     * of values
     * @param p1
     * @param p2
     * @returns array list of the points in the range
     */
    public ArrayList<Point> searchRange (Point p1, Point p2) {
        ArrayList<Point> results = new ArrayList<>();
        Point lowerLeft = new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
        Point upperRight = new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
        root.searchRangeY(lowerLeft, upperRight, results);
        return results;
    }


}



