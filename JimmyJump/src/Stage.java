
/**
 * 台阶类
 * @author stars
 * 
 */
class Stage 
	implements Comparable<Stage> {
	//台阶左右端点横坐标
	private final int xL, xR ;
	//台阶高度
	private final int high ;
	//台阶访问标记
	private boolean visited ;
	//从左端，右端跳下到平台的最短时间
	private int tmrLFastDis, tmrRFastDis ;
	
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	public int getTmrLFastDis() {
		return tmrLFastDis;
	}
	public void setTmrLFastDis(int tmrLFastDis) {
		this.tmrLFastDis = tmrLFastDis;
	}
	public int getTmrRFastDis() {
		return tmrRFastDis;
	}
	public void setTmrRFastDis(int tmrRFastDis) {
		this.tmrRFastDis = tmrRFastDis;
	}
	public Stage(int xL, int xR, int high) {
		this.xL = xL ;
		this.xR = xR ;
		this.high = high ;
		this.visited = false ;
	}
	public int getxL() {
		return xL;
	}
	public int getxR() {
		return xR;
	}
	public int getHigh() {
		return high;
	}
	
	@Override
	public int compareTo(Stage other) {
		int otherHigh = other.getHigh() ;
		int high = this.getHigh() ;
		//return high.compareTo(otherHigh) ;
		int tmp = otherHigh - high ;
		if (tmp > 0)
			tmp = 1 ;
		else if (tmp < 0)
			tmp = -1 ;
		
		return tmp ;
	}
	
	@Override
	public String toString() {
		return "high = " + high + ", xL = " + xL + ", xR = " + xR ;
	}
}
