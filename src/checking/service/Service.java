package checking.service;

import java.util.ArrayList;

import checking.model.Defect;
import checking.model.DefectType;
import modeling.model.State;
import modeling.model.Statechart;
import modeling.model.Transition;

public class Service {

	private static Service service = new Service();

	private ArrayList<Defect> defects = new ArrayList<>();
	
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
		defects.clear();
		if (stateChart == null)
			return null;
		
		ArrayList<Transition> tl = new ArrayList<Transition>(stateChart.getTransitions());
		int len = tl.size();
		ArrayList<State> list1 = new ArrayList<State>(); // �ɴ�״̬��
		ArrayList<State> list2 = new ArrayList<State>(stateChart.getStates()); // ���ɴ�״̬��
		ArrayList<State> list3 = new ArrayList<State>(stateChart.getStates()); // �޷���������״̬��

		
		for(int i=0;i<stateChart.getStates().size();i++){
			if(stateChart.getStates().get(i).isIs_exception()&&stateChart.getStates().get(i).isIs_init()){
				Defect defect = new Defect();
				defect.setRelatedState(stateChart.getStates().get(i));
				defect.setDescription("��״̬��Ϊ��ʼ����Ϊ�쳣����Ӧ���������ܴ��ڴ���");
				defect.type=DefectType.init_from_exception;
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

		for (int i = 0; i < stateChart.getStates().size(); i++) {
			State state = stateChart.getStates().get(i);
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
			defect.type=DefectType.unreacheable;
			defect.setRelatedState(list2.get(i));
			defect.setDescription(description);
			defects.add(defect);

		}

		for (int i = 0; i < list3.size(); i++) {
			
			String description = "";
			Defect defect = new Defect();
			if (!list3.get(i).isIs_exception()){
				description = "��״̬�޷����������κ���ͨ״̬��";
				defect.type=DefectType.common_state_cannot_jmp_to_any_other_common_state;
			}
			else{
				description = "���쳣״̬�ܵ�����ͨ״̬����Ӧ���������ܴ��ڴ���";
				defect.type=DefectType.exception_can_jmp_to_common_states;
			}
			defect.setRelatedState(list3.get(i));
			defect.setDescription(description);
			defects.add(defect);
		}

		return defects;
	}
}