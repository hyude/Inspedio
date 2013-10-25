package com.inspedio.system.helper.record;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.inspedio.enums.SaveDataType;

public class SaveDataObject {
	
	public SaveDataType type = null;
	public int nameLength;
	public int dataLength;
	public String name = null;
	
	private boolean boolData;
	private int intData;
	private double doubleData;
	private String stringData;
	
	private boolean[] boolArrData = null;
	private int[] intArrData = null;
	private double[] doubleArrData = null;
	private String[] stringArrData = null;
	
	public SaveDataObject(String Name){
		this.name = Name;
		this.nameLength = Name.length();
	}
	
	public SaveDataObject(String Name, boolean Data){
		this(Name);
		this.setData(Data);
	}
	
	public SaveDataObject(String Name, int Data){
		this(Name);
		this.setData(Data);
	}
	
	public SaveDataObject(String Name, double Data){
		this(Name);
		this.setData(Data);
	}
	
	public SaveDataObject(String Name, String Data){
		this(Name);
		this.setData(Data);
	}
	
	public SaveDataObject(String Name, boolean[] Data){
		this(Name);
		this.setData(Data);
	}
	
	public SaveDataObject(String Name, int[] Data){
		this(Name);
		this.setData(Data);
	}
	
	public SaveDataObject(String Name, double[] Data){
		this(Name);
		this.setData(Data);
	}
	
	public SaveDataObject(String Name, String[] Data){
		this(Name);
		this.setData(Data);
	}
	
		
	public SaveDataObject(DataInputStream stream){
		this.read(stream);
	}
	
	public void read(DataInputStream stream){
		try {
			this.type = SaveDataType.getType(stream.readShort());
			this.nameLength = stream.readInt();
			this.dataLength = stream.readInt();
			this.name = stream.readUTF();
			
			switch(this.type.getValue()){
				case 0:
					this.boolData = stream.readBoolean();
				case 1:
					this.intData = stream.readInt();
				case 2:
					this.doubleData = stream.readDouble();
				case 3:
					this.stringData = stream.readUTF();
				case 4:
					this.boolArrData = new boolean[this.dataLength];
					for(int i = 0; i < this.dataLength; i++){
						this.boolArrData[i] = stream.readBoolean();
					}
				case 5:
					this.intArrData = new int[this.dataLength];
					for(int i = 0; i < this.dataLength; i++){
						this.intArrData[i] = stream.readInt();
					}
				case 6:
					this.doubleArrData = new double[this.dataLength];
					for(int i = 0; i < this.dataLength; i++){
						this.doubleArrData[i] = stream.readDouble();
					}
				case 7:
					this.stringArrData = new String[this.dataLength];
					for(int i = 0; i < this.dataLength; i++){
						this.stringArrData[i] = stream.readUTF();
					}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(DataOutputStream stream){
		try {
			stream.writeShort(this.type.getValue());
			stream.writeInt(this.nameLength);
			stream.writeInt(this.dataLength);
			stream.writeUTF(this.name);
			
			switch(this.type.getValue()){
				case 0:
					stream.writeBoolean(this.boolData);
				case 1:
					stream.writeInt(this.intData);
				case 2:
					stream.writeDouble(this.doubleData);
				case 3:
					stream.writeUTF(this.stringData);
				case 4:
					for(int i = 0; i < this.dataLength; i++){
						stream.writeBoolean(this.boolArrData[i]);
					}
				case 5:
					for(int i = 0; i < this.dataLength; i++){
						stream.writeInt(this.intArrData[i]);
					}
				case 6:
					for(int i = 0; i < this.dataLength; i++){
						stream.writeDouble(this.doubleArrData[i]);
					}
				case 7:
					for(int i = 0; i < this.dataLength; i++){
						stream.writeUTF(this.stringArrData[i]);
					}
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setData(SaveDataObject obj){
		if(obj.type == SaveDataType.BOOLEAN){
			this.setData(obj.boolData);
		} else if(obj.type == SaveDataType.INTEGER){
			this.setData(obj.intData);
		} else if(obj.type == SaveDataType.DOUBLE){
			this.setData(obj.doubleData);
		} else if(obj.type == SaveDataType.STRING){
			this.setData(obj.stringData);
		} else if(obj.type == SaveDataType.BOOLEAN_ARRAY){
			this.setData(obj.boolArrData);
		} else if(obj.type == SaveDataType.INTEGER_ARRAY){
			this.setData(obj.intArrData);
		} else if(obj.type == SaveDataType.DOUBLE_ARRAY){
			this.setData(obj.doubleArrData);
		} else if(obj.type == SaveDataType.STRING_ARRAY){
			this.setData(obj.stringArrData);
		}
	}
	
	public void setData(boolean data){
		this.boolData = data;
		this.type = SaveDataType.BOOLEAN;
		this.dataLength = 1;
	}
	
	public void setData(int data){
		this.intData = data;
		this.type = SaveDataType.INTEGER;
		this.dataLength = 1;
	}
	
	public void setData(double data){
		this.doubleData = data;
		this.type = SaveDataType.DOUBLE;
		this.dataLength = 1;
	}
	
	public void setData(String data){
		this.stringData = data;
		this.type = SaveDataType.STRING;
		this.dataLength = data.length();
	}
	
	public void setData(boolean[] data){
		this.boolArrData = data;
		this.type = SaveDataType.BOOLEAN_ARRAY;
		this.dataLength = data.length;
	}
	
	public void setData(int[] data){
		this.intArrData = data;
		this.type = SaveDataType.INTEGER_ARRAY;
		this.dataLength = data.length;
	}
	
	public void setData(double[] data){
		this.doubleArrData = data;
		this.type = SaveDataType.DOUBLE_ARRAY;
		this.dataLength = data.length;
	}
	
	public void setData(String[] data){
		this.stringArrData = data;
		this.type = SaveDataType.STRING_ARRAY;
		this.dataLength = data.length;
	}
	
	public boolean GetDataBoolean(){
		try{
			if(this.type == SaveDataType.BOOLEAN){
				return this.boolData;
			} else {
				throw new Exception("Record Data is in different format");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int GetDataInteger(){
		try{
			if(this.type == SaveDataType.INTEGER){
				return this.intData;
			} else {
				throw new Exception("Record Data is in different format");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public double GetDataDouble(){
		try{
			if(this.type == SaveDataType.DOUBLE){
				return this.doubleData;
			} else {
				throw new Exception("Record Data is in different format");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public String GetDataString(){
		try{
			if(this.type == SaveDataType.STRING){
				return this.stringData;
			} else {
				throw new Exception("Record Data is in different format");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public boolean[] GetDataBooleanArray(){
		try{
			if(this.type == SaveDataType.BOOLEAN_ARRAY){
				return this.boolArrData;
			} else {
				throw new Exception("Record Data is in different format");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int[] GetDataIntegerArray(){
		try{
			if(this.type == SaveDataType.INTEGER_ARRAY){
				return this.intArrData;
			} else {
				throw new Exception("Record Data is in different format");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public double[] GetDataDoubleArray(){
		try{
			if(this.type == SaveDataType.DOUBLE_ARRAY){
				return this.doubleArrData;
			} else {
				throw new Exception("Record Data is in different format");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String[] GetDataStringArray(){
		try{
			if(this.type == SaveDataType.STRING_ARRAY){
				return this.stringArrData;
			} else {
				throw new Exception("Record Data is in different format");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
