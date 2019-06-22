package com.example.demo.sort;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class QuickSortSearch  implements SortInterface{

	@Override
	public int sort(int[] input, int tosearch) {
		System.out.println("Qucik sort search");
		return 0;
	}

}
