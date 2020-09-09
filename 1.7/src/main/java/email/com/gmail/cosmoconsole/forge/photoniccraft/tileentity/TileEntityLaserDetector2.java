package email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TileEntityLaserDetector2 extends TileEntityLaserDetector {
	long lasertick = 0L;
	ArrayList<Object[]> thisTick = new ArrayList<Object[]>();
	ArrayList<Object[]> lastTick = new ArrayList<Object[]>();
	Object listLock = new Object();
	ConcurrentHashMap<Integer,ArrayList<Object[]>> captures = new ConcurrentHashMap<Integer,ArrayList<Object[]>>();
	public void addLaser(long tick, Object[] objects) {
		if (thisTick == null || lastTick == null) {
			thisTick = new ArrayList<Object[]>();
			lastTick = new ArrayList<Object[]>();
			captures = new ConcurrentHashMap<Integer,ArrayList<Object[]>>();
		}
		synchronized (listLock) {
			if (tick == lasertick) {
				thisTick.add(objects);
			} else if (tick == (lasertick + 1)) {
				lastTick = new ArrayList<Object[]>(thisTick);
				thisTick = new ArrayList<Object[]>();
				thisTick.add(objects);
			} else if (tick > (lasertick + 1)) {
				lastTick = new ArrayList<Object[]>();
				thisTick = new ArrayList<Object[]>();
				thisTick.add(objects);
			}
			lasertick = tick;
		}
	}
	public void catchComputer(long tick, int id) {
		if (thisTick == null || lastTick == null) {
			thisTick = new ArrayList<Object[]>();
			lastTick = new ArrayList<Object[]>();
			captures = new ConcurrentHashMap<Integer,ArrayList<Object[]>>();
		}
		synchronized (listLock) {
			if (tick == (lasertick + 1)) {
				lastTick = new ArrayList<Object[]>(thisTick);
				thisTick = new ArrayList<Object[]>();
			} else if (tick > (lasertick + 1)) {
				lastTick = new ArrayList<Object[]>();
				thisTick = new ArrayList<Object[]>();
			}
			captures.put(id, new ArrayList<Object[]>(lastTick));
		}
	}
	public ArrayList<Object[]> getLastBeams(long tick) {
		if (thisTick == null || lastTick == null) {
			thisTick = new ArrayList<Object[]>();
			lastTick = new ArrayList<Object[]>();
			captures = new ConcurrentHashMap<Integer,ArrayList<Object[]>>();
		}
		ArrayList<Object[]> results;
		synchronized (listLock) {
			if (tick == (lasertick + 1)) {
				lastTick = new ArrayList<Object[]>(thisTick);
				thisTick = new ArrayList<Object[]>();
			} else if (tick > (lasertick + 1)) {
				lastTick = new ArrayList<Object[]>();
				thisTick = new ArrayList<Object[]>();
			}
			results = new ArrayList<Object[]>(lastTick);
		}
		return results;
	}

	public boolean hasCatchForComputer(int id) {
		return captures.containsKey(id);
	}
	public void removeComputer(int id) {
		if (captures.containsKey(id))
			captures.remove(id);
	}
	public ArrayList<Object[]> getCatch(int id) {
		return captures.get(id);
	}
}
