package modeling.sign;

public abstract class ElementSign implements SignDistinguisherInterface{

	protected String type;//��ע�ͻ������� interface AnalysisKey
	protected String key;//������ stateTrans
	
	
	public String getType() {
		return type;
	}

	public String getKey() {
		return key;
	}
	
}
