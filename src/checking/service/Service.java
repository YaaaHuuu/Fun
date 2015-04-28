package checking.service;

import java.util.ArrayList;
import model.Defect;
import model.State;
import model.Statechart;
import model.Transition;

public class Service {

	private static Service service = new Service();

	private ArrayList<Defect> defects;
	
	private Service() {

	}

	public ArrayList<Defect> getDefects() {
		return defects;
	}

	public ArrayList<Defect> searchDefect(State state){
		ArrayList<Defect> results = new ArrayList<Defect>(); 
		for(int i=0;i<defects.size();i++){
			if(state.equals(defects.get(i).getRelatedState()))
				results.add(defects.get(i));
		}
		return results;
	}
	
	public void setDefects(ArrayList<Defect> defects) {
		this.defects = defects;
	}

	public static Service getInstance() {
		return service;
	}

	public ArrayList<Defect> check(Statechart stateChart) {

		if (stateChart == null)
			return null;

		
		ArrayList<Transition> tl = stateChart.getTransitions();
		int len = tl.size();
		ArrayList<State> list1 = new ArrayList<State>(); // �ɴ�״̬��
		ArrayList<State> list2 = stateChart.getStates(); // ���ɴ�״̬��
		ArrayList<State> list3 = stateChart.getStates(); // �޷���������״̬��

		for(int i=0;i<list2.size();i++){
			if(list2.get(i).isIs_exception()&&list2.get(i).isIs_init()){
				Defect defect = new Defect();
				defect.setRelatedState(list2.get(i));
				defect.setDescription("��״̬��Ϊ��ʼ����Ϊ�쳣����Ӧ���������ܴ��ڴ���");
				defects.add(defect);
			}
		}
		
		for (int i = 0; i < len; i++) {
			Transition transition = tl.get(i);
			if (transition.getPreState() != null
					&& transition.getPreState().isIs_init()) {
				list1.add(transition.getPreState());
				list1.add(transition.getPostState());
				for (int j = 0; j < len; j++) {
					if (transition.getPostState().equals(
							tl.get(j).getPreState())) {
						transition = tl.get(j);
						list1.add(transition.getPostState());
					}
				}
			}
		}

		for (int i = 0; i < list3.size(); i++) {
			State state = list3.get(i);
			if (list1.contains(state)) {
				list2.remove(state);
			}
		}

		for (int i = 0; i < len; i++) {
			Transition transition = tl.get(i);
			if (transition.getPreState() != null
					&& transition.getPostState() != null) {
				if (!transition.getPreState().isIs_exception()) {
					if (list3.contains(transition.getPreState()))
						list3.remove(transition.getPreState());
				} else if (transition.getPostState().isIs_exception()) {
					if (list3.contains(transition.getPreState()))
						list3.remove(transition.getPreState());
				}

			}
		}

		for (int i = 0; i < list2.size(); i++) {

			String description = "��״̬���ɴ��Ӧ���������ܴ��ڴ���";
			Defect defect = new Defect();
			defect.setRelatedState(list2.get(i));
			defect.setDescription(description);
			defects.add(defect);

		}

		for (int i = 0; i < list3.size(); i++) {
			
			String description = "";

			if (!list3.get(i).isIs_exception())
				description = "��״̬�޷����������κ���ͨ״̬��";
			else
				description = "���쳣״̬�ܵ�����ͨ״̬����Ӧ���������ܴ��ڴ���";
			
			Defect defect = new Defect();
			defect.setRelatedState(list3.get(i));
			defect.setDescription(description);
			defects.add(defect);
		}

		return defects;
	}
}
