package com.example.demo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.sort.BinarySearchImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringCliDemog1ApplicationTests {
Logger LOGGER = LoggerFactory.getLogger(SpringBootTest.class);
	
	
	@Autowired
	BinarySearchImpl binarySearchImpl;
	
	@Test
	public void contextLoads() {
		int []  input = new int[] {  5,6,7};
		int res = binarySearchImpl.searchResult(input, 7);
		Assert.assertEquals(res, 0);

	}

}
