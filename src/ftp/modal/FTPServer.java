

package ftp.modal;


import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FTPServer {
    private Map<Path, ReentrantReadWriteLock> readWriteLockMap;


    public FTPServer() {
        readWriteLockMap = new HashMap<Path, ReentrantReadWriteLock>();

    }

    public void getLockAccess(Path path) {


            if (readWriteLockMap.containsKey(path)) {

                readWriteLockMap.get(path).readLock().lock();

            } else {

                readWriteLockMap.put(path, new ReentrantReadWriteLock());

                readWriteLockMap.get(path).readLock().lock();


            }

    }

    public void getLockUnlock(Path path) {


            try {

                readWriteLockMap.get(path).readLock().unlock();

                if (readWriteLockMap.get(path).getReadLockCount() == 0 && !readWriteLockMap.get(path).isWriteLocked())
                    readWriteLockMap.remove(path);


            } catch (Exception e) {
                 e.printStackTrace();
            }



    }


    public void putLockAccess(Path path) {



            if (readWriteLockMap.containsKey(path)) {

                readWriteLockMap.get(path).writeLock().lock();

            } else {

                readWriteLockMap.put(path, new ReentrantReadWriteLock());
                readWriteLockMap.get(path).writeLock().lock();

            }

    }


    public void putLockRelease(Path path) {

            try {
                readWriteLockMap.get(path).writeLock().unlock();


                if (readWriteLockMap.get(path).getReadLockCount() == 0 && !readWriteLockMap.get(path).isWriteLocked())
                    readWriteLockMap.remove(path);

            } catch (Exception e) {
               e.printStackTrace();
            }


    }


    public synchronized boolean delete(Path path) {
        return !readWriteLockMap.containsKey(path);
    }



}