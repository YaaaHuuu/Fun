package model;

public class State{
	boolean is_exception;
	String name;
}


//ÿ������ǰ��ע�͸�ʽ�ĳ���������
/*
* Full >> push(size==MAX_INT) >> OF 
* Empty >> push() >> Full
* */

/*
 *  Full --> push(size==MAX_INT)--> OF 
 *  ����FullΪpreState��OFΪpostState
 *  size==MAX_INTΪcondition��
 *  
 */
