package studentstats;

import java.util.NoSuchElementException;

import itertools.DoubleEndedIterator;

import studentapi.*;

/**
 * A (double ended) iterator over student records pulled from the student API.
 *
 * <p>This does not load the whole student list immediately, but rather queries the API ({@link StudentList#getPage}) only as needed.
 */
public class StudentListIterator implements DoubleEndedIterator<Student> {
    private int retry;
    private StudentList slist;
    private int nPgs, nStudents, pgSize, nLastPg;

    // variables to store position of the next/reverseNext element
    private int frontPg, frontCount;
    private int backPg, backCount;

    // arrays to store the current page list
    private Student[] frontArr, backArr;

    // variables to track if a new page is needed
    private boolean needFPg = true;
    private boolean needBPg = true;

    /**
     * Construct an iterator over the given {@link StudentList} with the specified retry quota.
     *
     * @param list The API interface.
     * @param retries The number of times to retry a query after getting {@link
     *     QueryTimedOutException} before declaring the API unreachable and throwing an {@link
     *     ApiUnreachableException}.
     */
    public StudentListIterator(StudentList list, int retries) {
        this.slist = list;
        this.retry= retries;
        nPgs = list.getNumPages();
        nStudents = list.getNumStudents();
        pgSize = list.getPageSize();

        if(nStudents % pgSize == 0) {
            nLastPg = pgSize;
        } else nLastPg = nStudents % pgSize;

        frontPg = 0;
        frontCount = 0;
        backPg = nPgs - 1;
        backCount = nLastPg - 1;
    }

    /**
     * Construct an iterator over the given {@link StudentList} with a default retry quota of 3.
     *
     * @param list The API interface.
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
            if(!success) {
                throw new ApiUnreachableException();
            }
        }

        Student result = frontArr[frontCount];

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

        Student result = backArr[backCount];

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
