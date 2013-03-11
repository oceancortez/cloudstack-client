package br.com.abril.api.cloudstack.utils;

public class CloudStackHelper {
	public static String rebootVirtualMachine(String[] args){
		String param = "command=rebootVirtualMachine&id="+args[1];
		return param;
	}
	public static String startVirtualMachine(String[] args){
		String param = "command=startVirtualMachine&id="+args[1];
		return param;
	}
	public static String stopVirtualMachine(String[] args){
		String param = "command=stopVirtualMachine&id="+args[1]+"&forced=true";
		return param;
	}
}
