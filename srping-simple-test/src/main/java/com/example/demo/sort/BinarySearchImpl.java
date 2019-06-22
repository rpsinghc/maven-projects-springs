package com.example.demo.sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BinarySearchImpl  {
	
	@Autowired
	//@Qualifier(value="bs")
	SortInterface sortInterface;

	
	public BinarySearchImpl(SortInterface sortInterface) {
		super();
		this.sortInterface = sortInterface;
	}

	public int searchResult(int[]  input, int tosearch) {
		System.out.println(sortInterface);
		return sortInterface.sort(input, tosearch);
	}

}
