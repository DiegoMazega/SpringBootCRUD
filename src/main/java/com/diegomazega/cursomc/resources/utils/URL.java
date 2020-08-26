package com.diegomazega.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	public static  List<Integer> decodeIntegerList(String list){
		String[] vet = list.split(",");
		List<Integer> integer = new ArrayList<>();
		for(int i = 0; i < vet.length; i++) {
			integer.add(Integer.parseInt(vet[i]));
		}
		return integer;
		//ou
		// return Arrays.asList(list.spli(",").steam().map(x -> Integer.parseInt(x)).collect(Collector.toList());
		// se utilizar o segundo metodo é necessário atulizar os imports
	}
	
	public static String decodeParam(String param) {
		try {
			return URLDecoder.decode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
