package org.cresse.rpg.client.map.util;

import java.util.Random;

public class DiceRoll {
	
	private Random rand=new Random();
	
	public int roll(int number, int sides, int mod){
		int total=mod;
		for (int i = 0; i < number; i++) {
			total+=rand.nextInt(sides)+1;
		}
		return total;
	}

}
