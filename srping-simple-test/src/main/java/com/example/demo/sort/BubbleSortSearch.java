package com.example.demo.sort;

import org.springframework.stereotype.Component;

@Component
//@Qualifier(value="bs")
public class BubbleSortSearch  implements SortInterface{

	@Override
	public int sort(int[] input, int tosearch){
		System.out.println("BubbleSortSearch");
		return 0;
	}

}
