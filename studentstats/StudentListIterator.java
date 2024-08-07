package studentstats;

import itertools.DoubleEndedIterator;

import studentapi.*;

import java.util.NoSuchElementException;

/**
 * A {@link DoubleEndedIterator} over student records pulled from the student API.
 *
 * <p>This does not load the whole student list immediately, but rather
 * queries the API ({@link StudentList#getPage}) only as needed.
 */
public class StudentListIterator implements DoubleEndedIterator<Student> {
    private int retry;
    private StudentList slist;
    private int pgSize;

    // variables to track position of the next/reverseNext element
    private int frontPg, frontCount;
    private int backPg, backCount;

    // arrays to store the back and front pages
    private Student[] frontArr, backArr;

    // booleans to track if a new page is needed for the next/reverseNext
    // element
    private boolean needFPg = true;
    private boolean needBPg = true;

    /**
     * Construct an iterator over the given {@link StudentList} with the
     * given retry quota.
     *
     * @param list The student API interface.
     * @param retries The number of times to retry a query
     * ({@link StudentList#getPage})
     * after getting {@link QueryTimedOutException} before declaring the
     * API unreachable and throwing an {@link ApiUnreachableException}.
     */
    public StudentListIterator(StudentList list, int retries) {
        this.slist = list;
        this.retry = retries;

        int nStudents = list.getNumStudents();
        int nPgs = list.getNumPages();
        pgSize = list.getPageSize();

        // pagesize of the last page is needed to initialize backCount
        int lastPgSize;
        if(nStudents % pgSize == 0) {
            lastPgSize = pgSize;
        } else {
            lastPgSize = nStudents % pgSize;
        }

        frontPg = 0;
        frontCount = 0;
        backPg = nPgs - 1;
        backCount = lastPgSize - 1;
    }

    /**
     * Construct an iterator over the given {@link StudentList} with a retry
     * quota of 3.
     *
     * @param the student API interface.
     */
    public StudentListIterator(StudentList list) {
        this(list, 3);
    }

    @Override
    public boolean hasNext() {
        if(frontPg == backPg && frontCount > backCount) return false;
        if(frontPg > backPg) return false;
        return true;
    }

    @Override
    public Student next() {
        if(!hasNext()) throw new NoSuchElementException();

        if(needFPg) {
            //check if the needed page is in backArr
            if(frontPg == backPg && !needBPg) {
                frontArr = backArr;
                needFPg = false;
            } else {
                boolean success = false;
                for(int i=0; i<retry; i++) {
                    try {
                        frontArr = slist.getPage(frontPg);
                        success = true;
                        needFPg = false;
                        break;
                    } catch(QueryTimedOutException e) {
                        continue;
                    }
                }
                if(!success) throw new ApiUnreachableException();
            }
        }

        Student result = frontArr[frontCount];

        // update the position of front element before returning the result
        if(frontCount == pgSize-1) {
            needFPg = true;
            frontPg++;
            frontCount = 0;
        } else {
            frontCount++;
        }

        return result;
    }

    @Override
    public Student reverseNext() {
        if(!hasNext()) throw new NoSuchElementException();

        if(needBPg) {
            // check if the needed page is in frontArr
            if(backPg == frontPg && !needFPg) {
                backArr = frontArr;
                needBPg = false;
            } else {
                boolean success = false;
                for(int i=0; i<retry; i++) {
                    try {
                        backArr = slist.getPage(backPg);
                        success = true;
                        needBPg = false;
                        break;
                    } catch(QueryTimedOutException e) {
                        continue;
                    }
                }
                if(!success) throw new ApiUnreachableException();
            }
        }

        Student result = backArr[backCount];

        // update the position of back element before returning the result
        if(backCount == 0) {
            needBPg = true;
            backPg--;
            backCount = pgSize-1;
        } else {
            backCount--;
        }

        return result;
    }
}
