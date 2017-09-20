import java.util.List;

/**
 * Jimmy类
 * @author stars
 *
 */
class Jimmy {
	//Jimmy能承受的最大落差
	private final int maxJump ;
	//初始横坐标
	private final int xCur ;
	//初始高度
	private final int hCur ;
	//台阶列表
	private final List<Stage> stage ;
	//最终计算结果
	private int tmrMinDistance ;
	
	public Jimmy(int xCur, int hCur, int maxJump, List<Stage> stage) {
		this.maxJump = maxJump ;
		this.xCur = xCur ;
		this.hCur = hCur ;
		this.stage = stage ;
	}
	
	public int getTmrMinDistance() {
		int tmr = 0 ;
		int indexMeetCond = getNextStageIndex(xCur, hCur) ;
		tmr += hCur-stage.get(indexMeetCond).getHigh() ;
		tmr += getTmrFastDistance(xCur, indexMeetCond) ;
		tmrMinDistance = tmr ;
		return tmrMinDistance ;
	}
	

	/**
	 * 返回（x, h）位置能跳到的下一个台阶的index
	 */
	private int getNextStageIndex(int x, int h) {
		int maxHighIndexMeetCond=-1 ;
		
		for (int i = 0; i < stage.size(); ++i) {
			Stage item = stage.get(i) ;
			if (
				//高度在可承受范围之内
				h > item.getHigh()				
				&& (h - item.getHigh() <= maxJump)
				//横坐标在范围内
				&& x >= item.getxL()
				&& x <= item.getxR()) {
				maxHighIndexMeetCond = i ;
				break ;
			}
			if (h - item.getHigh() > maxJump)
				break ;
		}
		return maxHighIndexMeetCond ;
	}
	
	/**
	 * 
	 */
	private int getTmrFastDistance(int x, int indexStage) {
		//存放从该(x, h)位置到地面的最短时间
		int tmrCost ;
		//到台阶左右所需要花费的时间
		int tmrToL=0, tmrToR=0 ;
		
		//在最大跳跃高度以内没有台阶
		if (indexStage < 0) {
			return Global.TMRMAX ;
		}
		
		Stage itemStage = stage.get(indexStage) ;
		
		//x分别到左右端点的距离
		tmrToL += x-itemStage.getxL() ;
		tmrToR += itemStage.getxR()-x ;
		
		//落地返回
		if (stage.size()-1 == indexStage
			|| 0 == itemStage.getHigh()) {
			return 0 ;
		}
		
		//如果该台阶已经求过最小时间花费
		if (itemStage.isVisited()) {
			tmrToL += itemStage.getTmrLFastDis() ;
			tmrToR += itemStage.getTmrRFastDis() ;
			tmrCost = tmrToL < tmrToR? tmrToL: tmrToR ;
			
			return (tmrCost >= Global.TMRMAX)? Global.TMRMAX: tmrCost ;
		}
		
		//分别获取左右两端跳下的台阶
		int indexLNextStage = getNextStageIndex(itemStage.getxL(), itemStage.getHigh()) ;
		int indexRNextStage = getNextStageIndex(itemStage.getxR(), itemStage.getHigh()) ;
		
		//dp，并记忆化
		itemStage.setTmrLFastDis(
				itemStage.getHigh() - stage.get(indexLNextStage).getHigh() 
				+ getTmrFastDistance(itemStage.getxL(), indexLNextStage)) ;
		itemStage.setTmrRFastDis(
				itemStage.getHigh() - stage.get(indexRNextStage).getHigh()
				+ getTmrFastDistance(itemStage.getxR(), indexRNextStage)) ;
		itemStage.setVisited(true) ;
		
		tmrToL += itemStage.getTmrLFastDis() ;
		tmrToR += itemStage.getTmrRFastDis() ;
		//更新在该台阶落地的最短时间
		tmrCost = tmrToL<tmrToR? tmrToL: tmrToR ;
		
		return tmrCost >= Global.TMRMAX? Global.TMRMAX: tmrCost ;
	}
}