//import java.io.* ;
import java.util.* ;

public class Main {
	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in) ;
        //测试用例数t
		int t=cin.nextInt() ;
        for (int i = 0; i < t; ++i) {
        	//台阶数
        	int n=cin.nextInt() ;
        	//初始横坐标
        	int x=cin.nextInt() ;
        	//初始纵坐标（高度）
        	int y=cin.nextInt() ;
        	//Jimmy每次能下落的最大高度
        	int max=cin.nextInt() ;
        	
        	Stage ground = Global.GROUND_STAGE ;
        	ground.setVisited(true) ;
        	ground.setTmrLFastDis(0) ;
        	ground.setTmrRFastDis(0) ;
        	
        	List<Stage> stageList = new ArrayList<Stage>() ;
        	stageList.add(ground) ;
        	
        	for (int j = 0; j < n; ++j) {
        		//x1, x2 分别是台阶的左右端点横坐标
        		int x1=cin.nextInt(), x2=cin.nextInt() ;
        		//台阶高度
        		int h=cin.nextInt() ;
        		//添加台阶到列表
        		stageList.add(new Stage(x1, x2, h)) ;
        	}
        	
        	//排序，高在前，低在后
        	Collections.sort(stageList) ;
        	//Collections.reverse(stageList) ;
        	
        	//printList(stageList) ;
        	
        	Jimmy jimmy = new Jimmy(x, y, max, stageList) ;
        	jimmy.getTmrMinDistance() ;
        	System.out.println(jimmy.getTmrMinDistance());
        }
        cin.close();
	}
	
	@SuppressWarnings("unused")
	private static void printList(List<Stage> list) {
		for(Stage item: list) {
			System.out.println(item.toString()) ;
		}
	}

}



