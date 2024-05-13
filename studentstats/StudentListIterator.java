package studentstats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import itertools.DoubleEndedIterator;

import studentapi.*;

/**
 * A (double ended) iterator over student records pulled from the student API.
 *
 * <p>This does not load the whole student list immediately, but rather queries the API ({@link
 * StudentList#getPage}) only as needed.
 */
public class StudentListIterator implements DoubleEndedIterator<Student> {
    // TASK(8): Implement StudentListIterator: Add any fields you require
    private int retry;
    private StudentList slist;
    private int nPgs, nStudents, pgSize, nLastPg;
    private int frontPg, frontCount;
    private int backPg, backCount;
    private boolean needFPg = true;
    private boolean needBPg = true;
    private ArrayList<Student> frontArr, backArr;

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
        // TASK(8): Implement StudentListIterator
        if(frontPg == backPg && frontCount > backCount) return false;
        return true;
    }

    @Override
    public Student next() {
        // TASK(8): Implement StudentListIterator
        if(!hasNext()) throw new NoSuchElementException();

        if(needFPg && frontPg == nPgs-1) throw new NoSuchElementException();
        if(needFPg && frontPg != nPgs-1) {
            boolean success = false;
            for(int i=0; i<retry; i++) {
                try {
                    frontArr = new ArrayList<>(Arrays.asList(slist.getPage(frontPg)));
                    success = true;
                    needFPg = false;
                    frontPg++;
                    frontCount = 0;
                    break;
                } catch(QueryTimedOutException e) {
                    continue;
                }
            }
            if(!success) throw new ApiUnreachableException();
        }

        if(frontPg == nPgs-1 && frontCount == nLastPg - 1) needFPg = true;
        else if(frontPg != nPgs-1 && frontCount == pgSize-1) needFPg = true;

        return frontArr.get(frontCount++);
    }

    @Override
    public Student reverseNext() {
        // TASK(8): Implement StudentListIterator
        if(!hasNext()) throw new NoSuchElementException();

        if(needBPg && backPg == 0) throw new NoSuchElementException();
        if(needBPg && backPg != 0) {
            boolean success = false;
            for(int i=0; i<retry; i++) {
                try {
                    backArr = new ArrayList<>(Arrays.asList(slist.getPage(backPg)));
                    success = true;
                    needBPg = false;
                    backPg--;
                    backCount = pgSize - 1;
                    break;
                } catch(QueryTimedOutException e) {
                    continue;
                }
            }
            if(!success) throw new ApiUnreachableException();
        }

        if(backCount == 0) needBPg = true;

        return backArr.get(backCount--);
    }
}
